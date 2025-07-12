package sirttas.elementalcraft.datagen.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
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
	protected void generate(@NotNull HolderLookup.Provider registries, @NotNull Consumer<AdvancementHolder> saver) {
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
				.save(saver, ElementalCraftApi.createRL("main/root"), existingFileHelper);
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
				.save(saver, ElementalCraftApi.createRL("main/sources"), existingFileHelper);
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
				.addCriterion("has_empty_receptacle", hasItem(ECItems.EMPTY_RECEPTACLE.get()))
				.save(saver, ElementalCraftApi.createRL("main/empty_receptacle"), existingFileHelper);
		Advancement.Builder.advancement()
				.parent(emptyReceptacle)
				.display(
						ReceptacleHelper.create(ElementType.FIRE),
						Component.translatable("advancements.elementalcraft.receptacles.title"),
						Component.translatable("advancements.elementalcraft.receptacles.description"),
						null,
						AdvancementType.CHALLENGE,
						true,
						true,
						false
				)
				.addCriterion("use_fire_receptacle", useItem(ItemPredicate.Builder.item().of(ECBlocks.FIRE_SOURCE.get())))
				.addCriterion("use_water_receptacle", useItem(ItemPredicate.Builder.item().of(ECBlocks.WATER_SOURCE.get())))
				.addCriterion("use_earth_receptacle", useItem(ItemPredicate.Builder.item().of(ECBlocks.EARTH_SOURCE.get())))
				.addCriterion("use_air_receptacle", useItem(ItemPredicate.Builder.item().of(ECBlocks.AIR_SOURCE.get())))
				.save(saver, ElementalCraftApi.createRL("main/receptacles"), existingFileHelper);
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
				.addCriterion("has_source_analysis_glass", hasItem(ECItems.SOURCE_ANALYSIS_GLASS.get()))
				.save(saver, ElementalCraftApi.createRL("main/source_analysis_glass"), existingFileHelper);
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
				.addCriterion("has_source_stabilizer", hasItem(ECItems.SOURCE_STABILIZER.get()))
				.save(saver, ElementalCraftApi.createRL("main/source_stabilizer"), existingFileHelper);
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
				.addCriterion("has_inert_crystal", hasItem(ECItems.INERT_CRYSTAL.get()))
				.save(saver, ElementalCraftApi.createRL("main/inert_crystal"), existingFileHelper);
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
				.addCriterion("has_contained_crystal", hasItem(ECItems.CONTAINED_CRYSTAL.get()))
				.save(saver, ElementalCraftApi.createRL("main/contained_crystal"), existingFileHelper);
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
				.addCriterion("has_rudimentary_pipe", hasItem(ECBlocks.PIPE_RUDIMENTARY.get()))
				.save(saver, ElementalCraftApi.createRL("main/rudimentary_pipe"), existingFileHelper);
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
				.addCriterion("has_pipe", hasItem(ECBlocks.PIPE.get()))
				.save(saver, ElementalCraftApi.createRL("main/pipe"), existingFileHelper);
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
				.addCriterion("has_improved_pipe", hasItem(ECBlocks.PIPE_IMPROVED.get()))
				.save(saver, ElementalCraftApi.createRL("main/improved_pipe"), existingFileHelper);
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
				.addCriterion("has_small_container", hasItem(ECBlocks.SMALL_CONTAINER.get()))
				.save(saver, ElementalCraftApi.createRL("main/small_container"), existingFileHelper);
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
				.addCriterion("has_container", hasItem(ECBlocks.CONTAINER.get()))
				.save(saver, ElementalCraftApi.createRL("main/container"), existingFileHelper);
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
				.addCriterion("has_reservoir", hasItem(ECBlocks.FIRE_RESERVOIR.get(), ECBlocks.WATER_RESERVOIR.get(), ECBlocks.EARTH_RESERVOIR.get(), ECBlocks.AIR_RESERVOIR.get()))
				.save(saver, ElementalCraftApi.createRL("main/reservoirs"), existingFileHelper);
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
				.addCriterion("has_element_holder", hasItem(ECItems.FIRE_HOLDER.get(), ECItems.WATER_HOLDER.get(), ECItems.EARTH_HOLDER.get(), ECItems.AIR_HOLDER.get()))
				.save(saver, ElementalCraftApi.createRL("main/element_holders"), existingFileHelper);
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
				.addCriterion("has_pure_element_holder", hasItem(ECItems.PURE_HOLDER.get()))
				.save(saver, ElementalCraftApi.createRL("main/pure_element_holder"), existingFileHelper);
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
				.addCriterion("has_rudimentary_extractor", hasItem(ECBlocks.RUDIMENTARY_EXTRACTOR.get()))
				.save(saver, ElementalCraftApi.createRL("main/rudimentary_extractor"), existingFileHelper);
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
				.addCriterion("has_extractor", hasItem(ECBlocks.EXTRACTOR.get()))
				.save(saver, ElementalCraftApi.createRL("main/extractor"), existingFileHelper);
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
				.addCriterion("has_improved_extractor", hasItem(ECBlocks.IMPROVED_EXTRACTOR.get()))
				.save(saver, ElementalCraftApi.createRL("main/improved_extractor"), existingFileHelper);
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
				.addCriterion("has_infuser", hasItem(ECBlocks.INFUSER.get()))
				.save(saver, ElementalCraftApi.createRL("main/infuser"), existingFileHelper);
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
				.addCriterion("has_fire_crystal", hasItem(ECItems.FIRE_CRYSTAL.get()))
				.addCriterion("has_water_crystal", hasItem(ECItems.WATER_CRYSTAL.get()))
				.addCriterion("has_earth_crystal", hasItem(ECItems.EARTH_CRYSTAL.get()))
				.addCriterion("has_air_crystal", hasItem(ECItems.AIR_CRYSTAL.get()))
				.save(saver, ElementalCraftApi.createRL("main/crystals"), existingFileHelper);
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
				.addCriterion("has_crude_fire_gem", hasItem(ECItems.CRUDE_FIRE_GEM.get()))
				.addCriterion("has_crude_water_gem", hasItem(ECItems.CRUDE_WATER_GEM.get()))
				.addCriterion("has_crude_earth_gem", hasItem(ECItems.CRUDE_EARTH_GEM.get()))
				.addCriterion("has_crude_air_gem", hasItem(ECItems.CRUDE_AIR_GEM.get()))
				.save(saver, ElementalCraftApi.createRL("main/crude_gems"), existingFileHelper);
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
				.addCriterion("has_fine_fire_gem", hasItem(ECItems.FINE_FIRE_GEM.get()))
				.addCriterion("has_fine_water_gem", hasItem(ECItems.FINE_WATER_GEM.get()))
				.addCriterion("has_fine_earth_gem", hasItem(ECItems.FINE_EARTH_GEM.get()))
				.addCriterion("has_fine_air_gem", hasItem(ECItems.FINE_AIR_GEM.get()))
				.save(saver, ElementalCraftApi.createRL("main/fine_gems"), existingFileHelper);
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
				.addCriterion("has_pristine_fire_gem", hasItem(ECItems.PRISTINE_FIRE_GEM.get()))
				.addCriterion("has_pristine_water_gem", hasItem(ECItems.PRISTINE_WATER_GEM.get()))
				.addCriterion("has_pristine_earth_gem", hasItem(ECItems.PRISTINE_EARTH_GEM.get()))
				.addCriterion("has_pristine_air_gem", hasItem(ECItems.PRISTINE_AIR_GEM.get()))
				.save(saver, ElementalCraftApi.createRL("main/pristine_gems"), existingFileHelper);
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
				.addCriterion("has_drenched_iron", hasItem(ECItems.DRENCHED_IRON_INGOT.get()))
				.save(saver, ElementalCraftApi.createRL("main/drenched_iron"), existingFileHelper);
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
				.addCriterion("has_fire_furnace", hasItem(ECBlocks.FIRE_FURNACE.get()))
				.save(saver, ElementalCraftApi.createRL("main/fire_furnace"), existingFileHelper);
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
				.addCriterion("has_fire_blast_furnace", hasItem(ECBlocks.FIRE_BLAST_FURNACE.get()))
				.save(saver, ElementalCraftApi.createRL("main/fire_blast_furnace"), existingFileHelper);
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
				.addCriterion("has_water_mill_grindstone", hasItem(ECBlocks.WATER_MILL_GRINDSTONE.get()))
				.save(saver, ElementalCraftApi.createRL("main/water_mill_grindstone"), existingFileHelper);
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
				.addCriterion("has_air_mill_grindstone", hasItem(ECBlocks.AIR_MILL_GRINDSTONE.get()))
				.save(saver, ElementalCraftApi.createRL("main/air_mill_grindstone"), existingFileHelper);
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
				.addCriterion("has_water_mill_wood_saw", hasItem(ECBlocks.WATER_MILL_WOOD_SAW.get()))
				.save(saver, ElementalCraftApi.createRL("main/water_mill_wood_saw"), existingFileHelper);
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
				.addCriterion("has_air_mill_wood_saw", hasItem(ECBlocks.AIR_MILL_WOOD_SAW.get()))
				.save(saver, ElementalCraftApi.createRL("main/air_mill_wood_saw"), existingFileHelper);
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
				.addCriterion("has_binder", hasItem(ECBlocks.BINDER.get()))
				.save(saver, ElementalCraftApi.createRL("main/binder"), existingFileHelper);
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
				.addCriterion("has_inscriber", hasItem(ECBlocks.INSCRIBER.get()))
				.save(saver, ElementalCraftApi.createRL("main/inscriber"), existingFileHelper);
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
				.addCriterion("has_drenched_iron_chisel", hasItem(ECItems.DRENCHED_IRON_CHISEL.get()))
				.save(saver, ElementalCraftApi.createRL("main/drenched_iron_chisel"), existingFileHelper);
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
				.addCriterion("has_swift_alloy_chisel", hasItem(ECItems.SWIFT_ALLOY_CHISEL.get()))
				.save(saver, ElementalCraftApi.createRL("main/swift_alloy_chisel"), existingFileHelper);
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
				.addCriterion("has_fireite_chisel", hasItem(ECItems.FIREITE_CHISEL.get()))
				.save(saver, ElementalCraftApi.createRL("main/fireite_chisel"), existingFileHelper);
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
				.addCriterion("has_swift_alloy", hasItem(ECItems.SWIFT_ALLOY_INGOT.get()))
				.save(saver, ElementalCraftApi.createRL("main/swift_alloy"), existingFileHelper);
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
				.addCriterion("has_diffuser", hasItem(ECBlocks.DIFFUSER.get()))
				.save(saver, ElementalCraftApi.createRL("main/diffuser"), existingFileHelper);
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
				.addCriterion("has_springaline_shard", hasItem(ECItems.SPRINGALINE_SHARD.get()))
				.save(saver, ElementalCraftApi.createRL("main/springaline_shard"), existingFileHelper);
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
				.addCriterion("has_springaline_cluster", hasItem(ECBlocks.SPRINGALINE_CLUSTER.get()))
				.save(saver, ElementalCraftApi.createRL("main/springaline_cluster"), existingFileHelper);
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
				.addCriterion("has_strongly_contained_crystal", hasItem(ECItems.STRONGLY_CONTAINED_CRYSTAL.get()))
				.save(saver, ElementalCraftApi.createRL("main/strongly_contained_crystal"), existingFileHelper);
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
				.addCriterion("has_crystallizer", hasItem(ECBlocks.CRYSTALLIZER.get()))
				.save(saver, ElementalCraftApi.createRL("main/crystallizer"), existingFileHelper);
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
				.addCriterion("has_pure_infusion", hasItem(ECBlocks.PURE_INFUSER.get()))
				.addCriterion("has_fire_pedestal", hasItem(ECBlocks.FIRE_PEDESTAL.get()))
				.addCriterion("has_water_pedestal", hasItem(ECBlocks.WATER_PEDESTAL.get()))
				.addCriterion("has_earth_pedestal", hasItem(ECBlocks.EARTH_PEDESTAL.get()))
				.addCriterion("has_air_pedestal", hasItem(ECBlocks.AIR_PEDESTAL.get()))
				.save(saver, ElementalCraftApi.createRL("main/pure_infusion"), existingFileHelper);
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
				.addCriterion("has_pure_crystal", hasItem(ECItems.PURE_CRYSTAL.get()))
				.save(saver, ElementalCraftApi.createRL("main/pure_crystal"), existingFileHelper);
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
				.addCriterion("has_purifier", hasItem(ECBlocks.PURIFIER.get()))
				.save(saver, ElementalCraftApi.createRL("main/purifier"), existingFileHelper);
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
				.addCriterion("has_fireite", hasItem(ECItems.FIREITE_INGOT.get()))
				.save(saver, ElementalCraftApi.createRL("main/fireite"), existingFileHelper);
	}

}
