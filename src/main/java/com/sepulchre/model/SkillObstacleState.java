package com.sepulchre.model;

public enum SkillObstacleState
{
	HIGHLIGHT_PRE_CROSS,
	USED_AWAITING_LOOT,
	HIGHLIGHT_FOR_RETURN,
	DONE;

	public boolean isStillRelevant()
	{
		return this != DONE;
	}
}
