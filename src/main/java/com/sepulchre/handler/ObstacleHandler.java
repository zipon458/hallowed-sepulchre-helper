package com.sepulchre.handler;

import com.sepulchre.config.SepulchreConfig;
import com.sepulchre.model.CrossbowStatue;
import com.sepulchre.model.ObstacleType;
import com.sepulchre.model.SepulchreObstacle;
import com.sepulchre.model.TrackedGraphicsObject;
import com.sepulchre.model.TrackedProjectile;
import com.sepulchre.model.WizardStatue;
import com.sepulchre.util.SepulchreConstants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.events.GraphicsObjectCreated;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles tracking and updating of all obstacles in the Hallowed Sepulchre.
 * This is the central manager for all obstacle types.
 */
@Slf4j
@Singleton
public class ObstacleHandler
{
	private final Client client;
	private final SepulchreConfig config;

	@Getter
	private final List<SepulchreObstacle> activeObstacles = new ArrayList<>();

	@Getter
	private final List<CrossbowStatue> crossbowStatues = new ArrayList<>();

	@Getter
	private final List<WizardStatue> wizardStatues = new ArrayList<>();

	@Getter
	private final List<NPC> arrowNpcs = new ArrayList<>();

	@Getter
	private final List<NPC> swordNpcs = new ArrayList<>();

	@Getter
	private final List<TrackedGraphicsObject> trackedGraphics = new ArrayList<>();

	@Getter
	private final List<TrackedProjectile> trackedProjectiles = new ArrayList<>();

	@Getter
	private final List<GameObject> portalTiles = new ArrayList<>();

	// Stores portal tile locations (always present) - we check for active portals via graphics IDs
	@Getter
	private final List<WorldPoint> portalTileLocations = new ArrayList<>();

	// Stores active portal locations (detected via graphics IDs 1799, 1800, 1816)
	@Getter
	private final List<WorldPoint> activePortalLocations = new ArrayList<>();

	private final Map<WorldPoint, SepulchreObstacle> obstaclesByLocation = new HashMap<>();

	@Inject
	public ObstacleHandler(Client client, SepulchreConfig config)
	{
		this.client = client;
		this.config = config;
	}

	public void reset()
	{
		activeObstacles.clear();
		obstaclesByLocation.clear();
		crossbowStatues.clear();
		wizardStatues.clear();
		arrowNpcs.clear();
		swordNpcs.clear();
		trackedGraphics.clear();
		trackedProjectiles.clear();
		portalTiles.clear();
		portalTileLocations.clear();
		activePortalLocations.clear();
	}

