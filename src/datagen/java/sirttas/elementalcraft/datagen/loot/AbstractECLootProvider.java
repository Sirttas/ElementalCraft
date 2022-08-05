package sirttas.elementalcraft.datagen.loot;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.LootTables;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.item.ECItems;

import java.io.IOException;
import java.nio.file.Path;

public abstract class AbstractECLootProvider implements DataProvider {

	protected final DataGenerator generator;

	protected AbstractECLootProvider(DataGenerator generator) {
		this.generator = generator;
	}
	
	protected void save(CachedOutput cache, Builder builder, Path path) throws IOException {
		DataProvider.saveStable(cache, LootTables.serialize(builder.build()), path);
	}

	public static ECItem getCrystalForType(ElementType type) {
		return switch (type) {
			case AIR -> ECItems.AIR_CRYSTAL.get();
			case EARTH -> ECItems.EARTH_CRYSTAL.get();
			case FIRE -> ECItems.FIRE_CRYSTAL.get();
			case WATER -> ECItems.WATER_CRYSTAL.get();
			default -> ECItems.INERT_CRYSTAL.get();
		};
	}

	public static ECItem getShardForType(ElementType type) {
		return switch (type) {
			case AIR -> ECItems.AIR_SHARD.get();
			case EARTH -> ECItems.EARTH_SHARD.get();
			case FIRE -> ECItems.FIRE_SHARD.get();
			case WATER -> ECItems.WATER_SHARD.get();
			default -> throw new IllegalArgumentException("Element Type must not be NONE");
		};
	}

	public static ECItem getPowerfulShardForType(ElementType type) {
		return switch (type) {
			case AIR -> ECItems.POWERFUL_AIR_SHARD.get();
			case EARTH -> ECItems.POWERFUL_EARTH_SHARD.get();
			case FIRE -> ECItems.POWERFUL_FIRE_SHARD.get();
			case WATER -> ECItems.POWERFUL_WATER_SHARD.get();
			default -> throw new IllegalArgumentException("Element Type must not be NONE");
		};
	}
}
