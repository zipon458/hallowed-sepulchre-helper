package com.sepulchre.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PortalDisplayMode
{
	NONE("None"),
	TILE("Tile"),
	COUNTDOWN("Countdown"),
	BOTH("Both");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}

	public boolean showTile()
	{
		return this == TILE || this == BOTH;
	}

	public boolean showCountdown()
	{
		return this == COUNTDOWN || this == BOTH;
	}
}
