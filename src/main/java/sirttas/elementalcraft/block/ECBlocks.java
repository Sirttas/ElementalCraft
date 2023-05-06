package sirttas.elementalcraft.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.anchor.TranslocationAnchorBlock;
import sirttas.elementalcraft.block.container.ElementContainerBlock;
import sirttas.elementalcraft.block.container.SmallElementContainerBlock;
import sirttas.elementalcraft.block.container.creative.CreativeElementContainerBlock;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlock;
import sirttas.elementalcraft.block.diffuser.DiffuserBlock;
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
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AirMillGrindstoneBlock;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.WaterMillWoodSawBlock;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlock;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;
import sirttas.elementalcraft.block.shrine.breeding.BreedingShrineBlock;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrineBlock;
import sirttas.elementalcraft.block.shrine.enderlock.EnderLockShrineBlock;
import sirttas.elementalcraft.block.shrine.firepylon.FirePylonBlock;
import sirttas.elementalcraft.block.shrine.grove.GroveShrineBlock;
import sirttas.elementalcraft.block.shrine.growth.GrowthShrineBlock;
import sirttas.elementalcraft.block.shrine.harvest.HarvestShrineBlock;
import sirttas.elementalcraft.block.shrine.lava.LavaShrineBlock;
import sirttas.elementalcraft.block.shrine.lumber.LumberShrineBlock;
import sirttas.elementalcraft.block.shrine.ore.OreShrineBlock;
import sirttas.elementalcraft.block.shrine.overload.OverloadShrineBlock;
import sirttas.elementalcraft.block.shrine.spawning.SpawningShrineBlock;
import sirttas.elementalcraft.block.shrine.spring.SpringShrineBlock;
import sirttas.elementalcraft.block.shrine.sweet.SweetShrineBlock;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked.OverclockedAccelerationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.CapacityShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.EfficiencyShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.OptimizationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.RangeShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.StrengthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.CrystalHarvestShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.FortuneShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.NectarShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.ProtectionShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.SilkTouchShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.SpringalineShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BonelessGrowthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.CrystalGrowthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.FillingShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.MysticalGroveShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.PickupShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.StemPollinationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.vertical.PlantingShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.vortex.VortexShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.vacuum.VacuumShrineBlock;
import sirttas.elementalcraft.block.sorter.SorterBlock;
import sirttas.elementalcraft.block.source.SourceBlock;
import sirttas.elementalcraft.block.source.breeder.SourceBreederBlock;
import sirttas.elementalcraft.block.source.breeder.pedestal.SourceBreederPedestalBlock;
import sirttas.elementalcraft.block.source.displacement.plate.BrokenSourceDisplacementPlateBlock;
import sirttas.elementalcraft.block.source.displacement.plate.SourceDisplacementPlateBlock;
import sirttas.elementalcraft.block.spelldesk.SpellDeskBlock;
import sirttas.elementalcraft.block.synthesizer.mana.ManaSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerBlock;
import sirttas.elementalcraft.property.ECProperties;

import java.util.function.Supplier;

public class ECBlocks {

