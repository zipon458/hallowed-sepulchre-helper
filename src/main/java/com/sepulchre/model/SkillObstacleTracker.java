package com.sepulchre.model;

import com.sepulchre.util.GameObjectUtil;
import lombok.Getter;
import net.runelite.api.GameObject;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

import java.util.HashMap;
import java.util.Map;

public class SkillObstacleTracker
{
	private static final int ASSOCIATION_RADIUS = 30;

	@Getter
	private final WorldPoint coffinLocation;

	private final Map<WorldPoint, SkillObstacleState> obstacleStates = new HashMap<>();

	@Getter
	private boolean coffinLooted = false;

	private WorldPoint brazierSacrificePosition = null;
	private WorldPoint barrierLocation = null;

	public SkillObstacleTracker(WorldPoint coffinLocation)
	{
		this.coffinLocation = coffinLocation;
	}

	public boolean tryAssociateObstacle(TileObject obstacle)
	{
		WorldPoint obstacleLocation = obstacle.getWorldLocation();
		if (isWithinRange(obstacleLocation))
		{
			obstacleStates.putIfAbsent(obstacleLocation, SkillObstacleState.HIGHLIGHT_PRE_CROSS);
			return true;
		}
		return false;
	}

	public void associateBarrierOnSacrifice(GameObject barrier, WorldPoint playerPosition)
	{
		WorldPoint location = barrier.getWorldLocation();
		if (isWithinRange(location))
		{
			obstacleStates.put(location, SkillObstacleState.USED_AWAITING_LOOT);
			barrierLocation = location;
			brazierSacrificePosition = playerPosition;
		}
	}

	public boolean hasBarrier(GameObject barrier)
	{
		return barrierLocation != null && barrierLocation.equals(barrier.getWorldLocation());
	}

	public boolean shouldHighlight(TileObject obstacle)
	{
		SkillObstacleState state = obstacleStates.get(obstacle.getWorldLocation());
		return state != null && state.isStillRelevant();
	}

	public boolean hasBeenUsed(TileObject obstacle)
	{
		SkillObstacleState state = obstacleStates.get(obstacle.getWorldLocation());
		return state != null && state != SkillObstacleState.HIGHLIGHT_PRE_CROSS;
	}

	public void onObstacleUsed(TileObject obstacle)
	{
		WorldPoint location = obstacle.getWorldLocation();
		SkillObstacleState currentState = obstacleStates.get(location);

		if (currentState == null)
		{
			return;
		}

		if (!coffinLooted && currentState == SkillObstacleState.HIGHLIGHT_PRE_CROSS)
		{
			obstacleStates.put(location, SkillObstacleState.USED_AWAITING_LOOT);
		}
		else if (coffinLooted && currentState == SkillObstacleState.HIGHLIGHT_FOR_RETURN)
		{
			obstacleStates.put(location, SkillObstacleState.DONE);
		}
	}

	public void onCoffinLooted()
	{
		coffinLooted = true;
		for (Map.Entry<WorldPoint, SkillObstacleState> entry : obstacleStates.entrySet())
		{
			if (entry.getValue() == SkillObstacleState.USED_AWAITING_LOOT)
			{
				entry.setValue(SkillObstacleState.HIGHLIGHT_FOR_RETURN);
			}
		}
	}

	public boolean checkBarrierReturn(WorldPoint playerPosition)
	{
		if (barrierLocation == null || brazierSacrificePosition == null || !coffinLooted)
		{
			return false;
		}

		SkillObstacleState barrierState = obstacleStates.get(barrierLocation);
		if (barrierState != SkillObstacleState.HIGHLIGHT_FOR_RETURN)
		{
			return false;
		}

		if (GameObjectUtil.manhattanDistance(playerPosition, brazierSacrificePosition) <= 1
			&& playerPosition.getPlane() == brazierSacrificePosition.getPlane())
		{
			obstacleStates.put(barrierLocation, SkillObstacleState.DONE);
			return true;
		}

		return false;
	}

	public boolean hasObstacle(TileObject obstacle)
	{
		return obstacleStates.containsKey(obstacle.getWorldLocation());
	}

	private boolean isWithinRange(WorldPoint location)
	{
		return location.getPlane() == coffinLocation.getPlane()
			&& GameObjectUtil.manhattanDistance(location, coffinLocation) <= ASSOCIATION_RADIUS;
	}
}
