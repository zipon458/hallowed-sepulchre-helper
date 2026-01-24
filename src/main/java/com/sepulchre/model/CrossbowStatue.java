package com.sepulchre.model;

import com.sepulchre.util.GameObjectUtil;
import com.sepulchre.util.SepulchreConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.GameObject;

@Getter
@RequiredArgsConstructor
public class CrossbowStatue
{
	private final GameObject gameObject;

	public boolean isDangerous()
	{
		return SepulchreConstants.CROSSBOW_DANGER_ANIMS.contains(GameObjectUtil.getAnimationId(gameObject));
	}
}
