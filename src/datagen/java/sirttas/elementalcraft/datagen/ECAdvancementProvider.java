package sirttas.elementalcraft.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;

public class ECAdvancementProvider implements DataProvider {
	private final DataGenerator generator;

	public ECAdvancementProvider(DataGenerator generatorIn) {
		this.generator = generatorIn;
	}

	@Override
	public void run(@Nonnull CachedOutput cache) throws IOException {
		Path path = this.generator.getOutputFolder();

		for (var entry : ForgeRegistries.ITEMS.getEntries()) {
			var item = entry.getValue();
			var key = entry.getKey().location();

			if (ElementalCraft.owns(key)) {
				DataProvider.saveStable(cache, itemPickup(item, key).serializeToJson(), getPath(path, key));
			}
		}
	}

	private static Path getPath(Path path, ResourceLocation name) {
		return path.resolve("data/" + name.getNamespace() + "/advancements/pickup/" + name.getPath() + ".json");
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	@Nonnull
    @Override
	public String getName() {
		return "ElementalCraft Advancements";
	}

	private Advancement.Builder itemPickup(ItemLike item, ResourceLocation name) {
		return Advancement.Builder.advancement().parent(ElementalCraft.createRL("main/root")).addCriterion("has_" + name.getPath(), hasItem(item));
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
	protected static InventoryChangeTrigger.TriggerInstance hasItem(TagKey<Item> tag) {
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
