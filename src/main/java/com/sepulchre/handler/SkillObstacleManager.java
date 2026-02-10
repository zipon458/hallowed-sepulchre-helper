package com.sepulchre.handler;

import com.sepulchre.model.SkillObstacleTracker;
import com.sepulchre.util.GameObjectUtil;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillObstacleManager
{
	private final Client client;
	private final Map<WorldPoint, SkillObstacleTracker> skillObstacleTrackers = new HashMap<>();

	public SkillObstacleManager(Client client)
	{
		this.client = client;
	}

	public void createCoffinTracker(GameObject coffin, List<GroundObject> bridges,
		List<GameObject> grapples, List<GameObject> portalFrames)
	{
		WorldPoint coffinLocation = coffin.getWorldLocation();
		if (skillObstacleTrackers.containsKey(coffinLocation))
		{
			return;
		}

		SkillObstacleTracker tracker = new SkillObstacleTracker(coffinLocation);

		for (GroundObject bridge : bridges)
		{
			tracker.tryAssociateObstacle(bridge);
		}

		for (GameObject grapple : grapples)
		{
			tracker.tryAssociateObstacle(grapple);
		}

		for (GameObject portalFrame : portalFrames)
		{
			tracker.tryAssociateObstacle(portalFrame);
		}

		skillObstacleTrackers.put(coffinLocation, tracker);
	}

	public void associateObstacleWithTrackers(TileObject obstacle)
	{
		for (SkillObstacleTracker tracker : skillObstacleTrackers.values())
		{
			tracker.tryAssociateObstacle(obstacle);
		}
	}

	public void onBridgeBuilt(List<GroundObject> bridges)
	{
		for (GroundObject bridge : bridges)
		{
			notifyTrackersOfObstacleUse(bridge, 5);
		}
	}

	public void onBridgeCrossed(List<GroundObject> bridges)
	{
		for (GroundObject bridge : bridges)
		{
			notifyTrackersOfObstacleUse(bridge, 5);
		}
	}

	public void onGrappleUsed(List<GameObject> grapples)
	{
		for (GameObject grapple : grapples)
		{
			notifyTrackersOfObstacleUse(grapple, 15);
		}
	}

	public void onPortalUsed(List<GameObject> portalFrames)
	{
		for (GameObject portalFrame : portalFrames)
		{
			notifyTrackersOfObstacleUse(portalFrame, 15);
		}
	}

	private void notifyTrackersOfObstacleUse(TileObject obstacle, int maxDistance)
	{
		WorldPoint playerLocation = getPlayerWorldLocation();
		if (playerLocation == null)
		{
			return;
		}

		if (GameObjectUtil.manhattanDistance(obstacle.getWorldLocation(), playerLocation) <= maxDistance)
		{
			for (SkillObstacleTracker tracker : skillObstacleTrackers.values())
			{
				if (tracker.hasObstacle(obstacle))
				{
					tracker.onObstacleUsed(obstacle);
				}
			}
		}
	}

	private WorldPoint getPlayerWorldLocation()
	{
		Player player = client.getLocalPlayer();
		return player != null ? player.getWorldLocation() : null;
	}

	public void onCoffinLooted()
	{
		WorldPoint playerLocation = getPlayerWorldLocation();
		if (playerLocation == null)
		{
			return;
		}

		SkillObstacleTracker nearestTracker = null;
		int nearestDistance = Integer.MAX_VALUE;

		for (SkillObstacleTracker tracker : skillObstacleTrackers.values())
		{
			int distance = GameObjectUtil.manhattanDistance(tracker.getCoffinLocation(), playerLocation);
			if (distance < nearestDistance)
			{
				nearestDistance = distance;
				nearestTracker = tracker;
			}
		}

		if (nearestTracker != null && nearestDistance <= 5)
		{
			nearestTracker.onCoffinLooted();
		}
	}

	public void onBrazierSacrificed(List<GameObject> holyBarriers)
	{
		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return;
		}

		WorldPoint playerPosition = WorldPoint.fromLocalInstance(client, player.getLocalLocation());
		if (playerPosition == null)
		{
			return;
		}

		WorldPoint playerWorldLoc = player.getWorldLocation();

		GameObject nearestBarrier = null;
		int nearestDistance = Integer.MAX_VALUE;

		for (GameObject barrier : holyBarriers)
		{
			WorldPoint barrierLoc = barrier.getWorldLocation();
			if (barrierLoc.getPlane() != playerWorldLoc.getPlane())
			{
				continue;
			}

			int distance = GameObjectUtil.manhattanDistance(barrierLoc, playerWorldLoc);
			if (distance < nearestDistance && distance <= 10)
			{
				nearestDistance = distance;
				nearestBarrier = barrier;
			}
		}

		if (nearestBarrier == null)
		{
			return;
		}

		for (SkillObstacleTracker tracker : skillObstacleTrackers.values())
		{
			tracker.associateBarrierOnSacrifice(nearestBarrier, playerPosition);
		}
	}

	public void checkBarrierReturns(WorldPoint playerCanonicalPosition)
	{
		if (playerCanonicalPosition == null)
		{
			return;
		}

		for (SkillObstacleTracker tracker : skillObstacleTrackers.values())
		{
			tracker.checkBarrierReturn(playerCanonicalPosition);
		}
	}

	public boolean shouldHighlightBarrier(GameObject barrier)
	{
		for (SkillObstacleTracker tracker : skillObstacleTrackers.values())
		{
			if (tracker.hasBarrier(barrier))
			{
				return tracker.shouldHighlight(barrier);
			}
		}
		return false;
	}

	public boolean shouldHighlightObstacle(TileObject obstacle)
	{
		for (SkillObstacleTracker tracker : skillObstacleTrackers.values())
		{
			if (tracker.hasObstacle(obstacle))
			{
				return tracker.shouldHighlight(obstacle);
			}
		}
		return true;
	}

	public boolean hasObstacleBeenUsed(TileObject obstacle)
	{
		for (SkillObstacleTracker tracker : skillObstacleTrackers.values())
		{
			if (tracker.hasObstacle(obstacle))
			{
				return tracker.hasBeenUsed(obstacle);
			}
		}
		return false;
	}

	public void reset()
	{
		skillObstacleTrackers.clear();
	}
}
