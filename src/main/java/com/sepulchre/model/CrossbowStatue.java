package com.sepulchre.model;

import com.sepulchre.util.SepulchreConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Animation;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.Renderable;

/**
 * Represents a crossbow statue in the Hallowed Sepulchre.
 * Tracks the statue's animation state to determine when it's dangerous.
 */
@Getter
@RequiredArgsConstructor
public class CrossbowStatue
{
	private final GameObject gameObject;

	/**
	 * Checks if the crossbow statue is currently in a dangerous animation state
	 * (charging or firing).
	 */
	public boolean isDangerous()
	{
		int animationId = getAnimationId();
		return SepulchreConstants.CROSSBOW_DANGER_ANIMS.contains(animationId);
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
}
