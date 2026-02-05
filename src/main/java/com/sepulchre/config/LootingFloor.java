package com.sepulchre.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LootingFloor
{
	NONE("None", 0, false),
	GRAND_COFFIN_ONLY("Grand Coffin only", 6, true),
	FLOOR_5_ONLY("Floor 5", 5, false),
	FLOOR_4_PLUS("Floor 4+", 4, false),
	FLOOR_3_PLUS("Floor 3+", 3, false),
	FLOOR_2_PLUS("Floor 2+", 2, false),
	ALL_FLOORS("Floor 1+", 1, false);

	private final String name;
	private final int minimumFloor;
	private final boolean grandCoffinOnly;

	@Override
	public String toString()
	{
		return name;
	}

	public boolean includesFloor(int floor)
	{
		if (this == NONE || this == GRAND_COFFIN_ONLY)
		{
			return false;
		}
		return floor >= minimumFloor;
	}

	public boolean includesGrandCoffin()
	{
		return this != NONE;
	}
}
