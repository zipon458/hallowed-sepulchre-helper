package com.sepulchre.model;

import com.sepulchre.util.GameObjectUtil;
import com.sepulchre.util.SepulchreConstants;
import lombok.Getter;
import net.runelite.api.GameObject;
import net.runelite.api.coords.WorldPoint;

import java.util.Set;

@Getter
public class SwordStatue
{
	private final GameObject gameObject;
	private final WorldPoint location;
	private final int windup1Anim;

	private boolean wasInDangerousState = false;
	private int cachedAnimationId = -1;

	private static final Set<Integer> SPECIAL_STATUE_IDS = Set.of(38428, 38432, 38433, 38434, 38435, 38436, 38440, 38441, 38442, 38443);

	public SwordStatue(GameObject gameObject)
	{
		this.gameObject = gameObject;
		this.location = gameObject.getWorldLocation();

		boolean isSpecial = SPECIAL_STATUE_IDS.contains(gameObject.getId());
		this.windup1Anim = isSpecial ? SepulchreConstants.SWORD_STATUE_ANIM_WINDUP_1B : SepulchreConstants.SWORD_STATUE_ANIM_WINDUP_1;

		cachedAnimationId = GameObjectUtil.getAnimationId(gameObject);
		if (SepulchreConstants.SWORD_STATUE_DANGER_ANIMS.contains(cachedAnimationId))
		{
			wasInDangerousState = true;
		}
	}

	public WorldPoint getLocation()
	{
		return location;
	}

	public WorldPoint getDangerZoneCenter()
	{
		if (gameObject == null)
		{
			return null;
		}

		int orientation = gameObject.getOrientation();
		int dx = 0;
		int dy = 0;

		if (orientation >= 256 && orientation < 768)
		{
			dx = -2;
		}
		else if (orientation >= 768 && orientation < 1280)
		{
			dy = 2;
		}
		else if (orientation >= 1280 && orientation < 1792)
		{
			dx = 2;
		}
		else
		{
			dy = -2;
		}

		return new WorldPoint(
			location.getX() + dx,
			location.getY() + dy,
			location.getPlane()
		);
	}

	public void onGameTick()
	{
		cachedAnimationId = GameObjectUtil.getAnimationId(gameObject);

		if (cachedAnimationId == -1)
		{
			return;
		}

		if (cachedAnimationId == SepulchreConstants.SWORD_STATUE_ANIM_FIRE)
		{
			wasInDangerousState = false;
			return;
		}

		wasInDangerousState = SepulchreConstants.SWORD_STATUE_DANGER_ANIMS.contains(cachedAnimationId);
	}

	public boolean isInDangerousState()
	{
		if (cachedAnimationId == SepulchreConstants.SWORD_STATUE_ANIM_FIRE)
		{
			return false;
		}

		return wasInDangerousState;
	}

	public boolean isInImminentDangerState()
	{
		return cachedAnimationId == windup1Anim || cachedAnimationId == SepulchreConstants.SWORD_STATUE_ANIM_WINDUP_2;
	}

	public String getDisplayTicks()
	{
		if (cachedAnimationId == windup1Anim)
		{
			return "1";
		}
		if (cachedAnimationId == SepulchreConstants.SWORD_STATUE_ANIM_WINDUP_2)
		{
			return "0";
		}
		return null;
	}
}
