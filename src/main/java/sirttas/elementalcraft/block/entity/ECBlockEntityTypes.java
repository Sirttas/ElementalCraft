package sirttas.elementalcraft.block.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.ElementContainerBlock;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.container.creative.CreativeElementContainerBlockEntity;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlock;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlockEntity;
import sirttas.elementalcraft.block.diffuser.DiffuserBlockEntity;
import sirttas.elementalcraft.block.evaporator.EvaporatorBlockEntity;
import sirttas.elementalcraft.block.extractor.ExtractorBlock;
import sirttas.elementalcraft.block.extractor.ExtractorBlockEntity;
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
import sirttas.elementalcraft.block.shrine.lava.LavaShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.lumber.LumberShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.ore.OreShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.overload.OverloadShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.spawning.SpawningShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.spring.SpringShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.sweet.SweetShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked.OverclockedAccelerationShrineUpgradeBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.vortex.VortexShrineUpgradeBlockEntity;
import sirttas.elementalcraft.block.shrine.vacuum.VacuumShrineBlockEntity;
import sirttas.elementalcraft.block.sorter.SorterBlockEntity;
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.block.source.breeder.SourceBreederBlockEntity;
import sirttas.elementalcraft.block.source.breeder.pedestal.SourceBreederPedestalBlockEntity;
import sirttas.elementalcraft.block.source.displacement.plate.SourceDisplacementPlateBlock;
import sirttas.elementalcraft.block.source.displacement.plate.SourceDisplacementPlateBlockEntity;
import sirttas.elementalcraft.block.synthesizer.mana.ManaSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerBlockEntity;

import java.util.Arrays;
import java.util.function.Supplier;

public class ECBlockEntityTypes {

