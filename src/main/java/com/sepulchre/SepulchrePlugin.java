package com.sepulchre;

import com.google.inject.Provides;
import com.sepulchre.config.SepulchreConfig;
import com.sepulchre.handler.ObstacleHandler;
import com.sepulchre.model.SepulchreRoute;
import com.sepulchre.overlay.InfoPanelOverlay;
import com.sepulchre.overlay.RunTimerOverlay;
import com.sepulchre.overlay.SepulchreSceneOverlay;
import com.sepulchre.util.SepulchreConstants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.Map;

@Slf4j
@PluginDescriptor(
	name = "Hallowed Sepulchre Helper",
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
	private InfoPanelOverlay infoPanelOverlay;

	@Inject
	private RunTimerOverlay runTimerOverlay;

	@Inject
	private ObstacleHandler obstacleHandler;

	@Getter
	private boolean inSepulchre;

	private boolean pendingLocationVerification = false;
	private boolean verifiedThisTick = false;
	private boolean pendingRouteClassification = false;
	private int pendingRouteFloor = 0;

	@Override
	protected void startUp()
	{
		overlayManager.add(sceneOverlay);
		overlayManager.add(infoPanelOverlay);
		overlayManager.add(runTimerOverlay);
		obstacleHandler.setOnSepulchreDetected(this::onSepulchreObjectDetected);
		reset();
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(sceneOverlay);
		overlayManager.remove(infoPanelOverlay);
		overlayManager.remove(runTimerOverlay);
		reset();
	}

	private void reset()
	{
		inSepulchre = false;
		pendingLocationVerification = false;
		verifiedThisTick = false;
		pendingRouteClassification = false;
		pendingRouteFloor = 0;
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
			boolean wasDoorClosed = obstacleHandler.isDoorToNextFloorClosed();
			int previousFloor = obstacleHandler.getCurrentFloor();
			SepulchreRoute previousRoute = obstacleHandler.getCurrentRoute();
			boolean wasInLowerSection = obstacleHandler.isInLowerSection();
			int previousStartPlane = obstacleHandler.getFloorStartPlane();
			int previousFloorTicks = obstacleHandler.getFloorTicks();
			int previousRunTicks = obstacleHandler.getRunTicks();
			boolean wasTimerStarted = obstacleHandler.isTimerStarted();
			boolean wasTimerPaused = obstacleHandler.isTimerPaused();
			boolean wasInSepulchre = inSepulchre;

			if (wasInSepulchre)
			{
				obstacleHandler.saveProjectileNpcs();
			}

			obstacleHandler.reset();

			if (wasInSepulchre)
			{
				obstacleHandler.restoreProjectileNpcs();

				if (wasDoorClosed)
				{
					obstacleHandler.onDoorToNextFloorClosed();
				}
				obstacleHandler.setCurrentFloor(previousFloor);
				obstacleHandler.setCurrentRoute(previousRoute);
				obstacleHandler.restoreFloorState(wasInLowerSection, previousStartPlane, previousFloorTicks);
				obstacleHandler.setRunTicks(previousRunTicks);
				obstacleHandler.setTimerStarted(wasTimerStarted);
				obstacleHandler.setTimerPaused(wasTimerPaused);
				pendingLocationVerification = true;
				verifiedThisTick = false;
			}
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		if (pendingLocationVerification)
		{
			if (!verifiedThisTick)
			{
				reset();
				return;
			}
			pendingLocationVerification = false;
			verifiedThisTick = false;
			obstacleHandler.clearSavedProjectileNpcs();
		}

		if (pendingRouteClassification && inSepulchre)
		{
			classifySpawnTile(pendingRouteFloor);
			pendingRouteClassification = false;
			pendingRouteFloor = 0;
		}

		if (inSepulchre)
		{
			updateTimerPausedState();
			obstacleHandler.onGameTick();

			Player player = client.getLocalPlayer();
			if (player != null)
			{
				WorldPoint location = WorldPoint.fromLocalInstance(client, player.getLocalLocation());
				if (location != null)
				{
					obstacleHandler.updateLowerSectionStatus(location.getPlane());
				}
			}
		}
	}

	private void onSepulchreObjectDetected()
	{
		verifiedThisTick = true;
	}

	private void updateTimerPausedState()
	{
		Widget timerWidget = client.getWidget(SepulchreConstants.TIMER_WIDGET_GROUP, SepulchreConstants.TIMER_WIDGET_CHILD);
		if (timerWidget == null || timerWidget.getText() == null || timerWidget.getText().isEmpty())
		{
			if (!obstacleHandler.isTimerStarted())
			{
				obstacleHandler.setTimerPaused(true);
			}
			return;
		}

		obstacleHandler.setTimerStarted(true);

		String text = timerWidget.getText();
		boolean isPaused = text.contains("(Paused)");
		obstacleHandler.setTimerPaused(isPaused);
	}

	private void classifySpawnTile(int floor)
	{
		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return;
		}

		LocalPoint localPoint = player.getLocalLocation();
		if (localPoint == null)
		{
			return;
		}

		WorldPoint canonicalSpawn = WorldPoint.fromLocalInstance(client, localPoint);
		if (canonicalSpawn == null)
		{
			return;
		}

		obstacleHandler.setFloorStartPlane(canonicalSpawn.getPlane());

		if (floor == 5)
		{
			obstacleHandler.setCurrentRoute(SepulchreRoute.FLOOR_5_SINGLE);
			return;
		}

		Map<WorldPoint, SepulchreRoute> spawnTiles = SepulchreConstants.getSpawnTilesForFloor(floor);
		if (spawnTiles == null || spawnTiles.isEmpty())
		{
			obstacleHandler.setCurrentRoute(SepulchreRoute.UNKNOWN);
			return;
		}

		SepulchreRoute route = spawnTiles.get(canonicalSpawn);
		if (route == null)
		{
			obstacleHandler.setCurrentRoute(SepulchreRoute.UNKNOWN);
			return;
		}

		obstacleHandler.setCurrentRoute(route);
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		obstacleHandler.onGameObjectSpawned(event);
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		if (!inSepulchre)
		{
			return;
		}
		obstacleHandler.onGameObjectDespawned(event);
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		obstacleHandler.onNpcSpawned(event);
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		if (!inSepulchre)
		{
			return;
		}
		obstacleHandler.onNpcDespawned(event);
	}

	@Subscribe
	public void onGraphicsObjectCreated(GraphicsObjectCreated event)
	{
		if (!inSepulchre)
		{
			return;
		}
		obstacleHandler.onGraphicsObjectCreated(event);
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		ChatMessageType type = event.getType();
		if (type != ChatMessageType.GAMEMESSAGE && type != ChatMessageType.SPAM)
		{
			return;
		}

		String message = event.getMessage();

		if (message.contains(SepulchreConstants.FLOOR_CHANGE_MESSAGE))
		{
			obstacleHandler.onFloorEntered(false);
			pendingRouteClassification = true;
			pendingRouteFloor = obstacleHandler.getCurrentFloor();
		}
		else if (message.contains(SepulchreConstants.FLOOR_1_MESSAGE))
		{
			inSepulchre = true;
			obstacleHandler.onFloorEntered(true);
			pendingRouteClassification = true;
			pendingRouteFloor = 1;
		}

		if (!inSepulchre)
		{
			return;
		}

		if (message.contains("You hear a loud rumbling noise as the door to the next floor closes")
			|| message.contains("You hear the sound of a magical barrier activating"))
		{
			obstacleHandler.onDoorToNextFloorClosed();
		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (!inSepulchre)
		{
			return;
		}

		String option = event.getOption();
		String target = event.getTarget();

		if (option == null || !option.equals("Activate"))
		{
			return;
		}
		if (target == null || !target.toLowerCase().contains("magical obelisk"))
		{
			return;
		}

		int currentFloor = obstacleHandler.getCurrentFloor();
		int playerMaxFloor = obstacleHandler.getPlayerMaxFloor();
		boolean doorClosed = obstacleHandler.isDoorToNextFloorClosed();

		boolean shouldSwap = config.swapObeliskMenuEntry()
			&& config.highlightObelisk()
			&& (currentFloor == playerMaxFloor || doorClosed);

		if (!shouldSwap)
		{
			return;
		}

		MenuEntry[] entries = client.getMenuEntries();
		if (entries.length < 2)
		{
			return;
		}

		int quickExitIndex = -1;
		int activateIndex = entries.length - 1;

		for (int i = entries.length - 2; i >= 0; i--)
		{
			if ("Quick-exit".equals(entries[i].getOption()))
			{
				quickExitIndex = i;
				break;
			}
		}

		if (quickExitIndex >= 0)
		{
			MenuEntry temp = entries[quickExitIndex];
			entries[quickExitIndex] = entries[activateIndex];
			entries[activateIndex] = temp;
			client.setMenuEntries(entries);
		}
	}

	@Provides
	SepulchreConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SepulchreConfig.class);
	}
}
