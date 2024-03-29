package sirttas.elementalcraft.item;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.ITooltipImageBlock;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlock;
import sirttas.elementalcraft.block.container.ElementContainerBlockItem;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlockItem;
import sirttas.elementalcraft.item.chisel.ChiselItem;
import sirttas.elementalcraft.item.elemental.CrystalItem;
import sirttas.elementalcraft.item.elemental.ElementalItem;
import sirttas.elementalcraft.item.elemental.FireFuelItem;
import sirttas.elementalcraft.item.elemental.LensItem;
import sirttas.elementalcraft.item.elemental.ShardItem;
import sirttas.elementalcraft.item.holder.ElementHolderItem;
import sirttas.elementalcraft.item.holder.PureElementHolderItem;
import sirttas.elementalcraft.item.jewel.JewelItem;
import sirttas.elementalcraft.item.jewel.JewelModel;
import sirttas.elementalcraft.item.pipe.CoverFrameItem;
import sirttas.elementalcraft.item.pipe.PipeUpgradeItem;
import sirttas.elementalcraft.item.pureore.PureOreItem;
import sirttas.elementalcraft.item.rune.RuneItem;
import sirttas.elementalcraft.item.rune.RuneModel;
import sirttas.elementalcraft.item.source.SourceStabilizerItem;
import sirttas.elementalcraft.item.source.analysis.SourceAnalysisGlassItem;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleItem;
import sirttas.elementalcraft.item.spell.FocusItem;
import sirttas.elementalcraft.item.spell.ScrollItem;
import sirttas.elementalcraft.item.spell.SpellEffectItem;
import sirttas.elementalcraft.item.spell.StaffItem;
import sirttas.elementalcraft.item.spell.book.SpellBookItem;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.spell.SpellHelper;

