package com.sepulchre.util;

import net.runelite.api.Animation;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.Renderable;

public final class GameObjectUtil
{
	private GameObjectUtil() {}

	public static int getAnimationId(GameObject gameObject)
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
