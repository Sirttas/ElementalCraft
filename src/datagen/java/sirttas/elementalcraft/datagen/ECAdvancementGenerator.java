package sirttas.elementalcraft.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.function.Consumer;

public class ECAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {

	private ExistingFileHelper existingFileHelper;

	@Override
	public void generate(@NotNull HolderLookup.Provider registries, @NotNull Consumer<AdvancementHolder> saver, @NotNull ExistingFileHelper existingFileHelper) {
		this.existingFileHelper = existingFileHelper;

		var root = Advancement.Builder.advancement()
				.addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
				.save(saver, ElementalCraftApi.createRL("main/root"), existingFileHelper);

		for (var entry : BuiltInRegistries.ITEM.entrySet()) {
			var item = entry.getValue();
			var key = entry.getKey().location();

			if (ElementalCraft.owns(key)) {
				itemPickup(item, root, key, saver);
			}
		}
	}

	private AdvancementHolder itemPickup(ItemLike item, AdvancementHolder root, ResourceLocation name, @NotNull Consumer<AdvancementHolder> saver) {
		return Advancement.Builder.advancement()
				.parent( root)
				.addCriterion("has_" + name.getPath(), hasItem(item))
				.save(saver, new ResourceLocation(name.getNamespace(), "pickup/" + name.getPath()), existingFileHelper);
	}

	protected static Criterion<InventoryChangeTrigger.TriggerInstance> hasItem(ItemLike item) {
		return hasItem(ItemPredicate.Builder.item().of(item).build());
	}

	protected static Criterion<InventoryChangeTrigger.TriggerInstance> hasItem(ItemPredicate... predicate) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(predicate);
	}
}
