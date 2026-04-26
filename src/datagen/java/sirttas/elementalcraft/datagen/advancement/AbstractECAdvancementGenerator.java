package sirttas.elementalcraft.datagen.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.ItemUsedOnLocationTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractECAdvancementGenerator implements AdvancementSubProvider {

	protected AdvancementHolder itemPickup(@NotNull HolderGetter<@NotNull Item> registry, ItemLike item, AdvancementHolder parent, Identifier name, @NotNull Consumer<AdvancementHolder> saver) {
		return Advancement.Builder.advancement()
				.parent(parent)
				.addCriterion("has_" + name.getPath(), hasItem(registry, item))
				.save(saver, Identifier.fromNamespaceAndPath(name.getNamespace(), "pickup/" + name.getPath()));
	}

	protected static Criterion<InventoryChangeTrigger.@NotNull TriggerInstance> hasItem(@NotNull HolderLookup.Provider registries, ItemLike... item) {
		return hasItem(registries.lookupOrThrow(Registries.ITEM), item);
	}

    protected static Criterion<InventoryChangeTrigger.@NotNull TriggerInstance> hasItem(@NotNull HolderGetter<@NotNull Item> registry, ItemLike... item) {
		return hasItem(ItemPredicate.Builder.item().of(registry, item).build());
	}

	protected static Criterion<InventoryChangeTrigger.@NotNull TriggerInstance> hasItem(ItemPredicate... predicate) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(predicate);
	}

	public static Criterion<ItemUsedOnLocationTrigger.@NotNull TriggerInstance> useItem(ItemPredicate.Builder predicate) {
		ContextAwarePredicate contextawarepredicate = ContextAwarePredicate.create(
				MatchTool.toolMatches(predicate).build()
		);

		return CriteriaTriggers.ITEM_USED_ON_BLOCK.createCriterion(new ItemUsedOnLocationTrigger.TriggerInstance(Optional.empty(), Optional.of(contextawarepredicate)));
	}
}
