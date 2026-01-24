package com.sepulchre.model;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a tracked obstacle in the Hallowed Sepulchre.
 */
@Getter
@Setter
public class SepulchreObstacle
{
	private final ObstacleType type;
	private final WorldPoint location;

	private GameObject gameObject;
	private NPC npc;

	private int ticksUntilActive;
	private int cycleTicks;
	private boolean active;

	private List<WorldPoint> dangerZone;

	public SepulchreObstacle(ObstacleType type, WorldPoint location)
	{
		this.type = type;
		this.location = location;
		this.dangerZone = new ArrayList<>();
		this.ticksUntilActive = -1;
		this.cycleTicks = -1;
		this.active = false;
	}

	public SepulchreObstacle(ObstacleType type, GameObject gameObject)
	{
		this(type, gameObject.getWorldLocation());
		this.gameObject = gameObject;
	}

	public SepulchreObstacle(ObstacleType type, NPC npc)
	{
		this(type, npc.getWorldLocation());
		this.npc = npc;
	}

	public void addDangerTile(WorldPoint tile)
	{
		if (!dangerZone.contains(tile))
		{
			dangerZone.add(tile);
		}
	}

	public void clearDangerZone()
	{
		dangerZone.clear();
	}

	public void decrementTicks()
	{
		if (ticksUntilActive > 0)
		{
			ticksUntilActive--;
		}
		else if (ticksUntilActive == 0 && cycleTicks > 0)
		{
			// Wrap to cycleTicks - 1 for 0-indexed cycle (e.g., 8-tick cycle = 0..7)
			ticksUntilActive = cycleTicks - 1;
		}
	}

	public boolean isExpired()
	{
		return gameObject == null && npc == null;
	}
}
