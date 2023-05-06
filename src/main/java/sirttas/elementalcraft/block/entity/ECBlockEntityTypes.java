package sirttas.elementalcraft.block.entity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
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
import sirttas.elementalcraft.block.instrument.infuser.InfuserBlockEntity;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberBlockEntity;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.io.firefurnace.blast.FireBlastFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AirMillGrindstoneBlockEntity;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.WaterMillWoodSawBlockEntity;
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

    private static final DeferredRegister<BlockEntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ElementalCraftApi.MODID);

    public static final RegistryObject<BlockEntityType<SourceBlockEntity>> SOURCE = register(SourceBlockEntity::new, ECBlocks.SOURCE);
    public static final RegistryObject<BlockEntityType<ElementContainerBlockEntity>> CONTAINER = register(() -> builder(ElementContainerBlockEntity::new, ECBlocks.CONTAINER, ECBlocks.SMALL_CONTAINER), ElementContainerBlock.NAME);
    public static final RegistryObject<BlockEntityType<ReservoirBlockEntity>> RESERVOIR = register(() -> builder(ReservoirBlockEntity::new, ECBlocks.FIRE_RESERVOIR,ECBlocks. WATER_RESERVOIR, ECBlocks.EARTH_RESERVOIR, ECBlocks.AIR_RESERVOIR), ReservoirBlock.NAME);
    public static final RegistryObject<BlockEntityType<CreativeElementContainerBlockEntity>> CREATIVE_CONTAINER = register(CreativeElementContainerBlockEntity::new, ECBlocks.CREATIVE_CONTAINER);
    public static final RegistryObject<BlockEntityType<ExtractorBlockEntity>> EXTRACTOR = register(() -> builder(ExtractorBlockEntity::new, ECBlocks.EXTRACTOR, ECBlocks.EXTRACTOR_IMPROVED), ExtractorBlock.NAME);
    public static final RegistryObject<BlockEntityType<EvaporatorBlockEntity>> EVAPORATOR = register(EvaporatorBlockEntity::new, ECBlocks.EVAPORATOR);
    public static final RegistryObject<BlockEntityType<SolarSynthesizerBlockEntity>> SOLAR_SYNTHESIZER = register(SolarSynthesizerBlockEntity::new, ECBlocks.SOLAR_SYNTHESIZER);
    public static final RegistryObject<BlockEntityType<ManaSynthesizerBlockEntity>> MANA_SYNTHESIZER = register(ManaSynthesizerBlockEntity::new, ECBlocks.MANA_SYNTHESIZER);
    public static final RegistryObject<BlockEntityType<DiffuserBlockEntity>> DIFFUSER = register(DiffuserBlockEntity::new, ECBlocks.DIFFUSER);
    public static final RegistryObject<BlockEntityType<InfuserBlockEntity>> INFUSER = register(InfuserBlockEntity::new, ECBlocks.INFUSER);
    public static final RegistryObject<BlockEntityType<BinderBlockEntity>> BINDER = register(BinderBlockEntity::new, ECBlocks.BINDER);
    public static final RegistryObject<BlockEntityType<ImprovedBinderBlockEntity>> BINDER_IMPROVED = register(ImprovedBinderBlockEntity::new, ECBlocks.BINDER_IMPROVED);
    public static final RegistryObject<BlockEntityType<CrystallizerBlockEntity>> CRYSTALLIZER = register(CrystallizerBlockEntity::new, ECBlocks.CRYSTALLIZER);
    public static final RegistryObject<BlockEntityType<InscriberBlockEntity>> INSCRIBER = register(InscriberBlockEntity::new, ECBlocks.INSCRIBER);
    public static final RegistryObject<BlockEntityType<AirMillGrindstoneBlockEntity>> AIR_MILL_GRINDSTONE = register(AirMillGrindstoneBlockEntity::new, ECBlocks.AIR_MILL_GRINDSTONE);
    public static final RegistryObject<BlockEntityType<WaterMillWoodSawBlockEntity>> WATER_MILL_WOOD_SAW = register(WaterMillWoodSawBlockEntity::new, ECBlocks.WATER_MILL_WOOD_SAW);
    public static final RegistryObject<BlockEntityType<PedestalBlockEntity>> PEDESTAL = register(() -> builder(PedestalBlockEntity::new, ECBlocks.FIRE_PEDESTAL, ECBlocks.WATER_PEDESTAL, ECBlocks.EARTH_PEDESTAL, ECBlocks.AIR_PEDESTAL), PedestalBlock.NAME);
    public static final RegistryObject<BlockEntityType<PureInfuserBlockEntity>> PURE_INFUSER = register(PureInfuserBlockEntity::new, ECBlocks.PURE_INFUSER);
    public static final RegistryObject<BlockEntityType<FireFurnaceBlockEntity>> FIRE_FURNACE = register(FireFurnaceBlockEntity::new, ECBlocks.FIRE_FURNACE);
    public static final RegistryObject<BlockEntityType<FireBlastFurnaceBlockEntity>> FIRE_BLAST_FURNACE = register(FireBlastFurnaceBlockEntity::new, ECBlocks.FIRE_BLAST_FURNACE);
    public static final RegistryObject<BlockEntityType<PurifierBlockEntity>> PURIFIER = register(PurifierBlockEntity::new, ECBlocks.PURIFIER);
    public static final RegistryObject<BlockEntityType<ElementPipeBlockEntity>> PIPE = register(() -> builder(ElementPipeBlockEntity::new, ECBlocks.PIPE_IMPAIRED, ECBlocks.PIPE, ECBlocks.PIPE_IMPROVED, ECBlocks.PIPE_CREATIVE), ElementPipeBlock.NAME);
    public static final RegistryObject<BlockEntityType<FirePylonBlockEntity>> FIRE_PYLON = register(FirePylonBlockEntity::new, ECBlocks.FIRE_PYLON);
    public static final RegistryObject<BlockEntityType<VacuumShrineBlockEntity>> VACUUM_SHRINE = register(VacuumShrineBlockEntity::new, ECBlocks.VACUUM_SHRINE);
    public static final RegistryObject<BlockEntityType<GrowthShrineBlockEntity>> GROWTH_SHRINE = register(GrowthShrineBlockEntity::new, ECBlocks.GROWTH_SHRINE);
    public static final RegistryObject<BlockEntityType<HarvestShrineBlockEntity>> HARVEST_SHRINE = register(HarvestShrineBlockEntity::new, ECBlocks.HARVEST_SHRINE);
    public static final RegistryObject<BlockEntityType<LumberShrineBlockEntity>> LUMBER_SHRINE = register(LumberShrineBlockEntity::new, ECBlocks.LUMBER_SHRINE);
    public static final RegistryObject<BlockEntityType<LavaShrineBlockEntity>> LAVA_SHRINE = register(LavaShrineBlockEntity::new, ECBlocks.LAVA_SHRINE);
    public static final RegistryObject<BlockEntityType<OreShrineBlockEntity>> ORE_SHRINE = register(OreShrineBlockEntity::new, ECBlocks.ORE_SHRINE);
    public static final RegistryObject<BlockEntityType<OverloadShrineBlockEntity>> OVERLOAD_SHRINE = register(OverloadShrineBlockEntity::new, ECBlocks.OVERLOAD_SHRINE);
    public static final RegistryObject<BlockEntityType<SweetShrineBlockEntity>> SWEET_SHRINE = register(SweetShrineBlockEntity::new, ECBlocks.SWEET_SHRINE);
    public static final RegistryObject<BlockEntityType<EnderLockShrineBlockEntity>> ENDER_LOCK_SHRINE = register(EnderLockShrineBlockEntity::new, ECBlocks.ENDER_LOCK_SHRINE);
    public static final RegistryObject<BlockEntityType<BreedingShrineBlockEntity>> BREEDING_SHRINE = register(BreedingShrineBlockEntity::new, ECBlocks.BREEDING_SHRINE);
    public static final RegistryObject<BlockEntityType<GroveShrineBlockEntity>> GROVE_SHRINE = register(GroveShrineBlockEntity::new, ECBlocks.GROVE_SHRINE);
    public static final RegistryObject<BlockEntityType<SpringShrineBlockEntity>> SPRING_SHRINE = register(SpringShrineBlockEntity::new, ECBlocks.SPRING_SHRINE);
    public static final RegistryObject<BlockEntityType<BuddingShrineBlockEntity>> BUDDING_SHRINE = register(BuddingShrineBlockEntity::new, ECBlocks.BUDDING_SHRINE);
    public static final RegistryObject<BlockEntityType<SpawningShrineBlockEntity>> SPAWNING_SHRINE = register(SpawningShrineBlockEntity::new, ECBlocks.SPAWNING_SHRINE);
    public static final RegistryObject<BlockEntityType<AccelerationShrineUpgradeBlockEntity>> ACCELERATION_SHRINE_UPGRADE = register(AccelerationShrineUpgradeBlockEntity::new, ECBlocks.ACCELERATION_SHRINE_UPGRADE);
    public static final RegistryObject<BlockEntityType<OverclockedAccelerationShrineUpgradeBlockEntity>> OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE = register(OverclockedAccelerationShrineUpgradeBlockEntity::new, ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE);
    public static final RegistryObject<BlockEntityType<TranslocationShrineUpgradeBlockEntity>> TRANSLOCATION_SHRINE_UPGRADE = register(TranslocationShrineUpgradeBlockEntity::new, ECBlocks.TRANSLOCATION_SHRINE_UPGRADE);
    public static final RegistryObject<BlockEntityType<VortexShrineUpgradeBlockEntity>> VORTEX_SHRINE_UPGRADE = register(VortexShrineUpgradeBlockEntity::new, ECBlocks.VORTEX_SHRINE_UPGRADE);
    public static final RegistryObject<BlockEntityType<SorterBlockEntity>> SORTER = register(SorterBlockEntity::new, ECBlocks.SORTER);
    public static final RegistryObject<BlockEntityType<SourceDisplacementPlateBlockEntity>> SOURCE_DISPLACEMENT_PLATE = register(() -> builder(SourceDisplacementPlateBlockEntity::new, ECBlocks.FIRE_SOURCE_DISPLACEMENT_PLATE, ECBlocks.WATER_SOURCE_DISPLACEMENT_PLATE, ECBlocks.EARTH_SOURCE_DISPLACEMENT_PLATE, ECBlocks.AIR_SOURCE_DISPLACEMENT_PLATE), SourceDisplacementPlateBlock.NAME);

    public static final RegistryObject<BlockEntityType<SourceBreederBlockEntity>> SOURCE_BREEDER = register(SourceBreederBlockEntity::new, ECBlocks.SOURCE_BREEDER);
    public static final RegistryObject<BlockEntityType<SourceBreederPedestalBlockEntity>> SOURCE_BREEDER_PEDESTAL = register(SourceBreederPedestalBlockEntity::new, ECBlocks.SOURCE_BREEDER_PEDESTAL);


    private ECBlockEntityTypes() {}

    @SafeVarargs
    private static <T extends BlockEntity> BlockEntityType.Builder<T> builder(BlockEntityType.BlockEntitySupplier<T> factory, RegistryObject<? extends Block>... validBlocks) {
        return BlockEntityType.Builder.of(factory, Arrays.stream(validBlocks)
                .map(RegistryObject::get)
                .toArray(Block[]::new));
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(BlockEntityType.BlockEntitySupplier<T> factory, RegistryObject<? extends Block> block) {
        return register(() -> builder(factory, block), block.getId().getPath());
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(Supplier<BlockEntityType.Builder<T>> builder, String name) {
        return DEFERRED_REGISTER.register(name, () -> builder.get().build(null));
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }
}
