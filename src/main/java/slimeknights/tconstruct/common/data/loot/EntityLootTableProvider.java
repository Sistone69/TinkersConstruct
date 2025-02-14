package slimeknights.tconstruct.common.data.loot;

import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.Objects;
import java.util.stream.Collectors;

public class EntityLootTableProvider extends EntityLootTables {

  @Override
  protected Iterable<EntityType<?>> getKnownEntities() {
    return ForgeRegistries.ENTITIES.getValues().stream()
                                   .filter((block) -> TConstruct.MOD_ID.equals(Objects.requireNonNull(block.getRegistryName()).getNamespace()))
                                   .collect(Collectors.toList());
  }

  @Override
  protected void addTables() {
    this.registerLootTable(TinkerWorld.earthSlimeEntity.get(), dropSlimeballs(SlimeType.EARTH));
    this.registerLootTable(TinkerWorld.skySlimeEntity.get(), dropSlimeballs(SlimeType.SKY));
    this.registerLootTable(TinkerWorld.enderSlimeEntity.get(), dropSlimeballs(SlimeType.ENDER));
    this.registerLootTable(TinkerWorld.terracubeEntity.get(),
                           LootTable.builder().addLootPool(LootPool.builder()
                                                                   .rolls(ConstantRange.of(1))
                                                                   .addEntry(ItemLootEntry.builder(Items.CLAY_BALL)
                                                                                          .acceptFunction(SetCount.builder(RandomValueRange.of(-2.0F, 1.0F)))
                                                                                          .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));
  }

  private static LootTable.Builder dropSlimeballs(SlimeType type) {
    return LootTable.builder()
                    .addLootPool(LootPool.builder()
                                         .rolls(ConstantRange.of(1))
                                         .addEntry(ItemLootEntry.builder(TinkerCommons.slimeball.get(type))
                                                                .acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
                                                                .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))));
  }
}
