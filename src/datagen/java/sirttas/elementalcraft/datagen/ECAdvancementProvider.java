package sirttas.elementalcraft.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;

import java.util.function.Consumer;

public class ECAdvancementProvider implements ForgeAdvancementProvider.AdvancementGenerator {

	private ExistingFileHelper existingFileHelper;

	@Override
	public void generate(@NotNull HolderLookup.Provider registries, @NotNull Consumer<Advancement> saver, @NotNull ExistingFileHelper existingFileHelper) {
		this.existingFileHelper = existingFileHelper;

		for (var entry : ForgeRegistries.ITEMS.getEntries()) {
			var item = entry.getValue();
			var key = entry.getKey().location();

			if (ElementalCraft.owns(key)) {
				itemPickup(item, key, saver);
			}
		}
	}

	private Advancement itemPickup(ItemLike item, ResourceLocation name, @NotNull Consumer<Advancement> saver) {
		return Advancement.Builder.advancement()
				.parent(ElementalCraft.createRL("main/root"))
				.addCriterion("has_" + name.getPath(), hasItem(item))
				.save(saver, name, existingFileHelper);
	}

	protected static InventoryChangeTrigger.TriggerInstance hasItem(ItemLike item) {
		return hasItem(ItemPredicate.Builder.item().of(item).build());
	}

	protected static InventoryChangeTrigger.TriggerInstance hasItem(ItemPredicate... predicate) {
		return new InventoryChangeTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY,
				predicate);
	}

}