    private static final DeferredRegister<Block> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, ElementalCraftApi.MODID);

	private ECBlocks() { }

	public static final RegistryObject<SmallElementContainerBlock> SMALL_CONTAINER = register(SmallElementContainerBlock::new, SmallElementContainerBlock.NAME);
	public static final RegistryObject<ElementContainerBlock> CONTAINER = register(ElementContainerBlock::new, ElementContainerBlock.NAME);
	public static final RegistryObject<ReservoirBlock> FIRE_RESERVOIR = register(() -> new ReservoirBlock(ElementType.FIRE), ReservoirBlock.NAME_FIRE);
	public static final RegistryObject<ReservoirBlock> WATER_RESERVOIR = register(() -> new ReservoirBlock(ElementType.WATER), ReservoirBlock.NAME_WATER);
	public static final RegistryObject<ReservoirBlock> EARTH_RESERVOIR = register(() -> new ReservoirBlock(ElementType.EARTH), ReservoirBlock.NAME_EARTH);
	public static final RegistryObject<ReservoirBlock> AIR_RESERVOIR = register(() -> new ReservoirBlock(ElementType.AIR), ReservoirBlock.NAME_AIR);
	public static final RegistryObject<CreativeElementContainerBlock> CREATIVE_CONTAINER = register(CreativeElementContainerBlock::new, CreativeElementContainerBlock.NAME);
	public static final RegistryObject<ExtractorBlock> EXTRACTOR = register(ExtractorBlock::new, ExtractorBlock.NAME);
	public static final RegistryObject<ImprovedExtractorBlock> EXTRACTOR_IMPROVED = register(ImprovedExtractorBlock::new, ImprovedExtractorBlock.NAME);
	public static final RegistryObject<EvaporatorBlock> EVAPORATOR = register(EvaporatorBlock::new, EvaporatorBlock.NAME);
	public static final RegistryObject<SolarSynthesizerBlock> SOLAR_SYNTHESIZER = register(SolarSynthesizerBlock::new, SolarSynthesizerBlock.NAME);
	public static final RegistryObject<ManaSynthesizerBlock> MANA_SYNTHESIZER = register(ManaSynthesizerBlock::new, ManaSynthesizerBlock.NAME);
	public static final RegistryObject<DiffuserBlock> DIFFUSER = register(DiffuserBlock::new, DiffuserBlock.NAME);
	public static final RegistryObject<InfuserBlock> INFUSER = register(InfuserBlock::new, InfuserBlock.NAME);
	public static final RegistryObject<BinderBlock> BINDER = register(BinderBlock::new, BinderBlock.NAME);
	public static final RegistryObject<ImprovedBinderBlock> BINDER_IMPROVED = register(ImprovedBinderBlock::new, ImprovedBinderBlock.NAME);
	public static final RegistryObject<CrystallizerBlock> CRYSTALLIZER = register(CrystallizerBlock::new, CrystallizerBlock.NAME);
	public static final RegistryObject<InscriberBlock> INSCRIBER = register(InscriberBlock::new, InscriberBlock.NAME);
	public static final RegistryObject<AirMillGrindstoneBlock> AIR_MILL_GRINDSTONE = register(AirMillGrindstoneBlock::new, AirMillGrindstoneBlock.NAME);
	public static final RegistryObject<WaterMillWoodSawBlock> WATER_MILL_WOOD_SAW = register(WaterMillWoodSawBlock::new, WaterMillWoodSawBlock.NAME);
	public static final RegistryObject<PedestalBlock> FIRE_PEDESTAL = register(() -> new PedestalBlock(ElementType.FIRE), PedestalBlock.NAME_FIRE);
	public static final RegistryObject<PedestalBlock> WATER_PEDESTAL = register(() -> new PedestalBlock(ElementType.WATER), PedestalBlock.NAME_WATER);
	public static final RegistryObject<PedestalBlock> EARTH_PEDESTAL = register(() -> new PedestalBlock(ElementType.EARTH), PedestalBlock.NAME_EARTH);
	public static final RegistryObject<PedestalBlock> AIR_PEDESTAL = register(() -> new PedestalBlock(ElementType.AIR), PedestalBlock.NAME_AIR);
	public static final RegistryObject<PureInfuserBlock> PURE_INFUSER = register(PureInfuserBlock::new, PureInfuserBlock.NAME);
	public static final RegistryObject<FireFurnaceBlock> FIRE_FURNACE = register(FireFurnaceBlock::new, FireFurnaceBlock.NAME);
	public static final RegistryObject<FireBlastFurnaceBlock> FIRE_BLAST_FURNACE = register(FireBlastFurnaceBlock::new, FireBlastFurnaceBlock.NAME);
	public static final RegistryObject<PurifierBlock> PURIFIER = register(PurifierBlock::new, PurifierBlock.NAME);
	public static final RegistryObject<ElementPipeBlock> PIPE_IMPAIRED = register(() -> new ElementPipeBlock(ElementPipeBlock.PipeType.IMPAIRED), ElementPipeBlock.NAME_IMPAIRED);
	public static final RegistryObject<ElementPipeBlock> PIPE = register(() -> new ElementPipeBlock(ElementPipeBlock.PipeType.STANDARD), ElementPipeBlock.NAME);
	public static final RegistryObject<ElementPipeBlock> PIPE_IMPROVED = register(() -> new ElementPipeBlock(ElementPipeBlock.PipeType.IMPROVED), ElementPipeBlock.NAME_IMPROVED);
	public static final RegistryObject<ElementPipeBlock> PIPE_CREATIVE = register(() -> new ElementPipeBlock(ElementPipeBlock.PipeType.CREATIVE), ElementPipeBlock.NAME_CREATIVE);
	public static final RegistryObject<RetrieverBlock> RETRIEVER = register(RetrieverBlock::new, RetrieverBlock.NAME);
	public static final RegistryObject<SorterBlock> SORTER = register(SorterBlock::new, SorterBlock.NAME);
	public static final RegistryObject<SpellDeskBlock> SPELL_DESK = register(SpellDeskBlock::new, SpellDeskBlock.NAME);
	public static final RegistryObject<FirePylonBlock> FIRE_PYLON = register(FirePylonBlock::new, FirePylonBlock.NAME);
	public static final RegistryObject<VacuumShrineBlock> VACUUM_SHRINE = register(VacuumShrineBlock::new, VacuumShrineBlock.NAME);
	public static final RegistryObject<GrowthShrineBlock> GROWTH_SHRINE = register(GrowthShrineBlock::new, GrowthShrineBlock.NAME);
	public static final RegistryObject<HarvestShrineBlock> HARVEST_SHRINE = register(HarvestShrineBlock::new, HarvestShrineBlock.NAME);
	public static final RegistryObject<LumberShrineBlock> LUMBER_SHRINE = register(LumberShrineBlock::new, LumberShrineBlock.NAME);
	public static final RegistryObject<LavaShrineBlock> LAVA_SHRINE = register(LavaShrineBlock::new, LavaShrineBlock.NAME);
	public static final RegistryObject<OreShrineBlock> ORE_SHRINE = register(OreShrineBlock::new, OreShrineBlock.NAME);
	public static final RegistryObject<OverloadShrineBlock> OVERLOAD_SHRINE = register(OverloadShrineBlock::new, OverloadShrineBlock.NAME);
	public static final RegistryObject<SweetShrineBlock> SWEET_SHRINE = register(SweetShrineBlock::new, SweetShrineBlock.NAME);
	public static final RegistryObject<EnderLockShrineBlock> ENDER_LOCK_SHRINE = register(EnderLockShrineBlock::new, EnderLockShrineBlock.NAME);
	public static final RegistryObject<BreedingShrineBlock> BREEDING_SHRINE = register(BreedingShrineBlock::new, BreedingShrineBlock.NAME);
	public static final RegistryObject<GroveShrineBlock> GROVE_SHRINE = register(GroveShrineBlock::new, GroveShrineBlock.NAME);
	public static final RegistryObject<SpringShrineBlock> SPRING_SHRINE = register(SpringShrineBlock::new, SpringShrineBlock.NAME);
	public static final RegistryObject<BuddingShrineBlock> BUDDING_SHRINE = register(BuddingShrineBlock::new, BuddingShrineBlock.NAME);
	public static final RegistryObject<SpawningShrineBlock> SPAWNING_SHRINE = register(SpawningShrineBlock::new, SpawningShrineBlock.NAME);
	public static final RegistryObject<AccelerationShrineUpgradeBlock> ACCELERATION_SHRINE_UPGRADE = register(AccelerationShrineUpgradeBlock::new, AccelerationShrineUpgradeBlock.NAME);
	public static final RegistryObject<OverclockedAccelerationShrineUpgradeBlock> OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE = register(OverclockedAccelerationShrineUpgradeBlock::new, OverclockedAccelerationShrineUpgradeBlock.NAME);
	public static final RegistryObject<RangeShrineUpgradeBlock> RANGE_SHRINE_UPGRADE = register(RangeShrineUpgradeBlock::new, RangeShrineUpgradeBlock.NAME);
	public static final RegistryObject<CapacityShrineUpgradeBlock> CAPACITY_SHRINE_UPGRADE = register(CapacityShrineUpgradeBlock::new, CapacityShrineUpgradeBlock.NAME);
	public static final RegistryObject<EfficiencyShrineUpgradeBlock> EFFICIENCY_SHRINE_UPGRADE = register(EfficiencyShrineUpgradeBlock::new, EfficiencyShrineUpgradeBlock.NAME);
	public static final RegistryObject<StrengthShrineUpgradeBlock> STRENGTH_SHRINE_UPGRADE = register(StrengthShrineUpgradeBlock::new, StrengthShrineUpgradeBlock.NAME);
	public static final RegistryObject<OptimizationShrineUpgradeBlock> OPTIMIZATION_SHRINE_UPGRADE = register(OptimizationShrineUpgradeBlock::new, OptimizationShrineUpgradeBlock.NAME);
	public static final RegistryObject<FortuneShrineUpgradeBlock> FORTUNE_SHRINE_UPGRADE = register(FortuneShrineUpgradeBlock::new, FortuneShrineUpgradeBlock.NAME);
	public static final RegistryObject<SilkTouchShrineUpgradeBlock> SILK_TOUCH_SHRINE_UPGRADE = register(SilkTouchShrineUpgradeBlock::new, SilkTouchShrineUpgradeBlock.NAME);
	public static final RegistryObject<PlantingShrineUpgradeBlock> PLANTING_SHRINE_UPGRADE = register(PlantingShrineUpgradeBlock::new, PlantingShrineUpgradeBlock.NAME);
	public static final RegistryObject<BonelessGrowthShrineUpgradeBlock> BONELESS_GROWTH_SHRINE_UPGRADE = register(BonelessGrowthShrineUpgradeBlock::new, BonelessGrowthShrineUpgradeBlock.NAME);
	public static final RegistryObject<PickupShrineUpgradeBlock> PICKUP_SHRINE_UPGRADE = register(PickupShrineUpgradeBlock::new, PickupShrineUpgradeBlock.NAME);
	public static final RegistryObject<VortexShrineUpgradeBlock> VORTEX_SHRINE_UPGRADE = register(VortexShrineUpgradeBlock::new, VortexShrineUpgradeBlock.NAME);
	public static final RegistryObject<NectarShrineUpgradeBlock> NECTAR_SHRINE_UPGRADE = register(NectarShrineUpgradeBlock::new, NectarShrineUpgradeBlock.NAME);
	public static final RegistryObject<MysticalGroveShrineUpgradeBlock> MYSTICAL_GROVE_SHRINE_UPGRADE = register(MysticalGroveShrineUpgradeBlock::new, MysticalGroveShrineUpgradeBlock.NAME);
	public static final RegistryObject<StemPollinationShrineUpgradeBlock> STEM_POLLINATION_SHRINE_UPGRADE = register(StemPollinationShrineUpgradeBlock::new, StemPollinationShrineUpgradeBlock.NAME);
	public static final RegistryObject<ProtectionShrineUpgradeBlock> PROTECTION_SHRINE_UPGRADE = register(ProtectionShrineUpgradeBlock::new, ProtectionShrineUpgradeBlock.NAME);
	public static final RegistryObject<FillingShrineUpgradeBlock> FILLING_SHRINE_UPGRADE = register(FillingShrineUpgradeBlock::new, FillingShrineUpgradeBlock.NAME);
	public static final RegistryObject<SpringalineShrineUpgradeBlock> SPRINGALINE_SHRINE_UPGRADE = register(SpringalineShrineUpgradeBlock::new, SpringalineShrineUpgradeBlock.NAME);
	public static final RegistryObject<CrystalHarvestShrineUpgradeBlock> CRYSTAL_HARVEST_SHRINE_UPGRADE = register(CrystalHarvestShrineUpgradeBlock::new, CrystalHarvestShrineUpgradeBlock.NAME);
	public static final RegistryObject<CrystalGrowthShrineUpgradeBlock> CRYSTAL_GROWTH_SHRINE_UPGRADE = register(CrystalGrowthShrineUpgradeBlock::new, CrystalGrowthShrineUpgradeBlock.NAME);
	public static final RegistryObject<TranslocationShrineUpgradeBlock> TRANSLOCATION_SHRINE_UPGRADE = register(TranslocationShrineUpgradeBlock::new, TranslocationShrineUpgradeBlock.NAME);
	public static final RegistryObject<SourceBlock> SOURCE = register(SourceBlock::new, SourceBlock.NAME);
	public static final RegistryObject<SourceDisplacementPlateBlock> FIRE_SOURCE_DISPLACEMENT_PLATE = register(() -> new SourceDisplacementPlateBlock(ElementType.FIRE), SourceDisplacementPlateBlock.NAME_FIRE);
	public static final RegistryObject<SourceDisplacementPlateBlock> WATER_SOURCE_DISPLACEMENT_PLATE = register(() -> new SourceDisplacementPlateBlock(ElementType.WATER), SourceDisplacementPlateBlock.NAME_WATER);
	public static final RegistryObject<SourceDisplacementPlateBlock> EARTH_SOURCE_DISPLACEMENT_PLATE = register(() -> new SourceDisplacementPlateBlock(ElementType.EARTH), SourceDisplacementPlateBlock.NAME_EARTH);
	public static final RegistryObject<SourceDisplacementPlateBlock> AIR_SOURCE_DISPLACEMENT_PLATE = register(() -> new SourceDisplacementPlateBlock(ElementType.AIR), SourceDisplacementPlateBlock.NAME_AIR);
	public static final RegistryObject<BrokenSourceDisplacementPlateBlock> BROKEN_SOURCE_DISPLACEMENT_PLATE = register(BrokenSourceDisplacementPlateBlock::new, BrokenSourceDisplacementPlateBlock.NAME);
	public static final RegistryObject<SourceBreederBlock> SOURCE_BREEDER = register(SourceBreederBlock::new, SourceBreederBlock.NAME);
	public static final RegistryObject<SourceBreederPedestalBlock> SOURCE_BREEDER_PEDESTAL = register(SourceBreederPedestalBlock::new, SourceBreederPedestalBlock.NAME);
	public static final RegistryObject<TranslocationAnchorBlock> TRANSLOCATION_ANCHOR = register(TranslocationAnchorBlock::new, TranslocationAnchorBlock.NAME);


	public static final RegistryObject<DropExperienceBlock> CRYSTAL_ORE = register(() -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 2)), "inert_crystal_ore");
	public static final RegistryObject<DropExperienceBlock> DEEPSLATE_CRYSTAL_ORE = register(() -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().color(MaterialColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE), UniformInt.of(0, 2)), "deepslate_inert_crystal_ore");
	public static final RegistryObject<Block> WHITE_ROCK = registerSimple(ECProperties.Blocks.WHITEROCK, "whiterock");
	public static final RegistryObject<SlabBlock> WHITE_ROCK_SLAB = registerSlab(WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<StairBlock> WHITE_ROCK_STAIRS = registerStairs(WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<WallBlock> WHITE_ROCK_WALL = registerWall(WHITE_ROCK, ECProperties.Blocks.WHITEROCK);

	public static final RegistryObject<FenceBlock> WHITE_ROCK_FENCE = registerFence(WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<Block> WHITE_ROCK_BRICK = registerSimple(ECProperties.Blocks.WHITEROCK, "whiterock_brick");
	public static final RegistryObject<SlabBlock> WHITE_ROCK_BRICK_SLAB = registerSlab(WHITE_ROCK_BRICK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<StairBlock> WHITE_ROCK_BRICK_STAIRS = registerStairs(WHITE_ROCK_BRICK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<WallBlock> WHITE_ROCK_BRICK_WALL = registerWall(WHITE_ROCK_BRICK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<Block> MOSSY_WHITE_ROCK = registerSimple(ECProperties.Blocks.WHITEROCK, "whiterock_mossy");
	public static final RegistryObject<SlabBlock> MOSSY_WHITE_ROCK_SLAB = registerSlab(MOSSY_WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<StairBlock> MOSSY_WHITE_ROCK_STAIRS = registerStairs(MOSSY_WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<WallBlock> MOSSY_WHITE_ROCK_WALL = registerWall(MOSSY_WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<Block> BURNT_WHITE_ROCK = registerSimple(ECProperties.Blocks.WHITEROCK, "whiterock_burnt");
	public static final RegistryObject<SlabBlock> BURNT_WHITE_ROCK_SLAB = registerSlab(BURNT_WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<StairBlock> BURNT_WHITE_ROCK_STAIRS = registerStairs(BURNT_WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<WallBlock> BURNT_WHITE_ROCK_WALL = registerWall(BURNT_WHITE_ROCK, ECProperties.Blocks.WHITEROCK);
	public static final RegistryObject<Block> PURE_ROCK = registerSimple(ECProperties.Blocks.PUREROCK, "purerock");
	public static final RegistryObject<SlabBlock> PURE_ROCK_SLAB = registerSlab(PURE_ROCK, ECProperties.Blocks.PUREROCK);
	public static final RegistryObject<StairBlock> PURE_ROCK_STAIRS = registerStairs(PURE_ROCK, ECProperties.Blocks.PUREROCK);
	public static final RegistryObject<WallBlock> PURE_ROCK_WALL = registerWall(PURE_ROCK, ECProperties.Blocks.PUREROCK);
	public static final RegistryObject<GlassBlock> BURNT_GLASS = registerGlass("burnt_glass");
	public static final RegistryObject<IronBarsBlock> BURNT_GLASS_PANE = registerGlassPane(BURNT_GLASS);
	public static final RegistryObject<Block> DRENCHED_IRON_BLOCK = registerSimple(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL), "drenched_iron_block");
	public static final RegistryObject<Block> SWIFT_ALLOY_BLOCK = registerSimple(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL), "swift_alloy_block");
	public static final RegistryObject<Block> FIREITE_BLOCK = registerSimple(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).sound(SoundType.NETHERITE_BLOCK), "fireite_block");
	public static final RegistryObject<Block> INERT_CRYSTAL_BLOCK = registerSimple("inertcrystal_block");
	public static final RegistryObject<Block>FIRE_CRYSTAL_BLOCK = registerSimple("firecrystal_block");
	public static final RegistryObject<Block> WATER_CRYSTAL_BLOCK = registerSimple("watercrystal_block");
	public static final RegistryObject<Block> EARTH_CRYSTAL_BLOCK = registerSimple("earthcrystal_block");
	public static final RegistryObject<Block> AIR_CRYSTAL_BLOCK = registerSimple("aircrystal_block");
	public static final RegistryObject<AmethystBlock> SPRINGALINE_BLOCK = register(() -> new AmethystBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)), "springaline_block");
	public static final RegistryObject<AmethystClusterBlock> SPRINGALINE_CLUSTER = register(() -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER)), "springaline_cluster");
	public static final RegistryObject<AmethystClusterBlock> LARGE_SPRINGALINE_BUD = register(() -> new AmethystClusterBlock(5, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).sound(SoundType.MEDIUM_AMETHYST_BUD).lightLevel(s -> 4)), "large_springaline_bud");
	public static final RegistryObject<AmethystClusterBlock> MEDIUM_SPRINGALINE_BUD = register(() -> new AmethystClusterBlock(4, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).sound(SoundType.LARGE_AMETHYST_BUD).lightLevel(s -> 2)), "medium_springaline_bud");
	public static final RegistryObject<AmethystClusterBlock> SMALL_SPRINGALINE_BUD = register(() -> new AmethystClusterBlock(3, 4, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).sound(SoundType.SMALL_AMETHYST_BUD).lightLevel(s -> 1)), "small_springaline_bud");
	public static final RegistryObject<GlassBlock> SPRINGALINE_GLASS = registerGlass("springaline_glass");
	public static final RegistryObject<IronBarsBlock> SPRINGALINE_GLASS_PANE = registerGlassPane(SPRINGALINE_GLASS);
	public static final RegistryObject<Block> SPRINGALINE_LANTERN = register(() -> new Block(BlockBehaviour.Properties.of(Material.GLASS, MaterialColor.QUARTZ).strength(0.3F).sound(SoundType.GLASS).lightLevel(s -> 15)), "springaline_lantern");

	private static <T extends Block> RegistryObject<T> register(Supplier<T> block, String name) {
		return DEFERRED_REGISTER.register(name, block);
	}

	private static RegistryObject<Block> registerSimple(BlockBehaviour.Properties properties, String name) {
		return DEFERRED_REGISTER.register(name, () -> new Block(properties));
	}

	private static RegistryObject<Block> registerSimple(String name) {
		return DEFERRED_REGISTER.register(name, () -> new Block(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES));
	}

	private static <T extends Block> RegistryObject<SlabBlock> registerSlab(RegistryObject<T> block, BlockBehaviour.Properties properties) {
		return DEFERRED_REGISTER.register(block.getId().getPath() + "_slab", () -> new SlabBlock(properties));
	}

	private static <T extends Block> RegistryObject<StairBlock> registerStairs(RegistryObject<T> block, BlockBehaviour.Properties properties) {
		return DEFERRED_REGISTER.register(block.getId().getPath() + "_stairs", () -> new StairBlock(() -> block.get().defaultBlockState(), properties));
	}

	private static <T extends Block> RegistryObject<WallBlock> registerWall(RegistryObject<T> block, BlockBehaviour.Properties properties) {
		return DEFERRED_REGISTER.register(block.getId().getPath() + "_wall", () -> new WallBlock(properties));
	}

	private static <T extends Block> RegistryObject<FenceBlock> registerFence(RegistryObject<T> block, BlockBehaviour.Properties properties) {
		return DEFERRED_REGISTER.register(block.getId().getPath() + "_fence", () -> new FenceBlock(properties));
	}

	private static RegistryObject<GlassBlock> registerGlass(String name) {
		return DEFERRED_REGISTER.register(name, () -> new GlassBlock(ECProperties.Blocks.GLASS));
	}
	private static RegistryObject<IronBarsBlock> registerGlassPane(RegistryObject<? extends GlassBlock> block) {
		return DEFERRED_REGISTER.register(block.getId().getPath() + "_pane", () -> new IronBarsBlock(ECProperties.Blocks.GLASS_PANE));
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
}
