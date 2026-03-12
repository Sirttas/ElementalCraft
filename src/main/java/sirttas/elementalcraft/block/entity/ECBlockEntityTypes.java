package sirttas.elementalcraft.block.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.ElementContainerBlock;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.container.creative.CreativeElementContainerBlockEntity;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlock;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlockEntity;
import sirttas.elementalcraft.block.cover.CoverableBlockEntity;
import sirttas.elementalcraft.block.diffuser.DiffuserBlockEntity;
import sirttas.elementalcraft.block.extractor.ElementExtractorBlock;
import sirttas.elementalcraft.block.extractor.ElementExtractorBlockEntity;
import sirttas.elementalcraft.block.instrument.binder.BinderBlockEntity;
import sirttas.elementalcraft.block.instrument.binder.improved.ImprovedBinderBlockEntity;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerBlockEntity;
import sirttas.elementalcraft.block.instrument.enchantment.liquefier.EnchantmentLiquefierBlockEntity;
import sirttas.elementalcraft.block.instrument.infuser.InfuserBlockEntity;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberBlockEntity;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.io.firefurnace.blast.FireBlastFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.air.AirMillGrindstoneBlockEntity;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.water.WaterMillGrindstoneBlockEntity;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.air.AirMillWoodSawBlockEntity;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.water.WaterMillWoodSawBlockEntity;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlockEntity;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlockEntity;
import sirttas.elementalcraft.block.shrine.breeding.BreedingShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.enderlock.EnderLockShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.firepylon.FirePylonBlockEntity;
import sirttas.elementalcraft.block.shrine.grove.GroveShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.growth.GrowthShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.harvest.HarvestShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.lumber.LumberShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.melting.MeltingShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.ore.OreShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.overload.OverloadShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.spawning.SpawningShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.spring.SpringShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.sweet.SweetShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked.OverclockedAccelerationShrineUpgradeBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.fortune.greater.GreaterFortuneShrineUpgradeBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.vortex.VortexShrineUpgradeBlockEntity;
import sirttas.elementalcraft.block.shrine.vacuum.VacuumShrineBlockEntity;
import sirttas.elementalcraft.block.sorter.ordered.OrderedSorterBlockEntity;
import sirttas.elementalcraft.block.source.SourceBlock;
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.block.source.breeder.SourceBreederBlockEntity;
import sirttas.elementalcraft.block.source.breeder.pedestal.SourceBreederPedestalBlockEntity;
import sirttas.elementalcraft.block.synthesizer.combustion.CombustionSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.cracking.CrackingSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.cracking.sculk.SculkCrackingSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.culinary.CulinarySynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.draining.DrainingSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.mill.AirMillSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.vibration.VibrationSynthesizerBlockEntity;

import java.util.Arrays;

public class ECBlockEntityTypes {

