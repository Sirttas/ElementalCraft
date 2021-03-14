package sirttas.elementalcraft.datagen.loot;

import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.LootTableManager;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.ItemEC;

public abstract class AbstractECLootProvider implements IDataProvider {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	protected final DataGenerator generator;

	protected AbstractECLootProvider(DataGenerator generator) {
		this.generator = generator;
	}
	
	protected void save(DirectoryCache cache, Builder builder, Path path) throws IOException {
		IDataProvider.save(GSON, cache, LootTableManager.toJson(builder.build()), path);
	}

	public static ItemEC getCrystalForType(ElementType type) {
		switch (type) {
		case AIR:
			return ECItems.AIR_CRYSTAL;
		case EARTH:
			return ECItems.EARTH_CRYSTAL;
		case FIRE:
			return ECItems.FIRE_CRYSTAL;
		case WATER:
			return ECItems.WATER_CRYSTAL;
		default:
			return ECItems.INERT_CRYSTAL;
		}
	}

	public static ItemEC getShardForType(ElementType type) {
		switch (type) {
		case AIR:
			return ECItems.AIR_SHARD;
		case EARTH:
			return ECItems.EARTH_SHARD;
		case FIRE:
			return ECItems.FIRE_SHARD;
		case WATER:
			return ECItems.WATER_SHARD;
		default:
			throw new IllegalArgumentException("Element Type must not be NONE");
		}
	}

	public static ItemEC getPowerfulShardForType(ElementType type) {
		switch (type) {
		case AIR:
			return ECItems.POWERFUL_AIR_SHARD;
		case EARTH:
			return ECItems.POWERFUL_EARTH_SHARD;
		case FIRE:
			return ECItems.POWERFUL_FIRE_SHARD;
		case WATER:
			return ECItems.POWERFUL_WATER_SHARD;
		default:
			throw new IllegalArgumentException("Element Type must not be NONE");
		}
	}

}