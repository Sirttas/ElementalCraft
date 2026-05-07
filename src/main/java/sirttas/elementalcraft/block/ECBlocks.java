package sirttas.elementalcraft.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.anchor.TranslocationAnchorBlock;
import sirttas.elementalcraft.block.container.ElementContainerBlock;
import sirttas.elementalcraft.block.container.SmallElementContainerBlock;
import sirttas.elementalcraft.block.container.creative.CreativeElementContainerBlock;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlock;
import sirttas.elementalcraft.block.diffuser.DiffuserBlock;
import sirttas.elementalcraft.block.extractor.ElementExtractorBlock;
import sirttas.elementalcraft.block.extractor.ImprovedElementExtractorBlock;
import sirttas.elementalcraft.block.extractor.RudimentaryElementExtractorBlock;
import sirttas.elementalcraft.block.instrument.binder.BinderBlock;
import sirttas.elementalcraft.block.instrument.binder.improved.ImprovedBinderBlock;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerBlock;
import sirttas.elementalcraft.block.instrument.enchantment.liquefier.EnchantmentLiquefierBlock;
import sirttas.elementalcraft.block.instrument.infuser.InfuserBlock;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberBlock;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceBlock;
import sirttas.elementalcraft.block.instrument.io.firefurnace.blast.FireBlastFurnaceBlock;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.air.AirMillGrindstoneBlock;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.water.WaterMillGrindstoneBlock;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.air.AirMillWoodSawBlock;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.water.WaterMillWoodSawBlock;
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
import sirttas.elementalcraft.block.shrine.lumber.LumberShrineBlock;
import sirttas.elementalcraft.block.shrine.melting.MeltingShrineBlock;
import sirttas.elementalcraft.block.shrine.ore.OreShrineBlock;
import sirttas.elementalcraft.block.shrine.overload.OverloadShrineBlock;
import sirttas.elementalcraft.block.shrine.spawning.SpawningShrineBlock;
import sirttas.elementalcraft.block.shrine.spring.SpringShrineBlock;
import sirttas.elementalcraft.block.shrine.sweet.SweetShrineBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked.OverclockedAccelerationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.bonelessgrowth.BonelessGrowthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.bud.BudShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.crystalgrowth.CrystalGrowthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.crystalharvest.CrystalHarvestShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.CapacityShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.EfficiencyShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.OptimizationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.RangeShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.filling.FillingShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.fortune.FortuneShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.fortune.greater.GreaterFortuneShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.mysticalgrove.MysticalGroveShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.nectar.NectarShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.pickup.PickupShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.planting.PlantingShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.protection.ProtectionShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.silktouch.SilkTouchShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.stempollination.StemPollinationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.strength.OverwhelmingStrengthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.strength.StrengthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.vortex.VortexShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.vacuum.VacuumShrineBlock;
import sirttas.elementalcraft.block.sorter.ordered.OrderedSorterBlock;
import sirttas.elementalcraft.block.source.SourceBlock;
import sirttas.elementalcraft.block.source.breeder.SourceBreederBlock;
import sirttas.elementalcraft.block.source.breeder.pedestal.SourceBreederPedestalBlock;
import sirttas.elementalcraft.block.spelldesk.SpellDeskBlock;
import sirttas.elementalcraft.block.synthesizer.combustion.CombustionSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.cracking.CrackingSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.cracking.sculk.SculkCrackingSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.culinary.CulinarySynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.draining.DrainingSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.mill.AirMillSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.vibration.VibrationSynthesizerBlock;
import sirttas.elementalcraft.property.ECProperties;

import java.util.function.Function;

public class ECBlocks {

    private static final DeferredRegister<Block> DEFERRED_REGISTER = DeferredRegister.create(Registries.BLOCK, ElementalCraftApi.MODID);

	private ECBlocks() { }