    private static final DeferredRegister<BlockEntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ElementalCraftApi.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SourceBlockEntity>> SOURCE = register(SourceBlockEntity::new, ECBlocks.SOURCE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ElementContainerBlockEntity>> CONTAINER = register(() -> builder(ElementContainerBlockEntity::new, ECBlocks.CONTAINER, ECBlocks.SMALL_CONTAINER), ElementContainerBlock.NAME);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ReservoirBlockEntity>> RESERVOIR = register(() -> builder(ReservoirBlockEntity::new, ECBlocks.FIRE_RESERVOIR,ECBlocks. WATER_RESERVOIR, ECBlocks.EARTH_RESERVOIR, ECBlocks.AIR_RESERVOIR), ReservoirBlock.NAME);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CreativeElementContainerBlockEntity>> CREATIVE_CONTAINER = register(CreativeElementContainerBlockEntity::new, ECBlocks.CREATIVE_CONTAINER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExtractorBlockEntity>> EXTRACTOR = register(() -> builder(ExtractorBlockEntity::new, ECBlocks.EXTRACTOR, ECBlocks.EXTRACTOR_IMPROVED), ExtractorBlock.NAME);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EvaporatorBlockEntity>> EVAPORATOR = register(EvaporatorBlockEntity::new, ECBlocks.EVAPORATOR);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SolarSynthesizerBlockEntity>> SOLAR_SYNTHESIZER = register(SolarSynthesizerBlockEntity::new, ECBlocks.SOLAR_SYNTHESIZER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ManaSynthesizerBlockEntity>> MANA_SYNTHESIZER = register(ManaSynthesizerBlockEntity::new, ECBlocks.MANA_SYNTHESIZER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DiffuserBlockEntity>> DIFFUSER = register(DiffuserBlockEntity::new, ECBlocks.DIFFUSER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfuserBlockEntity>> INFUSER = register(InfuserBlockEntity::new, ECBlocks.INFUSER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BinderBlockEntity>> BINDER = register(BinderBlockEntity::new, ECBlocks.BINDER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ImprovedBinderBlockEntity>> BINDER_IMPROVED = register(ImprovedBinderBlockEntity::new, ECBlocks.BINDER_IMPROVED);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrystallizerBlockEntity>> CRYSTALLIZER = register(CrystallizerBlockEntity::new, ECBlocks.CRYSTALLIZER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InscriberBlockEntity>> INSCRIBER = register(InscriberBlockEntity::new, ECBlocks.INSCRIBER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WaterMillGrindstoneBlockEntity>> WATER_MILL_GRINDSTONE = register(WaterMillGrindstoneBlockEntity::new, ECBlocks.WATER_MILL_GRINDSTONE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AirMillGrindstoneBlockEntity>> AIR_MILL_GRINDSTONE = register(AirMillGrindstoneBlockEntity::new, ECBlocks.AIR_MILL_GRINDSTONE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WaterMillWoodSawBlockEntity>> WATER_MILL_WOOD_SAW = register(WaterMillWoodSawBlockEntity::new, ECBlocks.WATER_MILL_WOOD_SAW);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AirMillWoodSawBlockEntity>> AIR_MILL_WOOD_SAW = register(AirMillWoodSawBlockEntity::new, ECBlocks.AIR_MILL_WOOD_SAW);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnchantmentLiquefierBlockEntity>> ENCHANTMENT_LIQUEFIER = register(EnchantmentLiquefierBlockEntity::new, ECBlocks.ENCHANTMENT_LIQUEFIER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PedestalBlockEntity>> PEDESTAL = register(() -> builder(PedestalBlockEntity::new, ECBlocks.FIRE_PEDESTAL, ECBlocks.WATER_PEDESTAL, ECBlocks.EARTH_PEDESTAL, ECBlocks.AIR_PEDESTAL), PedestalBlock.NAME);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PureInfuserBlockEntity>> PURE_INFUSER = register(PureInfuserBlockEntity::new, ECBlocks.PURE_INFUSER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FireFurnaceBlockEntity>> FIRE_FURNACE = register(FireFurnaceBlockEntity::new, ECBlocks.FIRE_FURNACE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FireBlastFurnaceBlockEntity>> FIRE_BLAST_FURNACE = register(FireBlastFurnaceBlockEntity::new, ECBlocks.FIRE_BLAST_FURNACE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PurifierBlockEntity>> PURIFIER = register(PurifierBlockEntity::new, ECBlocks.PURIFIER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ElementPipeBlockEntity>> PIPE = register(() -> builder(ElementPipeBlockEntity::new, ECBlocks.PIPE_IMPAIRED, ECBlocks.PIPE, ECBlocks.PIPE_IMPROVED, ECBlocks.PIPE_CREATIVE), ElementPipeBlock.NAME);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FirePylonBlockEntity>> FIRE_PYLON = register(FirePylonBlockEntity::new, ECBlocks.FIRE_PYLON);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<VacuumShrineBlockEntity>> VACUUM_SHRINE = register(VacuumShrineBlockEntity::new, ECBlocks.VACUUM_SHRINE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GrowthShrineBlockEntity>> GROWTH_SHRINE = register(GrowthShrineBlockEntity::new, ECBlocks.GROWTH_SHRINE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HarvestShrineBlockEntity>> HARVEST_SHRINE = register(HarvestShrineBlockEntity::new, ECBlocks.HARVEST_SHRINE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LumberShrineBlockEntity>> LUMBER_SHRINE = register(LumberShrineBlockEntity::new, ECBlocks.LUMBER_SHRINE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LavaShrineBlockEntity>> LAVA_SHRINE = register(LavaShrineBlockEntity::new, ECBlocks.LAVA_SHRINE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OreShrineBlockEntity>> ORE_SHRINE = register(OreShrineBlockEntity::new, ECBlocks.ORE_SHRINE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OverloadShrineBlockEntity>> OVERLOAD_SHRINE = register(OverloadShrineBlockEntity::new, ECBlocks.OVERLOAD_SHRINE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SweetShrineBlockEntity>> SWEET_SHRINE = register(SweetShrineBlockEntity::new, ECBlocks.SWEET_SHRINE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnderLockShrineBlockEntity>> ENDER_LOCK_SHRINE = register(EnderLockShrineBlockEntity::new, ECBlocks.ENDER_LOCK_SHRINE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BreedingShrineBlockEntity>> BREEDING_SHRINE = register(BreedingShrineBlockEntity::new, ECBlocks.BREEDING_SHRINE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GroveShrineBlockEntity>> GROVE_SHRINE = register(GroveShrineBlockEntity::new, ECBlocks.GROVE_SHRINE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpringShrineBlockEntity>> SPRING_SHRINE = register(SpringShrineBlockEntity::new, ECBlocks.SPRING_SHRINE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BuddingShrineBlockEntity>> BUDDING_SHRINE = register(BuddingShrineBlockEntity::new, ECBlocks.BUDDING_SHRINE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpawningShrineBlockEntity>> SPAWNING_SHRINE = register(SpawningShrineBlockEntity::new, ECBlocks.SPAWNING_SHRINE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AccelerationShrineUpgradeBlockEntity>> ACCELERATION_SHRINE_UPGRADE = register(AccelerationShrineUpgradeBlockEntity::new, ECBlocks.ACCELERATION_SHRINE_UPGRADE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OverclockedAccelerationShrineUpgradeBlockEntity>> OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE = register(OverclockedAccelerationShrineUpgradeBlockEntity::new, ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TranslocationShrineUpgradeBlockEntity>> TRANSLOCATION_SHRINE_UPGRADE = register(TranslocationShrineUpgradeBlockEntity::new, ECBlocks.TRANSLOCATION_SHRINE_UPGRADE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<VortexShrineUpgradeBlockEntity>> VORTEX_SHRINE_UPGRADE = register(VortexShrineUpgradeBlockEntity::new, ECBlocks.VORTEX_SHRINE_UPGRADE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SorterBlockEntity>> SORTER = register(SorterBlockEntity::new, ECBlocks.SORTER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SourceDisplacementPlateBlockEntity>> SOURCE_DISPLACEMENT_PLATE = register(() -> builder(SourceDisplacementPlateBlockEntity::new, ECBlocks.FIRE_SOURCE_DISPLACEMENT_PLATE, ECBlocks.WATER_SOURCE_DISPLACEMENT_PLATE, ECBlocks.EARTH_SOURCE_DISPLACEMENT_PLATE, ECBlocks.AIR_SOURCE_DISPLACEMENT_PLATE), SourceDisplacementPlateBlock.NAME);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SourceBreederBlockEntity>> SOURCE_BREEDER = register(SourceBreederBlockEntity::new, ECBlocks.SOURCE_BREEDER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SourceBreederPedestalBlockEntity>> SOURCE_BREEDER_PEDESTAL = register(SourceBreederPedestalBlockEntity::new, ECBlocks.SOURCE_BREEDER_PEDESTAL);


    private ECBlockEntityTypes() {}

    @SafeVarargs
    private static <T extends BlockEntity> BlockEntityType.Builder<T> builder(BlockEntityType.BlockEntitySupplier<T> factory, DeferredHolder<Block, ? extends Block>... validBlocks) {
        return BlockEntityType.Builder.of(factory, Arrays.stream(validBlocks)
                .map(DeferredHolder::get)
                .toArray(Block[]::new));
    }

    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(BlockEntityType.BlockEntitySupplier<T> factory, DeferredHolder<Block, ? extends Block> block) {
        return register(() -> builder(factory, block), block.getId().getPath());
    }

    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(Supplier<BlockEntityType.Builder<T>> builder, String name) {
        return DEFERRED_REGISTER.register(name, () -> builder.get().build(null));
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }
}
