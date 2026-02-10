package com.sepulchre.util;

import com.sepulchre.model.SepulchreRoute;
import net.runelite.api.coords.WorldPoint;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Maps obstacle WorldPoints to the routes they are relevant for.
 */
public final class RouteObstacles
{
	private RouteObstacles()
	{
	}

	// Convenience sets for shared areas
	public static final Set<SepulchreRoute> FLOOR_3_BOTH = EnumSet.of(
		SepulchreRoute.FLOOR_3_EAST,
		SepulchreRoute.FLOOR_3_WEST
	);

	public static final Set<SepulchreRoute> FLOOR_4_BOTH = EnumSet.of(
		SepulchreRoute.FLOOR_4_NORTH,
		SepulchreRoute.FLOOR_4_SOUTH
	);

	// Floor 2 North + West share a coffin
	public static final Set<SepulchreRoute> FLOOR_2_NORTH_WEST = EnumSet.of(
		SepulchreRoute.FLOOR_2_NORTH,
		SepulchreRoute.FLOOR_2_WEST
	);

	// Floor 2 East + South share a coffin
	public static final Set<SepulchreRoute> FLOOR_2_EAST_SOUTH = EnumSet.of(
		SepulchreRoute.FLOOR_2_EAST,
		SepulchreRoute.FLOOR_2_SOUTH
	);

	public static final Set<SepulchreRoute> FLOOR_3_4_5_ALL = EnumSet.of(
		SepulchreRoute.FLOOR_3_EAST,
		SepulchreRoute.FLOOR_3_WEST,
		SepulchreRoute.FLOOR_4_NORTH,
		SepulchreRoute.FLOOR_4_SOUTH,
		SepulchreRoute.FLOOR_5_SINGLE
	);

	// Main mapping: WorldPoint -> Set of routes this obstacle is relevant for
	private static final Map<WorldPoint, Set<SepulchreRoute>> OBSTACLE_ROUTES = new HashMap<>();

	// Tile mappings for portals and lightning
	private static final Map<WorldPoint, Set<SepulchreRoute>> YELLOW_PORTAL_ROUTES = new HashMap<>();
	private static final Map<WorldPoint, Set<SepulchreRoute>> BLUE_PORTAL_ROUTES = new HashMap<>();
	private static final Map<WorldPoint, Set<SepulchreRoute>> LIGHTNING_ROUTES = new HashMap<>();

	// Convenience set for Floor 1 shared lower section (all 4 routes)
	public static final Set<SepulchreRoute> FLOOR_1_ALL = EnumSet.of(
		SepulchreRoute.FLOOR_1_SOUTHEAST,
		SepulchreRoute.FLOOR_1_SOUTHWEST,
		SepulchreRoute.FLOOR_1_NORTHEAST,
		SepulchreRoute.FLOOR_1_NORTHWEST
	);

	// Floor 1 East routes (SE + NE share grapples)
	public static final Set<SepulchreRoute> FLOOR_1_EAST = EnumSet.of(
		SepulchreRoute.FLOOR_1_SOUTHEAST,
		SepulchreRoute.FLOOR_1_NORTHEAST
	);

	// Floor 1 West routes (SW + NW share bridge)
	public static final Set<SepulchreRoute> FLOOR_1_WEST = EnumSet.of(
		SepulchreRoute.FLOOR_1_SOUTHWEST,
		SepulchreRoute.FLOOR_1_NORTHWEST
	);

	// Floor 1 South routes (SE + SW share obelisks)
	public static final Set<SepulchreRoute> FLOOR_1_SOUTH = EnumSet.of(
		SepulchreRoute.FLOOR_1_SOUTHEAST,
		SepulchreRoute.FLOOR_1_SOUTHWEST
	);

	// Floor 1 North routes (NE + NW share obelisks)
	public static final Set<SepulchreRoute> FLOOR_1_NORTH = EnumSet.of(
		SepulchreRoute.FLOOR_1_NORTHEAST,
		SepulchreRoute.FLOOR_1_NORTHWEST
	);

