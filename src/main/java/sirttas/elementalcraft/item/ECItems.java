package sirttas.elementalcraft.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;
import sirttas.elementalcraft.block.shrine.ShrineItem;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.bonelessgrowth.BonelessGrowthShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.bud.BudShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.crystalgrowth.CrystalGrowthShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.crystalharvest.CrystalHarvestShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.filling.FillingShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.fortune.FortuneShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.mysticalgrove.MysticalGroveShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.nectar.NectarShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.pickup.PickupShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.planting.PlantingShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.protection.ProtectionShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.silktouch.SilkTouchShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.stempollination.StemPollinationShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeItem;
import sirttas.elementalcraft.block.shrine.upgrade.vortex.VortexShrineUpgradeItem;
import sirttas.elementalcraft.block.source.SourceBlock;
import sirttas.elementalcraft.block.source.SourceElementStorage;
import sirttas.elementalcraft.block.source.trait.holder.ItemSourceTraitHolder;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.element.ElementAmounts;
import sirttas.elementalcraft.item.chisel.ChiselItem;
import sirttas.elementalcraft.item.chisel.ChiselToolMaterials;
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
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.spell.SpellList;
import sirttas.elementalcraft.spell.Spells;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class ECItems {
	private static final DeferredRegister<Item> DEFERRED_REGISTER = DeferredRegister.create(Registries.ITEM, ElementalCraftApi.MODID);

	public static final DeferredHolder<Item, FocusItem> FOCUS = register(FocusItem.NAME, FocusItem::new, () -> new Item.Properties()
			.stacksTo(1)
			.component(ECDataComponents.SPELL, Spells.NONE)
			.component(ECDataComponents.SPELL_LIST, SpellList.EMPTY));
	public static final DeferredHolder<Item, StaffItem> STAFF = register(StaffItem.NAME, StaffItem::new, () -> new Item.Properties()
			.durability(2252)
			.fireResistant()
			.component(ECDataComponents.SPELL, Spells.NONE)
			.component(ECDataComponents.SPELL_LIST, SpellList.EMPTY)
			.attributes(StaffItem.ATTRIBUTE_MODIFIERS));
	public static final DeferredHolder<Item, ScrollItem> SCROLL = register(ScrollItem.NAME, ScrollItem::new, () -> new Item.Properties()
			.stacksTo(1)
			.component(ECDataComponents.SPELL, Spells.NONE));
	public static final DeferredHolder<Item, SpellBookItem> SPELL_BOOK = register(SpellBookItem.NAME, SpellBookItem::new, () -> new Item.Properties()
			.stacksTo(1)
			.component(ECDataComponents.SPELL_LIST, SpellList.EMPTY));
	public static final DeferredHolder<Item, EmptyReceptacleItem> EMPTY_RECEPTACLE = register(EmptyReceptacleItem.NAME, EmptyReceptacleItem::new);
	public static final DeferredHolder<Item, SourceStabilizerItem> SOURCE_STABILIZER = register(SourceStabilizerItem.NAME, SourceStabilizerItem::new);
	public static final DeferredHolder<Item, SourceAnalysisGlassItem> SOURCE_ANALYSIS_GLASS = register(SourceAnalysisGlassItem.NAME, SourceAnalysisGlassItem::new, new Item.Properties()
			.stacksTo(1));
	public static final DeferredHolder<Item, ElementHolderItem> FIRE_HOLDER = register(ElementHolderItem.NAME_FIRE, p -> new ElementHolderItem(ElementType.FIRE, p), () -> new Item.Properties()
			.stacksTo(1)
			.component(ECDataComponents.ELEMENT_AMOUNT, 0));
	public static final DeferredHolder<Item, ElementHolderItem> WATER_HOLDER = register(ElementHolderItem.NAME_WATER, p -> new ElementHolderItem(ElementType.WATER, p), () -> new Item.Properties()
			.stacksTo(1)
			.component(ECDataComponents.ELEMENT_AMOUNT, 0));
	public static final DeferredHolder<Item, ElementHolderItem> EARTH_HOLDER = register(ElementHolderItem.NAME_EARTH, p -> new ElementHolderItem(ElementType.EARTH, p), () -> new Item.Properties()
			.stacksTo(1)
			.component(ECDataComponents.ELEMENT_AMOUNT, 0));
	public static final DeferredHolder<Item, ElementHolderItem> AIR_HOLDER = register(ElementHolderItem.NAME_AIR, p -> new ElementHolderItem(ElementType.AIR, p), () -> new Item.Properties()
			.stacksTo(1)
			.component(ECDataComponents.ELEMENT_AMOUNT, 0));
	public static final DeferredHolder<Item, Item> PURE_HOLDER_CORE = registerSimple(PureElementHolderItem.NAME + "_core");
	public static final DeferredHolder<Item, PureElementHolderItem> PURE_HOLDER = register(PureElementHolderItem.NAME, PureElementHolderItem::new, () -> new Item.Properties()
			.stacksTo(1)
			.component(ECDataComponents.ELEMENT_AMOUNTS, ElementAmounts.EMPTY));
	public static final DeferredHolder<Item, PureOreItem> PURE_ORE = register(PureOreItem.NAME, PureOreItem::new);
	public static final DeferredHolder<Item, RuneItem> RUNE = register(RuneItem.NAME, RuneItem::new);
	public static final DeferredHolder<Item, ChiselItem> DRENCHED_IRON_CHISEL = register(ChiselItem.NAME_DRENCHED_IRON, ChiselItem::new, () -> new Item.Properties()
			.tool(ChiselToolMaterials.DRENCHED_IRON, BlockTags.AIR, 1, -2.8F, 0));
	public static final DeferredHolder<Item, ChiselItem> SWIFT_ALLOY_CHISEL = register(ChiselItem.NAME_SWIFT_ALLOY, ChiselItem::new, () -> new Item.Properties()
			.tool(ChiselToolMaterials.SWIFT_ALLOY, BlockTags.AIR, 1, -2.8F, 0));
	public static final DeferredHolder<Item, ChiselItem> FIREITE_CHISEL = register(ChiselItem.NAME_FIREITE, ChiselItem::new, () -> new Item.Properties()
			.tool(ChiselToolMaterials.FIREITE, BlockTags.AIR, 1, -2.8F, 0)
			.fireResistant());
	public static final DeferredHolder<Item, DamageableCraftingElementalItem> ELEMENTAL_FIREFUEL = register("elemental_firefuel", p -> new DamageableCraftingElementalItem(ElementType.FIRE, p), new Item.Properties()
			.stacksTo(1)
			.durability(500));
	public static final DeferredHolder<Item, Item> INERT_CRYSTAL = registerSimple("inert_crystal");
	public static final DeferredHolder<Item, Item> CONTAINED_CRYSTAL = registerSimple("contained_crystal");
	public static final DeferredHolder<Item, Item> STRONGLY_CONTAINED_CRYSTAL = registerSimple("strongly_contained_crystal");
	public static final DeferredHolder<Item, Item> PURE_CRYSTAL = registerSimple("purecrystal", () -> new Item.Properties()
			.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
	public static final DeferredHolder<Item, Item> DRENCHED_IRON_INGOT = registerSimple("drenched_iron_ingot");
	public static final DeferredHolder<Item, Item> DRENCHED_IRON_NUGGET = registerSimple("drenched_iron_nugget");
	public static final DeferredHolder<Item, Item> SWIFT_ALLOY_INGOT = registerSimple("swift_alloy_ingot");
	public static final DeferredHolder<Item, Item> SWIFT_ALLOY_NUGGET = registerSimple("swift_alloy_nugget");
	public static final DeferredHolder<Item, Item> HARDENED_HANDLE = registerSimple("hardened_handle");
	public static final DeferredHolder<Item, Item> DRENCHED_SAW_BLADE = registerSimple("drenched_saw_blade");
	public static final DeferredHolder<Item, Item> SHRINE_BASE = registerSimple("shrinebase");
	public static final DeferredHolder<Item, Item> FIREITE_INGOT = registerSimple("fireite_ingot", new Item.Properties()
			.fireResistant());
	public static final DeferredHolder<Item, Item> FIREITE_NUGGET = registerSimple("fireite_nugget", new Item.Properties()
			.fireResistant());
	public static final DeferredHolder<Item, Item> AIR_SILK = registerSimple("air_silk");
	public static final DeferredHolder<Item, Item> SHRINE_UPGRADE_CORE = registerSimple("shrine_upgrade_core");
	public static final DeferredHolder<Item, Item> ADVANCED_SHRINE_UPGRADE_CORE = registerSimple("advanced_shrine_upgrade_core");
	public static final DeferredHolder<Item, Item> SCROLL_PAPER = registerSimple("scroll_paper");
	public static final DeferredHolder<Item, Item> SPRINGALINE_SHARD = registerSimple("springaline_shard");
	public static final DeferredHolder<Item, Item> SOLAR_PRISM = registerSimple("solar_prism");
	public static final DeferredHolder<Item, ElementalItem> FIRE_CRYSTAL = register("firecrystal", p -> new ElementalItem(ElementType.FIRE, p));
	public static final DeferredHolder<Item, ElementalItem> WATER_CRYSTAL = register("watercrystal", p -> new ElementalItem(ElementType.WATER, p));
	public static final DeferredHolder<Item, ElementalItem> EARTH_CRYSTAL = register("earthcrystal", p -> new ElementalItem(ElementType.EARTH, p));
	public static final DeferredHolder<Item, ElementalItem> AIR_CRYSTAL = register("aircrystal", p -> new ElementalItem(ElementType.AIR, p));
	public static final DeferredHolder<Item, ElementalItem> CRUDE_FIRE_GEM = register("crude_fire_gem", p -> new ElementalItem(ElementType.FIRE, p));
	public static final DeferredHolder<Item, ElementalItem> CRUDE_WATER_GEM = register("crude_water_gem", p -> new ElementalItem(ElementType.WATER, p));
	public static final DeferredHolder<Item, ElementalItem> CRUDE_EARTH_GEM = register("crude_earth_gem", p -> new ElementalItem(ElementType.EARTH, p));
	public static final DeferredHolder<Item, ElementalItem> CRUDE_AIR_GEM = register("crude_air_gem", p -> new ElementalItem(ElementType.AIR, p));
	public static final DeferredHolder<Item, ElementalItem> FINE_FIRE_GEM = register("fine_fire_gem", p -> new ElementalItem(ElementType.FIRE, p));
	public static final DeferredHolder<Item, ElementalItem> FINE_WATER_GEM = register("fine_water_gem", p -> new ElementalItem(ElementType.WATER, p));
	public static final DeferredHolder<Item, ElementalItem> FINE_EARTH_GEM = register("fine_earth_gem", p -> new ElementalItem(ElementType.EARTH, p));
	public static final DeferredHolder<Item, ElementalItem> FINE_AIR_GEM = register("fine_air_gem", p -> new ElementalItem(ElementType.AIR, p));
	public static final DeferredHolder<Item, ElementalItem> PRISTINE_FIRE_GEM = register("pristine_fire_gem", p -> new ElementalItem(ElementType.FIRE, p));
	public static final DeferredHolder<Item, ElementalItem> PRISTINE_WATER_GEM = register("pristine_water_gem", p -> new ElementalItem(ElementType.WATER, p));
	public static final DeferredHolder<Item, ElementalItem> PRISTINE_EARTH_GEM = register("pristine_earth_gem", p -> new ElementalItem(ElementType.EARTH, p));
	public static final DeferredHolder<Item, ElementalItem> PRISTINE_AIR_GEM = register("pristine_air_gem", p -> new ElementalItem(ElementType.AIR, p));
	public static final DeferredHolder<Item, Item> PRISTINE_SHARD = registerSimple("pristine_shard");
    public static final DeferredHolder<Item, ElementalItem> FIRE_SOURCE_SEED = register("fire_source_seed", p -> new ElementalItem(ElementType.FIRE, p));
    public static final DeferredHolder<Item, ElementalItem> WATER_SOURCE_SEED = register("water_source_seed", p -> new ElementalItem(ElementType.WATER, p));
    public static final DeferredHolder<Item, ElementalItem> EARTH_SOURCE_SEED = register("earth_source_seed", p -> new ElementalItem(ElementType.EARTH, p));
    public static final DeferredHolder<Item, ElementalItem> AIR_SOURCE_SEED = register("air_source_seed", p -> new ElementalItem(ElementType.AIR, p));
	public static final DeferredHolder<Item, DamageableCraftingElementalItem> FIRE_LENS = register("fire_lens", p -> new DamageableCraftingElementalItem(ElementType.FIRE, p), new Item.Properties()
			.stacksTo(1)
			.durability(1500));
	public static final DeferredHolder<Item, DamageableCraftingElementalItem> AIR_MILL = register("air_mill", p -> new DamageableCraftingElementalItem(ElementType.AIR, p), new Item.Properties()
			.stacksTo(1)
			.durability(3000));
	public static final DeferredHolder<Item, Item> MINOR_RUNE_SLATE = registerSimple("minor_rune_slate");
	public static final DeferredHolder<Item, Item> RUNE_SLATE = registerSimple("rune_slate");
	public static final DeferredHolder<Item, Item> MAJOR_RUNE_SLATE = registerSimple("major_rune_slate");
	public static final DeferredHolder<Item, Item> UNSET_JEWEL = registerSimple("unset_jewel");

    // Pipe Upgrade
    public static final DeferredHolder<Item, CoverFrameItem> COVER_FRAME = register(CoverFrameItem.NAME, CoverFrameItem::new);
    public static final DeferredHolder<Item, PipeUpgradeItem> ELEMENT_PUMP = registerPipeUpgrade(PipeUpgradeTypes.ELEMENT_PUMP);
    public static final DeferredHolder<Item, PipeUpgradeItem> PIPE_PRIORITY_RINGS = registerPipeUpgrade(PipeUpgradeTypes.PIPE_PRIORITY_RINGS);
    public static final DeferredHolder<Item, PipeUpgradeItem> ELEMENT_VALVE = registerPipeUpgrade(PipeUpgradeTypes.ELEMENT_VALVE);
    public static final DeferredHolder<Item, PipeUpgradeItem> ELEMENT_BEAM = registerPipeUpgrade(PipeUpgradeTypes.ELEMENT_BEAM);

    // Jewels
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

    // Blocks
    public static final DeferredHolder<Item, BlockItem> FIREITE_BLOCK = registerBlock(ECBlocks.FIREITE_BLOCK, BlockItem::new, new Item.Properties()
            .fireResistant());
    public static final DeferredHolder<Item, AirMillBlockItem> AIR_MILL_GRINDSTONE = registerBlock(ECBlocks.AIR_MILL_GRINDSTONE, AirMillBlockItem::new);
    public static final DeferredHolder<Item, AirMillBlockItem> AIR_MILL_WOOD_SAW = registerBlock(ECBlocks.AIR_MILL_WOOD_SAW, AirMillBlockItem::new);
    public static final DeferredHolder<Item, AirMillBlockItem> AIR_MILL_SYNTHESIZER = registerBlock(ECBlocks.AIR_MILL_SYNTHESIZER, AirMillBlockItem::new);
    public static final DeferredHolder<Item, FortuneShrineUpgradeItem> FORTUNE_SHRINE_UPGRADE = registerBlock(ECBlocks.FORTUNE_SHRINE_UPGRADE, FortuneShrineUpgradeItem::new);
    public static final DeferredHolder<Item, FortuneShrineUpgradeItem> GREATER_FORTUNE_SHRINE_UPGRADE = registerBlock(ECBlocks.GREATER_FORTUNE_SHRINE_UPGRADE, FortuneShrineUpgradeItem::new);
    public static final DeferredHolder<Item, SilkTouchShrineUpgradeItem> SILK_TOUCH_SHRINE_UPGRADE = registerBlock(ECBlocks.SILK_TOUCH_SHRINE_UPGRADE, SilkTouchShrineUpgradeItem::new);
    public static final DeferredHolder<Item, PlantingShrineUpgradeItem> PLANTING_SHRINE_UPGRADE = registerBlock(ECBlocks.PLANTING_SHRINE_UPGRADE, PlantingShrineUpgradeItem::new);
    public static final DeferredHolder<Item, BonelessGrowthShrineUpgradeItem> BONELESS_GROWTH_SHRINE_UPGRADE = registerBlock(ECBlocks.BONELESS_GROWTH_SHRINE_UPGRADE, BonelessGrowthShrineUpgradeItem::new);
    public static final DeferredHolder<Item, PickupShrineUpgradeItem> PICKUP_SHRINE_UPGRADE = registerBlock(ECBlocks.PICKUP_SHRINE_UPGRADE, PickupShrineUpgradeItem::new);
    public static final DeferredHolder<Item, VortexShrineUpgradeItem> VORTEX_SHRINE_UPGRADE = registerBlock(ECBlocks.VORTEX_SHRINE_UPGRADE, VortexShrineUpgradeItem::new);
    public static final DeferredHolder<Item, NectarShrineUpgradeItem> NECTAR_SHRINE_UPGRADE = registerBlock(ECBlocks.NECTAR_SHRINE_UPGRADE, NectarShrineUpgradeItem::new);
    public static final DeferredHolder<Item, MysticalGroveShrineUpgradeItem> MYSTICAL_GROVE_SHRINE_UPGRADE = registerBlock(ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE, MysticalGroveShrineUpgradeItem::new);
    public static final DeferredHolder<Item, StemPollinationShrineUpgradeItem> STEM_POLLINATION_SHRINE_UPGRADE = registerBlock(ECBlocks.STEM_POLLINATION_SHRINE_UPGRADE, StemPollinationShrineUpgradeItem::new);
    public static final DeferredHolder<Item, ProtectionShrineUpgradeItem> PROTECTION_SHRINE_UPGRADE = registerBlock(ECBlocks.PROTECTION_SHRINE_UPGRADE, ProtectionShrineUpgradeItem::new);
    public static final DeferredHolder<Item, FillingShrineUpgradeItem> FILLING_SHRINE_UPGRADE = registerBlock(ECBlocks.FILLING_SHRINE_UPGRADE, FillingShrineUpgradeItem::new);
    public static final DeferredHolder<Item, BudShrineUpgradeItem> SPRINGALINE_SHRINE_UPGRADE = registerBlock(ECBlocks.SPRINGALINE_SHRINE_UPGRADE, (b, p) -> new BudShrineUpgradeItem(b, "tooltip.elementalcraft.shrine_upgrade.springaline", p));
    public static final DeferredHolder<Item, BudShrineUpgradeItem> CERTUS_QUARTZ_SHRINE_UPGRADE = registerBlock(ECBlocks.CERTUS_QUARTZ_SHRINE_UPGRADE, (b, p) -> new BudShrineUpgradeItem(b, "tooltip.elementalcraft.shrine_upgrade.certus_quartz", p));
    public static final DeferredHolder<Item, CrystalHarvestShrineUpgradeItem> CRYSTAL_HARVEST_SHRINE_UPGRADE = registerBlock(ECBlocks.CRYSTAL_HARVEST_SHRINE_UPGRADE, CrystalHarvestShrineUpgradeItem::new);
    public static final DeferredHolder<Item, CrystalGrowthShrineUpgradeItem> CRYSTAL_GROWTH_SHRINE_UPGRADE = registerBlock(ECBlocks.CRYSTAL_GROWTH_SHRINE_UPGRADE, CrystalGrowthShrineUpgradeItem::new);
    public static final DeferredHolder<Item, TranslocationShrineUpgradeItem> TRANSLOCATION_SHRINE_UPGRADE = registerBlock(ECBlocks.TRANSLOCATION_SHRINE_UPGRADE, TranslocationShrineUpgradeItem::new);
    public static final DeferredHolder<Item, BlockItem> SPRINGALINE_CLUSTER = registerBlock(ECBlocks.SPRINGALINE_CLUSTER);
    public static final DeferredHolder<Item, BlockItem> LARGE_SPRINGALINE_BUD = registerBlock(ECBlocks.LARGE_SPRINGALINE_BUD);
    public static final DeferredHolder<Item, BlockItem> MEDIUM_SPRINGALINE_BUD = registerBlock(ECBlocks.MEDIUM_SPRINGALINE_BUD);
    public static final DeferredHolder<Item, BlockItem> SMALL_SPRINGALINE_BUD = registerBlock(ECBlocks.SMALL_SPRINGALINE_BUD);

	public static final DeferredHolder<Item, SpellEffectItem> REPAIR_HAMMER = register("repair_hammer", SpellEffectItem::new);

	private ECItems() {}

	@SubscribeEvent
	public static void registerBlockItems(RegisterEvent event) {
		if (!event.getRegistryKey().equals(Registries.ITEM)) {
			return;
		}

		var registry = event.getRegistry();

		event.register(Registries.ITEM, r -> {
			// Blocks
			for (var entry : BuiltInRegistries.BLOCK.entrySet()) {
				var block = entry.getValue();
				var registryName = entry.getKey().identifier();
				var key = ResourceKey.create(Registries.ITEM, registryName);

				if (ElementalCraft.owns(registryName) && !registry.containsKey(registryName)) {
					RegistryHelper.register(r, registryName, switch (block) {
						case AbstractElementContainerBlock containerBlock -> new ElementContainerBlockItem(containerBlock, new Item.Properties()
                                .component(ECDataComponents.ELEMENT_TYPE, block instanceof IElementTypeProvider provider ? provider.getElementType() : ElementType.NONE)
                                .component(ECDataComponents.ELEMENT_AMOUNT, 0)
								.useBlockDescriptionPrefix()
								.setId(key));
						case SourceBlock sourceBlock -> new ReceptacleItem(sourceBlock, new Item.Properties()
								.stacksTo(1)
								.component(ECDataComponents.SOURCE_TRAITS_HOLDER, ItemSourceTraitHolder.EMPTY)
								.component(ECDataComponents.ELEMENT_AMOUNT, SourceElementStorage.DEFAULT_CAPACITY)
								.component(ECDataComponents.SOURCE_ANALYZED, false)
								.setId(key));
						case ShrineUpgradeBlock shrineUpgradeBlock -> new ShrineUpgradeItem(shrineUpgradeBlock, new Item.Properties().useBlockDescriptionPrefix().setId(key));
						case AbstractShrineBlock<?> shrineBlock -> new ShrineItem(shrineBlock, new Item.Properties().useBlockDescriptionPrefix().setId(key));
						default -> new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(key));
					});
                    ElementalCraftApi.LOGGER.debug("ElementalCraft Block {} has been automatically registered as item.", registryName);
				}
			}
		});
	}

	private static <T extends PipeUpgrade> DeferredHolder<Item, PipeUpgradeItem> registerPipeUpgrade(DeferredHolder<PipeUpgradeType<?>, PipeUpgradeType<T>> pipeUpgrade) {
		var id = pipeUpgrade.getId();
		return register(pipeUpgrade.getId().getPath(), p -> new PipeUpgradeItem(pipeUpgrade::get, p), new Item.Properties().overrideDescription(PipeUpgradeType.createDescriptionId(id)));
	}

	private static DeferredHolder<Item, JewelItem> registerJewel(DeferredHolder<Jewel, ? extends Jewel> jewel) {
		var id = jewel.getId();
		return register(jewel.getId().getPath(), p -> new JewelItem(jewel::get, p), () -> new Item.Properties()
				.delayedComponent(ECDataComponents.JEWEL.get(), _ -> jewel.get())
				.stacksTo(1)
				.overrideDescription(Jewel.createDescriptionId(id)));
	}

	private static <B extends Block> DeferredHolder<Item, BlockItem> registerBlock(DeferredHolder<Block, ? extends B> block) {
		return registerBlock(block, BlockItem::new, new Item.Properties());
	}

	private static <B extends Block, T extends BlockItem> DeferredHolder<Item, T> registerBlock(DeferredHolder<Block, ? extends B> block, BiFunction<B, Item.Properties, T> item) {
		return registerBlock(block, item, new Item.Properties());
	}

    private static <B extends Block, T extends BlockItem> DeferredHolder<Item, T> registerBlock(DeferredHolder<Block, ? extends B> block, BiFunction<B, Item.Properties, T> item, Item.Properties properties) {
        return register(block.getId().getPath(), p -> item.apply(block.get(), p.useBlockDescriptionPrefix()), properties);
    }

	private static DeferredHolder<Item, Item> registerSimple(String name) {
		return registerSimple(name, new Item.Properties());
	}

	private static DeferredHolder<Item, Item> registerSimple(String name, Item.Properties properties) {
		return register(name, Item::new, properties);
	}

	private static DeferredHolder<Item, Item> registerSimple(String name, Supplier<Item.Properties> properties) {
		return register(name, Item::new, properties);
	}

	private static <T extends Item> DeferredHolder<Item, T> register(String name, Function<Item.Properties, T> itemFactory) {
		return register(name, itemFactory, new Item.Properties());
	}

	private static <T extends Item> DeferredHolder<Item, T> register(String name, Function<Item.Properties, T> itemFactory, Item.Properties properties) {
		return register(name, itemFactory, () -> properties);
	}

	private static <T extends Item> DeferredHolder<Item, T> register(String name, Function<Item.Properties, T> itemFactory, Supplier<Item.Properties> properties) {
		var id = ResourceKey.create(Registries.ITEM, ElementalCraftApi.createRL(name));
		return DEFERRED_REGISTER.register(name, () -> itemFactory.apply(properties.get().setId(id)));
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