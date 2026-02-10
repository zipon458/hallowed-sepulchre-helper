package com.sepulchre.handler;

import com.sepulchre.config.SepulchreConfig;
import com.sepulchre.model.SepulchreRoute;
import com.sepulchre.util.RouteObstacles;
import com.sepulchre.util.SepulchreConstants;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

@Getter
public class FloorState
{
	private final Client client;
	private final SepulchreConfig config;

	private Runnable onLowerSectionEntered;

	@Setter
	private int currentFloor = 0;

	@Setter
	private SepulchreRoute currentRoute = SepulchreRoute.UNKNOWN;

	private int floorStartPlane = -1;
	private boolean inLowerSection = false;

	private int floorTicks = 0;
	private int runTicks = 0;
	private int previousRunTicks = 0;
	private boolean timerPaused = false;
	private boolean timerStarted = false;

	private boolean doorToNextFloorClosed = false;

	public FloorState(Client client, SepulchreConfig config)
	{
		this.client = client;
		this.config = config;
	}

	public void setOnLowerSectionEntered(Runnable callback)
	{
		this.onLowerSectionEntered = callback;
	}

	public void onFloorEntered(boolean isFirstFloor)
	{
		if (isFirstFloor)
		{
			currentFloor = 1;
		}
		else if (currentFloor > 0 && currentFloor < 5)
		{
			currentFloor++;
		}
	}

	public void setFloorStartPlane(int plane)
	{
		this.floorStartPlane = plane;
		this.inLowerSection = false;
	}

	public void restoreFloorState(boolean wasInLowerSection, int previousStartPlane)
	{
		this.inLowerSection = wasInLowerSection;
		this.floorStartPlane = previousStartPlane;
		if (wasInLowerSection && previousStartPlane < 0)
		{
			this.floorStartPlane = 99;
		}
	}

	public void onDoorToNextFloorClosed()
	{
		doorToNextFloorClosed = true;
	}

	/**
	 * Returns true if the player moved to a lower plane, indicating a scan may be needed.
	 */
	public void updateLowerSectionStatus(int currentPlane)
	{
		if (floorStartPlane >= 0 && currentPlane < floorStartPlane)
		{
			boolean wasInLowerSection = inLowerSection;
			inLowerSection = true;

			if (!wasInLowerSection && currentFloor == 1
				&& (currentRoute == SepulchreRoute.FLOOR_1_NORTHEAST || currentRoute == SepulchreRoute.FLOOR_1_NORTHWEST))
			{
				if (onLowerSectionEntered != null)
				{
					onLowerSectionEntered.run();
				}
			}
		}
	}

	public void updateTimers()
	{
		runTicks = client.getVarbitValue(SepulchreConstants.VARBIT_HALLOWED_TIME_SPENT);
		floorTicks = client.getVarbitValue(SepulchreConstants.VARBIT_HALLOWED_CURRENT_FLOOR_TIME_SPENT);
		timerStarted = runTicks > 0;
		timerPaused = timerStarted && runTicks == previousRunTicks;
		previousRunTicks = runTicks;
	}

	public boolean shouldShowForCurrentRoute(LocalPoint localPoint)
	{
		if (!config.filterByRoute())
		{
			return true;
		}
		if (localPoint == null)
		{
			return true;
		}
		WorldPoint canonicalLocation = WorldPoint.fromLocalInstance(client, localPoint);
		if (canonicalLocation == null)
		{
			return true;
		}
		return RouteObstacles.isRelevantForRoute(canonicalLocation, currentRoute);
	}

	public boolean shouldShowForCurrentRoute(WorldPoint instanceLocation)
	{
		if (!config.filterByRoute())
		{
			return true;
		}
		if (instanceLocation == null)
		{
			return true;
		}
		LocalPoint localPoint = LocalPoint.fromWorld(client, instanceLocation);
		if (localPoint == null)
		{
			return true;
		}
		WorldPoint canonicalLocation = WorldPoint.fromLocalInstance(client, localPoint);
		if (canonicalLocation == null)
		{
			return true;
		}
		return RouteObstacles.isRelevantForRoute(canonicalLocation, currentRoute);
	}

	public boolean isCoffinLootingEnabledForCurrentFloor()
	{
		return config.lootingFloors().includesFloor(currentFloor);
	}

	public boolean isGrandCoffinLootingEnabled()
	{
		return config.lootingFloors().includesGrandCoffin();
	}

	public int getPlayerMaxFloor()
	{
		int agilityLevel = client.getBoostedSkillLevel(net.runelite.api.Skill.AGILITY);
		for (int i = SepulchreConstants.FLOOR_AGILITY_REQUIREMENTS.length - 1; i >= 0; i--)
		{
			if (agilityLevel >= SepulchreConstants.FLOOR_AGILITY_REQUIREMENTS[i])
			{
				return i + 1;
			}
		}
		return 0;
	}

	public void reset()
	{
		doorToNextFloorClosed = false;
		currentFloor = 0;
		currentRoute = SepulchreRoute.UNKNOWN;
		floorStartPlane = -1;
		inLowerSection = false;
		floorTicks = 0;
		runTicks = 0;
		previousRunTicks = 0;
		timerPaused = false;
		timerStarted = false;
	}
}
