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
	private final GameObject gameObject;
	private final Set<WorldPoint> fireTiles = new HashSet<>();

	@Setter
	private boolean hasEverFired = false;

	@Setter
	private int ticksSinceSpawn = 0;

	@Setter
	private int tickCounter = -1;

	private boolean wasFiringLastTick = false;
	private boolean wasWarningLastTick = false;

	private static final int ACTIVATION_CHECK_TICKS = 10;

	@Setter
	private int firePhaseTicks = 4;
	@Setter
	private int safePhaseTicks = 4;
	@Setter
	private int warningPhaseTicks = 2;

	public WizardStatue(GameObject gameObject)
	{
		this.gameObject = gameObject;
	}

	public boolean isConfirmedActiveOrUnknown()
	{
		return hasEverFired || ticksSinceSpawn < ACTIVATION_CHECK_TICKS;
	}

	private int getAnimationId()
	{
		return GameObjectUtil.getAnimationId(gameObject);
	}

	public boolean isFiring()
	{
		return getAnimationId() == SepulchreConstants.WIZARD_ANIM_FIRE;
	}

	public boolean isWarning()
	{
		int animId = getAnimationId();
		return animId == SepulchreConstants.WIZARD_ANIM_WARNING || animId == SepulchreConstants.WIZARD_ANIM_PRE_WARNING;
	}

	public boolean isSafe()
	{
		int animId = getAnimationId();
		return animId != SepulchreConstants.WIZARD_ANIM_FIRE
			&& animId != SepulchreConstants.WIZARD_ANIM_WARNING
			&& animId != SepulchreConstants.WIZARD_ANIM_PRE_WARNING;
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
		boolean currentlyFiring = isFiring();
		boolean currentlyWarning = isWarning();

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

	public boolean isSynced()
	{
		return tickCounter >= 0;
	}
}
