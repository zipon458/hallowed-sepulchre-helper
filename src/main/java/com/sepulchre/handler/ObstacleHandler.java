package com.sepulchre.handler;

import com.sepulchre.config.SepulchreConfig;
import com.sepulchre.model.CrossbowStatue;
import com.sepulchre.model.WizardCyclePhaseTracker;
import com.sepulchre.model.LightningStrike;
import com.sepulchre.model.SepulchreRoute;
import com.sepulchre.model.SwordStatue;
import com.sepulchre.model.WizardStatue;
import com.sepulchre.util.GameObjectUtil;
import com.sepulchre.util.SepulchreConstants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
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
@Slf4j
public class ObstacleHandler
{
	private final Client client;
	private final SepulchreConfig config;

	private Runnable onSepulchreDetected;

	@Getter
	private final List<LightningStrike> activeLightning = new ArrayList<>();

	@Getter
	private final List<CrossbowStatue> crossbowStatues = new ArrayList<>();

	@Getter
	private final List<WizardStatue> wizardStatues = new ArrayList<>();

	@Getter
	private final List<SwordStatue> swordStatues = new ArrayList<>();

	@Getter
	private final Set<NPC> boltNpcs = new HashSet<>();

	@Getter
	private final Set<NPC> swordNpcs = new HashSet<>();

	@Getter
	private final Set<WorldPoint> activeYellowPortals = new HashSet<>();

	@Getter
	private final Set<WorldPoint> activeBluePortals = new HashSet<>();

	@Getter
	private final Map<WorldPoint, Integer> activePortalGraphics = new HashMap<>();

	private final Map<WorldPoint, Integer> pendingLightning = new HashMap<>();

	@Getter
	private final List<GameObject> magicalObelisks = new ArrayList<>();

	@Getter
	private final WizardCyclePhaseTracker wizardCyclePhaseTracker = new WizardCyclePhaseTracker();

	private final Set<NPC> savedSwordNpcs = new HashSet<>();
	private final Set<NPC> savedBoltNpcs = new HashSet<>();

	@Getter
	private int playerImmunityTicks = 0;

	@Getter
	private boolean doorToNextFloorClosed = false;

	@Getter
	@lombok.Setter
	private int currentFloor = 0;

	@Getter
	@lombok.Setter
	private SepulchreRoute currentRoute = SepulchreRoute.UNKNOWN;

	@Getter
	private int floorStartPlane = -1;

	@Getter
	private boolean inLowerSection = false;

	@Getter
	private int floorTicks = 0;

	@Getter
	@lombok.Setter
	private int runTicks = 0;

	@Getter
	@lombok.Setter
	private boolean timerPaused = false;

	@Getter
	@lombok.Setter
	private boolean timerStarted = false;

	@Inject
	public ObstacleHandler(Client client, SepulchreConfig config)
	{
		this.client = client;
		this.config = config;
	}

	public int getPlayerMaxFloor()
	{
		int agilityLevel = client.getBoostedSkillLevel(net.runelite.api.Skill.AGILITY);
		for (int i = SepulchreConstants.FLOOR_AGILITY_REQUIREMENTS.length - 1; i >= 0; i--)
		{
			if (agilityLevel >= SepulchreConstants.FLOOR_AGILITY_REQUIREMENTS[i])
			{
				return i + 1;
			}
		}
		return 0;
	}

	public int getBluePortalRemainingTicks(WorldPoint location)
	{
		return getPortalRemainingTicks(location, activeBluePortals);
	}

	public int getYellowPortalRemainingTicks(WorldPoint location)
	{
		return getPortalRemainingTicks(location, activeYellowPortals);
	}

	private int getPortalRemainingTicks(WorldPoint location, Set<WorldPoint> activePortals)
	{
		if (!activePortals.contains(location))
		{
			return -1;
		}
		Integer remaining = activePortalGraphics.get(location);
		return remaining != null ? remaining : -1;
	}

	public boolean isPlayerImmune()
	{
		if (playerImmunityTicks > 0)
		{
			return true;
		}
		Player player = client.getLocalPlayer();
		if (player != null)
		{
			return player.hasSpotAnim(SepulchreConstants.BLUE_PORTAL_TELEPORT_GRAPHICS_ID)
				|| player.hasSpotAnim(SepulchreConstants.YELLOW_PORTAL_TELEPORT_GRAPHICS_ID);
		}
		return false;
	}

	public String getFloor4CycleDisplayText()
	{
		return wizardCyclePhaseTracker.getDisplayText();
	}

	public int getFloor4CurrentCycle()
	{
		return wizardCyclePhaseTracker.getCurrentCycle();
	}

	public void onDoorToNextFloorClosed()
	{
		doorToNextFloorClosed = true;
	}

	public void setFloorStartPlane(int plane)
	{
		this.floorStartPlane = plane;
		this.inLowerSection = false;
		this.floorTicks = 0;
		this.timerStarted = false;
	}

