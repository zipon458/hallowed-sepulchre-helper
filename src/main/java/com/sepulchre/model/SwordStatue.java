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
}
