package sirttas.elementalcraft.data.loot;

import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.RandomChanceWithLooting;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.loot.LootHandler;
import sirttas.elementalcraft.loot.function.ECLootFunctions;

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
	public void act(DirectoryCache cache) throws IOException {
		save(cache, genShard(ElementType.EARTH), EntityType.ZOMBIE);
		save(cache, genShard(ElementType.EARTH), EntityType.ZOMBIE_VILLAGER);
		save(cache, genShard(ElementType.EARTH), EntityType.SKELETON);
		save(cache, genShard(ElementType.EARTH), EntityType.WITHER_SKELETON);
		save(cache, genShard(ElementType.EARTH), EntityType.SILVERFISH);
		save(cache, genShard(ElementType.EARTH), EntityType.IRON_GOLEM);
		save(cache, genShard(ElementType.EARTH), EntityType.SKELETON_HORSE);
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
		save(cache, genShard(ElementType.WATER), EntityType.POLAR_BEAR);
		save(cache, genShard(ElementType.WATER), EntityType.DOLPHIN);
		save(cache, genShard(ElementType.WATER), EntityType.COD);
		save(cache, genShard(ElementType.WATER), EntityType.SALMON);
		save(cache, genShard(ElementType.WATER), EntityType.TROPICAL_FISH);
		save(cache, genShard(ElementType.AIR), EntityType.ENDERMAN);
		save(cache, genShard(ElementType.AIR), EntityType.SPIDER);
		save(cache, genShard(ElementType.AIR), EntityType.CAVE_SPIDER);
		save(cache, genShard(ElementType.AIR), EntityType.PHANTOM);
		save(cache, genShard(ElementType.AIR), EntityType.SHULKER);
	}

	private static LootPool.Builder genShard(ElementType type) {
		return LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(ECItems.getShardForType(type))).acceptCondition(RandomChanceWithLooting.builder(0.25F, 0.03F));
	}

	private void save(DirectoryCache cache, LootPool.Builder pool, EntityType<?> entityType) throws IOException {
		save(cache, LootTable.builder().addLootPool(pool).setParameterSet(LootParameterSets.ENTITY), ElementalCraft.createRL(entityType.getLootTable().getPath()));
	}

	private void save(DirectoryCache cache, LootTable.Builder builder, ResourceLocation location) throws IOException {
		if (!LootHandler.INJECT_lIST.contains(location.getPath())) {
			throw new IllegalStateException(MessageFormat.format("{} is not present in LootHandler.INJECT_lIST and will not be injected at runtime!", location));
		}
		save(cache, builder, getPath(location));
	}

	private Path getPath(ResourceLocation id) {
		return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/loot_tables/inject/" + id.getPath() + ".json");
	}

	@Override
	public String getName() {
		return "ElementalCraft inject loot tables";
	}
}
