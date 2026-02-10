package com.sepulchre.handler;

import com.sepulchre.config.SepulchreConfig;
import com.sepulchre.model.CrossbowStatue;
import com.sepulchre.model.WizardCyclePhaseTracker;
import com.sepulchre.model.LightningStrike;
import com.sepulchre.model.SepulchreRoute;
import com.sepulchre.model.SwordStatue;
import com.sepulchre.model.WizardStatue;
import com.sepulchre.util.SepulchreConstants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.WallObject;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.GraphicsObjectCreated;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
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
	private final ProjectileTracker projectileTracker;

	@Getter
	private final List<CrossbowStatue> crossbowStatues = new ArrayList<>();

	@Getter
	private final List<WizardStatue> wizardStatues = new ArrayList<>();

	@Getter
	private final List<SwordStatue> swordStatues = new ArrayList<>();

	@Getter
	private final List<GameObject> magicalObelisks = new ArrayList<>();

	@Getter
	private final List<GameObject> coffins = new ArrayList<>();

	@Getter
	private final List<GameObject> grandCoffins = new ArrayList<>();

	@Getter
	private final List<GroundObject> bridges = new ArrayList<>();

	@Getter
	private final List<GameObject> grapples = new ArrayList<>();

	@Getter
	private final List<GameObject> portalFrames = new ArrayList<>();

	@Getter
	private final List<GameObject> braziers = new ArrayList<>();

	@Getter
	private final List<GameObject> holyBarriers = new ArrayList<>();

	@Getter
	private final List<WallObject> floor5Barriers = new ArrayList<>();

	@Getter
	private final List<GameObject> stairs = new ArrayList<>();

	@Getter
	private final List<GroundObject> platforms = new ArrayList<>();

	@Getter
	private final List<WallObject> gates = new ArrayList<>();

	@Getter
	private final SkillObstacleManager skillObstacleManager;

	@Getter
	private final WizardCyclePhaseTracker wizardCyclePhaseTracker = new WizardCyclePhaseTracker();

	@Getter
	private final FloorState floorState;

	@Inject
	public ObstacleHandler(Client client, SepulchreConfig config)
	{
		this.client = client;
		this.config = config;
		this.floorState = new FloorState(client, config);
		this.floorState.setOnLowerSectionEntered(this::scanForExistingGroundObjects);
		this.projectileTracker = new ProjectileTracker(client);
		this.skillObstacleManager = new SkillObstacleManager(client);
	}

	public boolean shouldShowForCurrentRoute(net.runelite.api.coords.LocalPoint localPoint)
	{
		return floorState.shouldShowForCurrentRoute(localPoint);
	}

	public boolean shouldShowForCurrentRoute(WorldPoint instanceLocation)
	{
		return floorState.shouldShowForCurrentRoute(instanceLocation);
	}

	public boolean isCoffinLootingEnabledForCurrentFloor()
	{
		return floorState.isCoffinLootingEnabledForCurrentFloor();
	}

	public boolean isGrandCoffinLootingEnabled()
	{
		return floorState.isGrandCoffinLootingEnabled();
	}

	public int getPlayerMaxFloor()
	{
		return floorState.getPlayerMaxFloor();
	}

	public int getCurrentFloor()
	{
		return floorState.getCurrentFloor();
	}

	public void setCurrentFloor(int floor)
	{
		floorState.setCurrentFloor(floor);
	}

	public SepulchreRoute getCurrentRoute()
	{
		return floorState.getCurrentRoute();
	}

	public void setCurrentRoute(SepulchreRoute route)
	{
		floorState.setCurrentRoute(route);
	}

	public int getFloorStartPlane()
	{
		return floorState.getFloorStartPlane();
	}

	public boolean isInLowerSection()
	{
		return floorState.isInLowerSection();
	}

	public int getFloorTicks()
	{
		return floorState.getFloorTicks();
	}

	public int getRunTicks()
	{
		return floorState.getRunTicks();
	}

	public boolean isTimerPaused()
	{
		return floorState.isTimerPaused();
	}

	public boolean isTimerStarted()
	{
		return floorState.isTimerStarted();
	}

	public boolean isDoorToNextFloorClosed()
	{
		return floorState.isDoorToNextFloorClosed();
	}

	public List<LightningStrike> getActiveLightning()
	{
		return projectileTracker.getActiveLightning();
	}

	public Set<NPC> getBoltNpcs()
	{
		return projectileTracker.getBoltNpcs();
	}

	public Set<NPC> getSwordNpcs()
	{
		return projectileTracker.getSwordNpcs();
	}

	public Set<WorldPoint> getActiveYellowPortals()
	{
		return projectileTracker.getActiveYellowPortals();
	}

	public Set<WorldPoint> getActiveBluePortals()
	{
		return projectileTracker.getActiveBluePortals();
	}

	public Map<WorldPoint, Integer> getActivePortalGraphics()
	{
		return projectileTracker.getActivePortalGraphics();
	}

	public int getPlayerImmunityTicks()
	{
		return projectileTracker.getPlayerImmunityTicks();
	}

	public int getBluePortalRemainingTicks(WorldPoint location)
	{
		return projectileTracker.getBluePortalRemainingTicks(location);
	}

	public int getYellowPortalRemainingTicks(WorldPoint location)
	{
		return projectileTracker.getYellowPortalRemainingTicks(location);
	}

	public boolean isPlayerImmune()
	{
		return projectileTracker.isPlayerImmune();
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
		floorState.onDoorToNextFloorClosed();
	}

	public void setFloorStartPlane(int plane)
	{
		floorState.setFloorStartPlane(plane);
	}

	public void updateLowerSectionStatus(int currentPlane)
	{
		floorState.updateLowerSectionStatus(currentPlane);
	}

	public void restoreFloorState(boolean wasInLowerSection, int previousStartPlane)
	{
		floorState.restoreFloorState(wasInLowerSection, previousStartPlane);
	}

	public void saveProjectileNpcs()
	{
		projectileTracker.saveProjectileNpcs();
	}

	public void restoreProjectileNpcs()
	{
		projectileTracker.restoreProjectileNpcs();
	}

	public void clearSavedProjectileNpcs()
	{
		projectileTracker.clearSavedProjectileNpcs();
	}

	public void savePortalState()
	{
		projectileTracker.savePortalState();
	}

	public void restorePortalState()
	{
		projectileTracker.restorePortalState();
	}

	public void clearSavedPortalState()
	{
		projectileTracker.clearSavedPortalState();
	}

	public void onFloorEntered(boolean isFirstFloor)
	{
		floorState.onFloorEntered(isFirstFloor);
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
		projectileTracker.reset();
		crossbowStatues.clear();
		wizardStatues.clear();
		swordStatues.clear();
		magicalObelisks.clear();
		coffins.clear();
		grandCoffins.clear();
		bridges.clear();
		grapples.clear();
		portalFrames.clear();
		braziers.clear();
		holyBarriers.clear();
		floor5Barriers.clear();
		stairs.clear();
		platforms.clear();
		gates.clear();
		skillObstacleManager.reset();
		floorState.reset();
		wizardCyclePhaseTracker.reset();
	}

	public void scanForExistingGroundObjects()
	{
		net.runelite.api.Scene scene = client.getScene();
		net.runelite.api.Tile[][][] tiles = scene.getTiles();

		for (int z = 0; z < tiles.length; z++)
		{
			for (int x = 0; x < tiles[z].length; x++)
			{
				for (int y = 0; y < tiles[z][x].length; y++)
				{
					net.runelite.api.Tile tile = tiles[z][x][y];
					if (tile == null)
					{
						continue;
					}

					GroundObject groundObject = tile.getGroundObject();
					if (groundObject == null)
					{
						continue;
					}

					int id = groundObject.getId();

					if (SepulchreConstants.BRIDGE_OBJECT_IDS.contains(id))
					{
						if (!bridges.contains(groundObject))
						{
							bridges.add(groundObject);
							associateObstacleWithTrackers(groundObject);
						}
					}
					else if (SepulchreConstants.ALL_PLATFORM_IDS.contains(id))
					{
						if (!platforms.contains(groundObject))
						{
							platforms.add(groundObject);
						}
					}
				}
			}
		}
	}

	public void onGameTick()
	{
		floorState.updateTimers();

		for (CrossbowStatue crossbow : crossbowStatues)
		{
			crossbow.onGameTick();
		}

		for (LightningStrike lightning : projectileTracker.getActiveLightning())
		{
			lightning.onGameTick();
		}

		for (WizardStatue wizard : wizardStatues)
		{
			if (floorState.getCurrentFloor() == 5)
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

		if (floorState.getCurrentFloor() == 4)
		{
			wizardCyclePhaseTracker.onGameTick(wizardStatues);
		}

		projectileTracker.onGameTick();
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
			wizard.setFirePhaseTicks(floorState.getCurrentFloor() == 5 ? 1 : 2);
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
			return;
		}

		if (SepulchreConstants.COFFIN_OBJECT_IDS.contains(id))
		{
			notifySepulchreDetected();
			if (!coffins.contains(gameObject))
			{
				coffins.add(gameObject);
				createCoffinTracker(gameObject);
			}
			return;
		}

		if (id == SepulchreConstants.GRAND_COFFIN_OBJECT_ID)
		{
			notifySepulchreDetected();
			if (!grandCoffins.contains(gameObject))
			{
				grandCoffins.add(gameObject);
			}
			return;
		}

		if (id == SepulchreConstants.GRAPPLE_OBJECT_ID)
		{
			notifySepulchreDetected();
			if (!grapples.contains(gameObject))
			{
				grapples.add(gameObject);
				associateObstacleWithTrackers(gameObject);
			}
			return;
		}

		if (id == SepulchreConstants.PORTAL_FRAME_OBJECT_ID)
		{
			notifySepulchreDetected();
			if (!portalFrames.contains(gameObject))
			{
				portalFrames.add(gameObject);
				associateObstacleWithTrackers(gameObject);
			}
			return;
		}

		if (SepulchreConstants.BRAZIER_OBJECT_IDS.contains(id))
		{
			notifySepulchreDetected();
			if (!braziers.contains(gameObject))
			{
				braziers.add(gameObject);
			}
			return;
		}

		if (id == SepulchreConstants.HOLY_BARRIER_OBJECT_ID)
		{
			notifySepulchreDetected();
			if (!holyBarriers.contains(gameObject))
			{
				holyBarriers.add(gameObject);
			}
			return;
		}

		if (SepulchreConstants.STAIRS_IDS.contains(id))
		{
			notifySepulchreDetected();
			if (!stairs.contains(gameObject))
			{
				stairs.add(gameObject);
			}
		}
	}

	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		GameObject gameObject = event.getGameObject();
		int id = gameObject.getId();

		if (SepulchreConstants.CROSSBOW_STATUE_IDS.contains(id))
		{
			crossbowStatues.removeIf(statue -> statue.getGameObject() == gameObject);
		}
		else if (SepulchreConstants.WIZARD_FLAME_OBJECT_IDS.contains(id))
		{
			wizardStatues.removeIf(statue -> statue.getGameObject() == gameObject);
		}
		else if (SepulchreConstants.SWORD_STATUE_IDS.contains(id))
		{
			swordStatues.removeIf(statue -> statue.getGameObject() == gameObject);
		}
		else if (id == SepulchreConstants.MAGICAL_OBELISK_ID)
		{
			magicalObelisks.remove(gameObject);
		}
		else if (SepulchreConstants.COFFIN_OBJECT_IDS.contains(id))
		{
			coffins.remove(gameObject);
		}
		else if (id == SepulchreConstants.GRAND_COFFIN_OBJECT_ID)
		{
			grandCoffins.remove(gameObject);
		}
		else if (id == SepulchreConstants.GRAPPLE_OBJECT_ID)
		{
			grapples.remove(gameObject);
		}
		else if (id == SepulchreConstants.PORTAL_FRAME_OBJECT_ID)
		{
			portalFrames.remove(gameObject);
		}
		else if (SepulchreConstants.BRAZIER_OBJECT_IDS.contains(id))
		{
			braziers.remove(gameObject);
		}
		else if (id == SepulchreConstants.HOLY_BARRIER_OBJECT_ID)
		{
			holyBarriers.remove(gameObject);
		}
		else if (SepulchreConstants.STAIRS_IDS.contains(id))
		{
			stairs.remove(gameObject);
		}
	}

	public void onWallObjectSpawned(WallObjectSpawned event)
	{
		WallObject wallObject = event.getWallObject();
		int id = wallObject.getId();

		if (id == SepulchreConstants.FLOOR_5_BARRIER_OBJECT_ID)
		{
			notifySepulchreDetected();
			if (!floor5Barriers.contains(wallObject))
			{
				floor5Barriers.add(wallObject);
			}
			return;
		}

		if (id == SepulchreConstants.GATE_WALL_OBJECT_ID)
		{
			notifySepulchreDetected();
			if (!gates.contains(wallObject))
			{
				gates.add(wallObject);
			}
		}
	}

	public void onWallObjectDespawned(WallObjectDespawned event)
	{
		WallObject wallObject = event.getWallObject();
		floor5Barriers.remove(wallObject);
		gates.remove(wallObject);
	}

	public void onGroundObjectSpawned(GroundObjectSpawned event)
	{
		GroundObject groundObject = event.getGroundObject();
		int id = groundObject.getId();

		if (SepulchreConstants.BRIDGE_OBJECT_IDS.contains(id))
		{
			notifySepulchreDetected();
			if (!bridges.contains(groundObject))
			{
				bridges.add(groundObject);
				associateObstacleWithTrackers(groundObject);
			}
			return;
		}

		if (SepulchreConstants.ALL_PLATFORM_IDS.contains(id))
		{
			notifySepulchreDetected();
			if (!platforms.contains(groundObject))
			{
				platforms.add(groundObject);
			}
		}
	}

	public void onGroundObjectDespawned(GroundObjectDespawned event)
	{
		GroundObject groundObject = event.getGroundObject();
		bridges.remove(groundObject);
		platforms.remove(groundObject);
	}

	public void onNpcSpawned(NpcSpawned event)
	{
		if (projectileTracker.onNpcSpawned(event))
		{
			notifySepulchreDetected();
		}
	}

	public void onNpcDespawned(NpcDespawned event)
	{
		projectileTracker.onNpcDespawned(event);
	}

	public void onGraphicsObjectCreated(GraphicsObjectCreated event)
	{
		projectileTracker.onGraphicsObjectCreated(event);
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

	private void createCoffinTracker(GameObject coffin)
	{
		skillObstacleManager.createCoffinTracker(coffin, bridges, grapples, portalFrames);
	}

	private void associateObstacleWithTrackers(TileObject obstacle)
	{
		skillObstacleManager.associateObstacleWithTrackers(obstacle);
	}

	public void onBridgeBuilt()
	{
		skillObstacleManager.onBridgeBuilt(bridges);
	}

	public void onBridgeCrossed()
	{
		skillObstacleManager.onBridgeCrossed(bridges);
	}

	public void onGrappleUsed()
	{
		skillObstacleManager.onGrappleUsed(grapples);
	}

	public void onPortalUsed()
	{
		skillObstacleManager.onPortalUsed(portalFrames);
	}

	public void onBrazierSacrificed()
	{
		skillObstacleManager.onBrazierSacrificed(holyBarriers);
	}

	public void checkBarrierReturns(WorldPoint playerCanonicalPosition)
	{
		skillObstacleManager.checkBarrierReturns(playerCanonicalPosition);
	}

	public boolean shouldHighlightBarrier(GameObject barrier)
	{
		return skillObstacleManager.shouldHighlightBarrier(barrier);
	}

	public void onCoffinLooted()
	{
		skillObstacleManager.onCoffinLooted();
	}

	public boolean shouldHighlightObstacle(TileObject obstacle)
	{
		return skillObstacleManager.shouldHighlightObstacle(obstacle);
	}

	public boolean hasObstacleBeenUsed(TileObject obstacle)
	{
		return skillObstacleManager.hasObstacleBeenUsed(obstacle);
	}
}
