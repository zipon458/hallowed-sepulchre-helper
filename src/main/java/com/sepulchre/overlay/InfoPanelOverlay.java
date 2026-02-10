package com.sepulchre.overlay;

import com.sepulchre.SepulchrePlugin;
import com.sepulchre.config.SepulchreConfig;
import com.sepulchre.handler.ObstacleHandler;
import com.sepulchre.model.SepulchreRoute;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.EnumMap;
import java.util.Map;

public class InfoPanelOverlay extends OverlayPanel
{
	private static final Map<SepulchreRoute, double[]> TICK_PERFECT_SECONDS = new EnumMap<>(SepulchreRoute.class);
	static
	{
		TICK_PERFECT_SECONDS.put(SepulchreRoute.FLOOR_1_NORTHWEST, new double[]{27.6});
		TICK_PERFECT_SECONDS.put(SepulchreRoute.FLOOR_1_NORTHEAST, new double[]{31.8});
		TICK_PERFECT_SECONDS.put(SepulchreRoute.FLOOR_1_SOUTHEAST, new double[]{28.8, 29.4});
		TICK_PERFECT_SECONDS.put(SepulchreRoute.FLOOR_1_SOUTHWEST, new double[]{32.4});

		TICK_PERFECT_SECONDS.put(SepulchreRoute.FLOOR_2_NORTH, new double[]{38.4, 39.6});
		TICK_PERFECT_SECONDS.put(SepulchreRoute.FLOOR_2_EAST, new double[]{43.8});
		TICK_PERFECT_SECONDS.put(SepulchreRoute.FLOOR_2_SOUTH, new double[]{33.6});
		TICK_PERFECT_SECONDS.put(SepulchreRoute.FLOOR_2_WEST, new double[]{39.6});

		TICK_PERFECT_SECONDS.put(SepulchreRoute.FLOOR_3_EAST, new double[]{51.0, 60.6});
		TICK_PERFECT_SECONDS.put(SepulchreRoute.FLOOR_3_WEST, new double[]{51.0});

		TICK_PERFECT_SECONDS.put(SepulchreRoute.FLOOR_4_NORTH, new double[]{82.0, 94.0});
		TICK_PERFECT_SECONDS.put(SepulchreRoute.FLOOR_4_SOUTH, new double[]{90.0, 98.0});

		TICK_PERFECT_SECONDS.put(SepulchreRoute.FLOOR_5_SINGLE, new double[]{122.4});
	}

	private static final Map<SepulchreRoute, Color> ROUTE_COLORS = new EnumMap<>(SepulchreRoute.class);
	static
	{
		Color lightBlue = new Color(100, 200, 255);
		Color orange = new Color(255, 200, 100);
		Color lightGreen = new Color(100, 255, 100);
		Color pink = new Color(255, 100, 255);
		Color cyan = new Color(0, 255, 255);

		ROUTE_COLORS.put(SepulchreRoute.FLOOR_1_NORTHWEST, lightBlue);
		ROUTE_COLORS.put(SepulchreRoute.FLOOR_1_NORTHEAST, orange);
		ROUTE_COLORS.put(SepulchreRoute.FLOOR_1_SOUTHEAST, lightGreen);
		ROUTE_COLORS.put(SepulchreRoute.FLOOR_1_SOUTHWEST, pink);

		ROUTE_COLORS.put(SepulchreRoute.FLOOR_2_NORTH, lightBlue);
		ROUTE_COLORS.put(SepulchreRoute.FLOOR_2_EAST, orange);
		ROUTE_COLORS.put(SepulchreRoute.FLOOR_2_SOUTH, lightGreen);
		ROUTE_COLORS.put(SepulchreRoute.FLOOR_2_WEST, pink);

		ROUTE_COLORS.put(SepulchreRoute.FLOOR_3_EAST, orange);
		ROUTE_COLORS.put(SepulchreRoute.FLOOR_3_WEST, pink);

		ROUTE_COLORS.put(SepulchreRoute.FLOOR_4_NORTH, lightBlue);
		ROUTE_COLORS.put(SepulchreRoute.FLOOR_4_SOUTH, lightGreen);

		ROUTE_COLORS.put(SepulchreRoute.FLOOR_5_SINGLE, cyan);
	}

	private final SepulchrePlugin plugin;
	private final SepulchreConfig config;
	private final ObstacleHandler obstacleHandler;
	private final Client client;

	@Inject
	public InfoPanelOverlay(SepulchrePlugin plugin, SepulchreConfig config,
		ObstacleHandler obstacleHandler, Client client)
	{
		this.plugin = plugin;
		this.config = config;
		this.obstacleHandler = obstacleHandler;
		this.client = client;

		setPosition(OverlayPosition.TOP_LEFT);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(OverlayPriority.LOW);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showInfoPanel())
		{
			return null;
		}

		if (!plugin.isInSepulchre())
		{
			return null;
		}

		int floor = obstacleHandler.getCurrentFloor();
		if (floor == 0)
		{
			return null;
		}

		SepulchreRoute route = obstacleHandler.getCurrentRoute();
		boolean inLower = obstacleHandler.isInLowerSection();

		String routeText = formatRouteText(route, inLower);
		Color routeColor = getRouteColor(route);

		panelComponent.getChildren().add(LineComponent.builder()
			.left("Floor:")
			.right(floor + " " + routeText)
			.rightColor(routeColor)
			.build());

