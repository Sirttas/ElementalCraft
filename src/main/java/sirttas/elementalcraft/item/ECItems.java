package sirttas.elementalcraft.item;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.CrystalOreBlock;
import sirttas.elementalcraft.block.container.ElementContainerBlock;
import sirttas.elementalcraft.block.container.SmallElementContainerBlock;
import sirttas.elementalcraft.block.container.creative.CreativeElementContainerBlock;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlock;
import sirttas.elementalcraft.block.evaporator.EvaporatorBlock;
import sirttas.elementalcraft.block.extractor.ExtractorBlock;
import sirttas.elementalcraft.block.extractor.improved.ImprovedExtractorBlock;
import sirttas.elementalcraft.block.instrument.binder.BinderBlock;
import sirttas.elementalcraft.block.instrument.binder.improved.ImprovedBinderBlock;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerBlock;
import sirttas.elementalcraft.block.instrument.infuser.InfuserBlock;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberBlock;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceBlock;
import sirttas.elementalcraft.block.instrument.io.firefurnace.blast.FireBlastFurnaceBlock;
import sirttas.elementalcraft.block.instrument.io.mill.AirMillBlock;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlock;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;
import sirttas.elementalcraft.block.shrine.breeding.BreedingShrineBlock;
import sirttas.elementalcraft.block.shrine.enderlock.EnderLockShrineBlock;
import sirttas.elementalcraft.block.shrine.firepylon.FirePylonBlock;
import sirttas.elementalcraft.block.shrine.grove.GroveShrineBlock;
import sirttas.elementalcraft.block.shrine.growth.GrowthShrineBlock;
import sirttas.elementalcraft.block.shrine.harvest.HarvestShrineBlock;
import sirttas.elementalcraft.block.shrine.lava.LavaShrineBlock;
import sirttas.elementalcraft.block.shrine.ore.OreShrineBlock;
import sirttas.elementalcraft.block.shrine.overload.OverloadShrineBlock;
import sirttas.elementalcraft.block.shrine.sweet.SweetShrineBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.CapacityShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.EfficiencyShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.OptimizationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.RangeShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.StrengthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.AccelerationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.FortuneShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.NectarShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.ProtectionShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.SilkTouchShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BonelessGrowthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.MysticalGroveShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.PickupShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.PlantingShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.StemPollinationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.vacuum.VacuumShrineBlock;
import sirttas.elementalcraft.block.solarsynthesizer.SolarSynthesizerBlock;
import sirttas.elementalcraft.block.source.SourceBlock;
import sirttas.elementalcraft.block.spelldesk.SpellDeskBlock;
import sirttas.elementalcraft.item.chisel.ChiselItem;
import sirttas.elementalcraft.item.elemental.ElementalItem;
import sirttas.elementalcraft.item.elemental.LenseItem;
import sirttas.elementalcraft.item.elemental.ShardItem;
import sirttas.elementalcraft.item.holder.ElementHolderItem;
import sirttas.elementalcraft.item.holder.PureElementHolderItem;
import sirttas.elementalcraft.item.pipe.CoverFrameItem;
import sirttas.elementalcraft.item.pureore.PureOreItem;
import sirttas.elementalcraft.item.receptacle.EmptyReceptacleItem;
import sirttas.elementalcraft.item.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.item.receptacle.ReceptacleItem;
import sirttas.elementalcraft.item.receptacle.improved.EmptyImprovedReceptacleItem;
import sirttas.elementalcraft.item.receptacle.improved.ImprovedReceptacleItem;
import sirttas.elementalcraft.item.rune.RuneItem;
import sirttas.elementalcraft.item.rune.RuneModel;
import sirttas.elementalcraft.item.spell.FocusItem;
import sirttas.elementalcraft.item.spell.ScrollItem;
import sirttas.elementalcraft.item.spell.StaffItem;
import sirttas.elementalcraft.item.spell.book.SpellBookItem;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.spell.SpellHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECItems {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + FocusItem.NAME) public static final FocusItem FOCUS = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + StaffItem.NAME) public static final FocusItem STAFF = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ScrollItem.NAME) public static final ScrollItem SCROLL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SpellBookItem.NAME) public static final SpellBookItem SPELL_BOOK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ReceptacleItem.NAME) public static final ReceptacleItem RECEPTACLE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + EmptyReceptacleItem.NAME) public static final EmptyReceptacleItem EMPTY_RECEPTACLE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ImprovedReceptacleItem.NAME) public static final ImprovedReceptacleItem RECEPTACLE_IMPROVED = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + EmptyImprovedReceptacleItem.NAME) public static final EmptyImprovedReceptacleItem EMPTY_RECEPTACLE_IMPROVED = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementHolderItem.NAME_FIRE) public static final ElementHolderItem FIRE_HOLDER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementHolderItem.NAME_WATER) public static final ElementHolderItem WATER_HOLDER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementHolderItem.NAME_EARTH) public static final ElementHolderItem EARTH_HOLDER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementHolderItem.NAME_AIR) public static final ElementHolderItem AIR_HOLDER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PureElementHolderItem.NAME + "_core") public static final Item PURE_HOLDER_CORE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PureElementHolderItem.NAME) public static final PureElementHolderItem PURE_HOLDER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PureOreItem.NAME) public static final PureOreItem PURE_ORE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + RuneItem.NAME) public static final RuneItem RUNE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ChiselItem.NAME) public static final ChiselItem CHISEL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + CoverFrameItem.NAME) public static final CoverFrameItem COVER_FRAM = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":elementpipe_priority") public static final Item PIPE_PRIORITY = null;

	@ObjectHolder("patchouli:guide_book") public static final Item ELEMENTOPEDIA = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":inert_crystal") public static final ECItem INERT_CRYSTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":contained_crystal") public static final ECItem CONTAINED_CRYSTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":strongly_contained_crystal") public static final ECItem STRONGLY_CONTAINED_CRYSTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":purecrystal") public static final ECItem PURE_CRYSTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":drenched_iron_ingot") public static final ECItem DRENCHED_IRON_INGOT = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":drenched_iron_nugget") public static final ECItem DRENCHED_IRON_NUGGET = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":swift_alloy_ingot") public static final ECItem SWIFT_ALLOY_INGOT = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":swift_alloy_nugget") public static final ECItem SWIFT_ALLOY_NUGGET = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":hardened_handle") public static final ECItem HARDENED_HANDLE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":shrinebase") public static final ECItem SHRINE_BASE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":fireite_ingot") public static final ECItem FIREITE_INGOT = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":fireite_nugget") public static final ECItem FIREITE_NUGGET = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":air_silk") public static final ECItem AIR_SILK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":shrine_upgrade_core") public static final ECItem SHRINE_UPGRADE_CORE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":scroll_paper") public static final ECItem SCROLL_PAPER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":springaline_shard") public static final ECItem SPRINGALINE_SHARD = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":solar_prism") public static final ECItem SOLAR_PRISM = null;

	@ObjectHolder(ElementalCraftApi.MODID + ":firecrystal") public static final ElementalItem FIRE_CRYSTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":watercrystal") public static final ElementalItem WATER_CRYSTAL  = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":earthcrystal") public static final ElementalItem EARTH_CRYSTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":aircrystal") public static final ElementalItem AIR_CRYSTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ShardItem.NAME_FIRE) public static final ShardItem FIRE_SHARD = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ShardItem.NAME_WATER) public static final ShardItem WATER_SHARD = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ShardItem.NAME_EARTH) public static final ShardItem EARTH_SHARD = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ShardItem.NAME_AIR) public static final ShardItem AIR_SHARD = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ShardItem.NAME_FIRE_POWERFUL) public static final ShardItem POWERFUL_FIRE_SHARD = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ShardItem.NAME_WATER_POWERFUL) public static final ShardItem POWERFUL_WATER_SHARD = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ShardItem.NAME_EARTH_POWERFUL) public static final ShardItem POWERFUL_EARTH_SHARD = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ShardItem.NAME_AIR_POWERFUL) public static final ShardItem POWERFUL_AIR_SHARD = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":crude_fire_gem") public static final ElementalItem CRUDE_FIRE_GEM = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":crude_water_gem") public static final ElementalItem CRUDE_WATER_GEM = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":crude_earth_gem") public static final ElementalItem CRUDE_EARTH_GEM = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":crude_air_gem") public static final ElementalItem CRUDE_AIR_GEM = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":fine_fire_gem") public static final ElementalItem FINE_FIRE_GEM = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":fine_water_gem") public static final ElementalItem FINE_WATER_GEM = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":fine_earth_gem") public static final ElementalItem FINE_EARTH_GEM = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":fine_air_gem") public static final ElementalItem FINE_AIR_GEM = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":pristine_fire_gem") public static final ElementalItem PRISTINE_FIRE_GEM = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":pristine_water_gem") public static final ElementalItem PRISTINE_WATER_GEM = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":pristine_earth_gem") public static final ElementalItem PRISTINE_EARTH_GEM = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":pristine_air_gem") public static final ElementalItem PRISTINE_AIR_GEM = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + LenseItem.NAME_FIRE) public static final LenseItem FIRE_LENSE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + LenseItem.NAME_WATER) public static final LenseItem WATER_LENSE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + LenseItem.NAME_EARTH) public static final LenseItem EARTH_LENSE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + LenseItem.NAME_AIR) public static final LenseItem AIR_LENSE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":minor_rune_slate") public static final ECItem MINOR_RUNE_SLATE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":rune_slate") public static final ECItem RUNE_SLATE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":major_rune_slate") public static final ECItem MAJOR_RUNE_SLATE = null;
	
	// BLOCKS
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SmallElementContainerBlock.NAME) public static final Item TANK_SMALL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementContainerBlock.NAME) public static final Item TANK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ReservoirBlock.NAME_FIRE) public static final Item FIRE_RESERVOIR = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ReservoirBlock.NAME_WATER) public static final Item WATER_RESERVOIR = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ReservoirBlock.NAME_EARTH) public static final Item EARTH_RESERVOIR = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ReservoirBlock.NAME_AIR) public static final Item AIR_RESERVOIR = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + CreativeElementContainerBlock.NAME) public static final Item TANK_CREATIVE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ExtractorBlock.NAME) public static final Item EXTRACTOR = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ImprovedExtractorBlock.NAME) public static final Item EXTRACTOR_IMPROVED = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + EvaporatorBlock.NAME) public static final Item EVAPORATOR = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SolarSynthesizerBlock.NAME) public static final Item SOLAR_SYNTHESIZER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + InfuserBlock.NAME) public static final Item INFUSER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + BinderBlock.NAME) public static final Item BINDER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ImprovedBinderBlock.NAME) public static final Item BINDER_IMPROVED = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + CrystallizerBlock.NAME) public static final Item CRYSTALLIZER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + InscriberBlock.NAME) public static final Item INSCRIBER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + AirMillBlock.NAME) public static final Item AIR_MILL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SpellDeskBlock.NAME) public static final Item SPELL_DESK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PedestalBlock.NAME_FIRE) public static final Item FIRE_PEDESTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PedestalBlock.NAME_WATER) public static final Item WATER_PEDESTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PedestalBlock.NAME_EARTH) public static final Item EARTH_PEDESTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PedestalBlock.NAME_AIR) public static final Item AIR_PEDESTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PureInfuserBlock.NAME) public static final Item PURE_INFUSER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + FireFurnaceBlock.NAME) public static final Item FIRE_FURNACE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + FireBlastFurnaceBlock.NAME) public static final Item FIRE_BLAST_FURNACE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PurifierBlock.NAME) public static final Item PURIFIER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementPipeBlock.NAME_IMPAIRED) public static final Item PIPE_IMPAIRED = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementPipeBlock.NAME) public static final Item PIPE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementPipeBlock.NAME_IMPROVED) public static final Item PIPE_IMPROVED = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + RetrieverBlock.NAME) public static final Item RETRIEVER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + FirePylonBlock.NAME) public static final Item FIRE_PYLON = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + VacuumShrineBlock.NAME) public static final Item VACUUM_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + GrowthShrineBlock.NAME) public static final Item GROWTH_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + HarvestShrineBlock.NAME) public static final Item HARVEST_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + LavaShrineBlock.NAME) public static final Item LAVA_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + OreShrineBlock.NAME) public static final Item ORE_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + OverloadShrineBlock.NAME) public static final Item OVERLOAD_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SweetShrineBlock.NAME) public static final Item SWEET_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + EnderLockShrineBlock.NAME) public static final Item ENDER_LOC_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + BreedingShrineBlock.NAME) public static final Item BREEDING_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + GroveShrineBlock.NAME) public static final Item GROVE_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + AccelerationShrineUpgradeBlock.NAME) public static final Item ACCELERATION_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + RangeShrineUpgradeBlock.NAME) public static final Item RANGE_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + CapacityShrineUpgradeBlock.NAME) public static final Item CAPACITY_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + EfficiencyShrineUpgradeBlock.NAME) public static final Item EFFICIENCY_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + StrengthShrineUpgradeBlock.NAME) public static final Item STRENGTH_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + OptimizationShrineUpgradeBlock.NAME) public static final Item OPTIMIZATION_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + FortuneShrineUpgradeBlock.NAME) public static final Item FORTUNE_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SilkTouchShrineUpgradeBlock.NAME) public static final Item SILK_TOUCH_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PlantingShrineUpgradeBlock.NAME) public static final Item PLANTING_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + BonelessGrowthShrineUpgradeBlock.NAME) public static final Item BONELESS_GROWTH_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PickupShrineUpgradeBlock.NAME) public static final Item PICKUP_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + NectarShrineUpgradeBlock.NAME) public static final Item NECTOR_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + MysticalGroveShrineUpgradeBlock.NAME) public static final Item MYSTICAL_GROVE_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + StemPollinationShrineUpgradeBlock.NAME) public static final Item STEM_POLLINATION_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ProtectionShrineUpgradeBlock.NAME) public static final Item PROTECTION_SHRINE_UPGRADE = null;


	@ObjectHolder(ElementalCraftApi.MODID + ":" + SourceBlock.NAME) public static final Item SOURCE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + CrystalOreBlock.NAME) public static final Item CRYSTAL_ORE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":whiterock") public static final Item WHITE_ROCK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":whiterock_slab") public static final Item WHITE_ROCK_SLAB = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":whiterock_stairs") public static final Item WHITE_ROCK_STAIRS = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":whiterock_wall") public static final Item WHITE_ROCK_WALL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":purerock") public static final Item PURE_ROCK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":purerock_slab") public static final Item PURE_ROCK_SLAB = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":purerock_stairs") public static final Item PURE_ROCK_STAIR = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":purerock_wall") public static final Item PURE_ROCK_WALL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":burnt_glass") public static final Item BURNT_GLASS = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":burnt_glass_pane") public static final Item BURNT_GLASS_PANE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":drenched_iron_block") public static final Item DRENCHED_IRON_BLOCK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":swift_alloy_block") public static final Item SWIFT_ALLOY_BLOCK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":fireite_block") public static final Item FIREITE_BLOCK = null;

	private ECItems() {

	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		RegistryHelper.register(registry, new FocusItem(), FocusItem.NAME);
		RegistryHelper.register(registry, new StaffItem(), StaffItem.NAME);
		RegistryHelper.register(registry, new ScrollItem(), ScrollItem.NAME);
		RegistryHelper.register(registry, new SpellBookItem(), SpellBookItem.NAME);
		RegistryHelper.register(registry, new ReceptacleItem(), ReceptacleItem.NAME);
		RegistryHelper.register(registry, new EmptyReceptacleItem(), EmptyReceptacleItem.NAME);
		RegistryHelper.register(registry, new ImprovedReceptacleItem(), ImprovedReceptacleItem.NAME);
		RegistryHelper.register(registry, new EmptyImprovedReceptacleItem(), EmptyImprovedReceptacleItem.NAME);
		RegistryHelper.register(registry, new ElementHolderItem(ElementType.FIRE), ElementHolderItem.NAME_FIRE);
		RegistryHelper.register(registry, new ElementHolderItem(ElementType.WATER), ElementHolderItem.NAME_WATER);
		RegistryHelper.register(registry, new ElementHolderItem(ElementType.EARTH), ElementHolderItem.NAME_EARTH);
		RegistryHelper.register(registry, new ElementHolderItem(ElementType.AIR), ElementHolderItem.NAME_AIR);
		RegistryHelper.register(registry, new ECItem(), PureElementHolderItem.NAME + "_core");
		RegistryHelper.register(registry, new PureElementHolderItem(), PureElementHolderItem.NAME);
		RegistryHelper.register(registry, new PureOreItem(), PureOreItem.NAME);
		RegistryHelper.register(registry, new RuneItem(), RuneItem.NAME);
		RegistryHelper.register(registry, new ChiselItem(), ChiselItem.NAME);
		RegistryHelper.register(registry, new CoverFrameItem(), CoverFrameItem.NAME);
		RegistryHelper.register(registry, new ECItem(), "elementpipe_priority");
		
		RegistryHelper.register(registry, new ECItem(), "inert_crystal");
		RegistryHelper.register(registry, new ECItem(), "contained_crystal");
		RegistryHelper.register(registry, new ECItem(), "strongly_contained_crystal");
		RegistryHelper.register(registry, new ECItem().setEffect(true), "purecrystal");
		RegistryHelper.register(registry, new ECItem(), "drenched_iron_ingot");
		RegistryHelper.register(registry, new ECItem(), "drenched_iron_nugget");
		RegistryHelper.register(registry, new ECItem(), "swift_alloy_ingot");
		RegistryHelper.register(registry, new ECItem(), "swift_alloy_nugget");
		RegistryHelper.register(registry, new ECItem(), "hardened_handle");
		RegistryHelper.register(registry, new ECItem(), "shrinebase");
		RegistryHelper.register(registry, new ECItem(ECProperties.Items.FIREITE), "fireite_ingot");
		RegistryHelper.register(registry, new ECItem(ECProperties.Items.FIREITE), "fireite_nugget");
		RegistryHelper.register(registry, new ECItem(), "air_silk");
		RegistryHelper.register(registry, new ECItem(), "shrine_upgrade_core");
		RegistryHelper.register(registry, new ECItem(), "scroll_paper");
		RegistryHelper.register(registry, new ECItem(), "springaline_shard");
		RegistryHelper.register(registry, new ECItem(), "solar_prism");

		RegistryHelper.register(registry, new ElementalItem(ElementType.FIRE), "firecrystal");
		RegistryHelper.register(registry, new ElementalItem(ElementType.WATER), "watercrystal");
		RegistryHelper.register(registry, new ElementalItem(ElementType.EARTH), "earthcrystal");
		RegistryHelper.register(registry, new ElementalItem(ElementType.AIR), "aircrystal");
		RegistryHelper.register(registry, new ShardItem(ElementType.FIRE), ShardItem.NAME_FIRE);
		RegistryHelper.register(registry, new ShardItem(ElementType.WATER), ShardItem.NAME_WATER);
		RegistryHelper.register(registry, new ShardItem(ElementType.EARTH), ShardItem.NAME_EARTH);
		RegistryHelper.register(registry, new ShardItem(ElementType.AIR), ShardItem.NAME_AIR);
		RegistryHelper.register(registry, new ShardItem(ElementType.FIRE, 9), ShardItem.NAME_FIRE_POWERFUL);
		RegistryHelper.register(registry, new ShardItem(ElementType.WATER, 9), ShardItem.NAME_WATER_POWERFUL);
		RegistryHelper.register(registry, new ShardItem(ElementType.EARTH, 9), ShardItem.NAME_EARTH_POWERFUL);
		RegistryHelper.register(registry, new ShardItem(ElementType.AIR, 9), ShardItem.NAME_AIR_POWERFUL);
		RegistryHelper.register(registry, new ElementalItem(ElementType.FIRE), "crude_fire_gem");
		RegistryHelper.register(registry, new ElementalItem(ElementType.WATER), "crude_water_gem");
		RegistryHelper.register(registry, new ElementalItem(ElementType.EARTH), "crude_earth_gem");
		RegistryHelper.register(registry, new ElementalItem(ElementType.AIR), "crude_air_gem");
		RegistryHelper.register(registry, new ElementalItem(ElementType.FIRE), "fine_fire_gem");
		RegistryHelper.register(registry, new ElementalItem(ElementType.WATER), "fine_water_gem");
		RegistryHelper.register(registry, new ElementalItem(ElementType.EARTH), "fine_earth_gem");
		RegistryHelper.register(registry, new ElementalItem(ElementType.AIR), "fine_air_gem");
		RegistryHelper.register(registry, new ElementalItem(ElementType.FIRE), "pristine_fire_gem");
		RegistryHelper.register(registry, new ElementalItem(ElementType.WATER), "pristine_water_gem");
		RegistryHelper.register(registry, new ElementalItem(ElementType.EARTH), "pristine_earth_gem");
		RegistryHelper.register(registry, new ElementalItem(ElementType.AIR), "pristine_air_gem");
		RegistryHelper.register(registry, new LenseItem(ElementType.FIRE), LenseItem.NAME_FIRE);
		RegistryHelper.register(registry, new LenseItem(ElementType.WATER), LenseItem.NAME_WATER);
		RegistryHelper.register(registry, new LenseItem(ElementType.EARTH), LenseItem.NAME_EARTH);
		RegistryHelper.register(registry, new LenseItem(ElementType.AIR), LenseItem.NAME_AIR);
		RegistryHelper.register(registry, new ECItem(), "minor_rune_slate");
		RegistryHelper.register(registry, new ECItem(), "rune_slate");
		RegistryHelper.register(registry, new ECItem(), "major_rune_slate");
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void replaceModels(ModelBakeEvent event) {
		ModelResourceLocation key = new ModelResourceLocation(ElementalCraft.createRL(RuneItem.NAME), "inventory");
		BakedModel oldModel = event.getModelRegistry().get(key);
		
		if (oldModel != null) {
			event.getModelRegistry().put(key, new RuneModel(oldModel));
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerItemColors(ColorHandlerEvent.Item event) {
		event.getItemColors().register((s, l) -> l == 0 ? -1 : ReceptacleHelper.getElementType(s).getColor(), RECEPTACLE, RECEPTACLE_IMPROVED);
		event.getItemColors().register((s, l) -> l == 0 ? -1 : ElementalCraft.PURE_ORE_MANAGER.getColor(s), PURE_ORE);
		event.getItemColors().register((s, l) -> l == 0 ? -1 : SpellHelper.getSpell(s).getColor(), SCROLL);
		event.getItemColors().register((s, l) -> l == 0 ? -1 : ((ElementHolderItem) s.getItem()).getElementType().getColor(), 
				FIRE_HOLDER, WATER_HOLDER, EARTH_HOLDER, AIR_HOLDER);
	}
}
