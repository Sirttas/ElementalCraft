package sirttas.elementalcraft.datagen.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.criterion.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
	protected void doGenerate(@NotNull HolderLookup.Provider registries, @NotNull Consumer<AdvancementHolder> saver) {
        var itemRegistry = registries.lookupOrThrow(Registries.ITEM);
		var root = Advancement.Builder.advancement()
				.addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
				.save(saver, ElementalCraftApi.identifier("pickup/root"));

		for (var entry : BuiltInRegistries.ITEM.entrySet()) {
			var item = entry.getValue();
			var key = entry.getKey().identifier();

			if (ElementalCraft.owns(key)) {
				itemPickup(itemRegistry, item, root, key, saver);
			}
		}
	}
}
