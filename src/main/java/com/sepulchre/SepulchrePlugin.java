package com.sepulchre;

import com.google.inject.Provides;
import com.sepulchre.config.SepulchreConfig;
import com.sepulchre.handler.ObstacleHandler;
import com.sepulchre.overlay.SepulchreSceneOverlay;
import com.sepulchre.util.SepulchreConstants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
	name = "Sepulchre Helper",
	description = "Assists with the Hallowed Sepulchre minigame",
	tags = {"sepulchre", "hallowed", "agility", "darkmeyer"}
)
public class SepulchrePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private SepulchreConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private SepulchreSceneOverlay sceneOverlay;

	@Inject
	private ObstacleHandler obstacleHandler;

	@Getter
	private boolean inSepulchre;

	@Getter
	private int currentFloor;

	@Override
	protected void startUp() throws Exception
	{
		log.debug("Hallowed Sepulchre plugin started");
		overlayManager.add(sceneOverlay);
		reset();
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.debug("Hallowed Sepulchre plugin stopped");
		overlayManager.remove(sceneOverlay);
		reset();
	}

	private void reset()
	{
		inSepulchre = false;
		currentFloor = 0;
		obstacleHandler.reset();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		GameState state = event.getGameState();

		if (state == GameState.LOGIN_SCREEN || state == GameState.HOPPING)
		{
			reset();
		}
		else if (state == GameState.LOADING)
		{
			// Scene is being reloaded - clear tracked objects to avoid duplicates
			// They'll be re-added via GameObjectSpawned events
			obstacleHandler.reset();
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		updateLocation();

		if (inSepulchre)
		{
			obstacleHandler.onGameTick();
		}
	}

	private void updateLocation()
	{
		WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
		int regionId = playerLocation.getRegionID();

		// Debug: log current region
		log.debug("Current region: {}", regionId);

		boolean wasInSepulchre = inSepulchre;
		// TODO: Temporarily always true for debugging - remove this later
		inSepulchre = true;
		// inSepulchre = SepulchreConstants.SEPULCHRE_REGIONS.contains(regionId);

		if (inSepulchre)
		{
			currentFloor = determineFloor(regionId);
		}
		else if (wasInSepulchre)
		{
			reset();
		}
	}

	private int determineFloor(int regionId)
	{
		if (SepulchreConstants.FLOOR_1_REGIONS.contains(regionId)) return 1;
		if (SepulchreConstants.FLOOR_2_REGIONS.contains(regionId)) return 2;
		if (SepulchreConstants.FLOOR_3_REGIONS.contains(regionId)) return 3;
		if (SepulchreConstants.FLOOR_4_REGIONS.contains(regionId)) return 4;
		if (SepulchreConstants.FLOOR_5_REGIONS.contains(regionId)) return 5;
		return 0;
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		if (!inSepulchre) return;
		obstacleHandler.onGameObjectSpawned(event);
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		if (!inSepulchre) return;
		obstacleHandler.onGameObjectDespawned(event);
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		if (!inSepulchre) return;
		obstacleHandler.onNpcSpawned(event);
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		if (!inSepulchre) return;
		obstacleHandler.onNpcDespawned(event);
	}

	@Subscribe
	public void onProjectileMoved(ProjectileMoved event)
	{
		if (!inSepulchre) return;
		obstacleHandler.onProjectileMoved(event);
	}

	@Subscribe
	public void onGraphicsObjectCreated(GraphicsObjectCreated event)
	{
		if (!inSepulchre) return;
		obstacleHandler.onGraphicsObjectCreated(event);
	}

	@Provides
	SepulchreConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SepulchreConfig.class);
	}

	public Client getClient()
	{
		return client;
	}

	public SepulchreConfig getConfig()
	{
		return config;
	}
}
