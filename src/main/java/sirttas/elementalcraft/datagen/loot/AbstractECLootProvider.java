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
			return ECItems.airCrystal;
		case EARTH:
			return ECItems.earthCrystal;
		case FIRE:
			return ECItems.fireCrystal;
		case WATER:
			return ECItems.waterCrystal;
		default:
			return ECItems.inertCrystal;
		}
	}

	public static ItemEC getShardForType(ElementType type) {
		switch (type) {
		case AIR:
			return ECItems.airShard;
		case EARTH:
			return ECItems.earthShard;
		case FIRE:
			return ECItems.fireShard;
		case WATER:
			return ECItems.waterShard;
		default:
			throw new IllegalArgumentException("Element Type must not be NONE");
		}
	}

	public static ItemEC getPowerfulShardForType(ElementType type) {
		switch (type) {
		case AIR:
			return ECItems.powerfulAirShard;
		case EARTH:
			return ECItems.powerfulEarthShard;
		case FIRE:
			return ECItems.powerfulFireShard;
		case WATER:
			return ECItems.powerfulWaterShard;
		default:
			throw new IllegalArgumentException("Element Type must not be NONE");
		}
	}

}