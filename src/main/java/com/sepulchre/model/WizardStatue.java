package com.sepulchre.model;

import com.sepulchre.util.GameObjectUtil;
import com.sepulchre.util.SepulchreConstants;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.GameObject;
import net.runelite.api.coords.WorldPoint;

import java.util.HashSet;
import java.util.Set;

@Getter
public class WizardStatue
{
	private static final int ACTIVATION_CHECK_TICKS = 10;

	private final GameObject gameObject;
	private final Set<WorldPoint> fireTiles = new HashSet<>();

	@Setter
	private boolean hasEverFired = false;

	@Setter
	private int ticksSinceSpawn = 0;

	@Setter
	private int tickCounter = -1;

	@Setter
	private int firePhaseTicks = 2;

	@Setter
	private int safePhaseTicks = 1;

	@Setter
	private int warningPhaseTicks = 2;

	private boolean wasFiringLastTick = false;
	private boolean wasWarningLastTick = false;
	private boolean isFirstTick = true;

	private int cachedAnimationId = -1;

	public WizardStatue(GameObject gameObject)
	{
		this.gameObject = gameObject;
	}

	public boolean isConfirmedActiveOrUnknown()
	{
		return hasEverFired || ticksSinceSpawn < ACTIVATION_CHECK_TICKS;
	}

	public boolean isFiring()
	{
		return cachedAnimationId == SepulchreConstants.WIZARD_ANIM_FIRE;
	}

	public boolean isWarning()
	{
		return cachedAnimationId == SepulchreConstants.WIZARD_ANIM_WARNING
			|| cachedAnimationId == SepulchreConstants.WIZARD_ANIM_PRE_WARNING;
	}

	public boolean isSafe()
	{
		return cachedAnimationId != SepulchreConstants.WIZARD_ANIM_FIRE
			&& cachedAnimationId != SepulchreConstants.WIZARD_ANIM_WARNING
			&& cachedAnimationId != SepulchreConstants.WIZARD_ANIM_PRE_WARNING;
	}

	public void addFireTile(WorldPoint tile)
	{
		fireTiles.add(tile);
	}

	public WorldPoint getLocation()
	{
		return gameObject.getWorldLocation();
	}

	public void onGameTick()
	{
		cachedAnimationId = GameObjectUtil.getAnimationId(gameObject);
		boolean currentlyFiring = isFiring();
		boolean currentlyWarning = isWarning();

		if (isFirstTick)
		{
			isFirstTick = false;
			if (currentlyFiring || currentlyWarning)
			{
				hasEverFired = true;
			}
		}
		else
		{
			if (currentlyFiring && !wasFiringLastTick)
			{
				tickCounter = firePhaseTicks;
				hasEverFired = true;
			}
			else if (currentlyWarning && !wasWarningLastTick && !currentlyFiring)
			{
				tickCounter = warningPhaseTicks;
			}
			else if (!currentlyFiring && wasFiringLastTick)
			{
				tickCounter = safePhaseTicks;
			}
			else if (tickCounter > 0)
			{
				tickCounter--;
			}

			if (currentlyFiring || currentlyWarning)
			{
				hasEverFired = true;
			}
		}

		wasFiringLastTick = currentlyFiring;
		wasWarningLastTick = currentlyWarning;
		ticksSinceSpawn++;
	}

	public String getDisplayTicks()
	{
		if (tickCounter < 0)
		{
			return "?";
		}
		return String.valueOf(tickCounter);
	}
}
