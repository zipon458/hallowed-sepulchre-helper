package com.sepulchre.overlay;

import com.sepulchre.SepulchrePlugin;
import com.sepulchre.config.SepulchreConfig;
import com.sepulchre.handler.ObstacleHandler;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class RunTimerOverlay extends OverlayPanel
{
	private final SepulchrePlugin plugin;
	private final SepulchreConfig config;
	private final ObstacleHandler obstacleHandler;

	@Inject
	public RunTimerOverlay(SepulchrePlugin plugin, SepulchreConfig config,
		ObstacleHandler obstacleHandler)
	{
		this.plugin = plugin;
		this.config = config;
		this.obstacleHandler = obstacleHandler;

		setPosition(OverlayPosition.TOP_LEFT);
		setPriority(OverlayPriority.LOW);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showRunTimer())
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

		int runTicks = obstacleHandler.getRunTicks();
		String runTimerText = formatRunTime(runTicks);
		boolean isPaused = obstacleHandler.isTimerPaused();

		panelComponent.getChildren().add(LineComponent.builder()
			.left("Overall:")
			.right(runTimerText)
			.rightColor(isPaused ? Color.YELLOW : Color.WHITE)
			.build());

		return super.render(graphics);
	}

	private String formatRunTime(int ticks)
	{
		int totalSeconds = (int) (ticks * 0.6);
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;
		return String.format("%d:%02d", minutes, seconds);
	}
}
