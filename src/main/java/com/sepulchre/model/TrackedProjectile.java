package com.sepulchre.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Projectile;
import net.runelite.api.coords.LocalPoint;

/**
 * Tracks a projectile for overlay rendering.
 */
@Getter
@RequiredArgsConstructor
public class TrackedProjectile
{
	private final Projectile projectile;
	private final int id;
	private final int startCycle;

	public LocalPoint getLocalLocation()
	{
		return new LocalPoint((int) projectile.getX(), (int) projectile.getY());
	}

	public boolean isFinished()
	{
		return projectile.getRemainingCycles() <= 0;
	}
}
