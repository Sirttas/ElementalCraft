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
import org.jetbrains.annotations.NotNull;
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

    private static final DeferredRegister<@NotNull Block> DEFERRED_REGISTER = DeferredRegister.create(Registries.BLOCK, ElementalCraftApi.MODID);

	private ECBlocks() { }

	public static final DeferredHolder<@NotNull Block, @NotNull SmallElementContainerBlock> SMALL_CONTAINER = register(SmallElementContainerBlock.NAME, SmallElementContainerBlock::new, ECProperties.Blocks.container());
	public static final DeferredHolder<@NotNull Block, @NotNull ElementContainerBlock> CONTAINER = register(ElementContainerBlock.NAME, ElementContainerBlock::new, ECProperties.Blocks.container());
	public static final DeferredHolder<@NotNull Block, @NotNull ReservoirBlock> FIRE_RESERVOIR = register(ReservoirBlock.NAME_FIRE, p -> new ReservoirBlock(ElementType.FIRE, p), ECProperties.Blocks.container());
	public static final DeferredHolder<@NotNull Block, @NotNull ReservoirBlock> WATER_RESERVOIR = register(ReservoirBlock.NAME_WATER, p -> new ReservoirBlock(ElementType.WATER, p), ECProperties.Blocks.container());
	public static final DeferredHolder<@NotNull Block, @NotNull ReservoirBlock> EARTH_RESERVOIR = register(ReservoirBlock.NAME_EARTH, p -> new ReservoirBlock(ElementType.EARTH, p), ECProperties.Blocks.container());
	public static final DeferredHolder<@NotNull Block, @NotNull ReservoirBlock> AIR_RESERVOIR = register(ReservoirBlock.NAME_AIR, p -> new ReservoirBlock(ElementType.AIR, p), ECProperties.Blocks.container());
	public static final DeferredHolder<@NotNull Block, @NotNull CreativeElementContainerBlock> CREATIVE_CONTAINER = register(CreativeElementContainerBlock.NAME, CreativeElementContainerBlock::new, ECProperties.Blocks.container());
	public static final DeferredHolder<@NotNull Block, @NotNull RudimentaryElementExtractorBlock> RUDIMENTARY_EXTRACTOR = registerDefault(RudimentaryElementExtractorBlock.NAME, RudimentaryElementExtractorBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull ElementExtractorBlock> EXTRACTOR = registerDefault(ElementExtractorBlock.NAME, ElementExtractorBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull ImprovedElementExtractorBlock> IMPROVED_EXTRACTOR = registerDefault(ImprovedElementExtractorBlock.NAME, ImprovedElementExtractorBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull CrackingSynthesizerBlock> CRACKING_SYNTHESIZER = registerDefault(CrackingSynthesizerBlock.NAME, CrackingSynthesizerBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull CombustionSynthesizerBlock> COMBUSTION_SYNTHESIZER = registerDefault(CombustionSynthesizerBlock.NAME, CombustionSynthesizerBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull DrainingSynthesizerBlock> DRAINING_SYNTHESIZER = registerDefault(DrainingSynthesizerBlock.NAME, DrainingSynthesizerBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull VibrationSynthesizerBlock> VIBRATION_SYNTHESIZER = registerDefault(VibrationSynthesizerBlock.NAME, VibrationSynthesizerBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull SolarSynthesizerBlock> SOLAR_SYNTHESIZER = registerDefault(SolarSynthesizerBlock.NAME, SolarSynthesizerBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull CulinarySynthesizerBlock> CULINARY_SYNTHESIZER = registerDefault(CulinarySynthesizerBlock.NAME, CulinarySynthesizerBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull SculkCrackingSynthesizerBlock> SCULK_CRACKING_SYNTHESIZER = registerDefault(SculkCrackingSynthesizerBlock.NAME, SculkCrackingSynthesizerBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull AirMillSynthesizerBlock> AIR_MILL_SYNTHESIZER = registerDefault(AirMillSynthesizerBlock.NAME, AirMillSynthesizerBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull DiffuserBlock> DIFFUSER = registerDefault(DiffuserBlock.NAME, DiffuserBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull InfuserBlock> INFUSER = registerDefault(InfuserBlock.NAME, InfuserBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull BinderBlock> BINDER = registerDefault(BinderBlock.NAME, BinderBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull ImprovedBinderBlock> BINDER_IMPROVED = registerDefault(ImprovedBinderBlock.NAME, ImprovedBinderBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull CrystallizerBlock> CRYSTALLIZER = registerDefault(CrystallizerBlock.NAME, CrystallizerBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull InscriberBlock> INSCRIBER = registerDefault(InscriberBlock.NAME, InscriberBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull WaterMillGrindstoneBlock> WATER_MILL_GRINDSTONE = registerDefault(WaterMillGrindstoneBlock.NAME, WaterMillGrindstoneBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull AirMillGrindstoneBlock> AIR_MILL_GRINDSTONE = registerDefault(AirMillGrindstoneBlock.NAME, AirMillGrindstoneBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull WaterMillWoodSawBlock> WATER_MILL_WOOD_SAW = registerDefault(WaterMillWoodSawBlock.NAME, WaterMillWoodSawBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull AirMillWoodSawBlock> AIR_MILL_WOOD_SAW = registerDefault(AirMillWoodSawBlock.NAME, AirMillWoodSawBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull EnchantmentLiquefierBlock> ENCHANTMENT_LIQUEFIER = registerDefault(EnchantmentLiquefierBlock.NAME, EnchantmentLiquefierBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull PedestalBlock> FIRE_PEDESTAL = register(PedestalBlock.NAME_FIRE, p -> new PedestalBlock(ElementType.FIRE, p), ECProperties.Blocks.defaultProperties());
	public static final DeferredHolder<@NotNull Block, @NotNull PedestalBlock> WATER_PEDESTAL = register(PedestalBlock.NAME_WATER, p -> new PedestalBlock(ElementType.WATER, p), ECProperties.Blocks.defaultProperties());
	public static final DeferredHolder<@NotNull Block, @NotNull PedestalBlock> EARTH_PEDESTAL = register(PedestalBlock.NAME_EARTH, p -> new PedestalBlock(ElementType.EARTH, p), ECProperties.Blocks.defaultProperties());
	public static final DeferredHolder<@NotNull Block, @NotNull PedestalBlock> AIR_PEDESTAL = register(PedestalBlock.NAME_AIR, p -> new PedestalBlock(ElementType.AIR, p), ECProperties.Blocks.defaultProperties());
	public static final DeferredHolder<@NotNull Block, @NotNull PureInfuserBlock> PURE_INFUSER = registerDefault(PureInfuserBlock.NAME, PureInfuserBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull FireFurnaceBlock> FIRE_FURNACE = registerDefault(FireFurnaceBlock.NAME, FireFurnaceBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull FireBlastFurnaceBlock> FIRE_BLAST_FURNACE = registerDefault(FireBlastFurnaceBlock.NAME, FireBlastFurnaceBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull PurifierBlock> PURIFIER = registerDefault(PurifierBlock.NAME, PurifierBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull ElementPipeBlock> PIPE_RUDIMENTARY = register(ElementPipeBlock.NAME_RUDIMENTARY, p -> new ElementPipeBlock(ElementPipeBlock.PipeType.RUDIMENTARY, p), ECProperties.Blocks.pipe());
	public static final DeferredHolder<@NotNull Block, @NotNull ElementPipeBlock> PIPE = register(ElementPipeBlock.NAME, p -> new ElementPipeBlock(ElementPipeBlock.PipeType.STANDARD, p), ECProperties.Blocks.pipe());
	public static final DeferredHolder<@NotNull Block, @NotNull ElementPipeBlock> PIPE_IMPROVED = register(ElementPipeBlock.NAME_IMPROVED, p -> new ElementPipeBlock(ElementPipeBlock.PipeType.IMPROVED, p), ECProperties.Blocks.pipe());
	public static final DeferredHolder<@NotNull Block, @NotNull ElementPipeBlock> PIPE_CREATIVE = register(ElementPipeBlock.NAME_CREATIVE, p -> new ElementPipeBlock(ElementPipeBlock.PipeType.CREATIVE, p), ECProperties.Blocks.pipe());
	public static final DeferredHolder<@NotNull Block, @NotNull RetrieverBlock> RETRIEVER = registerDefault(RetrieverBlock.NAME, RetrieverBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull OrderedSorterBlock> ORDERED_SORTER = registerNoOcclusion(OrderedSorterBlock.NAME, OrderedSorterBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull SpellDeskBlock> SPELL_DESK = registerDefault(SpellDeskBlock.NAME, SpellDeskBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull FirePylonBlock> FIRE_PYLON = registerNoOcclusion(FirePylonBlock.NAME, FirePylonBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull VacuumShrineBlock> VACUUM_SHRINE = registerNoOcclusion(VacuumShrineBlock.NAME, VacuumShrineBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull GrowthShrineBlock> GROWTH_SHRINE = registerNoOcclusion(GrowthShrineBlock.NAME, GrowthShrineBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull HarvestShrineBlock> HARVEST_SHRINE = registerNoOcclusion(HarvestShrineBlock.NAME, HarvestShrineBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull LumberShrineBlock> LUMBER_SHRINE = registerNoOcclusion(LumberShrineBlock.NAME, LumberShrineBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull MeltingShrineBlock> MELTING_SHRINE = registerNoOcclusion(MeltingShrineBlock.NAME, MeltingShrineBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull OreShrineBlock> ORE_SHRINE = registerNoOcclusion(OreShrineBlock.NAME, OreShrineBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull OverloadShrineBlock> OVERLOAD_SHRINE = registerNoOcclusion(OverloadShrineBlock.NAME, OverloadShrineBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull SweetShrineBlock> SWEET_SHRINE = registerNoOcclusion(SweetShrineBlock.NAME, SweetShrineBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull EnderLockShrineBlock> ENDER_LOCK_SHRINE = registerNoOcclusion(EnderLockShrineBlock.NAME, EnderLockShrineBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull BreedingShrineBlock> BREEDING_SHRINE = registerNoOcclusion(BreedingShrineBlock.NAME, BreedingShrineBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull GroveShrineBlock> GROVE_SHRINE = registerNoOcclusion(GroveShrineBlock.NAME, GroveShrineBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull SpringShrineBlock> SPRING_SHRINE = registerNoOcclusion(SpringShrineBlock.NAME, SpringShrineBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull BuddingShrineBlock> BUDDING_SHRINE = registerNoOcclusion(BuddingShrineBlock.NAME, BuddingShrineBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull SpawningShrineBlock> SPAWNING_SHRINE = registerNoOcclusion(SpawningShrineBlock.NAME, SpawningShrineBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull AccelerationShrineUpgradeBlock> ACCELERATION_SHRINE_UPGRADE = registerNoOcclusion(AccelerationShrineUpgradeBlock.NAME, AccelerationShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull OverclockedAccelerationShrineUpgradeBlock> OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE = registerNoOcclusion(OverclockedAccelerationShrineUpgradeBlock.NAME, OverclockedAccelerationShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull RangeShrineUpgradeBlock> RANGE_SHRINE_UPGRADE = registerNoOcclusion(RangeShrineUpgradeBlock.NAME, RangeShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull CapacityShrineUpgradeBlock> CAPACITY_SHRINE_UPGRADE = registerNoOcclusion(CapacityShrineUpgradeBlock.NAME, CapacityShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull EfficiencyShrineUpgradeBlock> EFFICIENCY_SHRINE_UPGRADE = registerNoOcclusion(EfficiencyShrineUpgradeBlock.NAME, EfficiencyShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull StrengthShrineUpgradeBlock> STRENGTH_SHRINE_UPGRADE = registerNoOcclusion(StrengthShrineUpgradeBlock.NAME, StrengthShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull OverwhelmingStrengthShrineUpgradeBlock> OVERWHELMING_STRENGTH_SHRINE_UPGRADE = registerNoOcclusion(OverwhelmingStrengthShrineUpgradeBlock.NAME, OverwhelmingStrengthShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull OptimizationShrineUpgradeBlock> OPTIMIZATION_SHRINE_UPGRADE = registerNoOcclusion(OptimizationShrineUpgradeBlock.NAME, OptimizationShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull FortuneShrineUpgradeBlock> FORTUNE_SHRINE_UPGRADE = registerNoOcclusion(FortuneShrineUpgradeBlock.NAME, FortuneShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull GreaterFortuneShrineUpgradeBlock> GREATER_FORTUNE_SHRINE_UPGRADE = registerNoOcclusion(GreaterFortuneShrineUpgradeBlock.NAME, GreaterFortuneShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull SilkTouchShrineUpgradeBlock> SILK_TOUCH_SHRINE_UPGRADE = registerNoOcclusion(SilkTouchShrineUpgradeBlock.NAME, SilkTouchShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull PlantingShrineUpgradeBlock> PLANTING_SHRINE_UPGRADE = registerNoOcclusion(PlantingShrineUpgradeBlock.NAME, PlantingShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull BonelessGrowthShrineUpgradeBlock> BONELESS_GROWTH_SHRINE_UPGRADE = registerNoOcclusion(BonelessGrowthShrineUpgradeBlock.NAME, BonelessGrowthShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull PickupShrineUpgradeBlock> PICKUP_SHRINE_UPGRADE = registerNoOcclusion(PickupShrineUpgradeBlock.NAME, PickupShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull VortexShrineUpgradeBlock> VORTEX_SHRINE_UPGRADE = registerNoOcclusion(VortexShrineUpgradeBlock.NAME, VortexShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull NectarShrineUpgradeBlock> NECTAR_SHRINE_UPGRADE = registerNoOcclusion(NectarShrineUpgradeBlock.NAME, NectarShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull MysticalGroveShrineUpgradeBlock> MYSTICAL_GROVE_SHRINE_UPGRADE = registerNoOcclusion(MysticalGroveShrineUpgradeBlock.NAME, MysticalGroveShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull StemPollinationShrineUpgradeBlock> STEM_POLLINATION_SHRINE_UPGRADE = registerNoOcclusion(StemPollinationShrineUpgradeBlock.NAME, StemPollinationShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull ProtectionShrineUpgradeBlock> PROTECTION_SHRINE_UPGRADE = registerNoOcclusion(ProtectionShrineUpgradeBlock.NAME, ProtectionShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull FillingShrineUpgradeBlock> FILLING_SHRINE_UPGRADE = registerNoOcclusion(FillingShrineUpgradeBlock.NAME, FillingShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull BudShrineUpgradeBlock> SPRINGALINE_SHRINE_UPGRADE = registerNoOcclusion(BudShrineUpgradeBlock.SPRINGALINE_NAME, p -> new BudShrineUpgradeBlock(ShrineUpgrades.SPRINGALINE, p));
	public static final DeferredHolder<@NotNull Block, @NotNull BudShrineUpgradeBlock> CERTUS_QUARTZ_SHRINE_UPGRADE = registerNoOcclusion(BudShrineUpgradeBlock.CERTUS_QUARTZ_NAME, p -> new BudShrineUpgradeBlock(ShrineUpgrades.CERTUS_QUARTZ, p));
	public static final DeferredHolder<@NotNull Block, @NotNull CrystalHarvestShrineUpgradeBlock> CRYSTAL_HARVEST_SHRINE_UPGRADE = registerNoOcclusion(CrystalHarvestShrineUpgradeBlock.NAME, CrystalHarvestShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull CrystalGrowthShrineUpgradeBlock> CRYSTAL_GROWTH_SHRINE_UPGRADE = registerNoOcclusion(CrystalGrowthShrineUpgradeBlock.NAME, CrystalGrowthShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull TranslocationShrineUpgradeBlock> TRANSLOCATION_SHRINE_UPGRADE = registerNoOcclusion(TranslocationShrineUpgradeBlock.NAME, TranslocationShrineUpgradeBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull SourceBlock> FIRE_SOURCE = register(SourceBlock.NAME_FIRE, p -> new SourceBlock(ElementType.FIRE, p), ECProperties.Blocks.source());
	public static final DeferredHolder<@NotNull Block, @NotNull SourceBlock> WATER_SOURCE = register(SourceBlock.NAME_WATER, p -> new SourceBlock(ElementType.WATER, p), ECProperties.Blocks.source());
	public static final DeferredHolder<@NotNull Block, @NotNull SourceBlock> EARTH_SOURCE = register(SourceBlock.NAME_EARTH, p -> new SourceBlock(ElementType.EARTH, p), ECProperties.Blocks.source());
	public static final DeferredHolder<@NotNull Block, @NotNull SourceBlock> AIR_SOURCE = register(SourceBlock.NAME_AIR, p -> new SourceBlock(ElementType.AIR, p), ECProperties.Blocks.source());
	public static final DeferredHolder<@NotNull Block, @NotNull SourceBreederBlock> SOURCE_BREEDER = registerDefault(SourceBreederBlock.NAME, SourceBreederBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull SourceBreederPedestalBlock> SOURCE_BREEDER_PEDESTAL = registerNoOcclusion(SourceBreederPedestalBlock.NAME, SourceBreederPedestalBlock::new);
	public static final DeferredHolder<@NotNull Block, @NotNull TranslocationAnchorBlock> TRANSLOCATION_ANCHOR = registerDefault(TranslocationAnchorBlock.NAME, TranslocationAnchorBlock::new);


	public static final DeferredHolder<@NotNull Block, @NotNull DropExperienceBlock> CRYSTAL_ORE = register("inert_crystal_ore", p -> new DropExperienceBlock(UniformInt.of(0, 3), p), BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
	public static final DeferredHolder<@NotNull Block, @NotNull DropExperienceBlock> DEEPSLATE_CRYSTAL_ORE = register("deepslate_inert_crystal_ore", p -> new DropExperienceBlock(UniformInt.of(0, 3), p), BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
	public static final DeferredHolder<@NotNull Block, @NotNull Block> WHITE_ROCK = registerSimple("whiterock", ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull SlabBlock> WHITE_ROCK_SLAB = registerSlab(WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull StairBlock> WHITE_ROCK_STAIRS = registerStairs(WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull WallBlock> WHITE_ROCK_WALL = registerWall(WHITE_ROCK, ECProperties.Blocks.whiterock());

	public static final DeferredHolder<@NotNull Block, @NotNull FenceBlock> WHITE_ROCK_FENCE = registerFence(WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull Block> WHITE_ROCK_BRICKS = registerSimple("whiterock_brick", ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull SlabBlock> WHITE_ROCK_BRICK_SLAB = registerSlab(WHITE_ROCK_BRICKS, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull StairBlock> WHITE_ROCK_BRICK_STAIRS = registerStairs(WHITE_ROCK_BRICKS, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull WallBlock> WHITE_ROCK_BRICK_WALL = registerWall(WHITE_ROCK_BRICKS, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull Block> MOSSY_WHITE_ROCK = registerSimple("whiterock_mossy", ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull SlabBlock> MOSSY_WHITE_ROCK_SLAB = registerSlab(MOSSY_WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull StairBlock> MOSSY_WHITE_ROCK_STAIRS = registerStairs(MOSSY_WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull WallBlock> MOSSY_WHITE_ROCK_WALL = registerWall(MOSSY_WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull Block> BURNT_WHITE_ROCK = registerSimple("whiterock_burnt", ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull SlabBlock> BURNT_WHITE_ROCK_SLAB = registerSlab(BURNT_WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull StairBlock> BURNT_WHITE_ROCK_STAIRS = registerStairs(BURNT_WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull WallBlock> BURNT_WHITE_ROCK_WALL = registerWall(BURNT_WHITE_ROCK, ECProperties.Blocks.whiterock());
	public static final DeferredHolder<@NotNull Block, @NotNull Block> PURE_ROCK = registerSimple("purerock", ECProperties.Blocks.purerock());
	public static final DeferredHolder<@NotNull Block, @NotNull SlabBlock> PURE_ROCK_SLAB = registerSlab(PURE_ROCK, ECProperties.Blocks.purerock());
	public static final DeferredHolder<@NotNull Block, @NotNull StairBlock> PURE_ROCK_STAIRS = registerStairs(PURE_ROCK, ECProperties.Blocks.purerock());
	public static final DeferredHolder<@NotNull Block, @NotNull WallBlock> PURE_ROCK_WALL = registerWall(PURE_ROCK, ECProperties.Blocks.purerock());
	public static final DeferredHolder<@NotNull Block, @NotNull TransparentBlock> BURNT_GLASS = registerGlass("burnt_glass");
	public static final DeferredHolder<@NotNull Block, @NotNull IronBarsBlock> BURNT_GLASS_PANE = registerGlassPane(BURNT_GLASS);
	public static final DeferredHolder<@NotNull Block, @NotNull Block> DRENCHED_IRON_BLOCK = registerSimple("drenched_iron_block", BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
	public static final DeferredHolder<@NotNull Block, @NotNull Block> SWIFT_ALLOY_BLOCK = registerSimple("swift_alloy_block", BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK));
	public static final DeferredHolder<@NotNull Block, @NotNull Block> FIREITE_BLOCK = registerSimple("fireite_block", BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK));
	public static final DeferredHolder<@NotNull Block, @NotNull Block> INERT_CRYSTAL_BLOCK = registerSimple("inert_crystal_block");
	public static final DeferredHolder<@NotNull Block, @NotNull Block>FIRE_CRYSTAL_BLOCK = registerSimple("firecrystal_block");
	public static final DeferredHolder<@NotNull Block, @NotNull Block> WATER_CRYSTAL_BLOCK = registerSimple("watercrystal_block");
	public static final DeferredHolder<@NotNull Block, @NotNull Block> EARTH_CRYSTAL_BLOCK = registerSimple("earthcrystal_block");
	public static final DeferredHolder<@NotNull Block, @NotNull Block> AIR_CRYSTAL_BLOCK = registerSimple("aircrystal_block");
	public static final DeferredHolder<@NotNull Block, @NotNull AmethystBlock> SPRINGALINE_BLOCK = register("springaline_block", AmethystBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK));
	public static final DeferredHolder<@NotNull Block, @NotNull AmethystClusterBlock> SPRINGALINE_CLUSTER = register("springaline_cluster", p -> new AmethystClusterBlock(7, 3, p), BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER));
	public static final DeferredHolder<@NotNull Block, @NotNull AmethystClusterBlock> LARGE_SPRINGALINE_BUD = register("large_springaline_bud", p -> new AmethystClusterBlock(5, 3, p), BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_AMETHYST_BUD));
	public static final DeferredHolder<@NotNull Block, @NotNull AmethystClusterBlock> MEDIUM_SPRINGALINE_BUD = register("medium_springaline_bud", p -> new AmethystClusterBlock(4, 3, p), BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD));
	public static final DeferredHolder<@NotNull Block, @NotNull AmethystClusterBlock> SMALL_SPRINGALINE_BUD = register("small_springaline_bud", p -> new AmethystClusterBlock(3, 4, p), BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD));
	public static final DeferredHolder<@NotNull Block, @NotNull TransparentBlock> SPRINGALINE_GLASS = registerGlass("springaline_glass");
	public static final DeferredHolder<@NotNull Block, @NotNull IronBarsBlock> SPRINGALINE_GLASS_PANE = registerGlassPane(SPRINGALINE_GLASS);
	public static final DeferredHolder<@NotNull Block, @NotNull Block> SPRINGALINE_LANTERN = register("springaline_lantern", Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SEA_LANTERN));

	public static final DeferredHolder<@NotNull Block, @NotNull ElementalEmberBlock> ELEMENTAL_EMBER = register(ElementalEmberBlock.NAME, ElementalEmberBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TORCH));

	private static <T extends Block> DeferredHolder<@NotNull Block, @NotNull T> register(String name, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties properties) {
		var id = ResourceKey.create(Registries.BLOCK, ElementalCraftApi.createRL(name));
		return DEFERRED_REGISTER.register(name, () -> factory.apply(properties.setId(id)));
	}

	private static <T extends Block> DeferredHolder<@NotNull Block, @NotNull T> registerDefault(String name, Function<BlockBehaviour.Properties, T> block) {
		return register(name, block, ECProperties.Blocks.defaultProperties());
	}

	private static <T extends Block> DeferredHolder<@NotNull Block, @NotNull T> registerNoOcclusion(String name, Function<BlockBehaviour.Properties, T> block) {
		return register(name, block, ECProperties.Blocks.noOcclusion());
	}

	private static DeferredHolder<@NotNull Block, @NotNull Block> registerSimple(String name, BlockBehaviour.Properties properties) {
		return register(name, Block::new, properties);
	}

	private static DeferredHolder<@NotNull Block, @NotNull Block> registerSimple(String name) {
		return registerSimple(name, ECProperties.Blocks.defaultProperties());
	}

	private static <T extends Block> DeferredHolder<@NotNull Block, @NotNull SlabBlock> registerSlab(DeferredHolder<@NotNull Block, @NotNull T> block, BlockBehaviour.Properties properties) {
		return register(block.getId().getPath() + "_slab", SlabBlock::new, properties);
	}

	private static <T extends Block> DeferredHolder<@NotNull Block, @NotNull StairBlock> registerStairs(DeferredHolder<@NotNull Block, @NotNull T> block, BlockBehaviour.Properties properties) {
		return register(block.getId().getPath() + "_stairs", p -> new StairBlock(block.get().defaultBlockState(), p), properties);
	}

	private static <T extends Block> DeferredHolder<@NotNull Block, @NotNull WallBlock> registerWall(DeferredHolder<@NotNull Block, @NotNull T> block, BlockBehaviour.Properties properties) {
		return register(block.getId().getPath() + "_wall", WallBlock::new, properties);
	}

	private static <T extends Block> DeferredHolder<@NotNull Block, @NotNull FenceBlock> registerFence(DeferredHolder<@NotNull Block, @NotNull T> block, BlockBehaviour.Properties properties) {
		return register(block.getId().getPath() + "_fence", FenceBlock::new, properties);
	}

	private static DeferredHolder<@NotNull Block, @NotNull TransparentBlock> registerGlass(String name) {
		return register(name, TransparentBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));
	}
	private static DeferredHolder<@NotNull Block, @NotNull IronBarsBlock> registerGlassPane(DeferredHolder<@NotNull Block, ? extends @NotNull TransparentBlock> block) {
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
