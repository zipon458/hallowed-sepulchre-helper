package com.sepulchre.handler;

import com.sepulchre.model.CrossbowStatue;
import com.sepulchre.model.LightningStrike;
import com.sepulchre.model.WizardStatue;
import com.sepulchre.util.SepulchreConstants;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.GroundObject;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.GraphicsObjectCreated;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public class ObstacleHandler
{
	private final Client client;

	private Runnable onSepulchreDetected;

	@Getter
	private final List<LightningStrike> activeLightning = new ArrayList<>();

	@Getter
	private final List<CrossbowStatue> crossbowStatues = new ArrayList<>();

	@Getter
	private final List<WizardStatue> wizardStatues = new ArrayList<>();

	@Getter
	private final Set<NPC> boltNpcs = new HashSet<>();

	@Getter
	private final Set<NPC> swordNpcs = new HashSet<>();

	private final Set<WorldPoint> yellowPortalTileLocations = new HashSet<>();
	private final Set<WorldPoint> bluePortalTileLocations = new HashSet<>();

	@Getter
	private final Set<WorldPoint> activeYellowPortals = new HashSet<>();

	@Getter
	private final Set<WorldPoint> activeBluePortals = new HashSet<>();

	@Getter
	private final Map<WorldPoint, Integer> activePortalGraphics = new HashMap<>();

	private final Map<WorldPoint, Integer> pendingLightning = new HashMap<>();

	@Inject
	public ObstacleHandler(Client client)
	{
		this.client = client;
	}

	public void setOnSepulchreDetected(Runnable callback)
	{
		this.onSepulchreDetected = callback;
	}

	private void notifySepulchreDetected()
	{
		if (onSepulchreDetected != null)
		{
			onSepulchreDetected.run();
		}
	}

	public void reset()
	{
		activeLightning.clear();
		crossbowStatues.clear();
		wizardStatues.clear();
		boltNpcs.clear();
		swordNpcs.clear();
		yellowPortalTileLocations.clear();
		bluePortalTileLocations.clear();
		activeYellowPortals.clear();
		activeBluePortals.clear();
		activePortalGraphics.clear();
		pendingLightning.clear();
	}

	public void onGameTick()
	{
		for (LightningStrike lightning : activeLightning)
		{
			lightning.onGameTick();
		}

		for (WizardStatue wizard : wizardStatues)
		{
			wizard.onGameTick();
		}

		activeLightning.removeIf(LightningStrike::isExpired);

		activePortalGraphics.entrySet().removeIf(entry -> {
			int remaining = entry.getValue() - 1;
			if (remaining <= 0)
			{
				WorldPoint loc = entry.getKey();
				activeYellowPortals.remove(loc);
				activeBluePortals.remove(loc);
				return true;
			}
			entry.setValue(remaining);
			return false;
		});

		pendingLightning.entrySet().removeIf(entry -> {
			int remaining = entry.getValue() - 1;
			if (remaining <= 0)
			{
				activeLightning.add(new LightningStrike(entry.getKey(), 3));
				return true;
			}
			entry.setValue(remaining);
			return false;
		});
	}

	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		onGameObjectSpawned(event.getGameObject());
	}

	public void onGameObjectSpawned(GameObject gameObject)
	{
		int id = gameObject.getId();

		if (SepulchreConstants.CROSSBOW_STATUE_IDS.contains(id))
		{
			notifySepulchreDetected();
			for (CrossbowStatue existing : crossbowStatues)
			{
				if (existing.getGameObject() == gameObject)
				{
					return;
				}
			}
			crossbowStatues.add(new CrossbowStatue(gameObject));
			return;
		}

		if (SepulchreConstants.WIZARD_FLAME_OBJECT_IDS.contains(id))
		{
			notifySepulchreDetected();
			for (WizardStatue existing : wizardStatues)
			{
				if (existing.getGameObject() == gameObject)
				{
					return;
				}
			}
			WizardStatue wizard = new WizardStatue(gameObject);
			wizard.setFirePhaseTicks(2);
			wizard.setSafePhaseTicks(4);
			wizard.setWarningPhaseTicks(2);
			calculateWizardFlameFireTiles(wizard, gameObject.getOrientation());
			wizardStatues.add(wizard);
			return;
		}

		if (id == SepulchreConstants.PORTAL_YELLOW_ID)
		{
			notifySepulchreDetected();
			yellowPortalTileLocations.add(gameObject.getWorldLocation());
			return;
		}

		if (id == SepulchreConstants.PORTAL_BLUE_ID)
		{
			notifySepulchreDetected();
			bluePortalTileLocations.add(gameObject.getWorldLocation());
		}
	}

	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		GameObject gameObject = event.getGameObject();
		WorldPoint location = gameObject.getWorldLocation();

		crossbowStatues.removeIf(statue -> statue.getGameObject() == gameObject);
		wizardStatues.removeIf(statue -> statue.getGameObject() == gameObject);

		int id = gameObject.getId();
		if (id == SepulchreConstants.PORTAL_YELLOW_ID)
		{
			yellowPortalTileLocations.remove(location);
			activeYellowPortals.remove(location);
		}
		else if (id == SepulchreConstants.PORTAL_BLUE_ID)
		{
			bluePortalTileLocations.remove(location);
			activeBluePortals.remove(location);
		}
	}

	public void onGroundObjectSpawned(GroundObjectSpawned event)
	{
		GroundObject groundObject = event.getGroundObject();
		int id = groundObject.getId();

		if (id == SepulchreConstants.PORTAL_YELLOW_ID)
		{
			yellowPortalTileLocations.add(groundObject.getWorldLocation());
		}
		else if (id == SepulchreConstants.PORTAL_BLUE_ID)
		{
			bluePortalTileLocations.add(groundObject.getWorldLocation());
		}
	}

	public void onGroundObjectDespawned(GroundObjectDespawned event)
	{
		GroundObject groundObject = event.getGroundObject();
		int id = groundObject.getId();
		WorldPoint location = groundObject.getWorldLocation();

		if (id == SepulchreConstants.PORTAL_YELLOW_ID)
		{
			yellowPortalTileLocations.remove(location);
			activeYellowPortals.remove(location);
		}
		else if (id == SepulchreConstants.PORTAL_BLUE_ID)
		{
			bluePortalTileLocations.remove(location);
			activeBluePortals.remove(location);
		}
	}

	public void onNpcSpawned(NpcSpawned event)
	{
		NPC npc = event.getNpc();
		int id = npc.getId();

		if (SepulchreConstants.BOLT_NULL_NPC_IDS.contains(id))
		{
			notifySepulchreDetected();
			boltNpcs.add(npc);
		}
		else if (SepulchreConstants.SWORD_NULL_NPC_IDS.contains(id))
		{
			notifySepulchreDetected();
			swordNpcs.add(npc);
		}
	}

	public void onNpcDespawned(NpcDespawned event)
	{
		NPC npc = event.getNpc();
		boltNpcs.remove(npc);
		swordNpcs.remove(npc);
	}

	public void onGraphicsObjectCreated(GraphicsObjectCreated event)
	{
		int graphicsId = event.getGraphicsObject().getId();
		WorldPoint location = WorldPoint.fromLocal(client, event.getGraphicsObject().getLocation());

		if (graphicsId == SepulchreConstants.LIGHTNING_GRAPHICS_ID)
		{
			pendingLightning.put(location, 3);
			return;
		}

		if (SepulchreConstants.PORTAL_GRAPHICS_IDS.contains(graphicsId))
		{
			if (yellowPortalTileLocations.contains(location))
			{
				activeYellowPortals.add(location);
				activePortalGraphics.put(location, 5);
			}
			else if (bluePortalTileLocations.contains(location))
			{
				activeBluePortals.add(location);
				activePortalGraphics.put(location, 5);
			}
		}
	}

	private void calculateWizardFlameFireTiles(WizardStatue wizard, int orientation)
	{
		WorldPoint location = wizard.getLocation();
		int[] offsets = getWizardFlameOffsets(orientation);

		int startX = location.getX() + offsets[2];
		int startY = location.getY() + offsets[3];

		for (int i = offsets[4]; i <= offsets[4] + 2; i++)
		{
			wizard.addFireTile(new WorldPoint(
				startX + (offsets[0] * i),
				startY + (offsets[1] * i),
				location.getPlane()
			));
		}
	}

	private int[] getWizardFlameOffsets(int orientation)
	{
		int dx = 0;
		int dy = 0;
		int rightHandOffsetX = 0;
		int rightHandOffsetY = 0;
		int startOffset = 2;

		if (orientation >= 1280 && orientation < 1792)
		{
			dx = 1;
			startOffset = 2;
		}
		else if (orientation >= 768 && orientation < 1280)
		{
			dy = 1;
			rightHandOffsetX = 1;
		}
		else if (orientation >= 256 && orientation < 768)
		{
			dx = -1;
			rightHandOffsetY = 1;
			startOffset = 1;
		}
		else
		{
			dy = -1;
			startOffset = 1;
		}

		return new int[] { dx, dy, rightHandOffsetX, rightHandOffsetY, startOffset };
	}
}
