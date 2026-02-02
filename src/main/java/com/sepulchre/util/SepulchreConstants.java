package com.sepulchre.util;

import com.sepulchre.model.SepulchreRoute;
import net.runelite.api.coords.WorldPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class SepulchreConstants
{
	private SepulchreConstants()
	{
	}

	public static final int TIMER_WIDGET_GROUP = 668;
	public static final int TIMER_WIDGET_CHILD = 4;

	public static final int[] FLOOR_AGILITY_REQUIREMENTS = {52, 62, 72, 82, 92};

	private static final Map<Integer, Map<WorldPoint, SepulchreRoute>> SPAWN_TILES_BY_FLOOR = new HashMap<>();
	static
	{
		Map<WorldPoint, SepulchreRoute> floor1 = new HashMap<>();
		floor1.put(new WorldPoint(2253, 6018, 2), SepulchreRoute.FLOOR_1_NORTHWEST);
		floor1.put(new WorldPoint(2309, 6011, 2), SepulchreRoute.FLOOR_1_NORTHEAST);
		floor1.put(new WorldPoint(2293, 5949, 2), SepulchreRoute.FLOOR_1_SOUTHEAST);
		floor1.put(new WorldPoint(2234, 5960, 2), SepulchreRoute.FLOOR_1_SOUTHWEST);
		SPAWN_TILES_BY_FLOOR.put(1, floor1);

		Map<WorldPoint, SepulchreRoute> floor2 = new HashMap<>();
		floor2.put(new WorldPoint(2528, 5988, 2), SepulchreRoute.FLOOR_2_NORTH);
		floor2.put(new WorldPoint(2532, 5984, 2), SepulchreRoute.FLOOR_2_EAST);
		floor2.put(new WorldPoint(2528, 5980, 2), SepulchreRoute.FLOOR_2_SOUTH);
		floor2.put(new WorldPoint(2524, 5984, 2), SepulchreRoute.FLOOR_2_WEST);
		SPAWN_TILES_BY_FLOOR.put(2, floor2);

		Map<WorldPoint, SepulchreRoute> floor3 = new HashMap<>();
		floor3.put(new WorldPoint(2404, 5856, 2), SepulchreRoute.FLOOR_3_EAST);
		floor3.put(new WorldPoint(2396, 5856, 2), SepulchreRoute.FLOOR_3_WEST);
		SPAWN_TILES_BY_FLOOR.put(3, floor3);

		Map<WorldPoint, SepulchreRoute> floor4 = new HashMap<>();
		floor4.put(new WorldPoint(2528, 5860, 2), SepulchreRoute.FLOOR_4_NORTH);
		floor4.put(new WorldPoint(2528, 5852, 2), SepulchreRoute.FLOOR_4_SOUTH);
		SPAWN_TILES_BY_FLOOR.put(4, floor4);

		SPAWN_TILES_BY_FLOOR.put(5, new HashMap<>());
	}

	public static Map<WorldPoint, SepulchreRoute> getSpawnTilesForFloor(int floor)
	{
		return SPAWN_TILES_BY_FLOOR.get(floor);
	}

	public static final String FLOOR_1_MESSAGE = "You venture down into the Hallowed Sepulchre";
	public static final String FLOOR_CHANGE_MESSAGE = "You venture further down into the Hallowed Sepulchre";

	public static final Set<Integer> CROSSBOW_STATUE_IDS = Set.of(38444, 38445, 38446);
	public static final Set<Integer> CROSSBOW_DANGER_ANIMS = Set.of(8682, 8683, 8684, 8685);
	public static final Set<Integer> BOLT_NULL_NPC_IDS = Set.of(9672, 9673, 9674);

	public static final Set<Integer> WIZARD_FLAME_OBJECT_IDS = Set.of(
		38409, 38410, 38411, 38412, 38413, 38414, 38415,
		38416, 38417, 38418, 38419, 38420,
		38421, 38422, 38423, 38424, 38425
	);
	public static final int WIZARD_ANIM_FIRE = 8658;
	public static final int WIZARD_ANIM_WARNING = 8657;
	public static final int WIZARD_ANIM_PRE_WARNING = 8656;

	public static final Set<Integer> SWORD_STATUE_IDS = Set.of(
		38428, 38429, 38430, 38431, 38432, 38433, 38434, 38435,
		38436, 38437, 38438, 38439, 38440, 38441, 38442, 38443
	);
	public static final int SWORD_STATUE_ANIM_WINDUP_1 = 8665;
	public static final int SWORD_STATUE_ANIM_WINDUP_1B = 8666;
	public static final int SWORD_STATUE_ANIM_WINDUP_2 = 8667;
	public static final int SWORD_STATUE_ANIM_FIRE = 8669;
	public static final Set<Integer> SWORD_STATUE_DANGER_ANIMS = Set.of(
		8670,
		SWORD_STATUE_ANIM_WINDUP_1,
		SWORD_STATUE_ANIM_WINDUP_1B,
		SWORD_STATUE_ANIM_WINDUP_2
	);
	public static final Set<Integer> SWORD_NULL_NPC_IDS = Set.of(9669, 9670, 9671);

	public static final int LIGHTNING_GRAPHICS_ID = 1796;
	public static final Set<Integer> BLUE_PORTAL_GRAPHICS_IDS = Set.of(1799, 1815);
	public static final Set<Integer> YELLOW_PORTAL_GRAPHICS_IDS = Set.of(1800, 1816);
	public static final int BLUE_PORTAL_TELEPORT_GRAPHICS_ID = 1803;
	public static final int YELLOW_PORTAL_TELEPORT_GRAPHICS_ID = 1804;

	public static final int MAGICAL_OBELISK_ID = 38451;
}
