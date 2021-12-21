package sirttas.elementalcraft.datagen.loot;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.loot.LootHandler;
import sirttas.elementalcraft.loot.function.ECLootFunctions;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;

/**
 * greatly inspired by Botania
 *
 * 
 */
public class ECInjectLootProvider extends AbstractECLootProvider {

	public ECInjectLootProvider(DataGenerator generator) {
		super(generator);
		ECLootFunctions.setup();
	}

	@Override
	public void run(@Nonnull HashCache cache) throws IOException {
		save(cache, genShard(ElementType.EARTH), EntityType.ZOMBIE);
		save(cache, genShard(ElementType.EARTH), EntityType.ZOMBIE_VILLAGER);
		save(cache, genShard(ElementType.EARTH), EntityType.SKELETON);
		save(cache, genShard(ElementType.EARTH), EntityType.WITHER_SKELETON);
		save(cache, genShard(ElementType.EARTH), EntityType.SILVERFISH);
		save(cache, genShard(ElementType.EARTH), EntityType.IRON_GOLEM);
		save(cache, genShard(ElementType.EARTH), EntityType.SKELETON_HORSE);
		save(cache, genShard(ElementType.EARTH), EntityType.GOAT);
		save(cache, genShard(ElementType.FIRE), EntityType.CREEPER);
		save(cache, genShard(ElementType.FIRE), EntityType.GHAST);
		save(cache, genShard(ElementType.FIRE), EntityType.BLAZE);
		save(cache, genShard(ElementType.FIRE), EntityType.HUSK);
		save(cache, genShard(ElementType.FIRE), EntityType.MAGMA_CUBE);
		save(cache, genShard(ElementType.FIRE), EntityType.ZOMBIFIED_PIGLIN);
		save(cache, genShard(ElementType.FIRE), EntityType.ZOGLIN);
		save(cache, genShard(ElementType.WATER), EntityType.DROWNED);
		save(cache, genShard(ElementType.WATER), EntityType.GUARDIAN);
		save(cache, genShard(ElementType.WATER), EntityType.ELDER_GUARDIAN);
		save(cache, genShard(ElementType.WATER), EntityType.SLIME);
		save(cache, genShard(ElementType.WATER), EntityType.STRAY);
		save(cache, genShard(ElementType.WATER), EntityType.SQUID);
		save(cache, genShard(ElementType.WATER), EntityType.GLOW_SQUID);
		save(cache, genShard(ElementType.WATER), EntityType.AXOLOTL);
		save(cache, genShard(ElementType.WATER), EntityType.POLAR_BEAR);
		save(cache, genShard(ElementType.WATER), EntityType.DOLPHIN);
		save(cache, genShard(ElementType.WATER), EntityType.COD);
		save(cache, genShard(ElementType.WATER), EntityType.SALMON);
		save(cache, genShard(ElementType.WATER), EntityType.TROPICAL_FISH);
		save(cache, genShard(ElementType.WATER), EntityType.PUFFERFISH);
		save(cache, genShard(ElementType.AIR), EntityType.ENDERMAN);
		save(cache, genShard(ElementType.AIR), EntityType.SPIDER);
		save(cache, genShard(ElementType.AIR), EntityType.CAVE_SPIDER);
		save(cache, genShard(ElementType.AIR), EntityType.PHANTOM);
		save(cache, genShard(ElementType.AIR), EntityType.SHULKER);
	}

	private static LootPool.Builder genShard(ElementType type) {
		return LootPool.lootPool().setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(getShardForType(type)).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(getPowerfulShardForType(type)).when(LootItemKilledByPlayerCondition.killedByPlayer()))
				.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.25F, 0.03F));
	}

	private void save(HashCache cache, LootPool.Builder pool, EntityType<?> entityType) throws IOException {
		save(cache, LootTable.lootTable().withPool(pool).setParamSet(LootContextParamSets.ENTITY), ElementalCraft.createRL(entityType.getDefaultLootTable().getPath()));
	}

	private void save(HashCache cache, LootTable.Builder builder, ResourceLocation location) throws IOException {
		if (!LootHandler.INJECT_LIST.contains(location.getPath())) {
			throw new IllegalStateException(MessageFormat.format("{} is not present in LootHandler.INJECT_LIST and will not be injected at runtime!", location));
		}
		save(cache, builder, getPath(location));
	}

	private Path getPath(ResourceLocation id) {
		return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/loot_tables/inject/" + id.getPath() + ".json");
	}

	@Nonnull
    @Override
	public String getName() {
		return "ElementalCraft inject loot tables";
	}
}
