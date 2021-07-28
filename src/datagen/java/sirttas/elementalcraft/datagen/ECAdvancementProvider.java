package sirttas.elementalcraft.datagen;

import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.DataProvider;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECAdvancementProvider implements DataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;

	public ECAdvancementProvider(DataGenerator generatorIn) {
		this.generator = generatorIn;
	}

	@Override
	public void run(HashCache cache) throws IOException {
		Path path = this.generator.getOutputFolder();

		for (Item item : ForgeRegistries.ITEMS) {
			if (ElementalCraftApi.MODID.equals(item.getRegistryName().getNamespace())) {
				DataProvider.save(GSON, cache, itemPickup(item).serializeToJson(), getPath(path, item));
			}
		}
	}

	private static Path getPath(Path path, ItemLike item) {
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

	private Advancement.Builder itemPickup(ItemLike item) {
		return Advancement.Builder.advancement().parent(ElementalCraft.createRL("main/root")).addCriterion("has_" + item.asItem().getRegistryName().getPath(),
				hasItem(item));
	}

	/**
	 * Creates a new {@link InventoryChangeTrigger} that checks for a player having
	 * a certain item.
	 */
	protected static InventoryChangeTrigger.TriggerInstance hasItem(ItemLike item) {
		return hasItem(ItemPredicate.Builder.item().of(item).build());
	}

	/**
	 * Creates a new {@link InventoryChangeTrigger} that checks for a player having
	 * an item within the given tag.
	 */
	protected static InventoryChangeTrigger.TriggerInstance hasItem(Tag<Item> tag) {
		return hasItem(ItemPredicate.Builder.item().of(tag).build());
	}

	/**
	 * Creates a new {@link InventoryChangeTrigger} that checks for a player having
	 * a certain item.
	 */
	protected static InventoryChangeTrigger.TriggerInstance hasItem(ItemPredicate... predicate) {
		return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY,
				predicate);
	}

}
