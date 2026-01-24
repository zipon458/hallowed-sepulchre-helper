package com.sepulchre.model;

import com.sepulchre.util.SepulchreConstants;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Animation;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.Renderable;
import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a wizard statue in the Hallowed Sepulchre.
 * Tracks the statue's animation state to determine danger level.
 */
@Getter
public class WizardStatue
{
	private final GameObject gameObject;
	private final List<WorldPoint> dangerZone = new ArrayList<>();

	// Track if this statue has ever fired (to filter out broken/inactive statues)
	@Setter
	private boolean hasEverFired = false;

	// Count ticks since spawn to know when a full cycle has passed
	@Setter
	private int ticksSinceSpawn = 0;

	// Manual tick counter (counts down from 4)
	@Setter
	private int tickCounter = -1;  // -1 means not yet synced

	// Track if we were firing last tick (to detect transitions)
	private boolean wasFiringLastTick = false;

	// Number of ticks to wait before considering a statue inactive (full cycle + buffer)
	private static final int ACTIVATION_CHECK_TICKS = 10;

	// Configurable tick values for each phase
	@Setter
	private int firePhaseTicks = 4;
	@Setter
	private int safePhaseTicks = 4;

	public WizardStatue(GameObject gameObject)
	{
		this.gameObject = gameObject;
	}

	/**
	 * Returns true if we've confirmed this statue is active (fires),
	 * or if we haven't waited long enough to know yet.
	 */
	public boolean isConfirmedActiveOrUnknown()
	{
		// If it's ever fired, it's definitely active
		if (hasEverFired)
		{
			return true;
		}
		// If we haven't waited a full cycle yet, assume it might be active
		return ticksSinceSpawn < ACTIVATION_CHECK_TICKS;
	}

	/**
	 * Gets the current animation ID of the statue.
	 */
	public int getAnimationId()
	{
		Renderable renderable = gameObject.getRenderable();
		if (renderable instanceof DynamicObject)
		{
			DynamicObject dynamicObject = (DynamicObject) renderable;
			Animation animation = dynamicObject.getAnimation();
			if (animation != null)
			{
				return animation.getId();
			}
		}
		return -1;
	}

	/**
	 * Check if the statue is currently firing (danger).
	 */
	public boolean isFiring()
	{
		return getAnimationId() == SepulchreConstants.WIZARD_ANIM_FIRE;
	}

	/**
	 * Check if the statue is about to fire (warning).
	 */
	public boolean isWarning()
	{
		return getAnimationId() == SepulchreConstants.WIZARD_ANIM_WARNING;
	}

	/**
	 * Check if the statue is safe.
	 */
	public boolean isSafe()
	{
		int animId = getAnimationId();
		return animId != SepulchreConstants.WIZARD_ANIM_FIRE && animId != SepulchreConstants.WIZARD_ANIM_WARNING;
	}

	public void addDangerTile(WorldPoint tile)
	{
		if (!dangerZone.contains(tile))
		{
			dangerZone.add(tile);
		}
	}

	public WorldPoint getLocation()
	{
		return gameObject.getWorldLocation();
	}

	/**
	 * Called each game tick to update the cycle counter.
	 * Resets to configured ticks on phase transitions.
	 */
	public void onGameTick()
	{
		boolean currentlyFiring = isFiring();

		// Detect phase transitions
		if (currentlyFiring && !wasFiringLastTick)
		{
			// Fire just started - reset to fire phase ticks
			tickCounter = firePhaseTicks;
			hasEverFired = true;
		}
		else if (!currentlyFiring && wasFiringLastTick)
		{
			// Fire just ended (safe phase starting) - reset to safe phase ticks
			tickCounter = safePhaseTicks;
		}
		else if (tickCounter > 0)
		{
			// Normal countdown
			tickCounter--;
		}

		wasFiringLastTick = currentlyFiring;
		ticksSinceSpawn++;
	}

	/**
	 * Get the display tick value for the counter.
	 */
	public String getDisplayTicks()
	{
		if (tickCounter < 0)
		{
			return "?";  // Not yet synced
		}
		return String.valueOf(tickCounter);
	}

	/**
	 * Check if the cycle counter is synced.
	 */
	public boolean isSynced()
	{
		return tickCounter >= 0;
	}
}
