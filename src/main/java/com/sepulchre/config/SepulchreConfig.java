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
	// ==================== SECTIONS ====================

	@ConfigSection(
		name = "Obstacles",
		description = "Settings for obstacle highlights",
		position = 0
	)
	String obstacleSection = "obstacles";

	@ConfigSection(
		name = "Loot",
		description = "Settings for loot and coffins",
		position = 1
	)
	String lootSection = "loot";

	@ConfigSection(
		name = "Colors",
		description = "Customize overlay colors",
		position = 2
	)
	String colorSection = "colors";

	@ConfigSection(
		name = "Debug",
		description = "Debug options for development",
		position = 3
	)
	String debugSection = "debug";

	// ==================== OBSTACLES ====================

	@ConfigItem(
		keyName = "highlightArrows",
		name = "Highlight Arrows",
		description = "Highlight arrow trap danger zones",
		section = obstacleSection,
		position = 0
	)
	default boolean highlightArrows()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightSwords",
		name = "Highlight Swords",
		description = "Highlight sword statue danger zones",
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
		description = "Highlight wizard danger zones",
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
		description = "Show ticks until wizard attacks",
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
		description = "Highlight lightning strike zones",
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
		description = "Highlight crossbow danger zones",
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
		description = "How to highlight crossbow statues",
		section = obstacleSection,
		position = 6
	)
	default HighlightStyle crossbowHighlightStyle()
	{
		return HighlightStyle.CLICKBOX;
	}

	@ConfigItem(
		keyName = "highlightPortals",
		name = "Highlight Portals",
		description = "Highlight floor exit portals",
		section = obstacleSection,
		position = 7
	)
	default boolean highlightPortals()
	{
		return true;
	}

	// ==================== LOOT ====================

	@ConfigItem(
		keyName = "highlightCoffins",
		name = "Highlight Coffins",
		description = "Highlight lootable coffins",
		section = lootSection,
		position = 0
	)
	default boolean highlightCoffins()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightGrandCoffin",
		name = "Highlight Grand Coffin",
		description = "Highlight the grand coffin at the end of each floor",
		section = lootSection,
		position = 1
	)
	default boolean highlightGrandCoffin()
	{
		return true;
	}

	// ==================== COLORS ====================

	@ConfigItem(
		keyName = "showDangerZones",
		name = "Show Danger Zones",
		description = "Show red danger zones (1 tick or less)",
		section = colorSection,
		position = 0
	)
	default boolean showDangerZones()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "dangerColor",
		name = "Danger Color",
		description = "Color for dangerous areas",
		section = colorSection,
		position = 1
	)
	default Color dangerColor()
	{
		return new Color(255, 0, 0, 100);
	}

	@ConfigItem(
		keyName = "showWarningZones",
		name = "Show Warning Zones",
		description = "Show orange warning zones (2-3 ticks)",
		section = colorSection,
		position = 2
	)
	default boolean showWarningZones()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "warningColor",
		name = "Warning Color",
		description = "Color for warning areas",
		section = colorSection,
		position = 3
	)
	default Color warningColor()
	{
		return new Color(255, 165, 0, 100);
	}

	@ConfigItem(
		keyName = "showSafeZones",
		name = "Show Safe Zones",
		description = "Show green safe zones (4+ ticks)",
		section = colorSection,
		position = 4
	)
	default boolean showSafeZones()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "safeColor",
		name = "Safe Color",
		description = "Color for safe areas",
		section = colorSection,
		position = 5
	)
	default Color safeColor()
	{
		return new Color(0, 255, 0, 100);
	}

	@Alpha
	@ConfigItem(
		keyName = "coffinColor",
		name = "Coffin Color",
		description = "Color for coffin highlights",
		section = colorSection,
		position = 6
	)
	default Color coffinColor()
	{
		return new Color(138, 43, 226, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "portalColor",
		name = "Portal Color",
		description = "Color for portal highlights",
		section = colorSection,
		position = 7
	)
	default Color portalColor()
	{
		return new Color(0, 191, 255, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "crossbowColor",
		name = "Crossbow Color",
		description = "Color for crossbow statues when firing",
		section = colorSection,
		position = 8
	)
	default Color crossbowColor()
	{
		return new Color(0, 255, 255, 150);  // Cyan
	}

	@ConfigItem(
		keyName = "tileBorderOpacity",
		name = "Tile Border Opacity",
		description = "Opacity of tile border outlines (0-255)",
		section = colorSection,
		position = 9
	)
	default int tileBorderOpacity()
	{
		return 255;
	}

	@ConfigItem(
		keyName = "tileBorderWidth",
		name = "Tile Border Width",
		description = "Width of tile border outlines (1-5)",
		section = colorSection,
		position = 10
	)
	default int tileBorderWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		keyName = "arrowColor",
		name = "Arrow Color",
		description = "Color for arrow projectile highlights",
		section = colorSection,
		position = 11
	)
	default Color arrowColor()
	{
		return new Color(255, 255, 0, 150);  // Yellow
	}

	@Alpha
	@ConfigItem(
		keyName = "swordColor",
		name = "Sword Color",
		description = "Color for sword hazard highlights",
		section = colorSection,
		position = 12
	)
	default Color swordColor()
	{
		return new Color(255, 0, 255, 150);  // Magenta
	}

	// ==================== DEBUG ====================

	@ConfigItem(
		keyName = "showAnimationIds",
		name = "Show Animation IDs",
		description = "Display animation IDs above tracked objects for debugging",
		section = debugSection,
		position = 0
	)
	default boolean showAnimationIds()
	{
		return false;
	}

	@ConfigItem(
		keyName = "showGraphicsIds",
		name = "Show Graphics IDs",
		description = "Display graphics object IDs for debugging (flames, lightning, etc.)",
		section = debugSection,
		position = 1
	)
	default boolean showGraphicsIds()
	{
		return false;
	}

	@ConfigItem(
		keyName = "showProjectileIds",
		name = "Show Projectile IDs",
		description = "Display projectile IDs for debugging",
		section = debugSection,
		position = 2
	)
	default boolean showProjectileIds()
	{
		return false;
	}

	@ConfigItem(
		keyName = "showGameObjectIds",
		name = "Show Game Object IDs",
		description = "Display game object IDs for all nearby objects",
		section = debugSection,
		position = 3
	)
	default boolean showGameObjectIds()
	{
		return false;
	}

	@ConfigItem(
		keyName = "wizardFireTicks",
		name = "Wizard Fire Ticks",
		description = "Number of ticks for wizard fire phase countdown",
		section = debugSection,
		position = 4
	)
	default int wizardFireTicks()
	{
		return 2;
	}

	@ConfigItem(
		keyName = "wizardSafeTicks",
		name = "Wizard Safe Ticks",
		description = "Number of ticks for wizard safe phase countdown",
		section = debugSection,
		position = 5
	)
	default int wizardSafeTicks()
	{
		return 4;
	}
}