	public static final DeferredHolder<Block, SmallElementContainerBlock> SMALL_CONTAINER = register(SmallElementContainerBlock.NAME, SmallElementContainerBlock::new, ECProperties.Blocks.container());
	public static final DeferredHolder<Block, ElementContainerBlock> CONTAINER = register(ElementContainerBlock.NAME, ElementContainerBlock::new, ECProperties.Blocks.container());
	public static final DeferredHolder<Block, ReservoirBlock> FIRE_RESERVOIR = register(ReservoirBlock.NAME_FIRE, p -> new ReservoirBlock(ElementType.FIRE, p), ECProperties.Blocks.container());
	public static final DeferredHolder<Block, ReservoirBlock> WATER_RESERVOIR = register(ReservoirBlock.NAME_WATER, p -> new ReservoirBlock(ElementType.WATER, p), ECProperties.Blocks.container());
	public static final DeferredHolder<Block, ReservoirBlock> EARTH_RESERVOIR = register(ReservoirBlock.NAME_EARTH, p -> new ReservoirBlock(ElementType.EARTH, p), ECProperties.Blocks.container());
	public static final DeferredHolder<Block, ReservoirBlock> AIR_RESERVOIR = register(ReservoirBlock.NAME_AIR, p -> new ReservoirBlock(ElementType.AIR, p), ECProperties.Blocks.container());
	public static final DeferredHolder<Block, CreativeElementContainerBlock> CREATIVE_CONTAINER = register(CreativeElementContainerBlock.NAME, CreativeElementContainerBlock::new, ECProperties.Blocks.container());
	public static final DeferredHolder<Block, RudimentaryElementExtractorBlock> RUDIMENTARY_EXTRACTOR = registerDefault(RudimentaryElementExtractorBlock.NAME, RudimentaryElementExtractorBlock::new);
	public static final DeferredHolder<Block, ElementExtractorBlock> EXTRACTOR = registerDefault(ElementExtractorBlock.NAME, ElementExtractorBlock::new);
	public static final DeferredHolder<Block, ImprovedElementExtractorBlock> IMPROVED_EXTRACTOR = registerDefault(ImprovedElementExtractorBlock.NAME, ImprovedElementExtractorBlock::new);
	public static final DeferredHolder<Block, CrackingSynthesizerBlock> CRACKING_SYNTHESIZER = registerDefault(CrackingSynthesizerBlock.NAME, CrackingSynthesizerBlock::new);
	public static final DeferredHolder<Block, CombustionSynthesizerBlock> COMBUSTION_SYNTHESIZER = registerDefault(CombustionSynthesizerBlock.NAME, CombustionSynthesizerBlock::new);
	public static final DeferredHolder<Block, DrainingSynthesizerBlock> DRAINING_SYNTHESIZER = registerDefault(DrainingSynthesizerBlock.NAME, DrainingSynthesizerBlock::new);
	public static final DeferredHolder<Block, VibrationSynthesizerBlock> VIBRATION_SYNTHESIZER = registerDefault(VibrationSynthesizerBlock.NAME, VibrationSynthesizerBlock::new);
	public static final DeferredHolder<Block, SolarSynthesizerBlock> SOLAR_SYNTHESIZER = registerDefault(SolarSynthesizerBlock.NAME, SolarSynthesizerBlock::new);
	public static final DeferredHolder<Block, CulinarySynthesizerBlock> CULINARY_SYNTHESIZER = registerDefault(CulinarySynthesizerBlock.NAME, CulinarySynthesizerBlock::new);
	public static final DeferredHolder<Block, SculkCrackingSynthesizerBlock> SCULK_CRACKING_SYNTHESIZER = registerDefault(SculkCrackingSynthesizerBlock.NAME, SculkCrackingSynthesizerBlock::new);
	public static final DeferredHolder<Block, AirMillSynthesizerBlock> AIR_MILL_SYNTHESIZER = registerDefault(AirMillSynthesizerBlock.NAME, AirMillSynthesizerBlock::new);
	public static final DeferredHolder<Block, DiffuserBlock> DIFFUSER = registerDefault(DiffuserBlock.NAME, DiffuserBlock::new);
	public static final DeferredHolder<Block, InfuserBlock> INFUSER = registerDefault(InfuserBlock.NAME, InfuserBlock::new);
	public static final DeferredHolder<Block, BinderBlock> BINDER = registerDefault(BinderBlock.NAME, BinderBlock::new);
	public static final DeferredHolder<Block, ImprovedBinderBlock> BINDER_IMPROVED = registerDefault(ImprovedBinderBlock.NAME, ImprovedBinderBlock::new);
	public static final DeferredHolder<Block, CrystallizerBlock> CRYSTALLIZER = registerDefault(CrystallizerBlock.NAME, CrystallizerBlock::new);
	public static final DeferredHolder<Block, InscriberBlock> INSCRIBER = registerDefault(InscriberBlock.NAME, InscriberBlock::new);
	public static final DeferredHolder<Block, WaterMillGrindstoneBlock> WATER_MILL_GRINDSTONE = registerDefault(WaterMillGrindstoneBlock.NAME, WaterMillGrindstoneBlock::new);
	public static final DeferredHolder<Block, AirMillGrindstoneBlock> AIR_MILL_GRINDSTONE = registerDefault(AirMillGrindstoneBlock.NAME, AirMillGrindstoneBlock::new);
	public static final DeferredHolder<Block, WaterMillWoodSawBlock> WATER_MILL_WOOD_SAW = registerDefault(WaterMillWoodSawBlock.NAME, WaterMillWoodSawBlock::new);
	public static final DeferredHolder<Block, AirMillWoodSawBlock> AIR_MILL_WOOD_SAW = registerDefault(AirMillWoodSawBlock.NAME, AirMillWoodSawBlock::new);
	public static final DeferredHolder<Block, EnchantmentLiquefierBlock> ENCHANTMENT_LIQUEFIER = registerDefault(EnchantmentLiquefierBlock.NAME, EnchantmentLiquefierBlock::new);
	public static final DeferredHolder<Block, PedestalBlock> FIRE_PEDESTAL = register(PedestalBlock.NAME_FIRE, p -> new PedestalBlock(ElementType.FIRE, p), ECProperties.Blocks.defaultProperties());
	public static final DeferredHolder<Block, PedestalBlock> WATER_PEDESTAL = register(PedestalBlock.NAME_WATER, p -> new PedestalBlock(ElementType.WATER, p), ECProperties.Blocks.defaultProperties());
	public static final DeferredHolder<Block, PedestalBlock> EARTH_PEDESTAL = register(PedestalBlock.NAME_EARTH, p -> new PedestalBlock(ElementType.EARTH, p), ECProperties.Blocks.defaultProperties());
	public static final DeferredHolder<Block, PedestalBlock> AIR_PEDESTAL = register(PedestalBlock.NAME_AIR, p -> new PedestalBlock(ElementType.AIR, p), ECProperties.Blocks.defaultProperties());
	public static final DeferredHolder<Block, PureInfuserBlock> PURE_INFUSER = registerDefault(PureInfuserBlock.NAME, PureInfuserBlock::new);
	public static final DeferredHolder<Block, FireFurnaceBlock> FIRE_FURNACE = registerDefault(FireFurnaceBlock.NAME, FireFurnaceBlock::new);
	public static final DeferredHolder<Block, FireBlastFurnaceBlock> FIRE_BLAST_FURNACE = registerDefault(FireBlastFurnaceBlock.NAME, FireBlastFurnaceBlock::new);
	public static final DeferredHolder<Block, PurifierBlock> PURIFIER = registerDefault(PurifierBlock.NAME, PurifierBlock::new);
	public static final DeferredHolder<Block, ElementPipeBlock> PIPE_RUDIMENTARY = register(ElementPipeBlock.NAME_RUDIMENTARY, p -> new ElementPipeBlock(ElementPipeBlock.PipeType.RUDIMENTARY, p), ECProperties.Blocks.pipe());
	public static final DeferredHolder<Block, ElementPipeBlock> PIPE = register(ElementPipeBlock.NAME, p -> new ElementPipeBlock(ElementPipeBlock.PipeType.STANDARD, p), ECProperties.Blocks.pipe());
	public static final DeferredHolder<Block, ElementPipeBlock> PIPE_IMPROVED = register(ElementPipeBlock.NAME_IMPROVED, p -> new ElementPipeBlock(ElementPipeBlock.PipeType.IMPROVED, p), ECProperties.Blocks.pipe());
	public static final DeferredHolder<Block, ElementPipeBlock> PIPE_CREATIVE = register(ElementPipeBlock.NAME_CREATIVE, p -> new ElementPipeBlock(ElementPipeBlock.PipeType.CREATIVE, p), ECProperties.Blocks.pipe());
	public static final DeferredHolder<Block, RetrieverBlock> RETRIEVER = registerDefault(RetrieverBlock.NAME, RetrieverBlock::new);
	public static final DeferredHolder<Block, OrderedSorterBlock> ORDERED_SORTER = registerNoOcclusion(OrderedSorterBlock.NAME, OrderedSorterBlock::new);
	public static final DeferredHolder<Block, SpellDeskBlock> SPELL_DESK = registerDefault(SpellDeskBlock.NAME, SpellDeskBlock::new);
	public static final DeferredHolder<Block, FirePylonBlock> FIRE_PYLON = registerNoOcclusion(FirePylonBlock.NAME, FirePylonBlock::new);
	public static final DeferredHolder<Block, VacuumShrineBlock> VACUUM_SHRINE = registerNoOcclusion(VacuumShrineBlock.NAME, VacuumShrineBlock::new);
	public static final DeferredHolder<Block, GrowthShrineBlock> GROWTH_SHRINE = registerNoOcclusion(GrowthShrineBlock.NAME, GrowthShrineBlock::new);
	public static final DeferredHolder<Block, HarvestShrineBlock> HARVEST_SHRINE = registerNoOcclusion(HarvestShrineBlock.NAME, HarvestShrineBlock::new);
	public static final DeferredHolder<Block, LumberShrineBlock> LUMBER_SHRINE = registerNoOcclusion(LumberShrineBlock.NAME, LumberShrineBlock::new);
	public static final DeferredHolder<Block, MeltingShrineBlock> MELTING_SHRINE = registerNoOcclusion(MeltingShrineBlock.NAME, MeltingShrineBlock::new);
	public static final DeferredHolder<Block, OreShrineBlock> ORE_SHRINE = registerNoOcclusion(OreShrineBlock.NAME, OreShrineBlock::new);
	public static final DeferredHolder<Block, OverloadShrineBlock> OVERLOAD_SHRINE = registerNoOcclusion(OverloadShrineBlock.NAME, OverloadShrineBlock::new);
	public static final DeferredHolder<Block, SweetShrineBlock> SWEET_SHRINE = registerNoOcclusion(SweetShrineBlock.NAME, SweetShrineBlock::new);
	public static final DeferredHolder<Block, EnderLockShrineBlock> ENDER_LOCK_SHRINE = registerNoOcclusion(EnderLockShrineBlock.NAME, EnderLockShrineBlock::new);
	public static final DeferredHolder<Block, BreedingShrineBlock> BREEDING_SHRINE = registerNoOcclusion(BreedingShrineBlock.NAME, BreedingShrineBlock::new);
	public static final DeferredHolder<Block, GroveShrineBlock> GROVE_SHRINE = registerNoOcclusion(GroveShrineBlock.NAME, GroveShrineBlock::new);
	public static final DeferredHolder<Block, SpringShrineBlock> SPRING_SHRINE = registerNoOcclusion(SpringShrineBlock.NAME, SpringShrineBlock::new);
	public static final DeferredHolder<Block, BuddingShrineBlock> BUDDING_SHRINE = registerNoOcclusion(BuddingShrineBlock.NAME, BuddingShrineBlock::new);
	public static final DeferredHolder<Block, SpawningShrineBlock> SPAWNING_SHRINE = registerNoOcclusion(SpawningShrineBlock.NAME, SpawningShrineBlock::new);
	public static final DeferredHolder<Block, AccelerationShrineUpgradeBlock> ACCELERATION_SHRINE_UPGRADE = registerNoOcclusion(AccelerationShrineUpgradeBlock.NAME, AccelerationShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, OverclockedAccelerationShrineUpgradeBlock> OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE = registerNoOcclusion(OverclockedAccelerationShrineUpgradeBlock.NAME, OverclockedAccelerationShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, RangeShrineUpgradeBlock> RANGE_SHRINE_UPGRADE = registerNoOcclusion(RangeShrineUpgradeBlock.NAME, RangeShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, CapacityShrineUpgradeBlock> CAPACITY_SHRINE_UPGRADE = registerNoOcclusion(CapacityShrineUpgradeBlock.NAME, CapacityShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, EfficiencyShrineUpgradeBlock> EFFICIENCY_SHRINE_UPGRADE = registerNoOcclusion(EfficiencyShrineUpgradeBlock.NAME, EfficiencyShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, StrengthShrineUpgradeBlock> STRENGTH_SHRINE_UPGRADE = registerNoOcclusion(StrengthShrineUpgradeBlock.NAME, StrengthShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, OverwhelmingStrengthShrineUpgradeBlock> OVERWHELMING_STRENGTH_SHRINE_UPGRADE = registerNoOcclusion(OverwhelmingStrengthShrineUpgradeBlock.NAME, OverwhelmingStrengthShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, OptimizationShrineUpgradeBlock> OPTIMIZATION_SHRINE_UPGRADE = registerNoOcclusion(OptimizationShrineUpgradeBlock.NAME, OptimizationShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, FortuneShrineUpgradeBlock> FORTUNE_SHRINE_UPGRADE = registerNoOcclusion(FortuneShrineUpgradeBlock.NAME, FortuneShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, GreaterFortuneShrineUpgradeBlock> GREATER_FORTUNE_SHRINE_UPGRADE = registerNoOcclusion(GreaterFortuneShrineUpgradeBlock.NAME, GreaterFortuneShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, SilkTouchShrineUpgradeBlock> SILK_TOUCH_SHRINE_UPGRADE = registerNoOcclusion(SilkTouchShrineUpgradeBlock.NAME, SilkTouchShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, PlantingShrineUpgradeBlock> PLANTING_SHRINE_UPGRADE = registerNoOcclusion(PlantingShrineUpgradeBlock.NAME, PlantingShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, BonelessGrowthShrineUpgradeBlock> BONELESS_GROWTH_SHRINE_UPGRADE = registerNoOcclusion(BonelessGrowthShrineUpgradeBlock.NAME, BonelessGrowthShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, PickupShrineUpgradeBlock> PICKUP_SHRINE_UPGRADE = registerNoOcclusion(PickupShrineUpgradeBlock.NAME, PickupShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, VortexShrineUpgradeBlock> VORTEX_SHRINE_UPGRADE = registerNoOcclusion(VortexShrineUpgradeBlock.NAME, VortexShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, NectarShrineUpgradeBlock> NECTAR_SHRINE_UPGRADE = registerNoOcclusion(NectarShrineUpgradeBlock.NAME, NectarShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, MysticalGroveShrineUpgradeBlock> MYSTICAL_GROVE_SHRINE_UPGRADE = registerNoOcclusion(MysticalGroveShrineUpgradeBlock.NAME, MysticalGroveShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, StemPollinationShrineUpgradeBlock> STEM_POLLINATION_SHRINE_UPGRADE = registerNoOcclusion(StemPollinationShrineUpgradeBlock.NAME, StemPollinationShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, ProtectionShrineUpgradeBlock> PROTECTION_SHRINE_UPGRADE = registerNoOcclusion(ProtectionShrineUpgradeBlock.NAME, ProtectionShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, FillingShrineUpgradeBlock> FILLING_SHRINE_UPGRADE = registerNoOcclusion(FillingShrineUpgradeBlock.NAME, FillingShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, BudShrineUpgradeBlock> SPRINGALINE_SHRINE_UPGRADE = registerNoOcclusion(BudShrineUpgradeBlock.SPRINGALINE_NAME, p -> new BudShrineUpgradeBlock(ShrineUpgrades.SPRINGALINE, p));
	public static final DeferredHolder<Block, BudShrineUpgradeBlock> CERTUS_QUARTZ_SHRINE_UPGRADE = registerNoOcclusion(BudShrineUpgradeBlock.CERTUS_QUARTZ_NAME, p -> new BudShrineUpgradeBlock(ShrineUpgrades.CERTUS_QUARTZ, p));
	public static final DeferredHolder<Block, CrystalHarvestShrineUpgradeBlock> CRYSTAL_HARVEST_SHRINE_UPGRADE = registerNoOcclusion(CrystalHarvestShrineUpgradeBlock.NAME, CrystalHarvestShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, CrystalGrowthShrineUpgradeBlock> CRYSTAL_GROWTH_SHRINE_UPGRADE = registerNoOcclusion(CrystalGrowthShrineUpgradeBlock.NAME, CrystalGrowthShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, TranslocationShrineUpgradeBlock> TRANSLOCATION_SHRINE_UPGRADE = registerNoOcclusion(TranslocationShrineUpgradeBlock.NAME, TranslocationShrineUpgradeBlock::new);
	public static final DeferredHolder<Block, SourceBlock> FIRE_SOURCE = register(SourceBlock.NAME_FIRE, p -> new SourceBlock(ElementType.FIRE, p), ECProperties.Blocks.source());
	public static final DeferredHolder<Block, SourceBlock> WATER_SOURCE = register(SourceBlock.NAME_WATER, p -> new SourceBlock(ElementType.WATER, p), ECProperties.Blocks.source());
	public static final DeferredHolder<Block, SourceBlock> EARTH_SOURCE = register(SourceBlock.NAME_EARTH, p -> new SourceBlock(ElementType.EARTH, p), ECProperties.Blocks.source());
	public static final DeferredHolder<Block, SourceBlock> AIR_SOURCE = register(SourceBlock.NAME_AIR, p -> new SourceBlock(ElementType.AIR, p), ECProperties.Blocks.source());
	public static final DeferredHolder<Block, SourceBreederBlock> SOURCE_BREEDER = registerDefault(SourceBreederBlock.NAME, SourceBreederBlock::new);
	public static final DeferredHolder<Block, SourceBreederPedestalBlock> SOURCE_BREEDER_PEDESTAL = registerNoOcclusion(SourceBreederPedestalBlock.NAME, SourceBreederPedestalBlock::new);
	public static final DeferredHolder<Block, TranslocationAnchorBlock> TRANSLOCATION_ANCHOR = registerDefault(TranslocationAnchorBlock.NAME, TranslocationAnchorBlock::new);


	public static final DeferredHolder<Block, DropExperienceBlock> CRYSTAL_ORE = register("inert_crystal_ore", p -> new DropExperienceBlock(UniformInt.of(0, 3), p), BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
	public static final DeferredHolder<Block, DropExperienceBlock> DEEPSLATE_CRYSTAL_ORE = register("deepslate_inert_crystal_ore", p -> new DropExperienceBlock(UniformInt.of(0, 3), p), BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
	public static final DeferredHolder<Block, Block> WHITE_ROCK = registerSimple("whiterock", ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, SlabBlock> WHITE_ROCK_SLAB = registerSlab(WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, StairBlock> WHITE_ROCK_STAIRS = registerStairs(WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, WallBlock> WHITE_ROCK_WALL = registerWall(WHITE_ROCK, ECProperties.Blocks.whiterock());

	public static final DeferredHolder<Block, FenceBlock> WHITE_ROCK_FENCE = registerFence(WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, Block> WHITE_ROCK_BRICKS = registerSimple("whiterock_brick", ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, SlabBlock> WHITE_ROCK_BRICK_SLAB = registerSlab(WHITE_ROCK_BRICKS, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, StairBlock> WHITE_ROCK_BRICK_STAIRS = registerStairs(WHITE_ROCK_BRICKS, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, WallBlock> WHITE_ROCK_BRICK_WALL = registerWall(WHITE_ROCK_BRICKS, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, Block> MOSSY_WHITE_ROCK = registerSimple("whiterock_mossy", ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, SlabBlock> MOSSY_WHITE_ROCK_SLAB = registerSlab(MOSSY_WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, StairBlock> MOSSY_WHITE_ROCK_STAIRS = registerStairs(MOSSY_WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, WallBlock> MOSSY_WHITE_ROCK_WALL = registerWall(MOSSY_WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, Block> BURNT_WHITE_ROCK = registerSimple("whiterock_burnt", ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, SlabBlock> BURNT_WHITE_ROCK_SLAB = registerSlab(BURNT_WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, StairBlock> BURNT_WHITE_ROCK_STAIRS = registerStairs(BURNT_WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, WallBlock> BURNT_WHITE_ROCK_WALL = registerWall(BURNT_WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<Block, Block> PURE_ROCK = registerSimple("purerock", ECProperties.Blocks.purerock());
	public static final DeferredHolder<Block, SlabBlock> PURE_ROCK_SLAB = registerSlab(PURE_ROCK, ECProperties.Blocks.purerock());
	public static final DeferredHolder<Block, StairBlock> PURE_ROCK_STAIRS = registerStairs(PURE_ROCK, ECProperties.Blocks.purerock());
	public static final DeferredHolder<Block, WallBlock> PURE_ROCK_WALL = registerWall(PURE_ROCK, ECProperties.Blocks.purerock());
	public static final DeferredHolder<Block, TransparentBlock> BURNT_GLASS = registerGlass("burnt_glass");
	public static final DeferredHolder<Block, IronBarsBlock> BURNT_GLASS_PANE = registerGlassPane(BURNT_GLASS);
	public static final DeferredHolder<Block, Block> DRENCHED_IRON_BLOCK = registerSimple("drenched_iron_block", BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
	public static final DeferredHolder<Block, Block> SWIFT_ALLOY_BLOCK = registerSimple("swift_alloy_block", BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK));
	public static final DeferredHolder<Block, Block> FIREITE_BLOCK = registerSimple("fireite_block", BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK));
	public static final DeferredHolder<Block, Block> INERT_CRYSTAL_BLOCK = registerSimple("inert_crystal_block");
	public static final DeferredHolder<Block, Block>FIRE_CRYSTAL_BLOCK = registerSimple("firecrystal_block");
	public static final DeferredHolder<Block, Block> WATER_CRYSTAL_BLOCK = registerSimple("watercrystal_block");
	public static final DeferredHolder<Block, Block> EARTH_CRYSTAL_BLOCK = registerSimple("earthcrystal_block");
	public static final DeferredHolder<Block, Block> AIR_CRYSTAL_BLOCK = registerSimple("aircrystal_block");
	public static final DeferredHolder<Block, AmethystBlock> SPRINGALINE_BLOCK = register("springaline_block", AmethystBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK));
	public static final DeferredHolder<Block, AmethystClusterBlock> SPRINGALINE_CLUSTER = register("springaline_cluster", p -> new AmethystClusterBlock(7, 3, p), BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER));
	public static final DeferredHolder<Block, AmethystClusterBlock> LARGE_SPRINGALINE_BUD = register("large_springaline_bud", p -> new AmethystClusterBlock(5, 3, p), BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_AMETHYST_BUD));
	public static final DeferredHolder<Block, AmethystClusterBlock> MEDIUM_SPRINGALINE_BUD = register("medium_springaline_bud", p -> new AmethystClusterBlock(4, 3, p), BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD));
	public static final DeferredHolder<Block, AmethystClusterBlock> SMALL_SPRINGALINE_BUD = register("small_springaline_bud", p -> new AmethystClusterBlock(3, 4, p), BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD));
	public static final DeferredHolder<Block, TransparentBlock> SPRINGALINE_GLASS = registerGlass("springaline_glass");
	public static final DeferredHolder<Block, IronBarsBlock> SPRINGALINE_GLASS_PANE = registerGlassPane(SPRINGALINE_GLASS);
	public static final DeferredHolder<Block, Block> SPRINGALINE_LANTERN = register("springaline_lantern", Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SEA_LANTERN));

	public static final DeferredHolder<Block, ElementalEmberBlock> ELEMENTAL_EMBER = register(ElementalEmberBlock.NAME, ElementalEmberBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TORCH));

	private static <T extends Block> DeferredHolder<Block, T> register(String name, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties properties) {
		var id = ResourceKey.create(Registries.BLOCK, ElementalCraftApi.createRL(name));
		return DEFERRED_REGISTER.register(name, () -> factory.apply(properties.setId(id)));
	}

	private static <T extends Block> DeferredHolder<Block, T> registerDefault(String name, Function<BlockBehaviour.Properties, T> block) {
		return register(name, block, ECProperties.Blocks.defaultProperties());
	}

	private static <T extends Block> DeferredHolder<Block, T> registerNoOcclusion(String name, Function<BlockBehaviour.Properties, T> block) {
		return register(name, block, ECProperties.Blocks.noOcclusion());
	}

	private static DeferredHolder<Block, Block> registerSimple(String name, BlockBehaviour.Properties properties) {
		return register(name, Block::new, properties);
	}

	private static DeferredHolder<Block, Block> registerSimple(String name) {
		return registerSimple(name, ECProperties.Blocks.defaultProperties());
	}

	private static <T extends Block> DeferredHolder<Block, SlabBlock> registerSlab(DeferredHolder<Block, T> block, BlockBehaviour.Properties properties) {
		return register(block.getId().getPath() + "_slab", SlabBlock::new, properties);
	}

	private static <T extends Block> DeferredHolder<Block, StairBlock> registerStairs(DeferredHolder<Block, T> block, BlockBehaviour.Properties properties) {
		return register(block.getId().getPath() + "_stairs", p -> new StairBlock(block.get().defaultBlockState(), p), properties);
	}

	private static <T extends Block> DeferredHolder<Block, WallBlock> registerWall(DeferredHolder<Block, T> block, BlockBehaviour.Properties properties) {
		return register(block.getId().getPath() + "_wall", WallBlock::new, properties);
	}

	private static <T extends Block> DeferredHolder<Block, FenceBlock> registerFence(DeferredHolder<Block, T> block, BlockBehaviour.Properties properties) {
		return register(block.getId().getPath() + "_fence", FenceBlock::new, properties);
	}

	private static DeferredHolder<Block, TransparentBlock> registerGlass(String name) {
		return register(name, TransparentBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));
	}
	private static DeferredHolder<Block, IronBarsBlock> registerGlassPane(DeferredHolder<Block, ? extends TransparentBlock> block) {
		return register(block.getId().getPath() + "_pane", IronBarsBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE));
	}

	public static void register(IEventBus bus) {
		registerAliases(DEFERRED_REGISTER);
		DEFERRED_REGISTER.register(bus);
	}

	public static void registerAliases(DeferredRegister<?> register) {
		register.addAlias(ElementalCraftApi.createRL("extractor"), ElementalCraftApi.createRL(RudimentaryElementExtractorBlock.NAME));
		register.addAlias(ElementalCraftApi.createRL("extractor_improved"), ElementalCraftApi.createRL(ImprovedElementExtractorBlock.NAME));
		register.addAlias(ElementalCraftApi.createRL("elementpipe_impaired"), ElementalCraftApi.createRL(ElementPipeBlock.NAME_RUDIMENTARY));
		register.addAlias(ElementalCraftApi.createRL("solar_synthesizer"), ElementalCraftApi.createRL(SolarSynthesizerBlock.NAME));
		register.addAlias(ElementalCraftApi.createRL("pureinfuser"), ElementalCraftApi.createRL(PureInfuserBlock.NAME));
		register.addAlias(ElementalCraftApi.createRL("lavashrine"), ElementalCraftApi.createRL(MeltingShrineBlock.NAME));
		register.addAlias(ElementalCraftApi.createRL("sorter"), ElementalCraftApi.createRL(OrderedSorterBlock.NAME));
	}
}
