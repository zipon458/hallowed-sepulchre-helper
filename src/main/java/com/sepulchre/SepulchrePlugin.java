package com.sepulchre;

import com.google.inject.Provides;
import com.sepulchre.config.SepulchreConfig;
import com.sepulchre.handler.ObstacleHandler;
import com.sepulchre.overlay.SepulchreSceneOverlay;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@PluginDescriptor(
	name = "Sepulchre Helper",
	description = "Hallowed Sepulchre helper with more customization",
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

	private boolean detectedSepulchreObjects = false;

	@Override
	protected void startUp()
	{
		overlayManager.add(sceneOverlay);
		obstacleHandler.setOnSepulchreDetected(this::onSepulchreObjectDetected);
		reset();
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(sceneOverlay);
		reset();
	}

	private void reset()
	{
		inSepulchre = false;
		detectedSepulchreObjects = false;
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
			obstacleHandler.reset();
			detectedSepulchreObjects = false;
			inSepulchre = false;
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
		boolean wasInSepulchre = inSepulchre;
		inSepulchre = detectedSepulchreObjects;

		if (!inSepulchre && wasInSepulchre)
		{
			reset();
		}
	}

	public void onSepulchreObjectDetected()
	{
		if (!detectedSepulchreObjects)
		{
			detectedSepulchreObjects = true;
			inSepulchre = true;
		}
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		obstacleHandler.onGameObjectSpawned(event);
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		if (!inSepulchre) return;
		obstacleHandler.onGameObjectDespawned(event);
	}

	@Subscribe
	public void onGroundObjectSpawned(GroundObjectSpawned event)
	{
		if (!inSepulchre) return;
		obstacleHandler.onGroundObjectSpawned(event);
	}

	@Subscribe
	public void onGroundObjectDespawned(GroundObjectDespawned event)
	{
		if (!inSepulchre) return;
		obstacleHandler.onGroundObjectDespawned(event);
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		obstacleHandler.onNpcSpawned(event);
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		if (!inSepulchre) return;
		obstacleHandler.onNpcDespawned(event);
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
}
