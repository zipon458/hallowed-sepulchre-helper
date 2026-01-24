package com.sepulchre.util;

import java.util.Set;

public final class SepulchreConstants
{
	private SepulchreConstants() {}

	public static final Set<Integer> CROSSBOW_STATUE_IDS = Set.of(38444, 38445, 38446);

	public static final int CROSSBOW_ANIM_CHARGING = 8682;
	public static final int CROSSBOW_ANIM_FIRING_1 = 8683;
	public static final int CROSSBOW_ANIM_FIRING_2 = 8684;
	public static final int CROSSBOW_ANIM_FIRING_3 = 8685;
	public static final Set<Integer> CROSSBOW_DANGER_ANIMS = Set.of(
		CROSSBOW_ANIM_CHARGING,
		CROSSBOW_ANIM_FIRING_1,
		CROSSBOW_ANIM_FIRING_2,
		CROSSBOW_ANIM_FIRING_3
	);

	public static final int PORTAL_YELLOW_ID = 38447;
	public static final int PORTAL_BLUE_ID = 38448;

	public static final Set<Integer> PORTAL_GRAPHICS_IDS = Set.of(1799, 1800, 1815, 1816);

	public static final Set<Integer> WIZARD_FLAME_OBJECT_IDS = Set.of(
		38409, 38410, 38411, 38412, 38413, 38414, 38415,
		38416, 38417, 38418, 38419, 38420,
		38421, 38422, 38423, 38424, 38425
	);

	public static final int WIZARD_ANIM_FIRE = 8658;
	public static final int WIZARD_ANIM_WARNING = 8657;
	public static final int WIZARD_ANIM_PRE_WARNING = 8656;

	public static final Set<Integer> BOLT_NULL_NPC_IDS = Set.of(9672, 9673, 9674);
	public static final Set<Integer> SWORD_NULL_NPC_IDS = Set.of(9669, 9670, 9671);

	public static final int LIGHTNING_GRAPHICS_ID = 1796;
}
