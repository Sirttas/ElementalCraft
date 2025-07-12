package sirttas.elementalcraft.datagen.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.datagen.language.TranslationKeyValidator;

import java.util.function.Consumer;

public class ECPickupAdvancementGenerator extends AbstractECAdvancementGenerator {

	public ECPickupAdvancementGenerator(TranslationKeyValidator translationKeyValidator) {
		super(translationKeyValidator);
	}

	@Override
	protected void generate(@NotNull HolderLookup.Provider registries, @NotNull Consumer<AdvancementHolder> saver) {
		var root = Advancement.Builder.advancement()
				.addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
				.save(saver, ElementalCraftApi.createRL("pickup/root"), existingFileHelper);

		for (var entry : BuiltInRegistries.ITEM.entrySet()) {
			var item = entry.getValue();
			var key = entry.getKey().location();

			if (ElementalCraft.owns(key)) {
				itemPickup(item, root, key, saver);
			}
		}
	}
}
