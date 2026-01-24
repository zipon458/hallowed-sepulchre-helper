package com.sepulchre.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HighlightStyle
{
	TILE("Tile"),
	HULL("Hull"),
	CLICKBOX("Clickbox");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}
