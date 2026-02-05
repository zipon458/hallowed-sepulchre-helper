package com.sepulchre.model;

public enum SkillObstacleState
{
	HIGHLIGHT_PRE_CROSS,
	NO_HIGHLIGHT_AT_COFFIN,
	HIGHLIGHT_FOR_RETURN,
	DONE;

	public boolean shouldHighlight()
	{
		return this != DONE;
	}
}
