package sirttas.elementalcraft.datagen;

import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECAdvancementProvider implements IDataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;

	public ECAdvancementProvider(DataGenerator generatorIn) {
		this.generator = generatorIn;
	}

	@Override
	public void run(DirectoryCache cache) throws IOException {
		Path path = this.generator.getOutputFolder();

		for (Item item : ForgeRegistries.ITEMS) {
			if (ElementalCraftApi.MODID.equals(item.getRegistryName().getNamespace())) {
				IDataProvider.save(GSON, cache, itemPickup(item).serializeToJson(), getPath(path, item));
			}
		}
	}

	private static Path getPath(Path path, IItemProvider item) {
		ResourceLocation name = item.asItem().getRegistryName();

		return path.resolve("data/" + name.getNamespace() + "/advancements/pickup/" + name.getPath() + ".json");
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	@Override
	public String getName() {
		return "ElementalCraft Advancements";
	}

	private Advancement.Builder itemPickup(IItemProvider item) {
		return Advancement.Builder.advancement().parent(ElementalCraft.createRL("main/root")).addCriterion("has_" + item.asItem().getRegistryName().getPath(),
				hasItem(item));
	}

	/**
	 * Creates a new {@link InventoryChangeTrigger} that checks for a player having
	 * a certain item.
	 */
	protected static InventoryChangeTrigger.Instance hasItem(IItemProvider item) {
		return hasItem(ItemPredicate.Builder.item().of(item).build());
	}

	/**
	 * Creates a new {@link InventoryChangeTrigger} that checks for a player having
	 * an item within the given tag.
	 */
	protected static InventoryChangeTrigger.Instance hasItem(ITag<Item> tag) {
		return hasItem(ItemPredicate.Builder.item().of(tag).build());
	}

	/**
	 * Creates a new {@link InventoryChangeTrigger} that checks for a player having
	 * a certain item.
	 */
	protected static InventoryChangeTrigger.Instance hasItem(ItemPredicate... predicate) {
		return new InventoryChangeTrigger.Instance(EntityPredicate.AndPredicate.ANY, MinMaxBounds.IntBound.ANY, MinMaxBounds.IntBound.ANY, MinMaxBounds.IntBound.ANY,
				predicate);
	}

}
