package com.sepulchre.model;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.coords.WorldPoint;

@Getter
@Setter
public class LightningStrike
{
	private final WorldPoint location;
	private int ticksUntilExpired;

	public LightningStrike(WorldPoint location, int ticksToLive)
	{
		this.location = location;
		this.ticksUntilExpired = ticksToLive;
	}

	public void onGameTick()
	{
		if (ticksUntilExpired > 0)
		{
			ticksUntilExpired--;
		}
	}

	public boolean isExpired()
	{
		return ticksUntilExpired <= 0;
	}
}
