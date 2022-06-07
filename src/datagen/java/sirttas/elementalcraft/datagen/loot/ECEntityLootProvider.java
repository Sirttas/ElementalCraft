package sirttas.elementalcraft.datagen.loot;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.loot.function.ECLootFunctions;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;

/**
 * greatly inspired by Botania
 *
 * 
 */
public class ECEntityLootProvider extends AbstractECLootProvider {

	public ECEntityLootProvider(DataGenerator generator) {
		super(generator);
		ECLootFunctions.setup();
	}

	@Override
	public void run(@Nonnull HashCache cache) throws IOException {
		saveThrownElementCrystal(cache, ElementType.FIRE);
		saveThrownElementCrystal(cache, ElementType.WATER);
		saveThrownElementCrystal(cache, ElementType.EARTH);
		saveThrownElementCrystal(cache, ElementType.AIR);
	}

	private void saveThrownElementCrystal(HashCache cache, ElementType type) throws IOException {
		var crystalLocation = getCrystalForType(type).getRegistryName();

		save(cache, LootTable.lootTable().withPool(genShard(type)).setParamSet(LootContextParamSets.SELECTOR), new ResourceLocation(crystalLocation.getNamespace(), "thrown_element_crystal/" + crystalLocation.getPath()));
	}

	private static LootPool.Builder genShard(ElementType type) {
		return LootPool.lootPool().setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(getShardForType(type)).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 7))).setWeight(10))
				.add(LootItem.lootTableItem(getPowerfulShardForType(type)));
	}

	private void save(HashCache cache, LootTable.Builder builder, ResourceLocation location) throws IOException {
		save(cache, builder, getPath(location));
	}

	private Path getPath(ResourceLocation id) {
		return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/loot_tables/entities/" + id.getPath() + ".json");
	}

	@Nonnull
    @Override
	public String getName() {
		return "ElementalCraft inject loot tables";
	}
}