		if (config.showFloorTimer())
		{
			int floorTicks = obstacleHandler.getFloorTicks();
			String timerText = formatFloorTime(floorTicks);
			boolean isPaused = obstacleHandler.isTimerPaused();
			boolean timerStarted = obstacleHandler.isTimerStarted();

			Color timerColor = Color.WHITE;
			if (isPaused)
			{
				if (!timerStarted)
				{
					timerColor = Color.YELLOW;
				}
				else
				{
					double tickPerfectTicks = getTickPerfectMaxTicks(route);
					if (tickPerfectTicks > 0 && floorTicks > 0 && floorTicks <= tickPerfectTicks)
					{
						timerColor = Color.GREEN;
					}
					else
					{
						timerColor = Color.YELLOW;
					}
				}
			}

			panelComponent.getChildren().add(LineComponent.builder()
				.left("Timer:")
				.right(timerText)
				.rightColor(timerColor)
				.build());
		}

		if (config.showTickPerfectTime())
		{
			String[] times = getTickPerfectTimes(route);
			if (times != null)
			{
				Color tickPerfectColor = new Color(170, 170, 170);

				panelComponent.getChildren().add(LineComponent.builder()
					.left("Perfect:")
					.leftColor(tickPerfectColor)
					.right(times[0])
					.rightColor(tickPerfectColor)
					.build());

				if (times.length > 1 && times[1] != null)
				{
					panelComponent.getChildren().add(LineComponent.builder()
						.left("")
						.right("~" + times[1])
						.rightColor(tickPerfectColor)
						.build());
				}
			}
		}

		if (config.showFloor4CycleOverlay() && floor == 4)
		{
			Player player = client.getLocalPlayer();
			if (player != null)
			{
				int playerX = player.getWorldLocation().getX();
				int playerY = player.getWorldLocation().getY();
				if (obstacleHandler.getWizardCyclePhaseTracker().isPlayerInRange(playerX, playerY))
				{
					String cycleText = obstacleHandler.getFloor4CycleDisplayText();
					if (cycleText != null)
					{
						int currentCycle = obstacleHandler.getFloor4CurrentCycle();
						Color cycleColor = getFloor4CycleColor(currentCycle);

						panelComponent.getChildren().add(LineComponent.builder()
							.left("Cycle:")
							.right(cycleText)
							.rightColor(cycleColor)
							.build());
					}
				}
			}
		}

		return super.render(graphics);
	}

	private Color getFloor4CycleColor(int cycle)
	{
		switch (cycle)
		{
			case 1:
			case 2:
				return Color.GREEN;
			case 3:
			case 4:
				return Color.YELLOW;
			case 5:
				return Color.RED;
			default:
				return Color.WHITE;
		}
	}

	private String formatFloorTime(int ticks)
	{
		double totalSeconds = ticks * 0.6;
		int minutes = (int) (totalSeconds / 60);
		double seconds = totalSeconds % 60;
		return String.format("%d:%04.1f", minutes, seconds);
	}

	private String formatRouteText(SepulchreRoute route, boolean inLower)
	{
		if (route.getFloor() == 3 && inLower)
		{
			return "Lower";
		}

		if (route == SepulchreRoute.FLOOR_5_SINGLE)
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();

		if (route == SepulchreRoute.UNKNOWN)
		{
			sb.append("?");
		}
		else
		{
			sb.append(getAbbreviatedRouteName(route));
		}

		if (inLower && route.getFloor() > 1)
		{
			sb.append(" (Lower)");
		}

		return sb.toString();
	}

	private String getAbbreviatedRouteName(SepulchreRoute route)
	{
		switch (route)
		{
			case FLOOR_1_NORTHWEST: return "NW";
			case FLOOR_1_NORTHEAST: return "NE";
			case FLOOR_1_SOUTHEAST: return "SE";
			case FLOOR_1_SOUTHWEST: return "SW";
			case FLOOR_2_NORTH: return "N";
			case FLOOR_2_EAST: return "E";
			case FLOOR_2_SOUTH: return "S";
			case FLOOR_2_WEST: return "W";
			case FLOOR_3_EAST: return "E";
			case FLOOR_3_WEST: return "W";
			case FLOOR_4_NORTH: return "N";
			case FLOOR_4_SOUTH: return "S";
			case FLOOR_5_SINGLE: return "Main";
			default: return "?";
		}
	}

	private String[] getTickPerfectTimes(SepulchreRoute route)
	{
		double[] seconds = TICK_PERFECT_SECONDS.get(route);
		if (seconds == null)
		{
			return null;
		}

		String[] times = new String[seconds.length];
		for (int i = 0; i < seconds.length; i++)
		{
			times[i] = formatSecondsAsTime(seconds[i]);
		}
		return times;
	}

	private String formatSecondsAsTime(double totalSeconds)
	{
		int minutes = (int) (totalSeconds / 60);
		double secs = totalSeconds % 60;
		return String.format("%d:%04.1f", minutes, secs);
	}

	private double getTickPerfectMaxTicks(SepulchreRoute route)
	{
		double[] seconds = TICK_PERFECT_SECONDS.get(route);
		if (seconds == null)
		{
			return -1;
		}
		double maxSeconds = seconds[seconds.length - 1];
		return maxSeconds / 0.6;
	}

	private Color getRouteColor(SepulchreRoute route)
	{
		return ROUTE_COLORS.getOrDefault(route, Color.GRAY);
	}
}
