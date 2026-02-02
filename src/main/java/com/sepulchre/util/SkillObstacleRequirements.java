package com.sepulchre.util;

import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class SkillObstacleRequirements
{
	private static final int HALLOWED_SYMBOL = ItemID.HALLOWED_SYMBOL;
	private static final int HALLOWED_HAMMER = ItemID.HALLOWED_HAMMER;
	private static final int HALLOWED_GRAPPLE = ItemID.HALLOWED_GRAPPLE;

	private static final int VAMPYRE_DUST = ItemID.VAMPYRE_DUST;

	private static final int HAMMER = ItemID.HAMMER;
	private static final int IMCANDO_HAMMER = ItemID.IMCANDO_HAMMER;

	private static final int SAW = ItemID.SAW;
	private static final int CRYSTAL_SAW = ItemID.CRYSTAL_SAW;

	private static final int PLANK = ItemID.PLANK;
	private static final int STEEL_NAILS = ItemID.STEEL_NAILS;
	private static final int OAK_PLANK = ItemID.OAK_PLANK;
	private static final int MITHRIL_NAILS = ItemID.MITHRIL_NAILS;
	private static final int TEAK_PLANK = ItemID.TEAK_PLANK;
	private static final int ADAMANTITE_NAILS = ItemID.ADAMANTITE_NAILS;
	private static final int MAHOGANY_PLANK = ItemID.MAHOGANY_PLANK;
	private static final int RUNE_NAILS = ItemID.RUNE_NAILS;

	private static final int MITH_GRAPPLE = ItemID.MITH_GRAPPLE_9419;

	private static final int COSMIC_RUNE = ItemID.COSMIC_RUNE;
	private static final int WATER_RUNE = ItemID.WATER_RUNE;
	private static final int AIR_RUNE = ItemID.AIR_RUNE;
	private static final int FIRE_RUNE = ItemID.FIRE_RUNE;
	private static final int EARTH_RUNE = ItemID.EARTH_RUNE;
	private static final int BLOOD_RUNE = ItemID.BLOOD_RUNE;
	private static final int SOUL_RUNE = ItemID.SOUL_RUNE;

	private static final int RUNE_POUCH = ItemID.RUNE_POUCH;
	private static final int RUNE_POUCH_L = ItemID.RUNE_POUCH_L;
	private static final int DIVINE_RUNE_POUCH = ItemID.DIVINE_RUNE_POUCH;
	private static final int DIVINE_RUNE_POUCH_L = ItemID.DIVINE_RUNE_POUCH_L;

	private static final int BRIDGE_CONSTRUCTION_LEVEL = 56;
	private static final int BRIDGE_CONSTRUCTION_LEVEL_WITH_CRYSTAL_SAW = 53;

	private static final int SPELLBOOK_VARBIT = 4070;
	private static final int SPELLBOOK_STANDARD = 0;

	private static final Set<Integer> CROSSBOW_IDS = Set.of(
		ItemID.BRONZE_CROSSBOW, ItemID.IRON_CROSSBOW, ItemID.STEEL_CROSSBOW,
		ItemID.MITHRIL_CROSSBOW, ItemID.ADAMANT_CROSSBOW, ItemID.RUNE_CROSSBOW,
		ItemID.DRAGON_CROSSBOW, ItemID.ARMADYL_CROSSBOW, ItemID.ZARYTE_CROSSBOW,
		ItemID.DRAGON_HUNTER_CROSSBOW, ItemID.BLURITE_CROSSBOW,
		ItemID.HUNTERS_CROSSBOW, ItemID.KARILS_CROSSBOW, ItemID.DORGESHUUN_CROSSBOW
	);

	private static final Set<Integer> WATER_COMBO_RUNES = Set.of(ItemID.MUD_RUNE, ItemID.STEAM_RUNE, ItemID.MIST_RUNE);
	private static final Set<Integer> AIR_COMBO_RUNES = Set.of(ItemID.SMOKE_RUNE, ItemID.MIST_RUNE, ItemID.DUST_RUNE);
	private static final Set<Integer> FIRE_COMBO_RUNES = Set.of(ItemID.SMOKE_RUNE, ItemID.STEAM_RUNE, ItemID.LAVA_RUNE);
	private static final Set<Integer> EARTH_COMBO_RUNES = Set.of(ItemID.MUD_RUNE, ItemID.DUST_RUNE, ItemID.LAVA_RUNE);

	private final Client client;

	@Inject
	public SkillObstacleRequirements(Client client)
	{
		this.client = client;
	}

	public boolean canLightBrazier()
	{
		int requiredDust = hasEquippedItem(HALLOWED_SYMBOL) ? 1 : 2;
		return getItemCount(VAMPYRE_DUST) >= requiredDust;
	}

	public boolean canBuildBridge()
	{
		int constructionLevel = client.getRealSkillLevel(Skill.CONSTRUCTION);
		boolean hasCrystalSaw = hasItem(CRYSTAL_SAW);
		int requiredLevel = hasCrystalSaw ? BRIDGE_CONSTRUCTION_LEVEL_WITH_CRYSTAL_SAW : BRIDGE_CONSTRUCTION_LEVEL;

		if (constructionLevel < requiredLevel)
		{
			return false;
		}

		if (!hasItem(HAMMER) && !hasItem(IMCANDO_HAMMER) && !hasItem(HALLOWED_HAMMER))
		{
			return false;
		}

		if (!hasItem(SAW) && !hasCrystalSaw)
		{
			return false;
		}

		if (getItemCount(PLANK) >= 2 && getItemCount(STEEL_NAILS) >= 5)
		{
			return true;
		}

		if (getItemCount(OAK_PLANK) >= 2 && getItemCount(MITHRIL_NAILS) >= 5)
		{
			return true;
		}

		if (getItemCount(TEAK_PLANK) >= 2 && getItemCount(ADAMANTITE_NAILS) >= 5)
		{
			return true;
		}

		if (getItemCount(MAHOGANY_PLANK) >= 2 && getItemCount(RUNE_NAILS) >= 5)
		{
			return true;
		}

		return false;
	}

	public boolean canUseGrapple()
	{
		boolean hasGrapple = hasEquippedItem(MITH_GRAPPLE) || hasEquippedItem(HALLOWED_GRAPPLE);
		if (!hasGrapple)
		{
			return false;
		}

		return hasCrossbowEquipped();
	}

	private boolean hasCrossbowEquipped()
	{
		ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
		if (equipment == null)
		{
			return false;
		}

		Item weapon = equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
		if (weapon == null)
		{
			return false;
		}

		return CROSSBOW_IDS.contains(weapon.getId());
	}

	public boolean canConjurePortal()
	{
		if (client.getVarbitValue(SPELLBOOK_VARBIT) != SPELLBOOK_STANDARD)
		{
			return false;
		}

		int magicLevel = client.getRealSkillLevel(Skill.MAGIC);

		if (magicLevel >= 7 && hasRunes(COSMIC_RUNE, 1, WATER_RUNE, 1))
		{
			return true;
		}

		if (magicLevel >= 27 && hasRunes(COSMIC_RUNE, 1, AIR_RUNE, 3))
		{
			return true;
		}

		if (magicLevel >= 49 && hasRunes(COSMIC_RUNE, 1, FIRE_RUNE, 5))
		{
			return true;
		}

		if (magicLevel >= 57 && hasRunes(COSMIC_RUNE, 1, EARTH_RUNE, 10))
		{
			return true;
		}

		if (magicLevel >= 68 && hasRunes(COSMIC_RUNE, 1, WATER_RUNE, 15, EARTH_RUNE, 15))
		{
			return true;
		}

		if (magicLevel >= 87 && hasRunes(COSMIC_RUNE, 1, EARTH_RUNE, 20, FIRE_RUNE, 20))
		{
			return true;
		}

		if (magicLevel >= 93 && hasRunes(COSMIC_RUNE, 1, BLOOD_RUNE, 20, SOUL_RUNE, 20))
		{
			return true;
		}

		return false;
	}

	private boolean hasEquippedItem(int itemId)
	{
		ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
		if (equipment == null)
		{
			return false;
		}

		for (Item item : equipment.getItems())
		{
			if (item != null && item.getId() == itemId)
			{
				return true;
			}
		}
		return false;
	}

	private boolean hasItem(int itemId)
	{
		return getItemCount(itemId) > 0 || hasEquippedItem(itemId);
	}

	private int getItemCount(int itemId)
	{
		ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
		if (inventory == null)
		{
			return 0;
		}

		int count = 0;
		for (Item item : inventory.getItems())
		{
			if (item != null && item.getId() == itemId)
			{
				count += item.getQuantity();
			}
		}
		return count;
	}

	private boolean hasRunes(int rune1, int count1, int rune2, int count2)
	{
		return getRuneCount(rune1) >= count1 && getRuneCount(rune2) >= count2;
	}

	private boolean hasRunes(int rune1, int count1, int rune2, int count2, int rune3, int count3)
	{
		return getRuneCount(rune1) >= count1 && getRuneCount(rune2) >= count2 && getRuneCount(rune3) >= count3;
	}

	private int getRuneCount(int runeId)
	{
		if (hasStaffProvidingRune(runeId))
		{
			return Integer.MAX_VALUE;
		}

		int count = getItemCount(runeId);

		ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
		if (equipment != null)
		{
			for (Item item : equipment.getItems())
			{
				if (item != null && item.getId() == runeId)
				{
					count += item.getQuantity();
				}
			}
		}

		if (hasRunePouch())
		{
			count += getRunePouchCount(runeId);
		}

		count += getCombinationRuneCount(runeId);

		return count;
	}

	private boolean hasStaffProvidingRune(int runeId)
	{
		ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
		if (equipment == null)
		{
			return false;
		}

		Item weapon = equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
		if (weapon == null)
		{
			return false;
		}

		int weaponId = weapon.getId();

		switch (runeId)
		{
			case ItemID.WATER_RUNE:
				return isWaterStaff(weaponId);
			case ItemID.AIR_RUNE:
				return isAirStaff(weaponId);
			case ItemID.FIRE_RUNE:
				return isFireStaff(weaponId);
			case ItemID.EARTH_RUNE:
				return isEarthStaff(weaponId);
			default:
				return false;
		}
	}

	private boolean isWaterStaff(int itemId)
	{
		return itemId == ItemID.STAFF_OF_WATER || itemId == ItemID.WATER_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_WATER_STAFF || itemId == ItemID.MUD_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_MUD_STAFF || itemId == ItemID.STEAM_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_STEAM_STAFF || itemId == ItemID.MIST_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_MIST_STAFF || itemId == ItemID.KODAI_WAND
			|| itemId == ItemID.TOME_OF_WATER || itemId == ItemID.TOME_OF_WATER_EMPTY;
	}

	private boolean isAirStaff(int itemId)
	{
		return itemId == ItemID.STAFF_OF_AIR || itemId == ItemID.AIR_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_AIR_STAFF || itemId == ItemID.SMOKE_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_SMOKE_STAFF || itemId == ItemID.MIST_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_MIST_STAFF || itemId == ItemID.DUST_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_DUST_STAFF;
	}

	private boolean isFireStaff(int itemId)
	{
		return itemId == ItemID.STAFF_OF_FIRE || itemId == ItemID.FIRE_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_FIRE_STAFF || itemId == ItemID.SMOKE_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_SMOKE_STAFF || itemId == ItemID.STEAM_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_STEAM_STAFF || itemId == ItemID.LAVA_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_LAVA_STAFF || itemId == ItemID.TOME_OF_FIRE
			|| itemId == ItemID.TOME_OF_FIRE_EMPTY;
	}

	private boolean isEarthStaff(int itemId)
	{
		return itemId == ItemID.STAFF_OF_EARTH || itemId == ItemID.EARTH_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_EARTH_STAFF || itemId == ItemID.MUD_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_MUD_STAFF || itemId == ItemID.DUST_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_DUST_STAFF || itemId == ItemID.LAVA_BATTLESTAFF
			|| itemId == ItemID.MYSTIC_LAVA_STAFF;
	}

	private Set<Integer> getComboRunesForElement(int runeId)
	{
		switch (runeId)
		{
			case ItemID.WATER_RUNE:
				return WATER_COMBO_RUNES;
			case ItemID.AIR_RUNE:
				return AIR_COMBO_RUNES;
			case ItemID.FIRE_RUNE:
				return FIRE_COMBO_RUNES;
			case ItemID.EARTH_RUNE:
				return EARTH_COMBO_RUNES;
			default:
				return Set.of();
		}
	}

	private int getCombinationRuneCount(int runeId)
	{
		int count = 0;

		for (int comboRuneId : getComboRunesForElement(runeId))
		{
			count += getItemCount(comboRuneId);
		}

		if (runeId == ItemID.COSMIC_RUNE || runeId == ItemID.SOUL_RUNE)
		{
			count += getItemCount(ItemID.AETHER_RUNE);
		}

		return count;
	}

	private boolean hasRunePouch()
	{
		return hasItem(RUNE_POUCH) || hasItem(RUNE_POUCH_L)
			|| hasItem(DIVINE_RUNE_POUCH) || hasItem(DIVINE_RUNE_POUCH_L);
	}

	private int getRunePouchCount(int runeId)
	{
		int count = 0;
		Set<Integer> comboRunes = getComboRunesForElement(runeId);

		count += getRunePouchSlotCount(29, 1624, runeId, comboRunes);
		count += getRunePouchSlotCount(1622, 1625, runeId, comboRunes);
		count += getRunePouchSlotCount(1623, 1626, runeId, comboRunes);

		if (hasItem(DIVINE_RUNE_POUCH) || hasItem(DIVINE_RUNE_POUCH_L))
		{
			count += getRunePouchSlotCount(14285, 14286, runeId, comboRunes);
		}

		return count;
	}

	private int getRunePouchSlotCount(int typeVarbit, int amountVarbit, int targetRuneId, Set<Integer> comboRunes)
	{
		int slotType = client.getVarbitValue(typeVarbit);
		int slotRuneId = runeIdFromVarbit(slotType);

		if (slotRuneId == targetRuneId)
		{
			return client.getVarbitValue(amountVarbit);
		}

		if (comboRunes.contains(slotRuneId))
		{
			return client.getVarbitValue(amountVarbit);
		}

		if ((targetRuneId == ItemID.COSMIC_RUNE || targetRuneId == ItemID.SOUL_RUNE) && slotRuneId == ItemID.AETHER_RUNE)
		{
			return client.getVarbitValue(amountVarbit);
		}

		return 0;
	}

	private int runeIdFromVarbit(int varbitValue)
	{
		switch (varbitValue)
		{
			case 1: return ItemID.AIR_RUNE;
			case 2: return ItemID.WATER_RUNE;
			case 3: return ItemID.EARTH_RUNE;
			case 4: return ItemID.FIRE_RUNE;
			case 5: return ItemID.MIND_RUNE;
			case 6: return ItemID.CHAOS_RUNE;
			case 7: return ItemID.DEATH_RUNE;
			case 8: return ItemID.BLOOD_RUNE;
			case 9: return ItemID.COSMIC_RUNE;
			case 10: return ItemID.NATURE_RUNE;
			case 11: return ItemID.LAW_RUNE;
			case 12: return ItemID.BODY_RUNE;
			case 13: return ItemID.SOUL_RUNE;
			case 14: return ItemID.ASTRAL_RUNE;
			case 15: return ItemID.MIST_RUNE;
			case 16: return ItemID.MUD_RUNE;
			case 17: return ItemID.DUST_RUNE;
			case 18: return ItemID.LAVA_RUNE;
			case 19: return ItemID.STEAM_RUNE;
			case 20: return ItemID.SMOKE_RUNE;
			case 21: return ItemID.WRATH_RUNE;
			case 22: return ItemID.SUNFIRE_RUNE;
			case 23: return ItemID.AETHER_RUNE;
			default: return -1;
		}
	}
}
