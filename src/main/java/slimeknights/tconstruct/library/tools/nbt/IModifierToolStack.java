package slimeknights.tconstruct.library.tools.nbt;


import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;

import java.util.List;

/**
 * Provides mostly read only access to {@link ToolStack}.
 * Used since modifiers should not be modifying the tool materials or modifiers in their behaviors.
 * If you receive a modifier tool stack as a parameter, do NOT use an instanceof check and cast it to a ToolStack. Don't make me use a private wrapper class.
 */
public interface IModifierToolStack {
  /** Gets the item contained in this tool */
  Item getItem();

  /** Gets the tool definition */
  ToolDefinition getDefinition();

  /** Commonly used operation, getting a stat multiplier */
  default float getModifier(FloatToolStat stat) {
    return getDefinition().getBaseStatDefinition().getModifier(stat);
  }

  /** Checks if the tool has the given tag */
  default boolean hasTag(ITag<Item> tag) {
    return tag.contains(getItem());
  }


  /* Damage state */

  /** Gets the current damage of the tool */
  int getDamage();

  /** Gets the current durability remaining for this tool */
  int getCurrentDurability();

  /** Checks whether the tool is broken */
  boolean isBroken();

  /** If true, tool is marked unbreakable by vanilla */
  boolean isUnbreakable();

  /**
   * Sets the tools current damage.
   * Note in general you should use {@link ToolDamageUtil#damage(IModifierToolStack, int, LivingEntity, ItemStack)} or {@link ToolDamageUtil#repair(IModifierToolStack, int)} as they handle modifiers
   * @param damage  New damage
   */
  void setDamage(int damage);

  /* Materials */

  /** Gets the list of current materials making this tool */
  MaterialNBT getMaterials();

  /**
   * Gets the list of all materials
   * @return List of all materials
   */
  default List<IMaterial> getMaterialsList() {
    return getMaterials().getMaterials();
  }

  /**
   * Gets the material at the given index
   * @param index  Index
   * @return  Material, or unknown if index is invalid
   */
  default IMaterial getMaterial(int index) {
    return getMaterials().getMaterial(index);
  }


  /* Modifiers */

  /** Gets a list of modifiers that are specifically added to this tool. Unlike {@link #getModifiers()}, does not include modifiers from the tool or materials */
  ModifierNBT getUpgrades();

  /** Gets a full list of effective modifiers on this tool, from both upgrades/abilities and material traits */
  ModifierNBT getModifiers();

  /**
   * Helper to get a list of all modifiers on the tool. Note this list is already sorted by priority
   * @return  List of all modifiers
   */
  default List<ModifierEntry> getModifierList() {
    return getModifiers().getModifiers();
  }

  /**
   * Gets the level of a modifier on this tool. Will consider both raw modifiers and material traits
   * @param modifier  Modifier
   * @return  Level of modifier, 0 if the modifier is not on the tool
   */
  default int getModifierLevel(Modifier modifier) {
    return getModifiers().getLevel(modifier);
  }


  /* Tool data */

  /** Cached tool stats calculated from materials and modifiers */
  StatsNBT getStats();

  /**
   * Gets persistent modifier data from the tool.
   * This data may be edited by modifiers and will persist when stats rebuild
   */
  ModDataNBT getPersistentData();

  /**
   * Gets volatile modifier data from the tool.
   * This data will be reset whenever modifiers reload and should not be edited.
   */
  IModDataReadOnly getVolatileData();


  /* Helpers */

  /**
   * Gets the free upgrade slots remaining on the tool
   * @return  Free upgrade slots
   */
  default int getFreeSlots(SlotType type) {
    return getPersistentData().getSlots(type) + getVolatileData().getSlots(type);
  }

  /** @deprecated Use {@link #getFreeSlots(SlotType)} */
  @Deprecated
  default int getFreeUpgrades() {
    return getPersistentData().getSlots(SlotType.UPGRADE) + getVolatileData().getSlots(SlotType.UPGRADE);
  }

  /** @deprecated Use {@link #getFreeSlots(SlotType)} */
  @Deprecated
  default int getFreeAbilities() {
    return getPersistentData().getSlots(SlotType.ABILITY) + getVolatileData().getSlots(SlotType.ABILITY);
  }
}
