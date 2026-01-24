package com.sepulchre.util;

import java.util.Set;

/**
 * Constants for the Hallowed Sepulchre minigame.
 *
 * Note: Many IDs here are placeholders and need to be verified in-game.
 * Use RuneLite's developer tools to capture the correct IDs.
 */
public final class SepulchreConstants
{
	private SepulchreConstants() {}

	// ==================== REGION IDS ====================

	// Hallowed Sepulchre region IDs
	public static final Set<Integer> SEPULCHRE_REGIONS = Set.of(
		8797,  // Lobby
		9051, 9052, 9053, 9054,   // Floor 1
		9307, 9308, 9309, 9310,   // Floor 2
		9563, 9564, 9565, 9566,   // Floor 3
		9819, 9820, 9821, 9822,   // Floor 4
		10075, 10076, 10077, 10078, // Floor 5
		42100  // Additional region
	);

	public static final Set<Integer> FLOOR_1_REGIONS = Set.of(9051, 9052, 9053, 9054);
	public static final Set<Integer> FLOOR_2_REGIONS = Set.of(9307, 9308, 9309, 9310);
	public static final Set<Integer> FLOOR_3_REGIONS = Set.of(9563, 9564, 9565, 9566);
	public static final Set<Integer> FLOOR_4_REGIONS = Set.of(9819, 9820, 9821, 9822);
	public static final Set<Integer> FLOOR_5_REGIONS = Set.of(10075, 10076, 10077, 10078, 42100);

	// ==================== GAME OBJECT IDS ====================

	// Arrow trap IDs - empty for now, add verified IDs later
	public static final Set<Integer> ARROW_TRAP_IDS = Set.of(
		// Add verified arrow trap IDs here
	);

	// Sword statue IDs - placeholder, needs verification
	public static final Set<Integer> SWORD_STATUE_IDS = Set.of(
		38416, 38417, 38418, 38419
	);

	// Crossbow statue IDs
	public static final Set<Integer> CROSSBOW_STATUE_IDS = Set.of(38444, 38445);

	// Crossbow statue animation IDs
	public static final int CROSSBOW_ANIM_CHARGING = 8682;  // About to shoot
	public static final int CROSSBOW_ANIM_FIRING_1 = 8683;  // Firing
	public static final int CROSSBOW_ANIM_FIRING_2 = 8684;  // Firing
	public static final int CROSSBOW_ANIM_FIRING_3 = 8685;  // Firing
	public static final Set<Integer> CROSSBOW_DANGER_ANIMS = Set.of(
		CROSSBOW_ANIM_CHARGING,
		CROSSBOW_ANIM_FIRING_1,
		CROSSBOW_ANIM_FIRING_2,
		CROSSBOW_ANIM_FIRING_3
	);

	// Coffin IDs - placeholder, needs verification
	public static final Set<Integer> COFFIN_IDS = Set.of(
		39549, 39550, 39551, 39552
	);

	// Grand coffin IDs - placeholder, needs verification
	public static final Set<Integer> GRAND_COFFIN_IDS = Set.of(
		39553, 39554, 39555, 39556, 39557
	);

	// Portal tile IDs (the glowing floor tiles)
	public static final Set<Integer> PORTAL_TILE_IDS = Set.of(
		38447,  // Yellow portal tile
		38448   // Blue portal tile
	);

	// Portal graphics IDs (the actual portal effects)
	public static final Set<Integer> PORTAL_GRAPHICS_IDS = Set.of(
		1799,   // Portal effect
		1800,   // Portal effect
		1816    // Portal effect
	);

	// ==================== NPC IDS ====================

	// Wizard statue NPC IDs - placeholder, needs verification
	public static final Set<Integer> WIZARD_NPC_IDS = Set.of(
		9757, 9758, 9759, 9760
	);

	// ==================== PROJECTILE IDS ====================

	// Arrow projectile IDs - placeholder, needs verification
	public static final Set<Integer> ARROW_PROJECTILE_IDS = Set.of(
		1837, 1838
	);

	// Crossbow bolt projectile IDs - placeholder, needs verification
	public static final Set<Integer> CROSSBOW_PROJECTILE_IDS = Set.of(
		1839, 1840
	);

	// ==================== GRAPHICS IDS ====================

	// Wizard flame game object IDs - these spawn as game objects, not graphics
	public static final Set<Integer> WIZARD_FLAME_OBJECT_IDS = Set.of(
		38409, 38410, 38411, 38412
	);

	// Wizard statue animation IDs
	public static final int WIZARD_ANIM_FIRE = 8658;      // Flames are active (danger)
	public static final int WIZARD_ANIM_WARNING = 8657;   // About to fire (warning)
	// Other animations = safe

	// Wizard flame graphics IDs - placeholder, needs verification
	// Enable "Show Graphics IDs" in debug settings to find the correct IDs
	public static final Set<Integer> WIZARD_FLAME_GRAPHICS_IDS = Set.of(
		1585  // Placeholder - verify in game
	);

	// Null NPC IDs for arrows (from crossbowmen)
	public static final Set<Integer> ARROW_NULL_NPC_IDS = Set.of(
		9672, 9673, 9674
	);

	// Null NPC IDs for swinging swords
	public static final Set<Integer> SWORD_NULL_NPC_IDS = Set.of(
		9669, 9670, 9671
	);

	// Lightning strike graphics IDs - placeholder, needs verification
	public static final Set<Integer> LIGHTNING_GRAPHICS_IDS = Set.of(
		1882, 1883
	);

	// ==================== TIMING CONSTANTS ====================

	// Tick cycles for various obstacles (may need adjustment based on actual game data)
	public static final int ARROW_CYCLE_TICKS = 4;
	public static final int SWORD_CYCLE_TICKS = 5;
	public static final int CROSSBOW_CYCLE_TICKS = 4;
	public static final int WIZARD_CYCLE_TICKS = 8;

	// Floor time limits (in game ticks, 1 tick = 0.6 seconds)
	public static final int FLOOR_1_TIME_TICKS = 200;   // ~2 minutes
	public static final int FLOOR_2_TIME_TICKS = 250;   // ~2.5 minutes
	public static final int FLOOR_3_TIME_TICKS = 300;   // ~3 minutes
	public static final int FLOOR_4_TIME_TICKS = 350;   // ~3.5 minutes
	public static final int FLOOR_5_TIME_TICKS = 416;   // ~4.16 minutes

	// ==================== AGILITY LEVEL REQUIREMENTS ====================

	public static final int FLOOR_1_LEVEL = 52;
	public static final int FLOOR_2_LEVEL = 62;
	public static final int FLOOR_3_LEVEL = 72;
	public static final int FLOOR_4_LEVEL = 82;
	public static final int FLOOR_5_LEVEL = 92;
}
