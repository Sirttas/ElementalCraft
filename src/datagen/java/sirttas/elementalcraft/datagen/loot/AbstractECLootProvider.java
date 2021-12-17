package sirttas.elementalcraft.datagen.loot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.LootTables;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.item.ECItems;

import java.io.IOException;
import java.nio.file.Path;

public abstract class AbstractECLootProvider implements DataProvider {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	protected final DataGenerator generator;

	protected AbstractECLootProvider(DataGenerator generator) {
		this.generator = generator;
	}
	
	protected void save(HashCache cache, Builder builder, Path path) throws IOException {
		DataProvider.save(GSON, cache, LootTables.serialize(builder.build()), path);
	}

	public static ECItem getCrystalForType(ElementType type) {
		return switch (type) {
			case AIR -> ECItems.AIR_CRYSTAL;
			case EARTH -> ECItems.EARTH_CRYSTAL;
			case FIRE -> ECItems.FIRE_CRYSTAL;
			case WATER -> ECItems.WATER_CRYSTAL;
			default -> ECItems.INERT_CRYSTAL;
		};
	}

	public static ECItem getShardForType(ElementType type) {
		return switch (type) {
			case AIR -> ECItems.AIR_SHARD;
			case EARTH -> ECItems.EARTH_SHARD;
			case FIRE -> ECItems.FIRE_SHARD;
			case WATER -> ECItems.WATER_SHARD;
			default -> throw new IllegalArgumentException("Element Type must not be NONE");
		};
	}

	public static ECItem getPowerfulShardForType(ElementType type) {
		return switch (type) {
			case AIR -> ECItems.POWERFUL_AIR_SHARD;
			case EARTH -> ECItems.POWERFUL_EARTH_SHARD;
			case FIRE -> ECItems.POWERFUL_FIRE_SHARD;
			case WATER -> ECItems.POWERFUL_WATER_SHARD;
			default -> throw new IllegalArgumentException("Element Type must not be NONE");
		};
	}

}
