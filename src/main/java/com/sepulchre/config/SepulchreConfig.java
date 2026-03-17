package com.sepulchre.config;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

import java.awt.Color;

@ConfigGroup("sepulchre")
public interface SepulchreConfig extends Config
{
	@ConfigSection(
		name = "Overlay",
		description = "Settings for overlay",
		position = 0
	)
	String OVERLAY_SECTION = "overlay";

	@ConfigSection(
		name = "Obstacles",
		description = "Settings for obstacle highlights",
		position = 1
	)
	String OBSTACLE_SECTION = "obstacles";

	@ConfigSection(
		name = "Coffin Looting",
		description = "Settings for coffin looting highlights",
		position = 2
	)
	String COFFIN_LOOTING_SECTION = "coffinLooting";

	@ConfigSection(
		name = "Customization",
		description = "",
		position = 3
	)
	String COLOR_SECTION = "colors";

	@ConfigSection(
		name = "Player",
		description = "Settings for player highlights",
		position = 4
	)
	String PLAYER_SECTION = "player";

	@ConfigItem(
		keyName = "showInfoPanel",
		name = "Show Info Panel",
		description = "",
		section = OVERLAY_SECTION,
		position = 0
	)
	default boolean showInfoPanel()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showFloorTimer",
		name = "Show Timer",
		description = "",
		section = OVERLAY_SECTION,
		position = 1
	)
	default boolean showFloorTimer()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showTickPerfectTime",
		name = "Show Tick Perfect",
		description = "Show the tick perfect time(s) for the current route",
		section = OVERLAY_SECTION,
		position = 2
	)
	default boolean showTickPerfectTime()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showRunTimer",
		name = "Show Overall Timer",
		description = "",
		section = OVERLAY_SECTION,
		position = 3
	)
	default boolean showRunTimer()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightProjectiles",
		name = "Highlight Projectiles",
		description = "Highlight crossbow bolts and knight swords (You should disable this option in Agility plugin)",
		section = OBSTACLE_SECTION,
		position = 0
	)
	default boolean highlightProjectiles()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightCrossbows",
		name = "Highlight Crossbowmen",
		description = "",
		section = OBSTACLE_SECTION,
		position = 1
	)
	default boolean highlightCrossbows()
	{
		return true;
	}

	@ConfigItem(
		keyName = "crossbowHighlightStyle",
		name = "Crossbowmen Style",
		description = "",
		section = OBSTACLE_SECTION,
		position = 2
	)
	default HighlightStyle crossbowHighlightStyle()
	{
		return HighlightStyle.CLICKBOX;
	}

	@ConfigItem(
		keyName = "highlightYellowPortals",
		name = "Highlight Yellow Portals",
		description = "",
		section = OBSTACLE_SECTION,
		position = 3
	)
	default boolean highlightYellowPortals()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightBluePortals",
		name = "Highlight Blue Portals",
		description = "",
		section = OBSTACLE_SECTION,
		position = 4
	)
	default boolean highlightBluePortals()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightLightning",
		name = "Highlight Lightning",
		description = "",
		section = OBSTACLE_SECTION,
		position = 5
	)
	default boolean highlightLightning()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightObelisk",
		name = "Highlight Last Eligible Obelisk",
		description = "Highlight the obelisk on your last eligible floor or when door closes",
		section = OBSTACLE_SECTION,
		position = 6
	)
	default boolean highlightObelisk()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapObeliskMenuEntry",
		name = "Swap Obelisk Menu Entry",
		description = "Swap 'Activate' with 'Quick-exit' on highlighted obelisk",
		section = OBSTACLE_SECTION,
		position = 7
	)
	default boolean swapObeliskMenuEntry()
	{
		return false;
	}

	@ConfigItem(
		keyName = "highlightNavigation",
		name = "Highlight Navigation",
		description = "Highlight stairs, platforms, and gates that help navigate the sepulchre",
		section = OBSTACLE_SECTION,
		position = 8
	)
	default boolean highlightNavigation()
	{
		return false;
	}

	@ConfigItem(
		keyName = "filterByRoute",
		name = "Only Show Current Route (Experimental)",
		description = "Only show obstacles relevant to your current route (reduces clutter from parallel paths)",
		section = OBSTACLE_SECTION,
		position = 9
	)
	default boolean filterByRoute()
	{
		return false;
	}

	@ConfigItem(
		keyName = "lootingFloors",
		name = "Looting Floors",
		description = "Which floors to show coffin looting highlights",
		section = COFFIN_LOOTING_SECTION,
		position = 0
	)
	default LootingFloor lootingFloors()
	{
		return LootingFloor.ALL_FLOORS;
	}

	@ConfigItem(
		keyName = "highlightSkillObstacles",
		name = "Highlight Skill Obstacles",
		description = "(You should disable this option in Agility plugin)",
		section = COFFIN_LOOTING_SECTION,
		position = 1
	)
	default boolean highlightSkillObstacles()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightBridges",
		name = "Highlight Bridges",
		description = "",
		section = COFFIN_LOOTING_SECTION,
		position = 2
	)
	default boolean highlightBridges()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightGrapples",
		name = "Highlight Grapples",
		description = "",
		section = COFFIN_LOOTING_SECTION,
		position = 3
	)
	default boolean highlightGrapples()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightPortalFrames",
		name = "Highlight Portal Frames",
		description = "",
		section = COFFIN_LOOTING_SECTION,
		position = 4
	)
	default boolean highlightPortalFrames()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightBraziers",
		name = "Highlight Braziers",
		description = "",
		section = COFFIN_LOOTING_SECTION,
		position = 5
	)
	default boolean highlightBraziers()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showDanger",
		name = "Show Danger",
		description = "Highlight tiles that are currently dangerous",
		section = COLOR_SECTION,
		position = 0
	)
	default boolean showDanger()
	{
		return true;
	}

	@ConfigItem(
		keyName = "dangerBorderOpacity",
		name = "Zones Border Opacity",
		description = "",
		section = COLOR_SECTION,
		position = 1
	)
	default int dangerBorderOpacity()
	{
		return 100;
	}

	@Range(min = 1, max = 5)
	@ConfigItem(
		keyName = "dangerBorderWidth",
		name = "Zones Border Width",
		description = "",
		section = COLOR_SECTION,
		position = 2
	)
	default int dangerBorderWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		keyName = "dangerColor",
		name = "Danger Color",
		description = "",
		section = COLOR_SECTION,
		position = 3
	)
	default Color dangerColor()
	{
		return new Color(255, 0, 0, 100);
	}

	@Alpha
	@ConfigItem(
		keyName = "boltFillColor",
		name = "Bolt Fill Color",
		description = "",
		section = COLOR_SECTION,
		position = 4
	)
	default Color boltFillColor()
	{
		return new Color(0, 255, 255, 100);
	}

	@Alpha
	@ConfigItem(
		keyName = "boltBorderColor",
		name = "Bolt Border Color",
		description = "",
		section = COLOR_SECTION,
		position = 5
	)
	default Color boltBorderColor()
	{
		return new Color(0, 255, 255, 255);
	}

	@Alpha
	@ConfigItem(
		keyName = "swordFillColor",
		name = "Sword Projectile Fill Color",
		description = "",
		section = COLOR_SECTION,
		position = 6
	)
	default Color swordFillColor()
	{
		return new Color(0, 100, 0, 100);
	}

	@Alpha
	@ConfigItem(
		keyName = "swordBorderColor",
		name = "Sword Projectile Border Color",
		description = "",
		section = COLOR_SECTION,
		position = 7
	)
	default Color swordBorderColor()
	{
		return new Color(0, 255, 0, 255);
	}

	@Range(min = 1, max = 5)
	@ConfigItem(
		keyName = "projectileBorderWidth",
		name = "Projectile Border Width",
		description = "",
		section = COLOR_SECTION,
		position = 8
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
		section = COLOR_SECTION,
		position = 9
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
		section = COLOR_SECTION,
		position = 10
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
		section = COLOR_SECTION,
		position = 11
	)
	default Color crossbowColor()
	{
		return new Color(0, 255, 255, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "crossbowBorderColor",
		name = "Crossbow Border Color",
		description = "Border color for crossbowmen highlights",
		section = COLOR_SECTION,
		position = 12
	)
	default Color crossbowBorderColor()
	{
		return new Color(0, 255, 255, 255);
	}

	@Range(min = 1, max = 5)
	@ConfigItem(
		keyName = "crossbowBorderWidth",
		name = "Crossbow Border Width",
		description = "Border width for active crossbowmen highlights",
		section = COLOR_SECTION,
		position = 13
	)
	default int crossbowBorderWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		keyName = "portalYellowColor",
		name = "Yellow Portal Color",
		description = "",
		section = COLOR_SECTION,
		position = 14
	)
	default Color portalYellowColor()
	{
		return new Color(255, 255, 0, 100);
	}

	@Alpha
	@ConfigItem(
		keyName = "portalBlueColor",
		name = "Blue Portal Color",
		description = "",
		section = COLOR_SECTION,
		position = 15
	)
	default Color portalBlueColor()
	{
		return new Color(0, 150, 255, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "obeliskColor",
		name = "Obelisk Color",
		description = "",
		section = COLOR_SECTION,
		position = 16
	)
	default Color obeliskColor()
	{
		return new Color(255, 0, 255, 100);
	}

	@Alpha
	@ConfigItem(
		keyName = "coffinColor",
		name = "Coffin Color",
		description = "",
		section = COLOR_SECTION,
		position = 17
	)
	default Color coffinColor()
	{
		return new Color(138, 43, 226, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "skillObstacleColor",
		name = "Skill Obstacle Color",
		description = "",
		section = COLOR_SECTION,
		position = 18
	)
	default Color skillObstacleColor()
	{
		return new Color(0, 255, 0, 100);
	}

	@Alpha
	@ConfigItem(
		keyName = "skillObstacleMissingReqColor",
		name = "Missing Requirements Color",
		description = "Color for skill obstacles when missing required items/levels",
		section = COLOR_SECTION,
		position = 19
	)
	default Color skillObstacleMissingReqColor()
	{
		return new Color(255, 0, 0, 100);
	}

	@Alpha
	@ConfigItem(
		keyName = "navigationColor",
		name = "Navigation Color",
		description = "Color for stairs, platforms, and gates",
		section = COLOR_SECTION,
		position = 20
	)
	default Color navigationColor()
	{
		return new Color(0, 255, 128, 100);
	}

	@ConfigItem(
		keyName = "playerImmunityOutline",
		name = "Player Immunity Outline",
		description = "Show outline on player when immune after blue portal teleport",
		section = PLAYER_SECTION,
		position = 0
	)
	default boolean playerImmunityOutline()
	{
		return true;
	}

	@ConfigItem(
		keyName = "playerImmunityFill",
		name = "Player Immunity Fill",
		description = "Fill the player model with color during immunity",
		section = PLAYER_SECTION,
		position = 1
	)
	default boolean playerImmunityFill()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "playerImmunityColor",
		name = "Player Immunity Color",
		description = "",
		section = PLAYER_SECTION,
		position = 2
	)
	default Color playerImmunityColor()
	{
		return new Color(0, 150, 255, 255);
	}

	@Range(max = 255)
	@ConfigItem(
		keyName = "playerImmunityFillOpacity",
		name = "Fill Opacity",
		description = "",
		section = PLAYER_SECTION,
		position = 3
	)
	default int playerImmunityFillOpacity()
	{
		return 100;
	}

}

