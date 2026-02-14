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
	private final WorldPoint location;
	private final Set<WorldPoint> fireTiles = new HashSet<>();

	@Setter
	private boolean hasEverFired = false;

	@Setter
	private int ticksSinceSpawn = 0;

	private int cachedAnimationId = -1;

	public WizardStatue(GameObject gameObject)
	{
		this.gameObject = gameObject;
		this.location = gameObject.getWorldLocation();
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

	public void onGameTick()
	{
		cachedAnimationId = GameObjectUtil.getAnimationId(gameObject);
		if (isFiring() || isWarning())
		{
			hasEverFired = true;
		}

		ticksSinceSpawn++;
	}
}
