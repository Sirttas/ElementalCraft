package sirttas.elementalcraft.data;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;

public class ECAdvancementProvider implements IDataProvider {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	private final DataGenerator generator;

	public ECAdvancementProvider(DataGenerator generatorIn) {
		this.generator = generatorIn;
	}

	@Override
	public void act(DirectoryCache cache) throws IOException {
		Path path = this.generator.getOutputFolder();

		for (Item item : ForgeRegistries.ITEMS) {
			if (ElementalCraft.MODID.equals(item.getRegistryName().getNamespace())) {
				IDataProvider.save(GSON, cache, itemPickup(item).serialize(), getPath(path, item));
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
		return Advancement.Builder.builder().withParentId(new ResourceLocation(ElementalCraft.MODID, "main/root")).withCriterion("has_" + item.asItem().getRegistryName().getPath(),
				this.hasItem(item));
	}

	/**
	 * Creates a new {@link InventoryChangeTrigger} that checks for a player having
	 * a certain item.
	 */
	protected InventoryChangeTrigger.Instance hasItem(IItemProvider itemIn) {
		return this.hasItem(ItemPredicate.Builder.create().item(itemIn).build());
	}

	/**
	 * Creates a new {@link InventoryChangeTrigger} that checks for a player having
	 * an item within the given tag.
	 */
	protected InventoryChangeTrigger.Instance hasItem(Tag<Item> tagIn) {
		return this.hasItem(ItemPredicate.Builder.create().tag(tagIn).build());
	}

	/**
	 * Creates a new {@link InventoryChangeTrigger} that checks for a player having
	 * a certain item.
	 */
	protected InventoryChangeTrigger.Instance hasItem(ItemPredicate... predicates) {
		return new InventoryChangeTrigger.Instance(MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, predicates);
	}

}
