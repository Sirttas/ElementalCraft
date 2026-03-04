package sirttas.elementalcraft.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.airmill.AirMillBlockItem;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlock;
import sirttas.elementalcraft.block.container.ElementContainerBlockItem;
import sirttas.elementalcraft.block.cover.CoverFrameItem;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradeBlockItem;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlockItem;
import sirttas.elementalcraft.block.source.SourceBlock;
import sirttas.elementalcraft.block.source.SourceElementStorage;
import sirttas.elementalcraft.block.source.trait.holder.ItemSourceTraitHolder;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.element.ElementAmounts;
import sirttas.elementalcraft.item.chisel.ChiselItem;
import sirttas.elementalcraft.item.chisel.ChiselTiers;
import sirttas.elementalcraft.item.elemental.DamageableCraftingElementalItem;
import sirttas.elementalcraft.item.elemental.ElementalItem;
import sirttas.elementalcraft.item.holder.ElementHolderItem;
import sirttas.elementalcraft.item.holder.PureElementHolderItem;
import sirttas.elementalcraft.item.jewel.JewelItem;
import sirttas.elementalcraft.item.pipe.PipeUpgradeItem;
import sirttas.elementalcraft.item.pureore.PureOreItem;
import sirttas.elementalcraft.item.rune.RuneItem;
import sirttas.elementalcraft.item.source.SourceStabilizerItem;
import sirttas.elementalcraft.item.source.analysis.SourceAnalysisGlassItem;
import sirttas.elementalcraft.item.source.receptacle.EmptyReceptacleItem;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleItem;
import sirttas.elementalcraft.item.spell.FocusItem;
import sirttas.elementalcraft.item.spell.ScrollItem;
import sirttas.elementalcraft.item.spell.SpellEffectItem;
import sirttas.elementalcraft.item.spell.StaffItem;
import sirttas.elementalcraft.item.spell.book.SpellBookItem;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.spell.SpellList;
import sirttas.elementalcraft.spell.Spells;

import java.util.function.Supplier;

@EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ECItems {
	private static final DeferredRegister<Item> DEFERRED_REGISTER = DeferredRegister.create(Registries.ITEM, ElementalCraftApi.MODID);

	public static final DeferredHolder<Item, FocusItem> FOCUS = register(FocusItem.NAME, () -> new FocusItem(new Item.Properties()
			.stacksTo(1)
			.component(ECDataComponents.SPELL, Spells.NONE)
			.component(ECDataComponents.SPELL_LIST, SpellList.EMPTY)));
	public static final DeferredHolder<Item, StaffItem> STAFF = register(StaffItem.NAME, () -> new StaffItem(new Item.Properties()
			.durability(2252)
			.fireResistant()
			.component(ECDataComponents.SPELL, Spells.NONE)
			.component(ECDataComponents.SPELL_LIST, SpellList.EMPTY)
			.attributes(StaffItem.ATTRIBUTE_MODIFIERS)));
	public static final DeferredHolder<Item, ScrollItem> SCROLL = register(ScrollItem.NAME, () -> new ScrollItem(new Item.Properties()
			.stacksTo(1)
			.component(ECDataComponents.SPELL, Spells.NONE)));
	public static final DeferredHolder<Item, SpellBookItem> SPELL_BOOK = register(SpellBookItem.NAME, () -> new SpellBookItem(new Item.Properties()
			.stacksTo(1)
			.component(ECDataComponents.SPELL_LIST, SpellList.EMPTY)));
	public static final DeferredHolder<Item, EmptyReceptacleItem> EMPTY_RECEPTACLE = register(EmptyReceptacleItem.NAME, () -> new EmptyReceptacleItem(new Item.Properties()));
	public static final DeferredHolder<Item, SourceStabilizerItem> SOURCE_STABILIZER = register(SourceStabilizerItem.NAME, () -> new SourceStabilizerItem(new Item.Properties()));
	public static final DeferredHolder<Item, SourceAnalysisGlassItem> SOURCE_ANALYSIS_GLASS = register(SourceAnalysisGlassItem.NAME, SourceAnalysisGlassItem::new);
	public static final DeferredHolder<Item, ElementHolderItem> FIRE_HOLDER = register(ElementHolderItem.NAME_FIRE, () -> new ElementHolderItem(ElementType.FIRE, ECProperties.Items.HOLDER));
	public static final DeferredHolder<Item, ElementHolderItem> WATER_HOLDER = register(ElementHolderItem.NAME_WATER, () -> new ElementHolderItem(ElementType.WATER, ECProperties.Items.HOLDER));
	public static final DeferredHolder<Item, ElementHolderItem> EARTH_HOLDER = register(ElementHolderItem.NAME_EARTH, () -> new ElementHolderItem(ElementType.EARTH, ECProperties.Items.HOLDER));
	public static final DeferredHolder<Item, ElementHolderItem> AIR_HOLDER = register(ElementHolderItem.NAME_AIR, () -> new ElementHolderItem(ElementType.AIR, ECProperties.Items.HOLDER));
	public static final DeferredHolder<Item, Item> PURE_HOLDER_CORE = register(PureElementHolderItem.NAME + "_core", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, PureElementHolderItem> PURE_HOLDER = register(PureElementHolderItem.NAME, () -> new PureElementHolderItem(new Item.Properties()
			.stacksTo(1)
			.component(ECDataComponents.ELEMENT_AMOUNTS, ElementAmounts.EMPTY)));
	public static final DeferredHolder<Item, PureOreItem> PURE_ORE = register(PureOreItem.NAME, () -> new PureOreItem(new Item.Properties()));
	public static final DeferredHolder<Item, RuneItem> RUNE = register(RuneItem.NAME, () -> new RuneItem(ECProperties.Items.ITEM_UNSTACKABLE));
	public static final DeferredHolder<Item, ChiselItem> DRENCHED_IRON_CHISEL = register(ChiselItem.NAME_DRENCHED_IRON, () -> new ChiselItem(ChiselTiers.DRENCHED_IRON, new Item.Properties()));
	public static final DeferredHolder<Item, ChiselItem> SWIFT_ALLOY_CHISEL = register(ChiselItem.NAME_SWIFT_ALLOY, () -> new ChiselItem(ChiselTiers.SWIFT_ALLOY, new Item.Properties()));
	public static final DeferredHolder<Item, ChiselItem> FIREITE_CHISEL = register(ChiselItem.NAME_FIREITE, () -> new ChiselItem(ChiselTiers.FIREITE, new Item.Properties()
			.fireResistant()));
	public static final DeferredHolder<Item, DamageableCraftingElementalItem> ELEMENTAL_FIREFUEL = register("elemental_firefuel", () -> new DamageableCraftingElementalItem(ElementType.FIRE, new Item.Properties()
			.stacksTo(1)
			.durability(500)));
	public static final DeferredHolder<Item, CoverFrameItem> COVER_FRAME = register(CoverFrameItem.NAME, () -> new CoverFrameItem(new Item.Properties()));
	public static final DeferredHolder<Item, PipeUpgradeItem> ELEMENT_PUMP = registerPipeUpgrade(PipeUpgradeTypes.ELEMENT_PUMP);
	public static final DeferredHolder<Item, PipeUpgradeItem> PIPE_PRIORITY_RINGS = registerPipeUpgrade(PipeUpgradeTypes.PIPE_PRIORITY_RINGS);
	public static final DeferredHolder<Item, PipeUpgradeItem> ELEMENT_VALVE = registerPipeUpgrade(PipeUpgradeTypes.ELEMENT_VALVE);
	public static final DeferredHolder<Item, PipeUpgradeItem> ELEMENT_BEAM = registerPipeUpgrade(PipeUpgradeTypes.ELEMENT_BEAM);

	public static final DeferredHolder<Item, Item> INERT_CRYSTAL = register("inert_crystal", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> CONTAINED_CRYSTAL = register("contained_crystal", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> STRONGLY_CONTAINED_CRYSTAL = register("strongly_contained_crystal", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> PURE_CRYSTAL = register("purecrystal", () -> new Item(new Item.Properties()
			.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));
	public static final DeferredHolder<Item, Item> DRENCHED_IRON_INGOT = register("drenched_iron_ingot", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> DRENCHED_IRON_NUGGET = register("drenched_iron_nugget", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> SWIFT_ALLOY_INGOT = register("swift_alloy_ingot", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> SWIFT_ALLOY_NUGGET = register("swift_alloy_nugget", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> HARDENED_HANDLE = register("hardened_handle", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> DRENCHED_SAW_BLADE = register("drenched_saw_blade", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> SHRINE_BASE = register("shrinebase", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> FIREITE_INGOT = register("fireite_ingot", () -> new Item(new Item.Properties()
			.fireResistant()));
	public static final DeferredHolder<Item, Item> FIREITE_NUGGET = register("fireite_nugget", () -> new Item(new Item.Properties()
			.fireResistant()));
	public static final DeferredHolder<Item, Item> AIR_SILK = register("air_silk", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> SHRINE_UPGRADE_CORE = register("shrine_upgrade_core", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> ADVANCED_SHRINE_UPGRADE_CORE = register("advanced_shrine_upgrade_core", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> SCROLL_PAPER = register("scroll_paper", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> SPRINGALINE_SHARD = register("springaline_shard", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> SOLAR_PRISM = register("solar_prism", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> FIRE_CRYSTAL = register("firecrystal", () -> new ElementalItem(ElementType.FIRE, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> WATER_CRYSTAL = register("watercrystal", () -> new ElementalItem(ElementType.WATER, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> EARTH_CRYSTAL = register("earthcrystal", () -> new ElementalItem(ElementType.EARTH, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> AIR_CRYSTAL = register("aircrystal", () -> new ElementalItem(ElementType.AIR, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> CRUDE_FIRE_GEM = register("crude_fire_gem", () -> new ElementalItem(ElementType.FIRE, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> CRUDE_WATER_GEM = register("crude_water_gem", () -> new ElementalItem(ElementType.WATER, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> CRUDE_EARTH_GEM = register("crude_earth_gem", () -> new ElementalItem(ElementType.EARTH, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> CRUDE_AIR_GEM = register("crude_air_gem", () -> new ElementalItem(ElementType.AIR, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> FINE_FIRE_GEM = register("fine_fire_gem", () -> new ElementalItem(ElementType.FIRE, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> FINE_WATER_GEM = register("fine_water_gem", () -> new ElementalItem(ElementType.WATER, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> FINE_EARTH_GEM = register("fine_earth_gem", () -> new ElementalItem(ElementType.EARTH, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> FINE_AIR_GEM = register("fine_air_gem", () -> new ElementalItem(ElementType.AIR, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> PRISTINE_FIRE_GEM = register("pristine_fire_gem", () -> new ElementalItem(ElementType.FIRE, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> PRISTINE_WATER_GEM = register("pristine_water_gem", () -> new ElementalItem(ElementType.WATER, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> PRISTINE_EARTH_GEM = register("pristine_earth_gem", () -> new ElementalItem(ElementType.EARTH, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> PRISTINE_AIR_GEM = register("pristine_air_gem", () -> new ElementalItem(ElementType.AIR, new Item.Properties()));
	public static final DeferredHolder<Item, Item> PRISTINE_SHARD = register("pristine_shard", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, DamageableCraftingElementalItem> FIRE_LENS = register("fire_lens", () -> new DamageableCraftingElementalItem(ElementType.FIRE, new Item.Properties()
			.stacksTo(1)
			.durability(1500)));
	public static final DeferredHolder<Item, DamageableCraftingElementalItem> AIR_MILL = register("air_mill", () -> new DamageableCraftingElementalItem(ElementType.AIR, new Item.Properties()
			.stacksTo(1)
			.durability(3000)));
	public static final DeferredHolder<Item, Item> MINOR_RUNE_SLATE = register("minor_rune_slate", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> RUNE_SLATE = register("rune_slate", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> MAJOR_RUNE_SLATE = register("major_rune_slate", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> UNSET_JEWEL = register("unset_jewel", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, JewelItem> SALMON_JEWEL = registerJewel(Jewels.SALMON);
	public static final DeferredHolder<Item, JewelItem> PHOENIX_JEWEL = registerJewel(Jewels.PHOENIX);
	public static final DeferredHolder<Item, JewelItem> BASILISK_JEWEL = registerJewel(Jewels.BASILISK);
	public static final DeferredHolder<Item, JewelItem> BEAR_JEWEL = registerJewel(Jewels.BEAR);
	public static final DeferredHolder<Item, JewelItem> TIGER_JEWEL = registerJewel(Jewels.TIGER);
	public static final DeferredHolder<Item, JewelItem> LEOPARD_JEWEL = registerJewel(Jewels.LEOPARD);
	public static final DeferredHolder<Item, JewelItem> DOLPHIN_JEWEL = registerJewel(Jewels.DOLPHIN);
	public static final DeferredHolder<Item, JewelItem> KIRIN_JEWEL = registerJewel(Jewels.KIRIN);
	public static final DeferredHolder<Item, JewelItem> VIPER_JEWEL = registerJewel(Jewels.VIPER);
	public static final DeferredHolder<Item, JewelItem> TORTOISE_JEWEL = registerJewel(Jewels.TORTOISE);
	public static final DeferredHolder<Item, JewelItem> ARCTIC_HARE_JEWEL = registerJewel(Jewels.ARCTIC_HARE);
	public static final DeferredHolder<Item, JewelItem> MOLE_JEWEL = registerJewel(Jewels.MOLE);
	public static final DeferredHolder<Item, JewelItem> HAWK_JEWEL = registerJewel(Jewels.HAWK);
	public static final DeferredHolder<Item, JewelItem> DEMIGOD_JEWEL = registerJewel(Jewels.DEMIGOD);
	public static final DeferredHolder<Item, JewelItem> STRIDER_JEWEL = registerJewel(Jewels.STRIDER);
	public static final DeferredHolder<Item, JewelItem> WATER_STRIDER_JEWEL = registerJewel(Jewels.WATER_STRIDER);
	public static final DeferredHolder<Item, JewelItem> PIGLIN_JEWEL = registerJewel(Jewels.PIGLIN);
	public static final DeferredHolder<Item, ElementalItem> FIRE_SOURCE_SEED = register("fire_source_seed", () -> new ElementalItem(ElementType.FIRE, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> WATER_SOURCE_SEED = register("water_source_seed", () -> new ElementalItem(ElementType.WATER, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> EARTH_SOURCE_SEED = register("earth_source_seed", () -> new ElementalItem(ElementType.EARTH, new Item.Properties()));
	public static final DeferredHolder<Item, ElementalItem> AIR_SOURCE_SEED = register("air_source_seed", () -> new ElementalItem(ElementType.AIR, new Item.Properties()));
	public static final DeferredHolder<Item, SpellEffectItem> REPAIR_HAMMER = register("repair_hammer", () -> new SpellEffectItem(new Item.Properties()));

	private ECItems() {}

	@SubscribeEvent
	public static void registerBlockItems(RegisterEvent event) {
		if (!event.getRegistryKey().equals(Registries.ITEM)) {
			return;
		}

		var registry = event.getRegistry();

		event.register(Registries.ITEM, r -> {
			// Blocks
			RegistryHelper.register(r, new BlockItem(ECBlocks.FIREITE_BLOCK.get(), new Item.Properties()
					.fireResistant()), ECBlocks.FIREITE_BLOCK);
			RegistryHelper.register(r, new AirMillBlockItem(ECBlocks.AIR_MILL_GRINDSTONE.get(), new Item.Properties()), ECBlocks.AIR_MILL_GRINDSTONE);
			RegistryHelper.register(r, new AirMillBlockItem(ECBlocks.AIR_MILL_WOOD_SAW.get(), new Item.Properties()), ECBlocks.AIR_MILL_WOOD_SAW);
			RegistryHelper.register(r, new AirMillBlockItem(ECBlocks.AIR_MILL_SYNTHESIZER.get(), new Item.Properties()), ECBlocks.AIR_MILL_SYNTHESIZER);
			RegistryHelper.register(r, new TranslocationShrineUpgradeBlockItem(ECBlocks.TRANSLOCATION_SHRINE_UPGRADE.get(), new Item.Properties()), ECBlocks.TRANSLOCATION_SHRINE_UPGRADE);
			for (var entry : BuiltInRegistries.BLOCK.entrySet()) {
				var block = entry.getValue();
				var registryName = entry.getKey().identifier();

				if (ElementalCraft.owns(registryName) && !registry.containsKey(registryName)) {
					RegistryHelper.register(r, registryName, switch (block) {
						case AbstractElementContainerBlock containerBlock -> new ElementContainerBlockItem(containerBlock, new Item.Properties()
                                .component(ECDataComponents.ELEMENT_TYPE, block instanceof IElementTypeProvider provider ? provider.getElementType() : ElementType.NONE)
                                .component(ECDataComponents.ELEMENT_AMOUNT, 0));
						case SourceBlock sourceBlock -> new ReceptacleItem(sourceBlock, new Item.Properties()
								.stacksTo(1)
								.component(ECDataComponents.SOURCE_TRAITS_HOLDER, ItemSourceTraitHolder.EMPTY)
								.component(ECDataComponents.ELEMENT_AMOUNT, SourceElementStorage.DEFAULT_CAPACITY)
								.component(ECDataComponents.SOURCE_ANALYZED, false));
						case AbstractShrineUpgradeBlock shrineUpgradeBlock -> new ShrineUpgradeBlockItem(shrineUpgradeBlock, new Item.Properties());
						default -> new BlockItem(block, new Item.Properties());
					});
				}
			}
		});
	}

	private static <T extends PipeUpgrade> DeferredHolder<Item, PipeUpgradeItem> registerPipeUpgrade(DeferredHolder<PipeUpgradeType<?>, PipeUpgradeType<T>> pipeUpgrade) {
		return register(pipeUpgrade.getId().getPath(), () -> new PipeUpgradeItem(pipeUpgrade::get, new Item.Properties()));
	}

	private static DeferredHolder<Item, JewelItem> registerJewel(DeferredHolder<Jewel, ? extends Jewel> jewel) {
		return register(jewel.getId().getPath(), () -> new JewelItem(jewel::get, new Item.Properties().stacksTo(1)));
	}

	private static <T extends Item> DeferredHolder<Item, T> register(String name, Supplier<T> item) {
		return DEFERRED_REGISTER.register(name, item);
	}

	public static void register(IEventBus bus) {
		ECBlocks.registerAliases(DEFERRED_REGISTER);
		Jewels.registerAliases(DEFERRED_REGISTER);
		DEFERRED_REGISTER.addAlias(ElementalCraftApi.createRL("chisel"), ElementalCraftApi.createRL(ChiselItem.NAME_SWIFT_ALLOY));
		DEFERRED_REGISTER.addAlias(ElementalCraftApi.createRL("natural_fire_source_seed"), ElementalCraftApi.createRL("fire_source_seed"));
		DEFERRED_REGISTER.addAlias(ElementalCraftApi.createRL("natural_water_source_seed"), ElementalCraftApi.createRL("water_source_seed"));
		DEFERRED_REGISTER.addAlias(ElementalCraftApi.createRL("natural_earth_source_seed"), ElementalCraftApi.createRL("earth_source_seed"));
		DEFERRED_REGISTER.addAlias(ElementalCraftApi.createRL("natural_air_source_seed"), ElementalCraftApi.createRL("air_source_seed"));
		DEFERRED_REGISTER.register(bus);
	}
}
