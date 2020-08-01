package sirttas.elementalcraft.data;

import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.LootTableManager;

public abstract class AbstractECLootProvider implements IDataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	protected final DataGenerator generator;

	public AbstractECLootProvider(DataGenerator generator) {
		this.generator = generator;
	}
	
	protected void save(DirectoryCache cache, Builder builder, Path path) throws IOException {
		IDataProvider.save(GSON, cache, LootTableManager.toJson(builder.build()), path);
	}

}