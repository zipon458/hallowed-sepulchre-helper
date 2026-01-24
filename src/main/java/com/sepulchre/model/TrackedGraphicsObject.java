package com.sepulchre.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.GraphicsObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

/**
 * Tracks a graphics object (flames, lightning, etc.) for overlay rendering.
 */
@Getter
@RequiredArgsConstructor
public class TrackedGraphicsObject
{
	private final GraphicsObject graphicsObject;
	private final WorldPoint worldPoint;
	private final int id;
	private final int startCycle;

	public LocalPoint getLocalLocation()
	{
		return graphicsObject.getLocation();
	}

	public boolean isFinished()
	{
		return graphicsObject.finished();
	}
}
