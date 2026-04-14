package sirttas.elementalcraft.datagen.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.ConsumeItemTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.advancements.LookAtSourceTrigger;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.datagen.language.TranslationKeyValidator;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;

import java.util.function.Consumer;

public class ECAdvancementGenerator extends AbstractECAdvancementGenerator {

	public ECAdvancementGenerator(TranslationKeyValidator translationKeyValidator) {
		super(translationKeyValidator);
	}

	@Override
	protected void doGenerate(@NotNull HolderLookup.Provider registries, @NotNull Consumer<AdvancementHolder> saver) {
        var itemRegistry = registries.lookupOrThrow(Registries.ITEM);

		AdvancementHolder root = Advancement.Builder.advancement()
				.display(
						ECItems.FOCUS.get(),
						Component.translatable("advancements.elementalcraft.root.title"),
						Component.translatable("advancements.elementalcraft.root.description"),
						ElementalCraftApi.createRL("textures/block/whiterock.png"),
						AdvancementType.GOAL,
						false,
						false,
						false
				)
				.addCriterion("consumed_item", ConsumeItemTrigger.TriggerInstance.usedItem())
				.save(saver, ElementalCraftApi.createRL("main/root"));
		var source = Advancement.Builder.advancement()
				.parent(root)
				.display(
						ECBlocks.FIRE_SOURCE.get(),
						Component.translatable("advancements.elementalcraft.sources.title"),
						Component.translatable("advancements.elementalcraft.sources.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("player_look_at_source", LookAtSourceTrigger.TriggerInstance.playerLookAtSource())
				.save(saver, ElementalCraftApi.createRL("main/sources"));
		var emptyReceptacle = Advancement.Builder.advancement()
				.parent(source)
				.display(
						ECItems.EMPTY_RECEPTACLE.get(),
						Component.translatable("advancements.elementalcraft.empty_receptacle.title"),
						Component.translatable("advancements.elementalcraft.empty_receptacle.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_empty_receptacle", hasItem(registries, ECItems.EMPTY_RECEPTACLE.get()))
				.save(saver, ElementalCraftApi.createRL("main/empty_receptacle"));
		Advancement.Builder.advancement()
				.parent(emptyReceptacle)
				.display(
						ItemStackTemplate.fromNonEmptyStack(ReceptacleHelper.create(ElementType.FIRE)),
						Component.translatable("advancements.elementalcraft.receptacles.title"),
						Component.translatable("advancements.elementalcraft.receptacles.description"),
						null,
						AdvancementType.CHALLENGE,
						true,
						true,
						false
				)
				.addCriterion("use_fire_receptacle", useItem(ItemPredicate.Builder.item().of(itemRegistry, ECBlocks.FIRE_SOURCE.get())))
				.addCriterion("use_water_receptacle", useItem(ItemPredicate.Builder.item().of(itemRegistry, ECBlocks.WATER_SOURCE.get())))
				.addCriterion("use_earth_receptacle", useItem(ItemPredicate.Builder.item().of(itemRegistry, ECBlocks.EARTH_SOURCE.get())))
				.addCriterion("use_air_receptacle", useItem(ItemPredicate.Builder.item().of(itemRegistry, ECBlocks.AIR_SOURCE.get())))
				.save(saver, ElementalCraftApi.createRL("main/receptacles"));
		Advancement.Builder.advancement()
				.parent(source)
				.display(
						ECItems.SOURCE_ANALYSIS_GLASS.get(),
						Component.translatable("advancements.elementalcraft.source_analysis_glass.title"),
						Component.translatable("advancements.elementalcraft.source_analysis_glass.description"),
						null,
						AdvancementType.GOAL,
						true,
						true,
						false
				)
				.addCriterion("has_source_analysis_glass", hasItem(itemRegistry, ECItems.SOURCE_ANALYSIS_GLASS.get()))
				.save(saver, ElementalCraftApi.createRL("main/source_analysis_glass"));
		Advancement.Builder.advancement()
				.parent(source)
				.display(
						ECItems.SOURCE_STABILIZER.get(),
						Component.translatable("advancements.elementalcraft.source_stabilizer.title"),
						Component.translatable("advancements.elementalcraft.source_stabilizer.description"),
						null,
						AdvancementType.GOAL,
						true,
						true,
						false
				)
				.addCriterion("has_source_stabilizer", hasItem(itemRegistry, ECItems.SOURCE_STABILIZER.get()))
				.save(saver, ElementalCraftApi.createRL("main/source_stabilizer"));
		var inertCrystal = Advancement.Builder.advancement()
				.parent(root)
				.display(
						ECItems.INERT_CRYSTAL.get(),
						Component.translatable("advancements.elementalcraft.inert_crystal.title"),
						Component.translatable("advancements.elementalcraft.inert_crystal.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_inert_crystal", hasItem(itemRegistry, ECItems.INERT_CRYSTAL.get()))
				.save(saver, ElementalCraftApi.createRL("main/inert_crystal"));
		var containedCrystal = Advancement.Builder.advancement()
				.parent(inertCrystal)
				.display(
						ECItems.CONTAINED_CRYSTAL.get(),
						Component.translatable("advancements.elementalcraft.inert_crystal.title"),
						Component.translatable("advancements.elementalcraft.contained_crystal.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_contained_crystal", hasItem(itemRegistry, ECItems.CONTAINED_CRYSTAL.get()))
				.save(saver, ElementalCraftApi.createRL("main/contained_crystal"));
		var rudimentaryPipe = Advancement.Builder.advancement()
				.parent(containedCrystal)
				.display(
						ECBlocks.PIPE_RUDIMENTARY.get(),
						Component.translatable("advancements.elementalcraft.rudimentary_pipe.title"),
						Component.translatable("advancements.elementalcraft.rudimentary_pipe.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_rudimentary_pipe", hasItem(itemRegistry, ECBlocks.PIPE_RUDIMENTARY.get()))
				.save(saver, ElementalCraftApi.createRL("main/rudimentary_pipe"));
		var pipe = Advancement.Builder.advancement()
				.parent(rudimentaryPipe)
				.display(
						ECBlocks.PIPE.get(),
						Component.translatable("advancements.elementalcraft.pipe.title"),
						Component.translatable("advancements.elementalcraft.pipe.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_pipe", hasItem(itemRegistry, ECBlocks.PIPE.get()))
				.save(saver, ElementalCraftApi.createRL("main/pipe"));
		Advancement.Builder.advancement()
				.parent(pipe)
				.display(
						ECBlocks.PIPE_IMPROVED.get(),
						Component.translatable("advancements.elementalcraft.improved_pipe.title"),
						Component.translatable("advancements.elementalcraft.improved_pipe.description"),
						null,
						AdvancementType.GOAL,
						true,
						true,
						false
				)
				.addCriterion("has_improved_pipe", hasItem(itemRegistry, ECBlocks.PIPE_IMPROVED.get()))
				.save(saver, ElementalCraftApi.createRL("main/improved_pipe"));
		var smallContainer = Advancement.Builder.advancement()
				.parent(rudimentaryPipe)
				.display(
						ECBlocks.SMALL_CONTAINER.get(),
						Component.translatable("advancements.elementalcraft.small_container.title"),
						Component.translatable("advancements.elementalcraft.small_container.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_small_container", hasItem(itemRegistry, ECBlocks.SMALL_CONTAINER.get()))
				.save(saver, ElementalCraftApi.createRL("main/small_container"));
		var container = Advancement.Builder.advancement()
				.parent(smallContainer)
				.display(
						ECBlocks.CONTAINER.get(),
						Component.translatable("advancements.elementalcraft.container.title"),
						Component.translatable("advancements.elementalcraft.container.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_container", hasItem(itemRegistry, ECBlocks.CONTAINER.get()))
				.save(saver, ElementalCraftApi.createRL("main/container"));
		Advancement.Builder.advancement()
				.parent(container)
				.display(
						ECBlocks.FIRE_RESERVOIR.get(),
						Component.translatable("advancements.elementalcraft.reservoirs.title"),
						Component.translatable("advancements.elementalcraft.reservoirs.description"),
						null,
						AdvancementType.GOAL,
						true,
						true,
						false
				)
				.addCriterion("has_reservoir", hasItem(itemRegistry, ECBlocks.FIRE_RESERVOIR.get(), ECBlocks.WATER_RESERVOIR.get(), ECBlocks.EARTH_RESERVOIR.get(), ECBlocks.AIR_RESERVOIR.get()))
				.save(saver, ElementalCraftApi.createRL("main/reservoirs"));
		var elementHolders = Advancement.Builder.advancement()
				.parent(smallContainer)
				.display(
						ECItems.FIRE_HOLDER.get(),
						Component.translatable("advancements.elementalcraft.element_holders.title"),
						Component.translatable("advancements.elementalcraft.element_holders.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_element_holder", hasItem(itemRegistry, ECItems.FIRE_HOLDER.get(), ECItems.WATER_HOLDER.get(), ECItems.EARTH_HOLDER.get(), ECItems.AIR_HOLDER.get()))
				.save(saver, ElementalCraftApi.createRL("main/element_holders"));
		Advancement.Builder.advancement()
				.parent(elementHolders)
				.display(
						ECItems.PURE_HOLDER.get(),
						Component.translatable("advancements.elementalcraft.pure_element_holder.title"),
						Component.translatable("advancements.elementalcraft.pure_element_holder.description"),
						null,
						AdvancementType.GOAL,
						true,
						true,
						false
				)
				.addCriterion("has_pure_element_holder", hasItem(itemRegistry, ECItems.PURE_HOLDER.get()))
				.save(saver, ElementalCraftApi.createRL("main/pure_element_holder"));
		var rudimentaryExtractor = Advancement.Builder.advancement()
				.parent(containedCrystal)
				.display(
						ECBlocks.RUDIMENTARY_EXTRACTOR.get(),
						Component.translatable("advancements.elementalcraft.rudimentary_extractor.title"),
						Component.translatable("advancements.elementalcraft.rudimentary_extractor.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_rudimentary_extractor", hasItem(itemRegistry, ECBlocks.RUDIMENTARY_EXTRACTOR.get()))
				.save(saver, ElementalCraftApi.createRL("main/rudimentary_extractor"));
		var extractor = Advancement.Builder.advancement()
				.parent(rudimentaryExtractor)
				.display(
						ECBlocks.EXTRACTOR.get(),
						Component.translatable("advancements.elementalcraft.extractor.title"),
						Component.translatable("advancements.elementalcraft.extractor.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_extractor", hasItem(itemRegistry, ECBlocks.EXTRACTOR.get()))
				.save(saver, ElementalCraftApi.createRL("main/extractor"));
		Advancement.Builder.advancement()
				.parent(extractor)
				.display(
						ECBlocks.IMPROVED_EXTRACTOR.get(),
						Component.translatable("advancements.elementalcraft.improved_extractor.title"),
						Component.translatable("advancements.elementalcraft.improved_extractor.description"),
						null,
						AdvancementType.GOAL,
						true,
						true,
						false
				)
				.addCriterion("has_improved_extractor", hasItem(itemRegistry, ECBlocks.IMPROVED_EXTRACTOR.get()))
				.save(saver, ElementalCraftApi.createRL("main/improved_extractor"));
		var infuser = Advancement.Builder.advancement()
				.parent(containedCrystal)
				.display(
						ECBlocks.INFUSER.get(),
						Component.translatable("advancements.elementalcraft.infuser.title"),
						Component.translatable("advancements.elementalcraft.infuser.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_infuser", hasItem(itemRegistry, ECBlocks.INFUSER.get()))
				.save(saver, ElementalCraftApi.createRL("main/infuser"));
		Advancement.Builder.advancement()
				.parent(infuser)
				.display(
						ECItems.FIRE_CRYSTAL.get(),
						Component.translatable("advancements.elementalcraft.crystals.title"),
						Component.translatable("advancements.elementalcraft.crystals.description"),
						null,
						AdvancementType.GOAL,
						true,
						true,
						false
				)
				.addCriterion("has_fire_crystal", hasItem(itemRegistry, ECItems.FIRE_CRYSTAL.get()))
				.addCriterion("has_water_crystal", hasItem(itemRegistry, ECItems.WATER_CRYSTAL.get()))
				.addCriterion("has_earth_crystal", hasItem(itemRegistry, ECItems.EARTH_CRYSTAL.get()))
				.addCriterion("has_air_crystal", hasItem(itemRegistry, ECItems.AIR_CRYSTAL.get()))
				.save(saver, ElementalCraftApi.createRL("main/crystals"));
		var crudeGems = Advancement.Builder.advancement()
				.parent(infuser)
				.display(
						ECItems.CRUDE_FIRE_GEM.get(),
						Component.translatable("advancements.elementalcraft.crude_gems.title"),
						Component.translatable("advancements.elementalcraft.crude_gems.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_crude_fire_gem", hasItem(itemRegistry, ECItems.CRUDE_FIRE_GEM.get()))
				.addCriterion("has_crude_water_gem", hasItem(itemRegistry, ECItems.CRUDE_WATER_GEM.get()))
				.addCriterion("has_crude_earth_gem", hasItem(itemRegistry, ECItems.CRUDE_EARTH_GEM.get()))
				.addCriterion("has_crude_air_gem", hasItem(itemRegistry, ECItems.CRUDE_AIR_GEM.get()))
				.save(saver, ElementalCraftApi.createRL("main/crude_gems"));
		var fineGems = Advancement.Builder.advancement()
				.parent(crudeGems)
				.display(
						ECItems.FINE_FIRE_GEM.get(),
						Component.translatable("advancements.elementalcraft.fine_gems.title"),
						Component.translatable("advancements.elementalcraft.fine_gems.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_fine_fire_gem", hasItem(itemRegistry, ECItems.FINE_FIRE_GEM.get()))
				.addCriterion("has_fine_water_gem", hasItem(itemRegistry, ECItems.FINE_WATER_GEM.get()))
				.addCriterion("has_fine_earth_gem", hasItem(itemRegistry, ECItems.FINE_EARTH_GEM.get()))
				.addCriterion("has_fine_air_gem", hasItem(itemRegistry, ECItems.FINE_AIR_GEM.get()))
				.save(saver, ElementalCraftApi.createRL("main/fine_gems"));
		Advancement.Builder.advancement()
				.parent(fineGems)
				.display(
						ECItems.PRISTINE_FIRE_GEM.get(),
						Component.translatable("advancements.elementalcraft.pristine_gems.title"),
						Component.translatable("advancements.elementalcraft.pristine_gems.description"),
						null,
						AdvancementType.CHALLENGE,
						true,
						true,
						false
				)
				.addCriterion("has_pristine_fire_gem", hasItem(itemRegistry, ECItems.PRISTINE_FIRE_GEM.get()))
				.addCriterion("has_pristine_water_gem", hasItem(itemRegistry, ECItems.PRISTINE_WATER_GEM.get()))
				.addCriterion("has_pristine_earth_gem", hasItem(itemRegistry, ECItems.PRISTINE_EARTH_GEM.get()))
				.addCriterion("has_pristine_air_gem", hasItem(itemRegistry, ECItems.PRISTINE_AIR_GEM.get()))
				.save(saver, ElementalCraftApi.createRL("main/pristine_gems"));
		var drenchedIron = Advancement.Builder.advancement()
				.parent(infuser)
				.display(
						ECItems.DRENCHED_IRON_INGOT.get(),
						Component.translatable("advancements.elementalcraft.drenched_iron.title"),
						Component.translatable("advancements.elementalcraft.drenched_iron.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_drenched_iron", hasItem(itemRegistry, ECItems.DRENCHED_IRON_INGOT.get()))
				.save(saver, ElementalCraftApi.createRL("main/drenched_iron"));
		var fireFurnace = Advancement.Builder.advancement()
				.parent(drenchedIron)
				.display(
						ECBlocks.FIRE_FURNACE.get(),
						Component.translatable("advancements.elementalcraft.fire_furnace.title"),
						Component.translatable("advancements.elementalcraft.fire_furnace.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_fire_furnace", hasItem(itemRegistry, ECBlocks.FIRE_FURNACE.get()))
				.save(saver, ElementalCraftApi.createRL("main/fire_furnace"));
		Advancement.Builder.advancement()
				.parent(fireFurnace)
				.display(
						ECBlocks.FIRE_BLAST_FURNACE.get(),
						Component.translatable("advancements.elementalcraft.fire_blast_furnace.title"),
						Component.translatable("advancements.elementalcraft.fire_blast_furnace.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_fire_blast_furnace", hasItem(itemRegistry, ECBlocks.FIRE_BLAST_FURNACE.get()))
				.save(saver, ElementalCraftApi.createRL("main/fire_blast_furnace"));
		var waterMillGrindstone = Advancement.Builder.advancement()
				.parent(drenchedIron)
				.display(
						ECBlocks.WATER_MILL_GRINDSTONE.get(),
						Component.translatable("advancements.elementalcraft.water_mill_grindstone.title"),
						Component.translatable("advancements.elementalcraft.water_mill_grindstone.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_water_mill_grindstone", hasItem(itemRegistry, ECBlocks.WATER_MILL_GRINDSTONE.get()))
				.save(saver, ElementalCraftApi.createRL("main/water_mill_grindstone"));
		Advancement.Builder.advancement()
				.parent(waterMillGrindstone)
				.display(
						ECBlocks.AIR_MILL_GRINDSTONE.get(),
						Component.translatable("advancements.elementalcraft.air_mill_grindstone.title"),
						Component.translatable("advancements.elementalcraft.air_mill_grindstone.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_air_mill_grindstone", hasItem(itemRegistry, ECBlocks.AIR_MILL_GRINDSTONE.get()))
				.save(saver, ElementalCraftApi.createRL("main/air_mill_grindstone"));
		var waterMillWoodSaw = Advancement.Builder.advancement()
				.parent(drenchedIron)
				.display(
						ECBlocks.WATER_MILL_WOOD_SAW.get(),
						Component.translatable("advancements.elementalcraft.water_mill_wood_saw.title"),
						Component.translatable("advancements.elementalcraft.water_mill_wood_saw.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_water_mill_wood_saw", hasItem(itemRegistry, ECBlocks.WATER_MILL_WOOD_SAW.get()))
				.save(saver, ElementalCraftApi.createRL("main/water_mill_wood_saw"));
		Advancement.Builder.advancement()
				.parent(waterMillWoodSaw)
				.display(
						ECBlocks.AIR_MILL_WOOD_SAW.get(),
						Component.translatable("advancements.elementalcraft.air_mill_wood_saw.title"),
						Component.translatable("advancements.elementalcraft.air_mill_wood_saw.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_air_mill_wood_saw", hasItem(itemRegistry, ECBlocks.AIR_MILL_WOOD_SAW.get()))
				.save(saver, ElementalCraftApi.createRL("main/air_mill_wood_saw"));
		var binder = Advancement.Builder.advancement()
				.parent(drenchedIron)
				.display(
						ECBlocks.BINDER.get(),
						Component.translatable("advancements.elementalcraft.binder.title"),
						Component.translatable("advancements.elementalcraft.binder.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_binder", hasItem(itemRegistry, ECBlocks.BINDER.get()))
				.save(saver, ElementalCraftApi.createRL("main/binder"));
		var inscriber = Advancement.Builder.advancement()
				.parent(drenchedIron)
				.display(
						ECBlocks.INSCRIBER.get(),
						Component.translatable("advancements.elementalcraft.inscriber.title"),
						Component.translatable("advancements.elementalcraft.inscriber.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_inscriber", hasItem(itemRegistry, ECBlocks.INSCRIBER.get()))
				.save(saver, ElementalCraftApi.createRL("main/inscriber"));
		var drenchedIronChisel = Advancement.Builder.advancement()
				.parent(inscriber)
				.display(
						ECItems.DRENCHED_IRON_CHISEL.get(),
						Component.translatable("advancements.elementalcraft.drenched_iron_chisel.title"),
						Component.translatable("advancements.elementalcraft.drenched_iron_chisel.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_drenched_iron_chisel", hasItem(itemRegistry, ECItems.DRENCHED_IRON_CHISEL.get()))
				.save(saver, ElementalCraftApi.createRL("main/drenched_iron_chisel"));
		var swiftAlloyChisel = Advancement.Builder.advancement()
				.parent(drenchedIronChisel)
				.display(
						ECItems.SWIFT_ALLOY_CHISEL.get(),
						Component.translatable("advancements.elementalcraft.swift_alloy_chisel.title"),
						Component.translatable("advancements.elementalcraft.swift_alloy_chisel.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_swift_alloy_chisel", hasItem(itemRegistry, ECItems.SWIFT_ALLOY_CHISEL.get()))
				.save(saver, ElementalCraftApi.createRL("main/swift_alloy_chisel"));
		Advancement.Builder.advancement()
				.parent(swiftAlloyChisel)
				.display(
						ECItems.FIREITE_CHISEL.get(),
						Component.translatable("advancements.elementalcraft.fireite_chisel.title"),
						Component.translatable("advancements.elementalcraft.fireite_chisel.description"),
						null,
						AdvancementType.GOAL,
						true,
						true,
						false
				)
				.addCriterion("has_fireite_chisel", hasItem(itemRegistry, ECItems.FIREITE_CHISEL.get()))
				.save(saver, ElementalCraftApi.createRL("main/fireite_chisel"));
		var swiftAlloy = Advancement.Builder.advancement()
				.parent(binder)
				.display(
						ECItems.SWIFT_ALLOY_INGOT.get(),
						Component.translatable("advancements.elementalcraft.swift_alloy.title"),
						Component.translatable("advancements.elementalcraft.swift_alloy.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_swift_alloy", hasItem(itemRegistry, ECItems.SWIFT_ALLOY_INGOT.get()))
				.save(saver, ElementalCraftApi.createRL("main/swift_alloy"));
		Advancement.Builder.advancement()
				.parent(swiftAlloy)
				.display(
						ECBlocks.DIFFUSER.get(),
						Component.translatable("advancements.elementalcraft.diffuser.title"),
						Component.translatable("advancements.elementalcraft.diffuser.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_diffuser", hasItem(itemRegistry, ECBlocks.DIFFUSER.get()))
				.save(saver, ElementalCraftApi.createRL("main/diffuser"));
		var springalineShard = Advancement.Builder.advancement()
				.parent(binder)
				.display(
						ECItems.SPRINGALINE_SHARD.get(),
						Component.translatable("advancements.elementalcraft.springaline_shard.title"),
						Component.translatable("advancements.elementalcraft.springaline_shard.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_springaline_shard", hasItem(itemRegistry, ECItems.SPRINGALINE_SHARD.get()))
				.save(saver, ElementalCraftApi.createRL("main/springaline_shard"));
		Advancement.Builder.advancement()
				.parent(springalineShard)
				.display(
						ECBlocks.SPRINGALINE_CLUSTER.get(),
						Component.translatable("advancements.elementalcraft.springaline_cluster.title"),
						Component.translatable("advancements.elementalcraft.springaline_cluster.description"),
						null,
						AdvancementType.GOAL,
						true,
						true,
						false
				)
				.addCriterion("has_springaline_cluster", hasItem(itemRegistry, ECBlocks.SPRINGALINE_CLUSTER.get()))
				.save(saver, ElementalCraftApi.createRL("main/springaline_cluster"));
		var stronglyContainedCrystal = Advancement.Builder.advancement()
				.parent(swiftAlloy)
				.display(
						ECItems.STRONGLY_CONTAINED_CRYSTAL.get(),
						Component.translatable("advancements.elementalcraft.strongly_contained_crystal.title"),
						Component.translatable("advancements.elementalcraft.strongly_contained_crystal.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_strongly_contained_crystal", hasItem(itemRegistry, ECItems.STRONGLY_CONTAINED_CRYSTAL.get()))
				.save(saver, ElementalCraftApi.createRL("main/strongly_contained_crystal"));
		var crystallizer = Advancement.Builder.advancement()
				.parent(stronglyContainedCrystal)
				.display(
						ECBlocks.CRYSTALLIZER.get(),
						Component.translatable("advancements.elementalcraft.crystallizer.title"),
						Component.translatable("advancements.elementalcraft.crystallizer.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_crystallizer", hasItem(itemRegistry, ECBlocks.CRYSTALLIZER.get()))
				.save(saver, ElementalCraftApi.createRL("main/crystallizer"));
		var pureInfusion = Advancement.Builder.advancement()
				.parent(crystallizer)
				.display(
						ECBlocks.PURE_INFUSER.get(),
						Component.translatable("advancements.elementalcraft.pure_infusion.title"),
						Component.translatable("advancements.elementalcraft.pure_infusion.description"),
						null,
						AdvancementType.GOAL,
						true,
						true,
						false
				)
				.addCriterion("has_pure_infusion", hasItem(itemRegistry, ECBlocks.PURE_INFUSER.get()))
				.addCriterion("has_fire_pedestal", hasItem(itemRegistry, ECBlocks.FIRE_PEDESTAL.get()))
				.addCriterion("has_water_pedestal", hasItem(itemRegistry, ECBlocks.WATER_PEDESTAL.get()))
				.addCriterion("has_earth_pedestal", hasItem(itemRegistry, ECBlocks.EARTH_PEDESTAL.get()))
				.addCriterion("has_air_pedestal", hasItem(itemRegistry, ECBlocks.AIR_PEDESTAL.get()))
				.save(saver, ElementalCraftApi.createRL("main/pure_infusion"));
		var pureCrystal = Advancement.Builder.advancement()
				.parent(pureInfusion)
				.display(
						ECItems.PURE_CRYSTAL.get(),
						Component.translatable("advancements.elementalcraft.pure_crystal.title"),
						Component.translatable("advancements.elementalcraft.pure_crystal.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_pure_crystal", hasItem(itemRegistry, ECItems.PURE_CRYSTAL.get()))
				.save(saver, ElementalCraftApi.createRL("main/pure_crystal"));
		Advancement.Builder.advancement()
				.parent(pureInfusion)
				.display(
						ECBlocks.PURIFIER.get(),
						Component.translatable("advancements.elementalcraft.purifier.title"),
						Component.translatable("advancements.elementalcraft.purifier.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_purifier", hasItem(itemRegistry, ECBlocks.PURIFIER.get()))
				.save(saver, ElementalCraftApi.createRL("main/purifier"));
		var fireite = Advancement.Builder.advancement()
				.parent(pureCrystal)
				.display(
						ECItems.FIREITE_INGOT.get(),
						Component.translatable("advancements.elementalcraft.fireite.title"),
						Component.translatable("advancements.elementalcraft.fireite.description"),
						null,
						AdvancementType.TASK,
						true,
						true,
						false
				)
				.addCriterion("has_fireite", hasItem(itemRegistry, ECItems.FIREITE_INGOT.get()))
				.save(saver, ElementalCraftApi.createRL("main/fireite"));
	}

}
