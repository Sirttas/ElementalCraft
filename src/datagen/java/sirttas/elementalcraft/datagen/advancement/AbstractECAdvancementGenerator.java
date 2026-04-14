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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.datagen.language.TranslationKeyValidator;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractECAdvancementGenerator implements AdvancementSubProvider {

	private final TranslationKeyValidator translationKeyValidator;

	protected AbstractECAdvancementGenerator(TranslationKeyValidator translationKeyValidator) {
		this.translationKeyValidator = translationKeyValidator;
	}

	@Override
	public final void generate(@NotNull HolderLookup.Provider registries, @NotNull Consumer<AdvancementHolder> saver) {
        doGenerate(registries, advancementHolder -> {
			var advancement = advancementHolder.value();

			try {
				advancement.name().ifPresent(translationKeyValidator::checkHasComponent);
				advancement.display().ifPresent(displayInfo -> {
					translationKeyValidator.checkHasComponent(displayInfo.getTitle());
					translationKeyValidator.checkHasComponent(displayInfo.getDescription());
				});
			} catch (Exception e) {
				throw new IllegalStateException("Language check failed for " + advancementHolder.id(), e);
			}
			saver.accept(advancementHolder);
		});
	}

	protected abstract void doGenerate(@NotNull HolderLookup.Provider registries, @NotNull Consumer<AdvancementHolder> saver);

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

	public static CompoundTag elementTypeTag(ElementType elementType) {
		var tag = new CompoundTag();

		tag.putString(ECNames.ELEMENT_TYPE, elementType.getSerializedName());
		return tag;
	}
}
