package com.sepulchre.handler;

import com.sepulchre.model.LightningStrike;
import com.sepulchre.util.SepulchreConstants;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class ProjectileTracker
{
	private final Client client;

	private final List<LightningStrike> activeLightning = new ArrayList<>();
	private final Set<NPC> boltNpcs = new HashSet<>();
	private final Set<NPC> swordNpcs = new HashSet<>();
	private final Set<WorldPoint> activeYellowPortals = new HashSet<>();
	private final Set<WorldPoint> activeBluePortals = new HashSet<>();
	private final Map<WorldPoint, Integer> activePortalTicks = new HashMap<>();
	private final Map<WorldPoint, Integer> pendingLightning = new HashMap<>();

	private int playerImmunityTicks = 0;

	private final Set<NPC> savedSwordNpcs = new HashSet<>();
	private final Set<NPC> savedBoltNpcs = new HashSet<>();
	private final Set<WorldPoint> savedYellowPortals = new HashSet<>();
	private final Set<WorldPoint> savedBluePortals = new HashSet<>();
	private final Map<WorldPoint, Integer> savedPortalTicks = new HashMap<>();

	public ProjectileTracker(Client client)
	{
		this.client = client;
	}

	/**
	 * @return true if the NPC is a sepulchre projectile NPC (for detection purposes)
	 */
	public boolean onNpcSpawned(NpcSpawned event)
	{
		NPC npc = event.getNpc();
		int id = npc.getId();

		if (SepulchreConstants.BOLT_NULL_NPC_IDS.contains(id))
		{
			boltNpcs.add(npc);
			return true;
		}
		else if (SepulchreConstants.SWORD_NULL_NPC_IDS.contains(id))
		{
			swordNpcs.add(npc);
			return true;
		}
		return false;
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
			activePortalTicks.put(location, 5);
		}
		else if (SepulchreConstants.BLUE_PORTAL_GRAPHICS_IDS.contains(graphicsId))
		{
			activeBluePortals.add(location);
			activePortalTicks.put(location, 5);
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

	public void onGameTick()
	{
		activeLightning.removeIf(LightningStrike::isExpired);

		activePortalTicks.entrySet().removeIf(entry -> {
			int remaining = entry.getValue() - 1;
			if (remaining <= 0)
			{
				WorldPoint location = entry.getKey();
				activeYellowPortals.remove(location);
				activeBluePortals.remove(location);
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

	public void savePortalState()
	{
		savedYellowPortals.clear();
		savedYellowPortals.addAll(activeYellowPortals);
		savedBluePortals.clear();
		savedBluePortals.addAll(activeBluePortals);
		savedPortalTicks.clear();
		savedPortalTicks.putAll(activePortalTicks);
	}

	public void restorePortalState()
	{
		activeYellowPortals.addAll(savedYellowPortals);
		activeBluePortals.addAll(savedBluePortals);
		activePortalTicks.putAll(savedPortalTicks);
	}

	public void clearSavedPortalState()
	{
		savedYellowPortals.clear();
		savedBluePortals.clear();
		savedPortalTicks.clear();
	}

	public void reset()
	{
		activeLightning.clear();
		boltNpcs.clear();
		swordNpcs.clear();
		activeYellowPortals.clear();
		activeBluePortals.clear();
		activePortalTicks.clear();
		pendingLightning.clear();
		playerImmunityTicks = 0;
	}
}