	public void onGameTick()
	{
		// Update tick counters for all obstacles
		for (SepulchreObstacle obstacle : activeObstacles)
		{
			obstacle.decrementTicks();
		}

		// Update wizard statues - they handle their own tick tracking and sync
		for (WizardStatue wizard : wizardStatues)
		{
			wizard.onGameTick();
		}

		// Remove expired obstacles
		activeObstacles.removeIf(SepulchreObstacle::isExpired);

		// Remove finished graphics objects
		trackedGraphics.removeIf(TrackedGraphicsObject::isFinished);

		// Remove finished projectiles
		trackedProjectiles.removeIf(TrackedProjectile::isFinished);
	}

	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		onGameObjectSpawned(event.getGameObject());
	}

	public void onGameObjectSpawned(GameObject gameObject)
	{
		int id = gameObject.getId();

		// Track crossbow statues separately for animation-based highlighting
		if (SepulchreConstants.CROSSBOW_STATUE_IDS.contains(id))
		{
			// Avoid duplicates
			for (CrossbowStatue existing : crossbowStatues)
			{
				if (existing.getGameObject() == gameObject)
				{
					return;
				}
			}
			crossbowStatues.add(new CrossbowStatue(gameObject));
			log.debug("Tracked crossbow statue at {}", gameObject.getWorldLocation());
			return;
		}

		// Track wizard statues separately for animation-based highlighting
		if (SepulchreConstants.WIZARD_FLAME_OBJECT_IDS.contains(id))
		{
			// Avoid duplicates
			for (WizardStatue existing : wizardStatues)
			{
				if (existing.getGameObject() == gameObject)
				{
					return;
				}
			}
			WizardStatue wizard = new WizardStatue(gameObject);
			wizard.setFirePhaseTicks(config.wizardFireTicks());
			wizard.setSafePhaseTicks(config.wizardSafeTicks());
			calculateWizardFlameDangerZone(wizard, gameObject.getOrientation());
			wizardStatues.add(wizard);
			log.debug("Tracked wizard statue at {} with orientation {}", gameObject.getWorldLocation(), gameObject.getOrientation());
			return;
		}

		// Track portal tiles (the floor tiles that can have portals)
		if (SepulchreConstants.PORTAL_TILE_IDS.contains(id))
		{
			WorldPoint location = gameObject.getWorldLocation();
			if (!portalTileLocations.contains(location))
			{
				portalTileLocations.add(location);
				log.debug("Tracked portal tile location {} at {}", id, location);
			}
			return;
		}

		// Note: Active portals are now detected via graphics IDs (1799, 1800, 1816) in onGraphicsObjectCreated

		ObstacleType type = getObstacleTypeFromGameObject(id);
		if (type == null)
		{
			return;
		}

		SepulchreObstacle obstacle = new SepulchreObstacle(type, gameObject);
		initializeObstacle(obstacle, id);

		activeObstacles.add(obstacle);
		obstaclesByLocation.put(obstacle.getLocation(), obstacle);

		log.debug("Tracked {} at {}", type, obstacle.getLocation());
	}

	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		GameObject gameObject = event.getGameObject();
		WorldPoint location = gameObject.getWorldLocation();

		// Remove crossbow statues
		crossbowStatues.removeIf(statue -> statue.getGameObject() == gameObject);

		// Remove wizard statues
		wizardStatues.removeIf(statue -> statue.getGameObject() == gameObject);

		// Remove active portals (the -1 objects)
		portalTiles.remove(gameObject);

		// Remove portal tile locations if the tile itself despawns
		int id = gameObject.getId();
		if (SepulchreConstants.PORTAL_TILE_IDS.contains(id))
		{
			portalTileLocations.remove(location);
		}

		SepulchreObstacle obstacle = obstaclesByLocation.get(location);
		if (obstacle != null && obstacle.getGameObject() == gameObject)
		{
			obstacle.setGameObject(null);
			obstaclesByLocation.remove(location);
		}
	}

	public void onNpcSpawned(NpcSpawned event)
	{
		NPC npc = event.getNpc();
		int id = npc.getId();

		// Track arrow null NPCs
		if (SepulchreConstants.ARROW_NULL_NPC_IDS.contains(id))
		{
			if (!arrowNpcs.contains(npc))
			{
				arrowNpcs.add(npc);
				log.debug("Tracked arrow NPC {} at {}", id, npc.getWorldLocation());
			}
			return;
		}

		// Track sword null NPCs
		if (SepulchreConstants.SWORD_NULL_NPC_IDS.contains(id))
		{
			if (!swordNpcs.contains(npc))
			{
				swordNpcs.add(npc);
				log.debug("Tracked sword NPC {} at {}", id, npc.getWorldLocation());
			}
			return;
		}

		ObstacleType type = getObstacleTypeFromNpc(id);
		if (type == null)
		{
			return;
		}

		SepulchreObstacle obstacle = new SepulchreObstacle(type, npc);
		initializeNpcObstacle(obstacle, id);

		activeObstacles.add(obstacle);

		log.debug("Tracked NPC {} at {}", type, obstacle.getLocation());
	}

	public void onNpcDespawned(NpcDespawned event)
	{
		NPC npc = event.getNpc();

		arrowNpcs.remove(npc);
		swordNpcs.remove(npc);
		activeObstacles.removeIf(obstacle -> obstacle.getNpc() == npc);
	}

	public void onProjectileMoved(ProjectileMoved event)
	{
		int projectileId = event.getProjectile().getId();

		// Handle projectile-based hazards (arrows, crossbow bolts, wizard attacks)
		if (SepulchreConstants.ARROW_PROJECTILE_IDS.contains(projectileId) ||
			SepulchreConstants.CROSSBOW_PROJECTILE_IDS.contains(projectileId))
		{
			// Track projectile danger zones
			// Implementation will depend on specific projectile behavior
		}
	}

	public void onGraphicsObjectCreated(GraphicsObjectCreated event)
	{
		int graphicsId = event.getGraphicsObject().getId();
		WorldPoint location = WorldPoint.fromLocal(client, event.getGraphicsObject().getLocation());

		// Track all graphics objects for debugging
		TrackedGraphicsObject tracked = new TrackedGraphicsObject(
			event.getGraphicsObject(),
			location,
			graphicsId,
			client.getGameCycle()
		);
		trackedGraphics.add(tracked);

		log.debug("Graphics object spawned: ID={} at {}", graphicsId, location);

		// Handle portal graphics (1799, 1800, 1816 indicate active portal)
		if (SepulchreConstants.PORTAL_GRAPHICS_IDS.contains(graphicsId))
		{
			// Check if this graphics is at a portal tile location
			if (portalTileLocations.contains(location))
			{
				if (!activePortalLocations.contains(location))
				{
					activePortalLocations.add(location);
					log.debug("Detected active portal via graphics {} at {}", graphicsId, location);
				}
			}
		}

		// Handle wizard flames
		if (SepulchreConstants.WIZARD_FLAME_GRAPHICS_IDS.contains(graphicsId))
		{
			SepulchreObstacle flame = new SepulchreObstacle(ObstacleType.WIZARD, location);
			flame.addDangerTile(location);
			flame.setTicksUntilActive(0); // Already dangerous
			activeObstacles.add(flame);
		}

		// Handle graphics-based hazards (lightning strikes, etc.)
		if (SepulchreConstants.LIGHTNING_GRAPHICS_IDS.contains(graphicsId))
		{
			SepulchreObstacle lightning = new SepulchreObstacle(ObstacleType.LIGHTNING, location);
			lightning.addDangerTile(location);
			lightning.setTicksUntilActive(1);
			activeObstacles.add(lightning);
		}
	}

	private ObstacleType getObstacleTypeFromGameObject(int id)
	{
		if (SepulchreConstants.ARROW_TRAP_IDS.contains(id))
		{
			return ObstacleType.ARROW;
		}
		if (SepulchreConstants.SWORD_STATUE_IDS.contains(id))
		{
			return ObstacleType.SWORD;
		}
		if (SepulchreConstants.CROSSBOW_STATUE_IDS.contains(id))
		{
			return ObstacleType.CROSSBOW;
		}
		// Note: Wizard flame objects are now tracked separately via wizardStatues
		if (SepulchreConstants.COFFIN_IDS.contains(id))
		{
			return ObstacleType.COFFIN;
		}
		if (SepulchreConstants.GRAND_COFFIN_IDS.contains(id))
		{
			return ObstacleType.GRAND_COFFIN;
		}
		// Note: Portals are now tracked separately via portalTiles list
		return null;
	}

	private ObstacleType getObstacleTypeFromNpc(int id)
	{
		if (SepulchreConstants.WIZARD_NPC_IDS.contains(id))
		{
			return ObstacleType.WIZARD;
		}
		return null;
	}

	private void initializeObstacle(SepulchreObstacle obstacle, int gameObjectId)
	{
		switch (obstacle.getType())
		{
			case ARROW:
				initializeArrowTrap(obstacle);
				break;
			case SWORD:
				initializeSwordStatue(obstacle);
				break;
			case CROSSBOW:
				initializeCrossbow(obstacle);
				break;
			case WIZARD:
				initializeWizardFlame(obstacle);
				break;
			default:
				break;
		}
	}

	private void initializeNpcObstacle(SepulchreObstacle obstacle, int npcId)
	{
		if (obstacle.getType() == ObstacleType.WIZARD)
		{
			initializeWizard(obstacle);
		}
	}

	private void initializeArrowTrap(SepulchreObstacle obstacle)
	{
		// Arrow traps have a specific cycle and danger zone
		// This will need to be refined based on actual game data
		obstacle.setCycleTicks(SepulchreConstants.ARROW_CYCLE_TICKS);
		obstacle.setTicksUntilActive(SepulchreConstants.ARROW_CYCLE_TICKS);

		// Calculate danger zone based on arrow direction
		calculateArrowDangerZone(obstacle);
	}

	private void initializeSwordStatue(SepulchreObstacle obstacle)
	{
		obstacle.setCycleTicks(SepulchreConstants.SWORD_CYCLE_TICKS);
		obstacle.setTicksUntilActive(SepulchreConstants.SWORD_CYCLE_TICKS);

		// Swords typically have a 3x3 danger zone
		calculateSwordDangerZone(obstacle);
	}

	private void initializeCrossbow(SepulchreObstacle obstacle)
	{
		obstacle.setCycleTicks(SepulchreConstants.CROSSBOW_CYCLE_TICKS);
		obstacle.setTicksUntilActive(SepulchreConstants.CROSSBOW_CYCLE_TICKS);
	}

	private void initializeWizard(SepulchreObstacle obstacle)
	{
		obstacle.setCycleTicks(SepulchreConstants.WIZARD_CYCLE_TICKS);
		obstacle.setTicksUntilActive(SepulchreConstants.WIZARD_CYCLE_TICKS);
	}

	private void initializeWizardFlame(SepulchreObstacle obstacle)
	{
		// Wizard flames use an 8-tick cycle (0-7)
		// Fire happens at ticks 5,4,3,2 (counting down)
		// Start at 5 so danger begins when fire starts
		obstacle.setCycleTicks(SepulchreConstants.WIZARD_CYCLE_TICKS);
		obstacle.setTicksUntilActive(5);

		GameObject gameObject = obstacle.getGameObject();
		if (gameObject == null)
		{
			return;
		}

		WorldPoint location = obstacle.getLocation();
		int orientation = gameObject.getOrientation();

		// Log orientation for debugging
		log.debug("Wizard flame at {} with orientation {}", location, orientation);

		// Calculate danger tiles based on orientation
		// Orientation is in JAU (Jagex Angle Units): 0=South, 512=West, 1024=North, 1536=East
		// Flames extend from the wizard's right hand in the direction they face
		calculateWizardFlameDangerZone(obstacle, orientation);
	}

	private void calculateWizardFlameDangerZone(SepulchreObstacle obstacle, int orientation)
	{
		WorldPoint location = obstacle.getLocation();
		int[] offsets = getWizardFlameOffsets(orientation);

		int startX = location.getX() + offsets[2];
		int startY = location.getY() + offsets[3];

		for (int i = offsets[4]; i <= offsets[4] + 2; i++)
		{
			obstacle.addDangerTile(new WorldPoint(
				startX + (offsets[0] * i),
				startY + (offsets[1] * i),
				location.getPlane()
			));
		}
	}

	private void calculateWizardFlameDangerZone(WizardStatue wizard, int orientation)
	{
		WorldPoint location = wizard.getLocation();
		int[] offsets = getWizardFlameOffsets(orientation);

		int startX = location.getX() + offsets[2];
		int startY = location.getY() + offsets[3];

		for (int i = offsets[4]; i <= offsets[4] + 2; i++)
		{
			wizard.addDangerTile(new WorldPoint(
				startX + (offsets[0] * i),
				startY + (offsets[1] * i),
				location.getPlane()
			));
		}
	}

	/**
	 * Returns flame offsets: [dx, dy, rightHandOffsetX, rightHandOffsetY, startOffset]
	 */
	private int[] getWizardFlameOffsets(int orientation)
	{
		// Determine direction and right-hand offset based on orientation
		// Wizard is 2x2, flames come from right hand
		// 0 = South, 512 = West, 1024 = North, 1536 = East
		int dx = 0;
		int dy = 0;
		int rightHandOffsetX = 0;
		int rightHandOffsetY = 0;
		int startOffset = 2; // Default: skip 2 tiles (wizard body)

		if (orientation >= 1280 && orientation < 1792) // Facing East (~1536) - flames go west->east
		{
			dx = 1;
			dy = 0;
			rightHandOffsetX = 0;
			rightHandOffsetY = 0;
			startOffset = 2;
		}
		else if (orientation >= 768 && orientation < 1280) // Facing North (~1024)
		{
			dx = 0;
			dy = 1;
			rightHandOffsetX = 1;
			rightHandOffsetY = 0;
		}
		else if (orientation >= 256 && orientation < 768) // Facing West (~512) - flames go east->west
		{
			dx = -1;
			dy = 0;
			rightHandOffsetX = 0;
			rightHandOffsetY = 1;
			startOffset = 1;
		}
		else // Facing South (~0 or ~2048)
		{
			dx = 0;
			dy = -1;
			rightHandOffsetX = 0;
			rightHandOffsetY = 0;
			startOffset = 1;
		}

		return new int[] { dx, dy, rightHandOffsetX, rightHandOffsetY, startOffset };
	}

	private void calculateArrowDangerZone(SepulchreObstacle obstacle)
	{
		WorldPoint location = obstacle.getLocation();
		// Arrows typically shoot in a line - this needs to be refined based on orientation
		for (int i = 1; i <= 5; i++)
		{
			obstacle.addDangerTile(new WorldPoint(location.getX() + i, location.getY(), location.getPlane()));
		}
	}

	private void calculateSwordDangerZone(SepulchreObstacle obstacle)
	{
		WorldPoint location = obstacle.getLocation();
		// Swords have a 3x3 danger zone
		for (int dx = -1; dx <= 1; dx++)
		{
			for (int dy = -1; dy <= 1; dy++)
			{
				obstacle.addDangerTile(new WorldPoint(location.getX() + dx, location.getY() + dy, location.getPlane()));
			}
		}
	}
}