    private static final DeferredRegister<@NotNull BlockEntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ElementalCraftApi.MODID);

    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull SourceBlockEntity>> SOURCE = register(SourceBlock.NAME, SourceBlockEntity::new, ECBlocks.FIRE_SOURCE, ECBlocks.WATER_SOURCE, ECBlocks.EARTH_SOURCE, ECBlocks.AIR_SOURCE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull ElementContainerBlockEntity>> CONTAINER = register(ElementContainerBlock.NAME, ElementContainerBlockEntity::new, ECBlocks.CONTAINER, ECBlocks.SMALL_CONTAINER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull ReservoirBlockEntity>> RESERVOIR = register(ReservoirBlock.NAME, ReservoirBlockEntity::new,ECBlocks.FIRE_RESERVOIR, ECBlocks. WATER_RESERVOIR, ECBlocks.EARTH_RESERVOIR, ECBlocks.AIR_RESERVOIR);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull CreativeElementContainerBlockEntity>> CREATIVE_CONTAINER = register(CreativeElementContainerBlockEntity::new, ECBlocks.CREATIVE_CONTAINER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull ElementExtractorBlockEntity>> EXTRACTOR = register(ElementExtractorBlock.NAME, ElementExtractorBlockEntity::new, ECBlocks.RUDIMENTARY_EXTRACTOR, ECBlocks.EXTRACTOR, ECBlocks.IMPROVED_EXTRACTOR);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull CrackingSynthesizerBlockEntity>> CRACKING_SYNTHESIZER = register(CrackingSynthesizerBlockEntity::new, ECBlocks.CRACKING_SYNTHESIZER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull CombustionSynthesizerBlockEntity>> COMBUSTION_SYNTHESIZER = register(CombustionSynthesizerBlockEntity::new, ECBlocks.COMBUSTION_SYNTHESIZER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull DrainingSynthesizerBlockEntity>> DRAINING_SYNTHESIZER = register(DrainingSynthesizerBlockEntity::new, ECBlocks.DRAINING_SYNTHESIZER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull VibrationSynthesizerBlockEntity>> VIBRATION_SYNTHESIZER = register(VibrationSynthesizerBlockEntity::new, ECBlocks.VIBRATION_SYNTHESIZER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull SolarSynthesizerBlockEntity>> SOLAR_SYNTHESIZER = register(SolarSynthesizerBlockEntity::new, ECBlocks.SOLAR_SYNTHESIZER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull CulinarySynthesizerBlockEntity>> CULINARY_SYNTHESIZER = register(CulinarySynthesizerBlockEntity::new, ECBlocks.CULINARY_SYNTHESIZER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull SculkCrackingSynthesizerBlockEntity>> SCULK_CRACKING_SYNTHESIZER = register(SculkCrackingSynthesizerBlockEntity::new, ECBlocks.SCULK_CRACKING_SYNTHESIZER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull AirMillSynthesizerBlockEntity>> AIR_MILL_SYNTHESIZER = register(AirMillSynthesizerBlockEntity::new, ECBlocks.AIR_MILL_SYNTHESIZER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull DiffuserBlockEntity>> DIFFUSER = register(DiffuserBlockEntity::new, ECBlocks.DIFFUSER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull InfuserBlockEntity>> INFUSER = register(InfuserBlockEntity::new, ECBlocks.INFUSER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull BinderBlockEntity>> BINDER = register(BinderBlockEntity::new, ECBlocks.BINDER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull ImprovedBinderBlockEntity>> BINDER_IMPROVED = register(ImprovedBinderBlockEntity::new, ECBlocks.BINDER_IMPROVED);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull CrystallizerBlockEntity>> CRYSTALLIZER = register(CrystallizerBlockEntity::new, ECBlocks.CRYSTALLIZER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull InscriberBlockEntity>> INSCRIBER = register(InscriberBlockEntity::new, ECBlocks.INSCRIBER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull WaterMillGrindstoneBlockEntity>> WATER_MILL_GRINDSTONE = register(WaterMillGrindstoneBlockEntity::new, ECBlocks.WATER_MILL_GRINDSTONE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull AirMillGrindstoneBlockEntity>> AIR_MILL_GRINDSTONE = register(AirMillGrindstoneBlockEntity::new, ECBlocks.AIR_MILL_GRINDSTONE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull WaterMillWoodSawBlockEntity>> WATER_MILL_WOOD_SAW = register(WaterMillWoodSawBlockEntity::new, ECBlocks.WATER_MILL_WOOD_SAW);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull AirMillWoodSawBlockEntity>> AIR_MILL_WOOD_SAW = register(AirMillWoodSawBlockEntity::new, ECBlocks.AIR_MILL_WOOD_SAW);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull EnchantmentLiquefierBlockEntity>> ENCHANTMENT_LIQUEFIER = register(EnchantmentLiquefierBlockEntity::new, ECBlocks.ENCHANTMENT_LIQUEFIER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull PedestalBlockEntity>> PEDESTAL = register(PedestalBlock.NAME, PedestalBlockEntity::new, ECBlocks.FIRE_PEDESTAL, ECBlocks.WATER_PEDESTAL, ECBlocks.EARTH_PEDESTAL, ECBlocks.AIR_PEDESTAL);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull PureInfuserBlockEntity>> PURE_INFUSER = register(PureInfuserBlockEntity::new, ECBlocks.PURE_INFUSER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull FireFurnaceBlockEntity>> FIRE_FURNACE = register(FireFurnaceBlockEntity::new, ECBlocks.FIRE_FURNACE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull FireBlastFurnaceBlockEntity>> FIRE_BLAST_FURNACE = register(FireBlastFurnaceBlockEntity::new, ECBlocks.FIRE_BLAST_FURNACE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull PurifierBlockEntity>> PURIFIER = register(PurifierBlockEntity::new, ECBlocks.PURIFIER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull ElementPipeBlockEntity>> PIPE = register(ElementPipeBlock.NAME, ElementPipeBlockEntity::new, ECBlocks.PIPE_RUDIMENTARY, ECBlocks.PIPE, ECBlocks.PIPE_IMPROVED, ECBlocks.PIPE_CREATIVE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull FirePylonBlockEntity>> FIRE_PYLON = register(FirePylonBlockEntity::new, ECBlocks.FIRE_PYLON);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull VacuumShrineBlockEntity>> VACUUM_SHRINE = register(VacuumShrineBlockEntity::new, ECBlocks.VACUUM_SHRINE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull GrowthShrineBlockEntity>> GROWTH_SHRINE = register(GrowthShrineBlockEntity::new, ECBlocks.GROWTH_SHRINE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull HarvestShrineBlockEntity>> HARVEST_SHRINE = register(HarvestShrineBlockEntity::new, ECBlocks.HARVEST_SHRINE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull LumberShrineBlockEntity>> LUMBER_SHRINE = register(LumberShrineBlockEntity::new, ECBlocks.LUMBER_SHRINE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull MeltingShrineBlockEntity>> LAVA_SHRINE = register(MeltingShrineBlockEntity::new, ECBlocks.MELTING_SHRINE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull OreShrineBlockEntity>> ORE_SHRINE = register(OreShrineBlockEntity::new, ECBlocks.ORE_SHRINE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull OverloadShrineBlockEntity>> OVERLOAD_SHRINE = register(OverloadShrineBlockEntity::new, ECBlocks.OVERLOAD_SHRINE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull SweetShrineBlockEntity>> SWEET_SHRINE = register(SweetShrineBlockEntity::new, ECBlocks.SWEET_SHRINE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull EnderLockShrineBlockEntity>> ENDER_LOCK_SHRINE = register(EnderLockShrineBlockEntity::new, ECBlocks.ENDER_LOCK_SHRINE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull BreedingShrineBlockEntity>> BREEDING_SHRINE = register(BreedingShrineBlockEntity::new, ECBlocks.BREEDING_SHRINE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull GroveShrineBlockEntity>> GROVE_SHRINE = register(GroveShrineBlockEntity::new, ECBlocks.GROVE_SHRINE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull SpringShrineBlockEntity>> SPRING_SHRINE = register(SpringShrineBlockEntity::new, ECBlocks.SPRING_SHRINE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull BuddingShrineBlockEntity>> BUDDING_SHRINE = register(BuddingShrineBlockEntity::new, ECBlocks.BUDDING_SHRINE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull SpawningShrineBlockEntity>> SPAWNING_SHRINE = register(SpawningShrineBlockEntity::new, ECBlocks.SPAWNING_SHRINE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull AccelerationShrineUpgradeBlockEntity>> ACCELERATION_SHRINE_UPGRADE = register(AccelerationShrineUpgradeBlockEntity::new, ECBlocks.ACCELERATION_SHRINE_UPGRADE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull OverclockedAccelerationShrineUpgradeBlockEntity>> OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE = register(OverclockedAccelerationShrineUpgradeBlockEntity::new, ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull GreaterFortuneShrineUpgradeBlockEntity>> GREATER_FORTUNE_SHRINE_UPGRADE = register(GreaterFortuneShrineUpgradeBlockEntity::new, ECBlocks.GREATER_FORTUNE_SHRINE_UPGRADE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull TranslocationShrineUpgradeBlockEntity>> TRANSLOCATION_SHRINE_UPGRADE = register(TranslocationShrineUpgradeBlockEntity::new, ECBlocks.TRANSLOCATION_SHRINE_UPGRADE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull VortexShrineUpgradeBlockEntity>> VORTEX_SHRINE_UPGRADE = register(VortexShrineUpgradeBlockEntity::new, ECBlocks.VORTEX_SHRINE_UPGRADE);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull OrderedSorterBlockEntity>> SORTER = register(OrderedSorterBlockEntity::new, ECBlocks.ORDERED_SORTER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull CoverableBlockEntity>> COVERABLE = register(CoverableBlockEntity::new, ECBlocks.RETRIEVER);

    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull SourceBreederBlockEntity>> SOURCE_BREEDER = register(SourceBreederBlockEntity::new, ECBlocks.SOURCE_BREEDER);
    public static final DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull SourceBreederPedestalBlockEntity>> SOURCE_BREEDER_PEDESTAL = register(SourceBreederPedestalBlockEntity::new, ECBlocks.SOURCE_BREEDER_PEDESTAL);


    private ECBlockEntityTypes() {}

    @SafeVarargs
    private static <T extends BlockEntity> DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull T>> register(String name, BlockEntityType.BlockEntitySupplier<@NotNull T> supplier, DeferredHolder<@NotNull Block, ? extends @NotNull Block>... blocks) {
        return DEFERRED_REGISTER.register(name, () -> new BlockEntityType<>(supplier, Arrays.stream(blocks)
                .map(DeferredHolder::get)
                .toArray(Block[]::new)));
    }

    private static <T extends BlockEntity> DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull T>> register(BlockEntityType.BlockEntitySupplier<@NotNull T> supplier, DeferredHolder<@NotNull Block, ? extends @NotNull Block> block) {
        return DEFERRED_REGISTER.register(block.getId().getPath(), () -> new BlockEntityType<>(supplier, block.get()));
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }
}