	public void updateLowerSectionStatus(int currentPlane)
	{
		if (floorStartPlane >= 0 && currentPlane < floorStartPlane)
		{
			inLowerSection = true;
		}
	}

	public void restoreFloorState(boolean wasInLowerSection, int previousStartPlane, int previousFloorTicks)
	{
		this.inLowerSection = wasInLowerSection;
		this.floorStartPlane = previousStartPlane;
		this.floorTicks = previousFloorTicks;
		if (wasInLowerSection && previousStartPlane < 0)
		{
			this.floorStartPlane = 99;
		}
	}

	public void saveProjectileNpcs()
	{
		savedSwordNpcs.clear();
		savedSwordNpcs.addAll(swordNpcs);
		savedBoltNpcs.clear();
		savedBoltNpcs.addAll(boltNpcs);
	}

	public void restoreProjectileNpcs()
	{
		swordNpcs.addAll(savedSwordNpcs);
		boltNpcs.addAll(savedBoltNpcs);
	}

	public void clearSavedProjectileNpcs()
	{
		savedSwordNpcs.clear();
		savedBoltNpcs.clear();
	}

	public void onFloorEntered(boolean isFirstFloor)
	{
		if (isFirstFloor)
		{
			currentFloor = 1;
		}
		else if (currentFloor > 0 && currentFloor < 5)
		{
			currentFloor++;
		}
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
		swordStatues.clear();
		boltNpcs.clear();
		swordNpcs.clear();
		activeYellowPortals.clear();
		activeBluePortals.clear();
		activePortalGraphics.clear();
		pendingLightning.clear();
		magicalObelisks.clear();
		playerImmunityTicks = 0;
		doorToNextFloorClosed = false;
		currentFloor = 0;
		currentRoute = SepulchreRoute.UNKNOWN;
		floorStartPlane = -1;
		inLowerSection = false;
		floorTicks = 0;
		runTicks = 0;
		timerPaused = false;
		timerStarted = false;
		wizardCyclePhaseTracker.reset();
	}

	public void onGameTick()
	{
		if (currentFloor > 0 && !timerPaused)
		{
			runTicks++;
			floorTicks++;
		}

		for (LightningStrike lightning : activeLightning)
		{
			lightning.onGameTick();
		}

		for (WizardStatue wizard : wizardStatues)
		{
			if (currentFloor == 5)
			{
				wizard.setFirePhaseTicks(1);
			}
			else
			{
				wizard.setFirePhaseTicks(2);
			}
			wizard.onGameTick();
		}

		for (SwordStatue sword : swordStatues)
		{
			sword.onGameTick();
		}

		if (currentFloor == 4)
		{
			wizardCyclePhaseTracker.onGameTick(wizardStatues);
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

		if (playerImmunityTicks > 0)
		{
			playerImmunityTicks--;
		}
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
			wizard.setFirePhaseTicks(currentFloor == 5 ? 1 : 2);
			wizard.setSafePhaseTicks(1);
			wizard.setWarningPhaseTicks(2);
			calculateWizardFlameFireTiles(wizard, gameObject.getOrientation());
			wizardStatues.add(wizard);
			return;
		}

		if (SepulchreConstants.SWORD_STATUE_IDS.contains(id))
		{
			notifySepulchreDetected();

			for (SwordStatue existing : swordStatues)
			{
				if (existing.getGameObject() == gameObject)
				{
					return;
				}
			}

			swordStatues.add(new SwordStatue(gameObject));
			return;
		}

		if (id == SepulchreConstants.MAGICAL_OBELISK_ID)
		{
			notifySepulchreDetected();
			if (!magicalObelisks.contains(gameObject))
			{
				magicalObelisks.add(gameObject);
			}
		}
	}

	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		GameObject gameObject = event.getGameObject();

		crossbowStatues.removeIf(statue -> statue.getGameObject() == gameObject);
		wizardStatues.removeIf(statue -> statue.getGameObject() == gameObject);
		swordStatues.removeIf(statue -> statue.getGameObject() == gameObject);
		magicalObelisks.remove(gameObject);
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

		if (SepulchreConstants.YELLOW_PORTAL_GRAPHICS_IDS.contains(graphicsId))
		{
			activeYellowPortals.add(location);
			activePortalGraphics.put(location, 5);
		}
		else if (SepulchreConstants.BLUE_PORTAL_GRAPHICS_IDS.contains(graphicsId))
		{
			activeBluePortals.add(location);
			activePortalGraphics.put(location, 5);
		}

		if (graphicsId == SepulchreConstants.BLUE_PORTAL_TELEPORT_GRAPHICS_ID
			|| graphicsId == SepulchreConstants.YELLOW_PORTAL_TELEPORT_GRAPHICS_ID)
		{
			if (client.getLocalPlayer() != null)
			{
				WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
				if (playerLocation.equals(location))
				{
					playerImmunityTicks = 2;
				}
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