	static
	{
		// =====================
		// FLOOR 1 - 4 separate routes
		// =====================

		// Floor 1 Southeast - Upper section obstacles
		addObstacle(new WorldPoint(2284, 5953, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHEAST)); // SWORD_STATUE
		addObstacle(new WorldPoint(2275, 5964, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2270, 5964, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2275, 5968, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2270, 5968, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2270, 5972, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2275, 5972, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2260, 5975, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHEAST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2260, 5976, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHEAST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2260, 5977, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHEAST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2259, 5984, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHEAST)); // STAIRS

		// Floor 1 Southwest - Upper section obstacles
		addObstacle(new WorldPoint(2238, 5993, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHWEST)); // SWORD_STATUE
		addObstacle(new WorldPoint(2242, 5979, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2247, 5979, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2247, 5983, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2242, 5983, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2247, 5986, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2242, 5986, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2247, 5990, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2242, 5990, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2264, 6003, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHWEST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2264, 6002, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHWEST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2264, 6001, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHWEST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2262, 5988, 2), EnumSet.of(SepulchreRoute.FLOOR_1_SOUTHWEST)); // STAIRS

		// Floor 1 Northwest - Upper section obstacles
		addObstacle(new WorldPoint(2284, 6018, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2284, 6017, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2284, 6016, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2280, 6011, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2285, 6011, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2280, 6007, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2285, 6007, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2285, 6002, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2280, 6002, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2280, 5999, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2285, 5999, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2285, 5997, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2285, 5993, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2280, 5993, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2282, 5988, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHWEST)); // STAIRS

		// Floor 1 Northeast - Upper section obstacles
		addObstacle(new WorldPoint(2297, 6004, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2297, 6002, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2297, 6000, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2302, 6000, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2302, 5998, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2302, 5996, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2305, 5990, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHEAST)); // SWORD_STATUE
		addObstacle(new WorldPoint(2294, 5967, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2294, 5972, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2290, 5972, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2290, 5967, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2288, 5967, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHEAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2285, 5984, 2), EnumSet.of(SepulchreRoute.FLOOR_1_NORTHEAST)); // STAIRS

		// Floor 1 - Shared lower section obstacles
		// Bridge (shared by SE + SW - south routes)
		addObstacle(new WorldPoint(2262, 5980, 1), FLOOR_1_SOUTH); // BRIDGE
		addObstacle(new WorldPoint(2262, 5976, 1), FLOOR_1_SOUTH); // BRIDGE
		// Grapples (shared by NE + NW - north routes)
		addObstacle(new WorldPoint(2283, 5978, 1), FLOOR_1_NORTH); // GRAPPLE
		addObstacle(new WorldPoint(2281, 5978, 1), FLOOR_1_NORTH); // GRAPPLE
		// South obelisks (shared by SE + SW)
		addObstacle(new WorldPoint(2267, 5982, 1), FLOOR_1_SOUTH); // OBELISK
		addObstacle(new WorldPoint(2267, 5986, 1), FLOOR_1_SOUTH); // OBELISK
		// North obelisks (shared by NE + NW)
		addObstacle(new WorldPoint(2277, 5982, 1), FLOOR_1_NORTH); // OBELISK
		addObstacle(new WorldPoint(2277, 5986, 1), FLOOR_1_NORTH); // OBELISK
		// Coffin (shared by all Floor 1 routes)
		addObstacle(new WorldPoint(2272, 5973, 1), FLOOR_1_ALL); // COFFIN
		// South platforms and stairs (shared by SE + SW)
		addObstacle(new WorldPoint(2265, 5984, 1), FLOOR_1_SOUTH); // PLATFORM
		addObstacle(new WorldPoint(2270, 5984, 1), FLOOR_1_SOUTH); // STAIRS
		// North platforms and stairs (shared by NE + NW)
		addObstacle(new WorldPoint(2279, 5984, 1), FLOOR_1_NORTH); // PLATFORM
		addObstacle(new WorldPoint(2275, 5984, 1), FLOOR_1_NORTH); // STAIRS

		// =====================
		// FLOOR 2 - 4 separate routes
		// =====================

		// Floor 2 East - Upper section (plane 2)
		addObstacle(new WorldPoint(2538, 5982, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2544, 5982, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2548, 5982, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2538, 5987, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2542, 5987, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2548, 5987, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2556, 5954, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2555, 5954, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2554, 5954, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2528, 5962, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // BRAZIER
		addObstacle(new WorldPoint(2528, 5959, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // BRAZIER
		addObstacle(new WorldPoint(2524, 5961, 2), FLOOR_2_EAST_SOUTH); // COFFIN (shared with South)

		// Floor 2 East - Lower section (plane 1)
		addObstacle(new WorldPoint(2558, 5972, 1), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2558, 5978, 1), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2558, 5980, 1), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2553, 5972, 1), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2553, 5974, 1), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2553, 5980, 1), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2546, 5987, 1), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2542, 5987, 1), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2540, 5982, 1), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2544, 5982, 1), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2533, 5986, 1), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // OBELISK
		addObstacle(new WorldPoint(2533, 5982, 1), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // OBELISK
		addObstacle(new WorldPoint(2536, 5958, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // STAIRS
		addObstacle(new WorldPoint(2535, 5984, 1), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // PLATFORM
		addObstacle(new WorldPoint(2531, 5984, 1), EnumSet.of(SepulchreRoute.FLOOR_2_EAST)); // STAIRS

		// =====================
		// FLOOR 3 - 2 routes, lower section shared
		// =====================

		// Floor 3 East - Upper section (plane 2)
		addObstacle(new WorldPoint(2413, 5862, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2413, 5864, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2413, 5866, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2418, 5862, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2418, 5864, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2418, 5866, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2413, 5871, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2413, 5873, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2413, 5875, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2418, 5871, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2418, 5873, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2418, 5875, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2408, 5867, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // GRAPPLE
		addObstacle(new WorldPoint(2408, 5869, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // GRAPPLE
		addObstacle(new WorldPoint(2400, 5869, 2), FLOOR_3_BOTH); // COFFIN (shared with West)
		addObstacle(new WorldPoint(2412, 5880, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // SWORD_STATUE
		addObstacle(new WorldPoint(2426, 5837, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2425, 5837, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2424, 5837, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2419, 5836, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // SWORD_STATUE
		addObstacle(new WorldPoint(2409, 5836, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2409, 5838, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2409, 5840, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2409, 5842, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2409, 5844, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2414, 5836, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2414, 5838, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2414, 5840, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2414, 5842, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2414, 5844, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2414, 5830, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // BRAZIER
		addObstacle(new WorldPoint(2414, 5827, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // BRAZIER
		addObstacle(new WorldPoint(2419, 5828, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // COFFIN
		addObstacle(new WorldPoint(2405, 5828, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST)); // STAIRS

		// Floor 3 - Lower section (plane 1) - shared by East + West
		addObstacle(new WorldPoint(2401, 5822, 1), FLOOR_3_BOTH); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2400, 5822, 1), FLOOR_3_BOTH); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2399, 5822, 1), FLOOR_3_BOTH); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2398, 5832, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2398, 5834, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2398, 5836, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2398, 5838, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2403, 5832, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2403, 5834, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2403, 5836, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2403, 5838, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2398, 5842, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2398, 5844, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2398, 5846, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2398, 5848, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2403, 5842, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2403, 5844, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2403, 5846, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2403, 5848, 1), FLOOR_3_BOTH); // WIZARD_STATUE
		addObstacle(new WorldPoint(2402, 5860, 1), FLOOR_3_BOTH); // OBELISK
		addObstacle(new WorldPoint(2398, 5860, 1), FLOOR_3_BOTH); // OBELISK
		// Platforms and stairs (shared by East + West)
		addObstacle(new WorldPoint(2405, 5856, 1), FLOOR_3_BOTH); // PLATFORM
		addObstacle(new WorldPoint(2395, 5856, 1), FLOOR_3_BOTH); // PLATFORM
		addObstacle(new WorldPoint(2400, 5859, 1), FLOOR_3_BOTH); // STAIRS

		// Floor 2 North - Upper section (plane 2)
		addObstacle(new WorldPoint(2528, 6005, 2), FLOOR_2_NORTH_WEST); // COFFIN (shared with West)
		addObstacle(new WorldPoint(2528, 6012, 2), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2528, 6013, 2), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2528, 6014, 2), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2532, 6013, 2), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // SWORD_STATUE
		addObstacle(new WorldPoint(2539, 6005, 2), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // GRAPPLE
		addObstacle(new WorldPoint(2539, 6003, 2), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // GRAPPLE

		// Floor 2 North - Lower section (plane 1)
		addObstacle(new WorldPoint(2526, 6009, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2526, 6005, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2526, 6001, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2531, 6009, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2531, 6003, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2530, 5989, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // OBELISK
		addObstacle(new WorldPoint(2526, 5989, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // OBELISK
		addObstacle(new WorldPoint(2518, 6001, 2), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // PLATFORM
		addObstacle(new WorldPoint(2518, 6003, 2), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // PLATFORM
		addObstacle(new WorldPoint(2518, 6005, 2), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // PLATFORM
		addObstacle(new WorldPoint(2518, 6007, 2), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // PLATFORM
		addObstacle(new WorldPoint(2550, 6008, 2), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // STAIRS
		addObstacle(new WorldPoint(2528, 5991, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // PLATFORM
		addObstacle(new WorldPoint(2528, 5987, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH)); // STAIRS

		// Floor 2 West - Upper section (plane 2)
		addObstacle(new WorldPoint(2511, 5984, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // SWORD_STATUE
		// Note: Coffin at 2528, 6005 is shared with North (already added above)
		addObstacle(new WorldPoint(2517, 6005, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // GRAPPLE
		addObstacle(new WorldPoint(2517, 6003, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // GRAPPLE
		addObstacle(new WorldPoint(2491, 5986, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2491, 5983, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2491, 5981, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2486, 5988, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2486, 5983, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2486, 5981, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // WIZARD_STATUE

		// Floor 2 West - Lower section (plane 1)
		addObstacle(new WorldPoint(2493, 5984, 1), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // SWORD_STATUE
		addObstacle(new WorldPoint(2523, 5982, 1), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // OBELISK
		addObstacle(new WorldPoint(2523, 5986, 1), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // OBELISK
		addObstacle(new WorldPoint(2493, 5974, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // STAIRS
		addObstacle(new WorldPoint(2521, 5984, 1), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // PLATFORM
		addObstacle(new WorldPoint(2526, 5984, 1), EnumSet.of(SepulchreRoute.FLOOR_2_WEST)); // STAIRS

		// Floor 2 South - Upper section (plane 2)
		addObstacle(new WorldPoint(2512, 5973, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2512, 5972, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2512, 5971, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2506, 5963, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH)); // SWORD_STATUE
		// Note: Coffin at 2524, 5961, 2 is shared with East (already added above)
		addObstacle(new WorldPoint(2520, 5962, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH)); // BRAZIER
		addObstacle(new WorldPoint(2520, 5959, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH)); // BRAZIER

		// Floor 2 South - Lower section (plane 1)
		addObstacle(new WorldPoint(2531, 5973, 1), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2531, 5972, 1), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2531, 5971, 1), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2526, 5979, 1), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH)); // OBELISK
		addObstacle(new WorldPoint(2530, 5979, 1), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH)); // OBELISK
		addObstacle(new WorldPoint(2511, 5967, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH)); // STAIRS
		addObstacle(new WorldPoint(2528, 5977, 1), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH)); // PLATFORM
		addObstacle(new WorldPoint(2528, 5982, 1), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH)); // STAIRS

		// Floor 3 West - Upper section (plane 2)
		addObstacle(new WorldPoint(2378, 5859, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2378, 5860, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2378, 5861, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // CROSSBOW_STATUE
		// Note: Coffin at 2400, 5869, 2 is shared with East (already added above)
		addObstacle(new WorldPoint(2392, 5868, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // GRAPPLE
		addObstacle(new WorldPoint(2392, 5866, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // GRAPPLE
		addObstacle(new WorldPoint(2383, 5873, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2383, 5875, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2383, 5877, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2383, 5879, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2378, 5873, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2378, 5875, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2378, 5877, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2378, 5879, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2370, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // SWORD_STATUE
		addObstacle(new WorldPoint(2369, 5844, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2370, 5844, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2371, 5844, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2370, 5837, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // SWORD_STATUE
		addObstacle(new WorldPoint(2386, 5841, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // PORTAL_FRAME
		addObstacle(new WorldPoint(2386, 5847, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // COFFIN
		addObstacle(new WorldPoint(2395, 5828, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST)); // STAIRS
		// Note: Platforms and stairs at 2405/2395,5856,1 and 2400,5859,1 are shared with East (already added above)

		// =====================
		// FLOOR 4 - 2 routes
		// =====================

		// Floor 4 South - Upper section (plane 2)
		addObstacle(new WorldPoint(2521, 5845, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2519, 5845, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2515, 5845, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2513, 5845, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2511, 5845, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2521, 5840, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2519, 5840, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2517, 5840, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2513, 5840, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2511, 5840, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2508, 5839, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // SWORD_STATUE
		addObstacle(new WorldPoint(2511, 5831, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2511, 5829, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2511, 5827, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2511, 5825, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2511, 5823, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2506, 5831, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2506, 5829, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2506, 5827, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2506, 5825, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2506, 5823, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2555, 5824, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2555, 5825, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2555, 5826, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2553, 5844, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // BRIDGE
		addObstacle(new WorldPoint(2553, 5848, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // BRIDGE
		addObstacle(new WorldPoint(2554, 5852, 2), FLOOR_4_BOTH); // COFFIN (shared with North)

		// Floor 4 South - Lower section (plane 1)
		addObstacle(new WorldPoint(2514, 5839, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // SWORD_STATUE
		addObstacle(new WorldPoint(2504, 5828, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2505, 5828, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2506, 5828, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2532, 5842, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH)); // STAIRS
		// Note: Lower section coffin, braziers, obelisks, gate, stairs are shared - defined in Floor 4 North section

		// Floor 4 North - Upper section (plane 2)
		addObstacle(new WorldPoint(2537, 5881, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2535, 5881, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2533, 5881, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2531, 5881, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2529, 5881, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2527, 5881, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2525, 5881, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2523, 5881, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2521, 5881, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2519, 5881, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2537, 5876, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2535, 5876, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2533, 5876, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2531, 5876, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2529, 5876, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2527, 5876, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2525, 5876, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2523, 5876, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2521, 5876, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2519, 5876, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2553, 5871, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2553, 5870, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2553, 5869, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2551, 5857, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // PORTAL_FRAME
		// Note: Coffin at 2554, 5852, 2 is shared with South (already added above)
		addObstacle(new WorldPoint(2546, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2546, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2546, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2543, 5860, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // STAIRS

		// Floor 4 North - Lower section (plane 1)
		addObstacle(new WorldPoint(2553, 5866, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // SWORD_STATUE
		addObstacle(new WorldPoint(2552, 5825, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2553, 5825, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2554, 5825, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2543, 5842, 1), FLOOR_4_BOTH); // COFFIN (shared with South - same location)
		addObstacle(new WorldPoint(2538, 5841, 1), FLOOR_4_BOTH); // BRAZIER (shared with South - same location)
		addObstacle(new WorldPoint(2538, 5844, 1), FLOOR_4_BOTH); // BRAZIER (shared with South - same location)
		addObstacle(new WorldPoint(2526, 5852, 1), FLOOR_4_BOTH); // OBELISK (shared with South)
		addObstacle(new WorldPoint(2530, 5852, 1), FLOOR_4_BOTH); // OBELISK (shared with South)
		addObstacle(new WorldPoint(2528, 5845, 1), FLOOR_4_BOTH); // GATE (shared with South)
		addObstacle(new WorldPoint(2528, 5856, 1), FLOOR_4_BOTH); // STAIRS (shared with South)

		// =====================
		// FLOOR 5 - single route (stored for future use)
		// =====================

		// Floor 5 - Upper section (plane 2)
		addObstacle(new WorldPoint(2268, 5884, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2266, 5884, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2264, 5884, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2262, 5884, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2260, 5884, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2258, 5884, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2256, 5884, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2268, 5879, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2266, 5879, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2264, 5879, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2262, 5879, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2260, 5879, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2258, 5879, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2256, 5879, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2244, 5881, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // SWORD_STATUE
		addObstacle(new WorldPoint(2246, 5827, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // STAIRS

		// Floor 5 - Middle section (plane 1)
		addObstacle(new WorldPoint(2258, 5835, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // PORTAL_FRAME
		addObstacle(new WorldPoint(2264, 5835, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // COFFIN
		addObstacle(new WorldPoint(2245, 5861, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2244, 5861, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2243, 5861, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2254, 5879, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2256, 5879, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2258, 5879, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2260, 5879, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2262, 5879, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2264, 5879, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2266, 5879, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2254, 5884, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2256, 5884, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2258, 5884, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2260, 5884, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2262, 5884, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2264, 5884, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2266, 5884, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2279, 5879, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2281, 5879, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2283, 5879, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2285, 5879, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2287, 5879, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2289, 5879, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2291, 5879, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2279, 5884, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2281, 5884, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2283, 5884, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2285, 5884, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2287, 5884, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2289, 5884, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2291, 5884, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2300, 5882, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // SWORD_STATUE
		addObstacle(new WorldPoint(2288, 5836, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // GRAPPLE
		addObstacle(new WorldPoint(2288, 5834, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // GRAPPLE
		addObstacle(new WorldPoint(2281, 5835, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // COFFIN
		addObstacle(new WorldPoint(2300, 5831, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // STAIRS

		// Floor 5 - Lower section (plane 0)
		addObstacle(new WorldPoint(2296, 5870, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2297, 5870, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2298, 5870, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2294, 5879, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2292, 5879, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2290, 5879, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2288, 5879, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2286, 5879, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2294, 5874, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2292, 5874, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2290, 5874, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2288, 5874, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2286, 5874, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2275, 5879, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2270, 5879, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2270, 5874, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2275, 5874, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2259, 5879, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2257, 5879, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2255, 5879, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2253, 5879, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2251, 5879, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2251, 5874, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2253, 5874, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2255, 5874, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2257, 5874, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2259, 5874, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // WIZARD_STATUE
		addObstacle(new WorldPoint(2244, 5863, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // BRIDGE
		addObstacle(new WorldPoint(2244, 5859, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // BRIDGE
		addObstacle(new WorldPoint(2248, 5845, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // COFFIN
		addObstacle(new WorldPoint(2270, 5858, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // OBELISK
		addObstacle(new WorldPoint(2274, 5858, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // OBELISK
		addObstacle(new WorldPoint(2272, 5854, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // COFFIN
		addObstacle(new WorldPoint(2242, 5875, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2242, 5876, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // CROSSBOW_STATUE
		addObstacle(new WorldPoint(2242, 5877, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE)); // CROSSBOW_STATUE

		// Explicitly hidden obstacles (empty set = never show)
		addObstacle(new WorldPoint(2272, 5846, 0), EnumSet.noneOf(SepulchreRoute.class)); // OBELISK
	}

	private static void addObstacle(WorldPoint point, Set<SepulchreRoute> routes)
	{
		OBSTACLE_ROUTES.put(point, routes);
	}

	/**
	 * Check if an obstacle at the given location is relevant for the current route.
	 *
	 * @param location The WorldPoint of the obstacle
	 * @param currentRoute The player's current route
	 * @return true if the obstacle should be rendered, false if it should be hidden
	 */
	public static boolean isRelevantForRoute(WorldPoint location, SepulchreRoute currentRoute)
	{
		if (currentRoute == null || currentRoute == SepulchreRoute.UNKNOWN)
		{
			// If we don't know the route, show everything
			return true;
		}

		// Check all maps for route relevance
		Set<SepulchreRoute> relevantRoutes = OBSTACLE_ROUTES.get(location);
		if (relevantRoutes != null)
		{
			return relevantRoutes.contains(currentRoute);
		}

		// Check portal and lightning maps
		relevantRoutes = YELLOW_PORTAL_ROUTES.get(location);
		if (relevantRoutes != null)
		{
			return relevantRoutes.contains(currentRoute);
		}

		relevantRoutes = BLUE_PORTAL_ROUTES.get(location);
		if (relevantRoutes != null)
		{
			return relevantRoutes.contains(currentRoute);
		}

		relevantRoutes = LIGHTNING_ROUTES.get(location);
		if (relevantRoutes != null)
		{
			return relevantRoutes.contains(currentRoute);
		}

		// No mapping = relevant to all routes (default behavior)
		return true;
	}

	/**
	 * Check if route filtering has any mappings for the given floor.
	 * Useful for knowing if filtering is actually configured.
	 */
	public static boolean hasDataForFloor(int floor)
	{
		for (Map.Entry<WorldPoint, Set<SepulchreRoute>> entry : OBSTACLE_ROUTES.entrySet())
		{
			for (SepulchreRoute route : entry.getValue())
			{
				if (route.getFloor() == floor)
				{
					return true;
				}
			}
		}
		return false;
	}

	// =====================
	// TILE HELPERS
	// =====================

	private static void addYellowPortal(WorldPoint point, Set<SepulchreRoute> routes)
	{
		YELLOW_PORTAL_ROUTES.put(point, routes);
	}

	private static void addBluePortal(WorldPoint point, Set<SepulchreRoute> routes)
	{
		BLUE_PORTAL_ROUTES.put(point, routes);
	}

	private static void addLightning(WorldPoint point, Set<SepulchreRoute> routes)
	{
		LIGHTNING_ROUTES.put(point, routes);
	}

	// =====================
	// TILE DATA - Portals and Lightning
	// =====================

	static
	{
		// Floor 2 East - Portals (plane 2)
		addYellowPortal(new WorldPoint(2552, 5957, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2551, 5957, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2550, 5957, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2552, 5956, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2552, 5955, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2551, 5955, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2550, 5955, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2550, 5956, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addBluePortal(new WorldPoint(2551, 5956, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addBluePortal(new WorldPoint(2547, 5956, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addBluePortal(new WorldPoint(2543, 5956, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2548, 5957, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2547, 5957, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2546, 5957, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2546, 5956, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2546, 5955, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2547, 5955, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2548, 5955, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2548, 5956, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2544, 5957, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2543, 5957, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2542, 5957, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2542, 5956, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2542, 5955, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2543, 5955, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2544, 5955, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));
		addYellowPortal(new WorldPoint(2544, 5956, 2), EnumSet.of(SepulchreRoute.FLOOR_2_EAST));

		// Floor 2 West - Portals (plane 2)
		addYellowPortal(new WorldPoint(2492, 6008, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2492, 6007, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2492, 6006, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2493, 6006, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2494, 6006, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2494, 6007, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2494, 6008, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2493, 6008, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addBluePortal(new WorldPoint(2493, 6007, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addBluePortal(new WorldPoint(2493, 5999, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addBluePortal(new WorldPoint(2493, 6003, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2492, 6004, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2492, 6003, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2492, 6002, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2493, 6002, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2494, 6002, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2494, 6003, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2494, 6004, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2493, 6004, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2492, 6000, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2492, 5999, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2492, 5998, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2493, 5998, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2494, 5998, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2494, 5999, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2494, 6000, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));
		addYellowPortal(new WorldPoint(2493, 6000, 2), EnumSet.of(SepulchreRoute.FLOOR_2_WEST));

		// Floor 2 South - Portals (plane 2, yellows only)
		addYellowPortal(new WorldPoint(2504, 5970, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH));
		addYellowPortal(new WorldPoint(2503, 5970, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH));
		addYellowPortal(new WorldPoint(2502, 5970, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH));
		addYellowPortal(new WorldPoint(2502, 5969, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH));
		addYellowPortal(new WorldPoint(2503, 5969, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH));
		addYellowPortal(new WorldPoint(2504, 5969, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH));
		addYellowPortal(new WorldPoint(2504, 5966, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH));
		addYellowPortal(new WorldPoint(2503, 5966, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH));
		addYellowPortal(new WorldPoint(2502, 5966, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH));
		addYellowPortal(new WorldPoint(2502, 5965, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH));
		addYellowPortal(new WorldPoint(2503, 5965, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH));
		addYellowPortal(new WorldPoint(2504, 5965, 2), EnumSet.of(SepulchreRoute.FLOOR_2_SOUTH));

		// Floor 2 North - Portals (plane 1)
		addYellowPortal(new WorldPoint(2544, 6014, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2543, 6014, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2542, 6014, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2542, 6013, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2542, 6012, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2543, 6012, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2544, 6012, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2544, 6013, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addBluePortal(new WorldPoint(2543, 6013, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2539, 6014, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2539, 6013, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2539, 6012, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2538, 6012, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2537, 6012, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2537, 6013, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2537, 6014, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2538, 6014, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addBluePortal(new WorldPoint(2538, 6013, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2534, 6012, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2534, 6013, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2534, 6014, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2533, 6014, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2532, 6014, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2532, 6013, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2532, 6012, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addYellowPortal(new WorldPoint(2533, 6012, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));
		addBluePortal(new WorldPoint(2533, 6013, 1), EnumSet.of(SepulchreRoute.FLOOR_2_NORTH));

		// Floor 3 West - Portals (plane 2)
		addBluePortal(new WorldPoint(2371, 5880, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addBluePortal(new WorldPoint(2370, 5880, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addBluePortal(new WorldPoint(2369, 5880, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addBluePortal(new WorldPoint(2370, 5878, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addYellowPortal(new WorldPoint(2369, 5878, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addYellowPortal(new WorldPoint(2369, 5879, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addYellowPortal(new WorldPoint(2370, 5879, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addYellowPortal(new WorldPoint(2371, 5879, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addYellowPortal(new WorldPoint(2371, 5878, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addBluePortal(new WorldPoint(2370, 5873, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addYellowPortal(new WorldPoint(2371, 5872, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addYellowPortal(new WorldPoint(2371, 5873, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addYellowPortal(new WorldPoint(2371, 5874, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addYellowPortal(new WorldPoint(2370, 5874, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addYellowPortal(new WorldPoint(2369, 5874, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addYellowPortal(new WorldPoint(2369, 5873, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addYellowPortal(new WorldPoint(2369, 5872, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));
		addYellowPortal(new WorldPoint(2370, 5872, 2), EnumSet.of(SepulchreRoute.FLOOR_3_WEST));

		// Floor 4 South - Lightning (plane 2)
		addLightning(new WorldPoint(2503, 5846, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2501, 5846, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2502, 5845, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2501, 5844, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2503, 5844, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2503, 5839, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2501, 5839, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2502, 5838, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2501, 5837, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2503, 5837, 2), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));

		// Floor 4 South - Lightning (plane 1)
		addLightning(new WorldPoint(2515, 5843, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2514, 5844, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2513, 5843, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2513, 5845, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2515, 5845, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2515, 5851, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2513, 5851, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2514, 5852, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2513, 5853, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2515, 5853, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2515, 5859, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2513, 5859, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2514, 5860, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2515, 5861, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addLightning(new WorldPoint(2513, 5861, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));

		// Floor 4 North - Lightning (plane 2)
		addLightning(new WorldPoint(2519, 5866, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2519, 5864, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2518, 5865, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2517, 5866, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2517, 5864, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2509, 5866, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2510, 5865, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2511, 5864, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2511, 5866, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2509, 5864, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2534, 5871, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2534, 5869, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2535, 5870, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2536, 5871, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2536, 5869, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2542, 5871, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2542, 5869, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2543, 5870, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2544, 5871, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addLightning(new WorldPoint(2544, 5869, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));

		// Floor 4 North - Portals (plane 2)
		addBluePortal(new WorldPoint(2505, 5867, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2504, 5867, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2503, 5867, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2503, 5871, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2504, 5871, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2505, 5871, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2505, 5875, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2504, 5875, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2503, 5875, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2513, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2512, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2513, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2512, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2512, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2513, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2518, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2518, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2516, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2516, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2516, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2517, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2518, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2517, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2517, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2521, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2521, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2521, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2522, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2522, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2522, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2525, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2525, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2527, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2527, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2526, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2526, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2526, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2525, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2527, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2530, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2530, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2530, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2531, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2531, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2531, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2534, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2535, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2536, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2535, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2534, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2535, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2536, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2534, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2536, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2539, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2539, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2539, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2540, 5885, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2540, 5886, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2540, 5887, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2517, 5869, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2517, 5871, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2517, 5870, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2521, 5869, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2521, 5871, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2521, 5870, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2525, 5869, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2525, 5871, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2525, 5870, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2530, 5869, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2530, 5871, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2530, 5870, 2), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));

		// Floor 4 North - Portals (plane 1)
		addBluePortal(new WorldPoint(2554, 5844, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2553, 5844, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2552, 5844, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2554, 5845, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2553, 5845, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2552, 5845, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2544, 5831, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2544, 5830, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2544, 5829, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2542, 5831, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2542, 5830, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addBluePortal(new WorldPoint(2542, 5829, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2540, 5831, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2540, 5830, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));
		addYellowPortal(new WorldPoint(2540, 5829, 1), EnumSet.of(SepulchreRoute.FLOOR_4_NORTH));

		// Floor 3 East - Portals (plane 2)
		addBluePortal(new WorldPoint(2424, 5864, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addBluePortal(new WorldPoint(2425, 5863, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addBluePortal(new WorldPoint(2426, 5862, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2426, 5863, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2426, 5864, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2425, 5864, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2424, 5863, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2424, 5862, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2425, 5862, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2425, 5858, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2424, 5858, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2424, 5857, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2425, 5856, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2426, 5856, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2426, 5857, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addBluePortal(new WorldPoint(2426, 5858, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addBluePortal(new WorldPoint(2425, 5857, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addBluePortal(new WorldPoint(2424, 5856, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2426, 5852, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2425, 5852, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2426, 5851, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2425, 5850, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2424, 5850, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2424, 5851, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addBluePortal(new WorldPoint(2424, 5852, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addBluePortal(new WorldPoint(2425, 5851, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addBluePortal(new WorldPoint(2426, 5850, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addBluePortal(new WorldPoint(2426, 5846, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addBluePortal(new WorldPoint(2425, 5845, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addBluePortal(new WorldPoint(2424, 5844, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2425, 5844, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2426, 5844, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2426, 5845, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2424, 5845, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2424, 5846, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));
		addYellowPortal(new WorldPoint(2425, 5846, 2), EnumSet.of(SepulchreRoute.FLOOR_3_EAST));

		// Floor 4 South - Portals (plane 1)
		addYellowPortal(new WorldPoint(2515, 5849, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2515, 5848, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2515, 5847, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2514, 5847, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2513, 5847, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2513, 5848, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2513, 5849, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2514, 5849, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2514, 5848, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2515, 5857, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2515, 5856, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2515, 5855, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2514, 5855, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2513, 5855, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2513, 5856, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2513, 5857, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2514, 5857, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2514, 5856, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2506, 5852, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2505, 5852, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2504, 5852, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2504, 5850, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2506, 5850, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2505, 5850, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2505, 5851, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2506, 5851, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2504, 5851, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2504, 5846, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2505, 5846, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2506, 5846, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2506, 5844, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2505, 5844, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2504, 5844, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2504, 5845, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2505, 5845, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2506, 5845, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2505, 5840, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2505, 5839, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2505, 5838, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2504, 5838, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2504, 5839, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2504, 5840, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2506, 5838, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2506, 5839, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2506, 5840, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2504, 5834, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2505, 5834, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2506, 5834, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2506, 5832, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2506, 5833, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2505, 5833, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2504, 5833, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2504, 5832, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2505, 5832, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2512, 5829, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2512, 5830, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2512, 5831, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2514, 5829, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2514, 5830, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addBluePortal(new WorldPoint(2514, 5831, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2516, 5829, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2516, 5830, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));
		addYellowPortal(new WorldPoint(2516, 5831, 1), EnumSet.of(SepulchreRoute.FLOOR_4_SOUTH));

		// Floor 5 - Portals (plane 2)
		addBluePortal(new WorldPoint(2245, 5872, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2244, 5872, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2243, 5872, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2243, 5864, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2244, 5864, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2245, 5864, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2243, 5856, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2244, 5856, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2245, 5856, 2), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));

		// Floor 5 - Portals (plane 1)
		addBluePortal(new WorldPoint(2245, 5836, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2244, 5836, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2243, 5836, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2243, 5837, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2244, 5837, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2245, 5837, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2245, 5842, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2244, 5842, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2243, 5842, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2243, 5843, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2244, 5843, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2245, 5843, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2245, 5848, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2244, 5848, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2243, 5848, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2243, 5849, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2244, 5849, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2245, 5849, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2244, 5852, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2244, 5854, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2245, 5839, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2244, 5839, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2243, 5839, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2243, 5840, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2244, 5840, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2245, 5840, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2245, 5845, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2244, 5845, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2243, 5845, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2243, 5846, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2244, 5846, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2245, 5846, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2243, 5852, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2243, 5851, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2244, 5851, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2245, 5851, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2245, 5852, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2243, 5854, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2243, 5855, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2244, 5855, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2245, 5855, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2245, 5854, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2299, 5873, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2300, 5873, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2301, 5873, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2301, 5869, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2300, 5869, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2299, 5869, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2299, 5861, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2300, 5861, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2301, 5861, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2299, 5865, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2300, 5865, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2301, 5865, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2299, 5857, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2300, 5857, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2301, 5857, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));

		// Floor 5 - Lightning (plane 1)
		addLightning(new WorldPoint(2268, 5882, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2269, 5881, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2268, 5880, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2270, 5880, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2272, 5880, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2274, 5880, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2276, 5880, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2275, 5881, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2273, 5881, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2271, 5881, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2270, 5882, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2272, 5882, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2274, 5882, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2276, 5882, 1), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));

		// Floor 5 - Lightning (plane 0)
		addLightning(new WorldPoint(2298, 5836, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2298, 5838, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2298, 5840, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2297, 5837, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2297, 5839, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2296, 5840, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2296, 5838, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2296, 5836, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2298, 5859, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2298, 5861, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2298, 5863, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2297, 5862, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2297, 5860, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2296, 5859, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2296, 5861, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2296, 5863, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2283, 5875, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2281, 5875, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2279, 5875, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2280, 5876, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2282, 5876, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2283, 5877, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2281, 5877, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2279, 5877, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2261, 5875, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2261, 5877, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2262, 5876, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2263, 5875, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2264, 5876, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2263, 5877, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2265, 5877, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addLightning(new WorldPoint(2265, 5875, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));

		// Floor 5 - Portals (plane 0)
		addYellowPortal(new WorldPoint(2298, 5842, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2297, 5842, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2296, 5842, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2298, 5843, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2297, 5843, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2296, 5843, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2298, 5845, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2297, 5845, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2296, 5845, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2298, 5846, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2297, 5846, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2296, 5846, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2298, 5848, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2297, 5848, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2296, 5848, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2298, 5851, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2297, 5851, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2296, 5851, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2298, 5856, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2297, 5856, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2296, 5856, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2296, 5857, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2297, 5857, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addBluePortal(new WorldPoint(2298, 5857, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2298, 5853, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2297, 5853, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2296, 5853, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2296, 5854, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2297, 5854, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
		addYellowPortal(new WorldPoint(2298, 5854, 0), EnumSet.of(SepulchreRoute.FLOOR_5_SINGLE));
	}
}
