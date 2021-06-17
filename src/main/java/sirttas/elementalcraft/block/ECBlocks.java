package sirttas.elementalcraft.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DatagenModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.container.ElementContainerBlock;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.container.SmallElementContainerBlock;
import sirttas.elementalcraft.block.container.creative.CreativeElementContainerBlock;
import sirttas.elementalcraft.block.container.creative.CreativeElementContainerBlockEntity;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlock;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlockEntity;
import sirttas.elementalcraft.block.diffuser.DiffuserBlock;
import sirttas.elementalcraft.block.diffuser.DiffuserBlockEntity;
import sirttas.elementalcraft.block.evaporator.EvaporatorBlock;
import sirttas.elementalcraft.block.evaporator.EvaporatorBlockEntity;
import sirttas.elementalcraft.block.extractor.ExtractorBlock;
import sirttas.elementalcraft.block.extractor.ExtractorBlockEntity;
import sirttas.elementalcraft.block.extractor.improved.ImprovedExtractorBlock;
import sirttas.elementalcraft.block.instrument.binder.BinderBlock;
import sirttas.elementalcraft.block.instrument.binder.BinderBlockEntity;
import sirttas.elementalcraft.block.instrument.binder.improved.ImprovedBinderBlock;
import sirttas.elementalcraft.block.instrument.binder.improved.ImprovedBinderBlockEntity;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerBlock;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerBlockEntity;
import sirttas.elementalcraft.block.instrument.firefurnace.FireFurnaceBlock;
import sirttas.elementalcraft.block.instrument.firefurnace.FireFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.firefurnace.blast.FireBlastFurnaceBlock;
import sirttas.elementalcraft.block.instrument.firefurnace.blast.FireBlastFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.infuser.InfuserBlock;
import sirttas.elementalcraft.block.instrument.infuser.InfuserBlockEntity;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberBlock;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberBlockEntity;
import sirttas.elementalcraft.block.instrument.mill.AirMillBlock;
import sirttas.elementalcraft.block.instrument.mill.AirMillBlockEntity;
import sirttas.elementalcraft.block.instrument.purifier.PurifierBlock;
import sirttas.elementalcraft.block.instrument.purifier.PurifierBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlock;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlockEntity;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlockEntity;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;
import sirttas.elementalcraft.block.shrine.breeding.BreedingShrineBlock;
import sirttas.elementalcraft.block.shrine.breeding.BreedingShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.enderlock.EnderLockShrineBlock;
import sirttas.elementalcraft.block.shrine.enderlock.EnderLockShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.firepylon.FirePylonBlock;
import sirttas.elementalcraft.block.shrine.firepylon.FirePylonBlockEntity;
import sirttas.elementalcraft.block.shrine.grove.GroveShrineBlock;
import sirttas.elementalcraft.block.shrine.grove.GroveShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.growth.GrowthShrineBlock;
import sirttas.elementalcraft.block.shrine.growth.GrowthShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.harvest.HarvestShrineBlock;
import sirttas.elementalcraft.block.shrine.harvest.HarvestShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.lava.LavaShrineBlock;
import sirttas.elementalcraft.block.shrine.lava.LavaShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.ore.OreShrineBlock;
import sirttas.elementalcraft.block.shrine.ore.OreShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.overload.OverloadShrineBlock;
import sirttas.elementalcraft.block.shrine.overload.OverloadShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.sweet.SweetShrineBlock;
import sirttas.elementalcraft.block.shrine.sweet.SweetShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.directional.CapacityShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.EfficiencyShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.OptimizationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.RangeShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.StrengthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.AccelerationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.AccelerationShrineUpgradeBlockEntity;
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
import sirttas.elementalcraft.block.shrine.vacuum.VacuumShrineBlockEntity;
import sirttas.elementalcraft.block.solarsynthesizer.SolarSynthesizerBlock;
import sirttas.elementalcraft.block.solarsynthesizer.SolarSynthesizerBlockEntity;
import sirttas.elementalcraft.block.sorter.SorterBlock;
import sirttas.elementalcraft.block.sorter.SorterBlockEntity;
import sirttas.elementalcraft.block.source.SourceBlock;
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.block.spelldesk.SpellDeskBlock;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECBlocks {

	public static final AbstractBlock.IPositionPredicate ALWAYS_FALSE = (a, b, c) -> false;

	private ECBlocks() {
	}

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SmallElementContainerBlock.NAME) public static final SmallElementContainerBlock TANK_SMALL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementContainerBlock.NAME) public static final ElementContainerBlock TANK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ReservoirBlock.NAME_FIRE) public static final ReservoirBlock FIRE_RESERVOIR = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ReservoirBlock.NAME_WATER) public static final ReservoirBlock WATER_RESERVOIR = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ReservoirBlock.NAME_EARTH) public static final ReservoirBlock EARTH_RESERVOIR = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ReservoirBlock.NAME_AIR) public static final ReservoirBlock AIR_RESERVOIR = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + CreativeElementContainerBlock.NAME) public static final CreativeElementContainerBlock TANK_CREATIVE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ExtractorBlock.NAME) public static final ExtractorBlock EXTRACTOR = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ImprovedExtractorBlock.NAME) public static final ImprovedExtractorBlock EXTRACTOR_IMPROVED = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + EvaporatorBlock.NAME) public static final EvaporatorBlock EVAPORATOR = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SolarSynthesizerBlock.NAME) public static final SolarSynthesizerBlock SOLAR_SYNTHESIZER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + DiffuserBlock.NAME) public static final DiffuserBlock DIFFUSER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + InfuserBlock.NAME) public static final InfuserBlock INFUSER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + BinderBlock.NAME) public static final BinderBlock BINDER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ImprovedBinderBlock.NAME) public static final ImprovedBinderBlock BINDER_IMPROVED = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + CrystallizerBlock.NAME) public static final CrystallizerBlock CRYSTALLIZER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + InscriberBlock.NAME) public static final InscriberBlock INSCRIBER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + AirMillBlock.NAME) public static final AirMillBlock AIR_MILL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PedestalBlock.NAME_FIRE) public static final PedestalBlock FIRE_PEDESTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PedestalBlock.NAME_WATER) public static final PedestalBlock WATER_PEDESTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PedestalBlock.NAME_EARTH) public static final PedestalBlock EARTH_PEDESTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PedestalBlock.NAME_AIR) public static final PedestalBlock AIR_PEDESTAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PureInfuserBlock.NAME) public static final PureInfuserBlock PURE_INFUSER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + FireFurnaceBlock.NAME) public static final FireFurnaceBlock FIRE_FURNACE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + FireBlastFurnaceBlock.NAME) public static final FireBlastFurnaceBlock FIRE_BLAST_FURNACE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PurifierBlock.NAME) public static final PurifierBlock PURIFIER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementPipeBlock.NAME_IMPAIRED) public static final ElementPipeBlock PIPE_IMPAIRED = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementPipeBlock.NAME) public static final ElementPipeBlock PIPE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementPipeBlock.NAME_IMPROVED) public static final ElementPipeBlock PIPE_IMPROVED = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + RetrieverBlock.NAME) public static final RetrieverBlock RETRIEVER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SorterBlock.NAME) public static final SorterBlock SORTER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SpellDeskBlock.NAME) public static final SpellDeskBlock SPELL_DESK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + FirePylonBlock.NAME) public static final FirePylonBlock FIRE_PYLON = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + VacuumShrineBlock.NAME) public static final VacuumShrineBlock VACUUM_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + GrowthShrineBlock.NAME) public static final GrowthShrineBlock GROWTH_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + HarvestShrineBlock.NAME) public static final HarvestShrineBlock HARVEST_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + LavaShrineBlock.NAME) public static final LavaShrineBlock LAVA_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + OreShrineBlock.NAME) public static final OreShrineBlock ORE_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + OverloadShrineBlock.NAME) public static final OverloadShrineBlock OVERLOAD_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SweetShrineBlock.NAME) public static final SweetShrineBlock SWEET_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + EnderLockShrineBlock.NAME) public static final EnderLockShrineBlock ENDER_LOCK_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + BreedingShrineBlock.NAME) public static final BreedingShrineBlock BREEDING_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + GroveShrineBlock.NAME) public static final GroveShrineBlock GROVE_SHRINE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + AccelerationShrineUpgradeBlock.NAME) public static final AccelerationShrineUpgradeBlock ACCELERATION_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + RangeShrineUpgradeBlock.NAME) public static final RangeShrineUpgradeBlock RANGE_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + CapacityShrineUpgradeBlock.NAME) public static final CapacityShrineUpgradeBlock CAPACITY_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + EfficiencyShrineUpgradeBlock.NAME) public static final EfficiencyShrineUpgradeBlock EFFICIENCY_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + StrengthShrineUpgradeBlock.NAME) public static final StrengthShrineUpgradeBlock STRENGTH_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + OptimizationShrineUpgradeBlock.NAME) public static final OptimizationShrineUpgradeBlock OPTIMIZATION_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + FortuneShrineUpgradeBlock.NAME) public static final FortuneShrineUpgradeBlock FORTUNE_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SilkTouchShrineUpgradeBlock.NAME) public static final SilkTouchShrineUpgradeBlock SILK_TOUCH_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PlantingShrineUpgradeBlock.NAME) public static final PlantingShrineUpgradeBlock PLANTING_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + BonelessGrowthShrineUpgradeBlock.NAME) public static final BonelessGrowthShrineUpgradeBlock BONELESS_GROWTH_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PickupShrineUpgradeBlock.NAME) public static final PickupShrineUpgradeBlock PICKUP_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + NectarShrineUpgradeBlock.NAME) public static final NectarShrineUpgradeBlock NECTAR_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + MysticalGroveShrineUpgradeBlock.NAME) public static final MysticalGroveShrineUpgradeBlock MYSTICAL_GROVE_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + StemPollinationShrineUpgradeBlock.NAME) public static final StemPollinationShrineUpgradeBlock STEM_POLLINATION_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ProtectionShrineUpgradeBlock.NAME) public static final ProtectionShrineUpgradeBlock PROTECTION_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SourceBlock.NAME) public static final SourceBlock SOURCE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + CrystalOreBlock.NAME) public static final CrystalOreBlock CRYSTAL_ORE = null;
	
	@ObjectHolder(ElementalCraftApi.MODID + ":whiterock") public static final Block WHITE_ROCK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":whiterock_slab") public static final SlabBlock WHITE_ROCK_SLAB = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":whiterock_stairs") public static final StairsBlock WHITE_ROCK_STAIRS = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":whiterock_wall") public static final WallBlock WHITE_ROCK_WALL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":whiterock_fence") public static final FenceBlock WHITE_ROCK_FENCE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":whiterock_brick") public static final Block WHITE_ROCK_BRICK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":whiterock_brick_slab") public static final SlabBlock WHITE_ROCK_BRICK_SLAB = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":whiterock_brick_stairs") public static final StairsBlock WHITE_ROCK_BRICK_STAIRS = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":whiterock_brick_wall") public static final WallBlock WHITE_ROCK_BRICK_WALL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":purerock") public static final Block PURE_ROCK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":purerock_slab") public static final SlabBlock PURE_ROCK_SLAB = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":purerock_stairs") public static final StairsBlock PURE_ROCK_STAIRS = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":purerock_wall") public static final WallBlock PURE_ROCK_WALL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":burnt_glass") public static final GlassBlock BURNT_GLASS = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":burnt_glass_pane") public static final PaneBlock BURNT_GLASS_PANE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":drenched_iron_block") public static final Block DRENCHED_IRON_BLOCK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":swift_alloy_block") public static final Block SWIFT_ALLOY_BLOCK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":fireite_block") public static final Block FIREITE_BLOCK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":inertcrystal_block") public static final Block INERT_CRYSTAL_BLOCK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":firecrystal_block") public static final Block FIRE_CRYSTAL_BLOCK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":watercrystal_block") public static final Block WATER_CRYSTAL_BLOCK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":earthcrystal_block") public static final Block EARTH_CRYSTAL_BLOCK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":aircrystal_block") public static final Block AIR_CRYSTAL_BLOCK = null;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		RegistryHelper.register(registry, new SmallElementContainerBlock(), SmallElementContainerBlock.NAME);
		RegistryHelper.register(registry, new ElementContainerBlock(), ElementContainerBlock.NAME);
		RegistryHelper.register(registry, new ReservoirBlock(ElementType.FIRE), ReservoirBlock.NAME_FIRE);
		RegistryHelper.register(registry, new ReservoirBlock(ElementType.WATER), ReservoirBlock.NAME_WATER);
		RegistryHelper.register(registry, new ReservoirBlock(ElementType.EARTH), ReservoirBlock.NAME_EARTH);
		RegistryHelper.register(registry, new ReservoirBlock(ElementType.AIR), ReservoirBlock.NAME_AIR);
		RegistryHelper.register(registry, new CreativeElementContainerBlock(), CreativeElementContainerBlock.NAME);
		RegistryHelper.register(registry, new ExtractorBlock(), ExtractorBlock.NAME);
		RegistryHelper.register(registry, new ImprovedExtractorBlock(), ImprovedExtractorBlock.NAME);
		RegistryHelper.register(registry, new EvaporatorBlock(), EvaporatorBlock.NAME);
		RegistryHelper.register(registry, new SolarSynthesizerBlock(), SolarSynthesizerBlock.NAME);
		RegistryHelper.register(registry, new DiffuserBlock(), DiffuserBlock.NAME);
		RegistryHelper.register(registry, new InfuserBlock(), InfuserBlock.NAME);
		RegistryHelper.register(registry, new BinderBlock(), BinderBlock.NAME);
		RegistryHelper.register(registry, new ImprovedBinderBlock(), ImprovedBinderBlock.NAME);
		RegistryHelper.register(registry, new CrystallizerBlock(), CrystallizerBlock.NAME);
		RegistryHelper.register(registry, new InscriberBlock(), InscriberBlock.NAME);
		RegistryHelper.register(registry, new AirMillBlock(), AirMillBlock.NAME);
		RegistryHelper.register(registry, new PedestalBlock(ElementType.FIRE), PedestalBlock.NAME_FIRE);
		RegistryHelper.register(registry, new PedestalBlock(ElementType.WATER), PedestalBlock.NAME_WATER);
		RegistryHelper.register(registry, new PedestalBlock(ElementType.EARTH), PedestalBlock.NAME_EARTH);
		RegistryHelper.register(registry, new PedestalBlock(ElementType.AIR), PedestalBlock.NAME_AIR);
		RegistryHelper.register(registry, new PureInfuserBlock(), PureInfuserBlock.NAME);
		RegistryHelper.register(registry, new FireFurnaceBlock(), FireFurnaceBlock.NAME);
		RegistryHelper.register(registry, new FireBlastFurnaceBlock(), FireBlastFurnaceBlock.NAME);
		RegistryHelper.register(registry, new PurifierBlock(), PurifierBlock.NAME);
		RegistryHelper.register(registry, new ElementPipeBlock(ECConfig.COMMON.impairedPipeTransferAmount.get()), ElementPipeBlock.NAME_IMPAIRED);
		RegistryHelper.register(registry, new ElementPipeBlock(ECConfig.COMMON.pipeTransferAmount.get()), ElementPipeBlock.NAME);
		RegistryHelper.register(registry, new ElementPipeBlock(ECConfig.COMMON.improvedPipeTransferAmount.get()), ElementPipeBlock.NAME_IMPROVED);
		RegistryHelper.register(registry, new RetrieverBlock(), RetrieverBlock.NAME);
		RegistryHelper.register(registry, new SorterBlock(), SorterBlock.NAME);
		RegistryHelper.register(registry, new SpellDeskBlock(), SpellDeskBlock.NAME);
		RegistryHelper.register(registry, new FirePylonBlock(), FirePylonBlock.NAME);
		RegistryHelper.register(registry, new VacuumShrineBlock(), VacuumShrineBlock.NAME);
		RegistryHelper.register(registry, new GrowthShrineBlock(), GrowthShrineBlock.NAME);
		RegistryHelper.register(registry, new HarvestShrineBlock(), HarvestShrineBlock.NAME);
		RegistryHelper.register(registry, new LavaShrineBlock(), LavaShrineBlock.NAME);
		RegistryHelper.register(registry, new OreShrineBlock(), OreShrineBlock.NAME);
		RegistryHelper.register(registry, new OverloadShrineBlock(), OverloadShrineBlock.NAME);
		RegistryHelper.register(registry, new SweetShrineBlock(), SweetShrineBlock.NAME);
		RegistryHelper.register(registry, new EnderLockShrineBlock(), EnderLockShrineBlock.NAME);
		RegistryHelper.register(registry, new BreedingShrineBlock(), BreedingShrineBlock.NAME);
		RegistryHelper.register(registry, new GroveShrineBlock(), GroveShrineBlock.NAME);
		RegistryHelper.register(registry, new AccelerationShrineUpgradeBlock(), AccelerationShrineUpgradeBlock.NAME);
		RegistryHelper.register(registry, new RangeShrineUpgradeBlock(), RangeShrineUpgradeBlock.NAME);
		RegistryHelper.register(registry, new CapacityShrineUpgradeBlock(), CapacityShrineUpgradeBlock.NAME);
		RegistryHelper.register(registry, new EfficiencyShrineUpgradeBlock(), EfficiencyShrineUpgradeBlock.NAME);
		RegistryHelper.register(registry, new StrengthShrineUpgradeBlock(), StrengthShrineUpgradeBlock.NAME);
		RegistryHelper.register(registry, new OptimizationShrineUpgradeBlock(), OptimizationShrineUpgradeBlock.NAME);
		RegistryHelper.register(registry, new FortuneShrineUpgradeBlock(), FortuneShrineUpgradeBlock.NAME);
		RegistryHelper.register(registry, new SilkTouchShrineUpgradeBlock(), SilkTouchShrineUpgradeBlock.NAME);
		RegistryHelper.register(registry, new PlantingShrineUpgradeBlock(), PlantingShrineUpgradeBlock.NAME);
		RegistryHelper.register(registry, new BonelessGrowthShrineUpgradeBlock(), BonelessGrowthShrineUpgradeBlock.NAME);
		RegistryHelper.register(registry, new PickupShrineUpgradeBlock(), PickupShrineUpgradeBlock.NAME);
		RegistryHelper.register(registry, new NectarShrineUpgradeBlock(), NectarShrineUpgradeBlock.NAME);
		RegistryHelper.register(registry, new StemPollinationShrineUpgradeBlock(), StemPollinationShrineUpgradeBlock.NAME);
		RegistryHelper.register(registry, new ProtectionShrineUpgradeBlock(), ProtectionShrineUpgradeBlock.NAME);

		RegistryHelper.register(registry, new SourceBlock(), SourceBlock.NAME);
		RegistryHelper.register(registry, new CrystalOreBlock(AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F)), CrystalOreBlock.NAME);
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.WHITEROCK), "whiterock");
		RegistryHelper.register(registry, new SlabBlock(ECProperties.Blocks.WHITEROCK), "whiterock_slab");
		RegistryHelper.register(registry, new StairsBlock(() -> WHITE_ROCK.defaultBlockState(), ECProperties.Blocks.WHITEROCK), "whiterock_stairs");
		RegistryHelper.register(registry, new WallBlock(ECProperties.Blocks.WHITEROCK), "whiterock_wall");
		RegistryHelper.register(registry, new FenceBlock(ECProperties.Blocks.WHITEROCK), "whiterock_fence");
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.WHITEROCK), "whiterock_brick");
		RegistryHelper.register(registry, new SlabBlock(ECProperties.Blocks.WHITEROCK), "whiterock_brick_slab");
		RegistryHelper.register(registry, new StairsBlock(() -> WHITE_ROCK_BRICK.defaultBlockState(), ECProperties.Blocks.WHITEROCK), "whiterock_brick_stairs");
		RegistryHelper.register(registry, new WallBlock(ECProperties.Blocks.WHITEROCK), "whiterock_brick_wall");
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.PUREROCK), "purerock");
		RegistryHelper.register(registry, new SlabBlock(ECProperties.Blocks.PUREROCK), "purerock_slab");
		RegistryHelper.register(registry, new StairsBlock(() -> PURE_ROCK.defaultBlockState(), ECProperties.Blocks.PUREROCK), "purerock_stairs");
		RegistryHelper.register(registry, new WallBlock(ECProperties.Blocks.PUREROCK), "purerock_wall");
		RegistryHelper.register(registry, new GlassBlock(AbstractBlock.Properties.of(Material.GLASS).strength(0.7F).sound(SoundType.GLASS).noOcclusion()
				.isValidSpawn((a, b, c, d) -> false).isRedstoneConductor(ALWAYS_FALSE).isSuffocating(ALWAYS_FALSE).isViewBlocking(ALWAYS_FALSE)), "burnt_glass");
		RegistryHelper.register(registry, new PaneBlock(AbstractBlock.Properties.of(Material.GLASS).strength(0.7F).sound(SoundType.GLASS).noOcclusion()), "burnt_glass_pane");
		RegistryHelper.register(registry, new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)),
				"drenched_iron_block");
		RegistryHelper.register(registry, new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.GOLD).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)),
				"swift_alloy_block");
		RegistryHelper.register(registry,
				new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).sound(SoundType.NETHERITE_BLOCK)), "fireite_block");
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES), "inertcrystal_block");
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES), "firecrystal_block");
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES), "watercrystal_block");
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES), "earthcrystal_block");
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES), "aircrystal_block");
		
		if (DatagenModLoader.isRunningDataGen() || ECinteractions.isBotaniaActive()) {
			RegistryHelper.register(registry, new MysticalGroveShrineUpgradeBlock(), MysticalGroveShrineUpgradeBlock.NAME);
		}
		
	}

	@SubscribeEvent
	public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt) {
		IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();

		RegistryHelper.register(r, TileEntityType.Builder.of(SourceBlockEntity::new, SOURCE).build(null), SourceBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(ElementContainerBlockEntity::new, TANK, TANK_SMALL).build(null), ElementContainerBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(ReservoirBlockEntity::createFire, FIRE_RESERVOIR).build(null), ReservoirBlock.NAME_FIRE);
		RegistryHelper.register(r, TileEntityType.Builder.of(ReservoirBlockEntity::createWater, WATER_RESERVOIR).build(null), ReservoirBlock.NAME_WATER);
		RegistryHelper.register(r, TileEntityType.Builder.of(ReservoirBlockEntity::createEarth, EARTH_RESERVOIR).build(null), ReservoirBlock.NAME_EARTH);
		RegistryHelper.register(r, TileEntityType.Builder.of(ReservoirBlockEntity::createAir, AIR_RESERVOIR).build(null), ReservoirBlock.NAME_AIR);
		RegistryHelper.register(r, TileEntityType.Builder.of(CreativeElementContainerBlockEntity::new, TANK_CREATIVE).build(null), CreativeElementContainerBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(ExtractorBlockEntity::new, EXTRACTOR, EXTRACTOR_IMPROVED).build(null), ExtractorBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(EvaporatorBlockEntity::new, EVAPORATOR).build(null), EvaporatorBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(SolarSynthesizerBlockEntity::new, SOLAR_SYNTHESIZER).build(null), SolarSynthesizerBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(DiffuserBlockEntity::new, DIFFUSER).build(null), DiffuserBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(InfuserBlockEntity::new, INFUSER).build(null), InfuserBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(BinderBlockEntity::new, BINDER).build(null), BinderBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(ImprovedBinderBlockEntity::new, BINDER_IMPROVED).build(null), ImprovedBinderBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(CrystallizerBlockEntity::new, CRYSTALLIZER).build(null), CrystallizerBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(InscriberBlockEntity::new, INSCRIBER).build(null), InscriberBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(AirMillBlockEntity::new, AIR_MILL).build(null), AirMillBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(PedestalBlockEntity::createFire, FIRE_PEDESTAL).build(null), PedestalBlock.NAME_FIRE);
		RegistryHelper.register(r, TileEntityType.Builder.of(PedestalBlockEntity::createWater, WATER_PEDESTAL).build(null), PedestalBlock.NAME_WATER);
		RegistryHelper.register(r, TileEntityType.Builder.of(PedestalBlockEntity::createEarth, EARTH_PEDESTAL).build(null), PedestalBlock.NAME_EARTH);
		RegistryHelper.register(r, TileEntityType.Builder.of(PedestalBlockEntity::createAir, AIR_PEDESTAL).build(null), PedestalBlock.NAME_AIR);
		RegistryHelper.register(r, TileEntityType.Builder.of(PureInfuserBlockEntity::new, PURE_INFUSER).build(null), PureInfuserBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(FireFurnaceBlockEntity::new, FIRE_FURNACE).build(null), FireFurnaceBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(FireBlastFurnaceBlockEntity::new, FIRE_BLAST_FURNACE).build(null), FireBlastFurnaceBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(PurifierBlockEntity::new, PURIFIER).build(null), PurifierBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(ElementPipeBlockEntity::new, PIPE_IMPAIRED, PIPE, PIPE_IMPROVED).build(null), ElementPipeBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(FirePylonBlockEntity::new, FIRE_PYLON).build(null), FirePylonBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(VacuumShrineBlockEntity::new, VACUUM_SHRINE).build(null), VacuumShrineBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(GrowthShrineBlockEntity::new, GROWTH_SHRINE).build(null), GrowthShrineBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(HarvestShrineBlockEntity::new, HARVEST_SHRINE).build(null), HarvestShrineBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(LavaShrineBlockEntity::new, LAVA_SHRINE).build(null), LavaShrineBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(OreShrineBlockEntity::new, ORE_SHRINE).build(null), OreShrineBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(OverloadShrineBlockEntity::new, OVERLOAD_SHRINE).build(null), OverloadShrineBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(SweetShrineBlockEntity::new, SWEET_SHRINE).build(null), SweetShrineBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(EnderLockShrineBlockEntity::new, ENDER_LOCK_SHRINE).build(null), EnderLockShrineBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(BreedingShrineBlockEntity::new, BREEDING_SHRINE).build(null), BreedingShrineBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(GroveShrineBlockEntity::new, GROVE_SHRINE).build(null), GroveShrineBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(AccelerationShrineUpgradeBlockEntity::new, ACCELERATION_SHRINE_UPGRADE).build(null), AccelerationShrineUpgradeBlock.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.of(SorterBlockEntity::new, SORTER).build(null), SorterBlock.NAME);
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		RegistryHelper.register(registry, new BlockItem(FIREITE_BLOCK, ECProperties.Items.FIREITE), "fireite_block");
		for (Block block : ForgeRegistries.BLOCKS) {
			if (ElementalCraftApi.MODID.equals(block.getRegistryName().getNamespace()) && !registry.containsKey(block.getRegistryName())) {
				RegistryHelper.register(registry, new BlockItem(block, ECProperties.Items.DEFAULT_ITEM_PROPERTIES), block.getRegistryName());
			}
		}
	}
}
