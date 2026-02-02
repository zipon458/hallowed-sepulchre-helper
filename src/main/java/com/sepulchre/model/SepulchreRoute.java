package com.sepulchre.model;

public enum SepulchreRoute
{
	FLOOR_1_NORTHWEST(1),
	FLOOR_1_NORTHEAST(1),
	FLOOR_1_SOUTHEAST(1),
	FLOOR_1_SOUTHWEST(1),

	FLOOR_2_NORTH(2),
	FLOOR_2_EAST(2),
	FLOOR_2_SOUTH(2),
	FLOOR_2_WEST(2),

	FLOOR_3_EAST(3),
	FLOOR_3_WEST(3),

	FLOOR_4_NORTH(4),
	FLOOR_4_SOUTH(4),

	FLOOR_5_SINGLE(5),

	UNKNOWN(0);

	private final int floor;

	SepulchreRoute(int floor)
	{
		this.floor = floor;
	}

	public int getFloor()
	{
		return floor;
	}
}
