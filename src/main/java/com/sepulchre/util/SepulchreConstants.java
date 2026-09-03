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

	public static final int VARBIT_HALLOWED_TIME_SPENT = 10393;
	public static final int VARBIT_HALLOWED_CURRENT_FLOOR_TIME_SPENT = 10417;

	public static final int[] FLOOR_AGILITY_REQUIREMENTS = {52, 62, 72, 77, 87};

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
	public static final String BRIDGE_BUILT_MESSAGE = "You repair the broken bridge";
	public static final String BRIDGE_CROSSED_MESSAGE = "You rapidly make your way across the bridge";
	public static final String GRAPPLE_USED_MESSAGE = "and swing safely to the other side";
	public static final String PORTAL_USED_MESSAGE = "You pass through the portal and end up on the other side";
	public static final String BRAZIER_SACRIFICED_MESSAGE = "You see the flame change as your offerings are accepted";
	public static final String COFFIN_LOOTED_MESSAGE = "You push the coffin lid aside";

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

	public static final Set<Integer> COFFIN_OBJECT_IDS = Set.of(39544, 39545, 39536, 39537, 39538);
	public static final Set<Integer> COFFIN_MORPH_OPEN_IDS = Set.of(38831, 38833, 38835, 38837);
	public static final int GRAND_COFFIN_OBJECT_ID = 39539;
	public static final int GRAND_COFFIN_MORPH_OPEN = 38839;
	public static final Set<Integer> BRIDGE_OBJECT_IDS = Set.of(39527, 39528);
	public static final int GRAPPLE_OBJECT_ID = 39524;
	public static final int PORTAL_FRAME_OBJECT_ID = 39533;
	public static final Set<Integer> BRAZIER_OBJECT_IDS = Set.of(39525, 39526);
	public static final Set<Integer> BRAZIER_UNSACRIFICED_MORPHS = Set.of(38798, 38801);
	public static final int HOLY_BARRIER_OBJECT_ID = 39534;
	public static final int FLOOR_5_BARRIER_OBJECT_ID = 39540;
	public static final int MAGICAL_OBELISK_ID = 38451;

	public static final Set<Integer> STAIRS_IDS = Set.of(38453, 38454, 38462, 38463, 38464, 38465, 38466, 38467, 38468, 38469, 38471, 38472, 38473, 38474, 38475, 38476, 39622, 39623, 39624, 39625);
	public static final Set<Integer> END_FLOOR_STAIRS_IDS = Set.of(39622, 39623, 39624, 39625);

	public static final Set<Integer> ALL_PLATFORM_IDS = Set.of(38455, 38456, 38457, 38458, 38459, 38470, 38477);

	public static final int GATE_WALL_OBJECT_ID = 38460;
}