import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECItems {
	private static final DeferredRegister<Item> DEFERRED_REGISTER = DeferredRegister.create(Registries.ITEM, ElementalCraftApi.MODID);

	public static final DeferredHolder<Item, FocusItem> FOCUS = register(FocusItem::new, FocusItem.NAME);
	public static final DeferredHolder<Item, StaffItem> STAFF = register(StaffItem::new, StaffItem.NAME);
	public static final DeferredHolder<Item, ScrollItem> SCROLL = register(ScrollItem::new, ScrollItem.NAME);
	public static final DeferredHolder<Item, SpellBookItem> SPELL_BOOK = register(SpellBookItem::new, SpellBookItem.NAME);
	public static final DeferredHolder<Item, ReceptacleItem> RECEPTACLE = register(ReceptacleItem::new, ReceptacleItem.NAME);
	public static final DeferredHolder<Item, SourceStabilizerItem> SOURCE_STABILIZER = register(SourceStabilizerItem::new, SourceStabilizerItem.NAME);
	public static final DeferredHolder<Item, SourceAnalysisGlassItem> SOURCE_ANALYSIS_GLASS = register(SourceAnalysisGlassItem::new, SourceAnalysisGlassItem.NAME);
	public static final DeferredHolder<Item, ElementHolderItem> FIRE_HOLDER = register(() -> new ElementHolderItem(ElementType.FIRE), ElementHolderItem.NAME_FIRE);
	public static final DeferredHolder<Item, ElementHolderItem> WATER_HOLDER = register(() -> new ElementHolderItem(ElementType.WATER), ElementHolderItem.NAME_WATER);
	public static final DeferredHolder<Item, ElementHolderItem> EARTH_HOLDER = register(() -> new ElementHolderItem(ElementType.EARTH), ElementHolderItem.NAME_EARTH);
	public static final DeferredHolder<Item, ElementHolderItem> AIR_HOLDER = register(() -> new ElementHolderItem(ElementType.AIR), ElementHolderItem.NAME_AIR);
	public static final DeferredHolder<Item, ECItem> PURE_HOLDER_CORE = register(ECItem::new, PureElementHolderItem.NAME + "_core");
	public static final DeferredHolder<Item, PureElementHolderItem> PURE_HOLDER = register(PureElementHolderItem::new, PureElementHolderItem.NAME);
	public static final DeferredHolder<Item, PureOreItem> PURE_ORE = register(PureOreItem::new, PureOreItem.NAME);
	public static final DeferredHolder<Item, RuneItem> RUNE = register(RuneItem::new, RuneItem.NAME);
	public static final DeferredHolder<Item, ChiselItem> CHISEL = register(ChiselItem::new, ChiselItem.NAME);
	public static final DeferredHolder<Item, FireFuelItem> ELEMENTAL_FIREFUEL = register(FireFuelItem::new, FireFuelItem.NAME);
	public static final DeferredHolder<Item, CoverFrameItem> COVER_FRAME = register(CoverFrameItem::new, CoverFrameItem.NAME);
	public static final DeferredHolder<Item, PipeUpgradeItem> ELEMENT_PUMP = register(PipeUpgradeTypes.ELEMENT_PUMP);
	public static final DeferredHolder<Item, PipeUpgradeItem> PIPE_PRIORITY_RINGS = register(PipeUpgradeTypes.PIPE_PRIORITY_RINGS);
	public static final DeferredHolder<Item, PipeUpgradeItem> ELEMENT_VALVE = register(PipeUpgradeTypes.ELEMENT_VALVE);
	public static final DeferredHolder<Item, PipeUpgradeItem> ELEMENT_BEAM = register(PipeUpgradeTypes.ELEMENT_BEAM);

	public static final DeferredHolder<Item, Item> ELEMENTOPEDIA = DeferredHolder.create(Registries.ITEM, new ResourceLocation("patchouli", "guide_book"));

	public static final DeferredHolder<Item, ECItem> INERT_CRYSTAL = register(ECItem::new, "inert_crystal");
	public static final DeferredHolder<Item, ECItem> CONTAINED_CRYSTAL = register(ECItem::new, "contained_crystal");
	public static final DeferredHolder<Item, ECItem> STRONGLY_CONTAINED_CRYSTAL = register(ECItem::new, "strongly_contained_crystal");
	public static final DeferredHolder<Item, ECItem> PURE_CRYSTAL = register(() -> new ECItem().setFoil(true), "purecrystal");
	public static final DeferredHolder<Item, ECItem> DRENCHED_IRON_INGOT = register(ECItem::new, "drenched_iron_ingot");
	public static final DeferredHolder<Item, ECItem> DRENCHED_IRON_NUGGET = register(ECItem::new, "drenched_iron_nugget");
	public static final DeferredHolder<Item, ECItem> SWIFT_ALLOY_INGOT = register(ECItem::new, "swift_alloy_ingot");
	public static final DeferredHolder<Item, ECItem> SWIFT_ALLOY_NUGGET = register(ECItem::new, "swift_alloy_nugget");
	public static final DeferredHolder<Item, ECItem> HARDENED_HANDLE = register(ECItem::new, "hardened_handle");
	public static final DeferredHolder<Item, ECItem> DRENCHED_SAW_BLADE = register(ECItem::new, "drenched_saw_blade");
	public static final DeferredHolder<Item, ECItem> SHRINE_BASE = register(ECItem::new, "shrinebase");
	public static final DeferredHolder<Item, ECItem> FIREITE_INGOT = register(ECItem::new, "fireite_ingot");
	public static final DeferredHolder<Item, ECItem> FIREITE_NUGGET = register(ECItem::new, "fireite_nugget");
	public static final DeferredHolder<Item, ECItem> AIR_SILK = register(ECItem::new, "air_silk");
	public static final DeferredHolder<Item, ECItem> SHRINE_UPGRADE_CORE = register(ECItem::new, "shrine_upgrade_core");
	public static final DeferredHolder<Item, ECItem> ADVANCED_SHRINE_UPGRADE_CORE = register(ECItem::new, "advanced_shrine_upgrade_core");
	public static final DeferredHolder<Item, ECItem> SCROLL_PAPER = register(ECItem::new, "scroll_paper");
	public static final DeferredHolder<Item, ECItem> SPRINGALINE_SHARD = register(ECItem::new, "springaline_shard");
	public static final DeferredHolder<Item, ECItem> SOLAR_PRISM = register(ECItem::new, "solar_prism");
	public static final DeferredHolder<Item, CrystalItem> FIRE_CRYSTAL = register(() -> new CrystalItem(ElementType.FIRE), "firecrystal");
	public static final DeferredHolder<Item, CrystalItem> WATER_CRYSTAL = register(() -> new CrystalItem(ElementType.WATER), "watercrystal");
	public static final DeferredHolder<Item, CrystalItem> EARTH_CRYSTAL = register(() -> new CrystalItem(ElementType.EARTH), "earthcrystal");
	public static final DeferredHolder<Item, CrystalItem> AIR_CRYSTAL = register(() -> new CrystalItem(ElementType.AIR), "aircrystal");
	public static final DeferredHolder<Item, ShardItem> FIRE_SHARD = register(() -> new ShardItem(ElementType.FIRE), ShardItem.NAME_FIRE);
	public static final DeferredHolder<Item, ShardItem> WATER_SHARD = register(() -> new ShardItem(ElementType.WATER), ShardItem.NAME_WATER);
	public static final DeferredHolder<Item, ShardItem> EARTH_SHARD = register(() -> new ShardItem(ElementType.EARTH), ShardItem.NAME_EARTH);
	public static final DeferredHolder<Item, ShardItem> AIR_SHARD = register(() -> new ShardItem(ElementType.AIR), ShardItem.NAME_AIR);
	public static final DeferredHolder<Item, ShardItem> POWERFUL_FIRE_SHARD = register(() -> new ShardItem(ElementType.FIRE, 9), ShardItem.NAME_FIRE_POWERFUL);
	public static final DeferredHolder<Item, ShardItem> POWERFUL_WATER_SHARD = register(() -> new ShardItem(ElementType.WATER, 9), ShardItem.NAME_WATER_POWERFUL);
	public static final DeferredHolder<Item, ShardItem> POWERFUL_EARTH_SHARD = register(() -> new ShardItem(ElementType.EARTH, 9), ShardItem.NAME_EARTH_POWERFUL);
	public static final DeferredHolder<Item, ShardItem> POWERFUL_AIR_SHARD = register(() -> new ShardItem(ElementType.AIR, 9), ShardItem.NAME_AIR_POWERFUL);
	public static final DeferredHolder<Item, ElementalItem> CRUDE_FIRE_GEM = register(() -> new ElementalItem(ElementType.FIRE), "crude_fire_gem");
	public static final DeferredHolder<Item, ElementalItem> CRUDE_WATER_GEM = register(() -> new ElementalItem(ElementType.WATER), "crude_water_gem");
	public static final DeferredHolder<Item, ElementalItem> CRUDE_EARTH_GEM = register(() -> new ElementalItem(ElementType.EARTH), "crude_earth_gem");
	public static final DeferredHolder<Item, ElementalItem> CRUDE_AIR_GEM = register(() -> new ElementalItem(ElementType.AIR), "crude_air_gem");
	public static final DeferredHolder<Item, ElementalItem> FINE_FIRE_GEM = register(() -> new ElementalItem(ElementType.FIRE), "fine_fire_gem");
	public static final DeferredHolder<Item, ElementalItem> FINE_WATER_GEM = register(() -> new ElementalItem(ElementType.WATER), "fine_water_gem");
	public static final DeferredHolder<Item, ElementalItem> FINE_EARTH_GEM = register(() -> new ElementalItem(ElementType.EARTH), "fine_earth_gem");
	public static final DeferredHolder<Item, ElementalItem> FINE_AIR_GEM = register(() -> new ElementalItem(ElementType.AIR), "fine_air_gem");
	public static final DeferredHolder<Item, ElementalItem> PRISTINE_FIRE_GEM = register(() -> new ElementalItem(ElementType.FIRE), "pristine_fire_gem");
	public static final DeferredHolder<Item, ElementalItem> PRISTINE_WATER_GEM = register(() -> new ElementalItem(ElementType.WATER), "pristine_water_gem");
	public static final DeferredHolder<Item, ElementalItem> PRISTINE_EARTH_GEM = register(() -> new ElementalItem(ElementType.EARTH), "pristine_earth_gem");
	public static final DeferredHolder<Item, ElementalItem> PRISTINE_AIR_GEM = register(() -> new ElementalItem(ElementType.AIR), "pristine_air_gem");
	public static final DeferredHolder<Item, LensItem> FIRE_LENS = register(() -> new LensItem(ElementType.FIRE), LensItem.NAME_FIRE);
	public static final DeferredHolder<Item, LensItem> WATER_LENS = register(() -> new LensItem(ElementType.WATER), LensItem.NAME_WATER);
	public static final DeferredHolder<Item, LensItem> EARTH_LENS = register(() -> new LensItem(ElementType.EARTH), LensItem.NAME_EARTH);
	public static final DeferredHolder<Item, LensItem> AIR_LENS = register(() -> new LensItem(ElementType.AIR), LensItem.NAME_AIR);
	public static final DeferredHolder<Item, ECItem> MINOR_RUNE_SLATE = register(ECItem::new, "minor_rune_slate");
	public static final DeferredHolder<Item, ECItem> RUNE_SLATE = register(ECItem::new, "rune_slate");
	public static final DeferredHolder<Item, ECItem> MAJOR_RUNE_SLATE = register(ECItem::new, "major_rune_slate");
	public static final DeferredHolder<Item, ECItem> UNSET_JEWEL = register(ECItem::new, "unset_jewel");
	public static final DeferredHolder<Item, JewelItem> JEWEL = register(JewelItem::new, JewelItem.NAME);
	public static final DeferredHolder<Item, ElementalItem> ARTIFICIAL_FIRE_SOURCE_SEED = register(() -> new ElementalItem(ElementType.FIRE), "artificial_fire_source_seed");
	public static final DeferredHolder<Item, ElementalItem> ARTIFICIAL_WATER_SOURCE_SEED = register(() -> new ElementalItem(ElementType.WATER), "artificial_water_source_seed");
	public static final DeferredHolder<Item, ElementalItem> ARTIFICIAL_EARTH_SOURCE_SEED = register(() -> new ElementalItem(ElementType.EARTH), "artificial_earth_source_seed");
	public static final DeferredHolder<Item, ElementalItem> ARTIFICIAL_AIR_SOURCE_SEED = register(() -> new ElementalItem(ElementType.AIR), "artificial_air_source_seed");
	public static final DeferredHolder<Item, ElementalItem> NATURAL_FIRE_SOURCE_SEED = register(() -> new ElementalItem(ElementType.FIRE), "natural_fire_source_seed");
	public static final DeferredHolder<Item, ElementalItem> NATURAL_WATER_SOURCE_SEED = register(() -> new ElementalItem(ElementType.WATER), "natural_water_source_seed");
	public static final DeferredHolder<Item, ElementalItem> NATURAL_EARTH_SOURCE_SEED = register(() -> new ElementalItem(ElementType.EARTH), "natural_earth_source_seed");
	public static final DeferredHolder<Item, ElementalItem> NATURAL_AIR_SOURCE_SEED = register(() -> new ElementalItem(ElementType.AIR), "natural_air_source_seed");
	public static final DeferredHolder<Item, SpellEffectItem> REPAIR_HAMMER = register(SpellEffectItem::new, "repair_hammer");


	private ECItems() {}

	@SubscribeEvent
	public static void registerBlockItems(RegisterEvent event) {
		if (!event.getRegistryKey().equals(Registries.ITEM)) {
			return;
		}

		var registry = event.getRegistry();

		event.register(Registries.ITEM, r -> {
			RegistryHelper.register(r, new BlockItem(ECBlocks.FIREITE_BLOCK.get(), ECProperties.Items.FIREITE), ECBlocks.FIREITE_BLOCK);
			RegistryHelper.register(r, new TranslocationShrineUpgradeBlockItem(ECBlocks.TRANSLOCATION_SHRINE_UPGRADE.get(), ECProperties.Items.DEFAULT_ITEM_PROPERTIES), ECBlocks.TRANSLOCATION_SHRINE_UPGRADE);
			for (var entry : BuiltInRegistries.BLOCK.entrySet()) {
				var block = entry.getValue();
				var registryName = entry.getKey().location();

				if (ElementalCraft.owns(registryName) && !registry.containsKey(registryName)) {
					BlockItem blockItem;

					if (block instanceof AbstractElementContainerBlock containerBlock) {
						blockItem = new ElementContainerBlockItem(containerBlock, ECProperties.Items.DEFAULT_ITEM_PROPERTIES);
					} else if (block instanceof ITooltipImageBlock) {
						blockItem = new TooltipImageBlockItem(block, ECProperties.Items.DEFAULT_ITEM_PROPERTIES);
					} else {
						blockItem = new BlockItem(block, ECProperties.Items.DEFAULT_ITEM_PROPERTIES);
					}

					RegistryHelper.register(r, registryName, blockItem);
				}
			}
		});
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void replaceModels(ModelEvent.ModifyBakingResult event) {
		var modelRegistry = event.getModels();

		replaceModels(modelRegistry, RuneItem.NAME, RuneModel::new);
		replaceModels(modelRegistry, JewelItem.NAME, JewelModel::new);
	}

	@OnlyIn(Dist.CLIENT)
	private static void replaceModels(Map<ResourceLocation, BakedModel> modelRegistry, String name, UnaryOperator<BakedModel> modelFactory) {
		modelRegistry.computeIfPresent(new ModelResourceLocation(ElementalCraftApi.createRL(name), "inventory"), (k, v) -> modelFactory.apply(v));
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((s, l) -> l == 0 ? -1 : ReceptacleHelper.getElementType(s).getColor(), RECEPTACLE.get());
		event.register((s, l) -> {
			var colors = ElementalCraft.PURE_ORE_MANAGER.getColors(s);

			return colors != null && l < colors.length ? colors[l] : -1;
		}, PURE_ORE.get());
		event.register((s, l) -> l == 0 ? -1 : SpellHelper.getSpell(s).getColor(), SCROLL.get());
		event.register((s, l) -> l == 0 ? -1 : ((ElementHolderItem) s.getItem()).getElementType().getColor(), FIRE_HOLDER.get(), WATER_HOLDER.get(), EARTH_HOLDER.get(), AIR_HOLDER.get());
	}

	private static <T extends PipeUpgrade> DeferredHolder<Item, PipeUpgradeItem> register(DeferredHolder<PipeUpgradeType<?>, PipeUpgradeType<T>> pipeUpgrade) {
		return register(() -> new PipeUpgradeItem(pipeUpgrade::get, ECProperties.Items.DEFAULT_ITEM_PROPERTIES), pipeUpgrade.getId().getPath());
	}

	private static <T extends Item> DeferredHolder<Item, T> register(Supplier<T> item, String name) {
		return DEFERRED_REGISTER.register(name, item);
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
}
