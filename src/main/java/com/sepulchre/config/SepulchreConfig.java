package com.sepulchre.config;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.awt.Color;

@ConfigGroup("sepulchre")
public interface SepulchreConfig extends Config
{
	@ConfigSection(
		name = "Obstacles",
		description = "Settings for obstacle highlights",
		position = 0
	)
	String obstacleSection = "obstacles";

	@ConfigSection(
		name = "Customization",
		description = "Customize overlay colors and styles",
		position = 1
	)
	String colorSection = "colors";

	@ConfigItem(
		keyName = "highlightBolts",
		name = "Highlight Bolts",
		description = "",
		section = obstacleSection,
		position = 0
	)
	default boolean highlightBolts()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightSwords",
		name = "Highlight Swords",
		description = "",
		section = obstacleSection,
		position = 1
	)
	default boolean highlightSwords()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightWizards",
		name = "Highlight Wizards",
		description = "",
		section = obstacleSection,
		position = 2
	)
	default boolean highlightWizards()
	{
		return true;
	}

	@ConfigItem(
		keyName = "wizardTickCounter",
		name = "Wizard Tick Counter",
		description = "",
		section = obstacleSection,
		position = 3
	)
	default boolean wizardTickCounter()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightLightning",
		name = "Highlight Lightning",
		description = "",
		section = obstacleSection,
		position = 4
	)
	default boolean highlightLightning()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightCrossbows",
		name = "Highlight Crossbows",
		description = "",
		section = obstacleSection,
		position = 5
	)
	default boolean highlightCrossbows()
	{
		return true;
	}

	@ConfigItem(
		keyName = "crossbowHighlightStyle",
		name = "Crossbow Highlight Style",
		description = "",
		section = obstacleSection,
		position = 6
	)
	default HighlightStyle crossbowHighlightStyle()
	{
		return HighlightStyle.TILE;
	}

	@ConfigItem(
		keyName = "highlightYellowPortals",
		name = "Highlight Yellow Portals",
		description = "",
		section = obstacleSection,
		position = 7
	)
	default boolean highlightYellowPortals()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightBluePortals",
		name = "Highlight Blue Portals",
		description = "",
		section = obstacleSection,
		position = 8
	)
	default boolean highlightBluePortals()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFireTiles",
		name = "Show Fire Tiles",
		description = "Highlight tiles with fire",
		section = colorSection,
		position = 0
	)
	default boolean showFireTiles()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFireIncoming",
		name = "Show Fire Incoming",
		description = "Highlight tiles with incoming fire",
		section = colorSection,
		position = 1
	)
	default boolean showFireIncoming()
	{
		return true;
	}

	@ConfigItem(
		keyName = "fireBorderOpacity",
		name = "Fire Border Opacity",
		description = "",
		section = colorSection,
		position = 2
	)
	default int fireBorderOpacity()
	{
		return 100;
	}

	@ConfigItem(
		keyName = "fireBorderWidth",
		name = "Fire Border Width",
		description = "",
		section = colorSection,
		position = 3
	)
	default int fireBorderWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		keyName = "fireColor",
		name = "Fire Color",
		description = "",
		section = colorSection,
		position = 4
	)
	default Color fireColor()
	{
		return new Color(255, 0, 0, 100);
	}

	@Alpha
	@ConfigItem(
		keyName = "incomingColor",
		name = "Incoming Color",
		description = "",
		section = colorSection,
		position = 5
	)
	default Color incomingColor()
	{
		return new Color(255, 165, 0, 100);
	}

	@Alpha
	@ConfigItem(
		keyName = "boltFillColor",
		name = "Bolt Fill Color",
		description = "",
		section = colorSection,
		position = 6
	)
	default Color boltFillColor()
	{
		return new Color(0, 100, 0, 100);
	}

	@Alpha
	@ConfigItem(
		keyName = "boltBorderColor",
		name = "Bolt Border Color",
		description = "",
		section = colorSection,
		position = 7
	)
	default Color boltBorderColor()
	{
		return new Color(0, 255, 0, 255);
	}

	@Alpha
	@ConfigItem(
		keyName = "swordFillColor",
		name = "Sword Fill Color",
		description = "",
		section = colorSection,
		position = 8
	)
	default Color swordFillColor()
	{
		return new Color(0, 100, 0, 100);
	}

	@Alpha
	@ConfigItem(
		keyName = "swordBorderColor",
		name = "Sword Border Color",
		description = "",
		section = colorSection,
		position = 9
	)
	default Color swordBorderColor()
	{
		return new Color(0, 255, 0, 255);
	}

	@ConfigItem(
		keyName = "projectileBorderWidth",
		name = "Projectile Border Width",
		description = "",
		section = colorSection,
		position = 10
	)
	default int projectileBorderWidth()
	{
		return 2;
	}

	@Alpha
	@ConfigItem(
		keyName = "lightningFillColor",
		name = "Lightning Fill Color",
		description = "",
		section = colorSection,
		position = 11
	)
	default Color lightningFillColor()
	{
		return new Color(255, 255, 255, 100);
	}

	@Alpha
	@ConfigItem(
		keyName = "lightningBorderColor",
		name = "Lightning Border Color",
		description = "",
		section = colorSection,
		position = 12
	)
	default Color lightningBorderColor()
	{
		return new Color(255, 255, 255, 255);
	}

	@Alpha
	@ConfigItem(
		keyName = "crossbowColor",
		name = "Crossbow Color",
		description = "",
		section = colorSection,
		position = 13
	)
	default Color crossbowColor()
	{
		return new Color(0, 255, 255, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "portalYellowColor",
		name = "Yellow Portal Color",
		description = "",
		section = colorSection,
		position = 14
	)
	default Color portalYellowColor()
	{
		return new Color(255, 255, 0, 0);
	}

	@Alpha
	@ConfigItem(
		keyName = "portalBlueColor",
		name = "Blue Portal Color",
		description = "",
		section = colorSection,
		position = 15
	)
	default Color portalBlueColor()
	{
		return new Color(0, 150, 255, 150);
	}
}
