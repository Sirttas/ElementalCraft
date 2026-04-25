package sirttas.elementalcraft.datagen.model;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.cover.CoverType;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractAirMillBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.shrine.breeding.BreedingShrineBlock;
import sirttas.elementalcraft.block.shrine.overload.OverloadShrineBlock;
import sirttas.elementalcraft.block.shrine.upgrade.VerticalShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked.OverclockedAccelerationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.silktouch.SilkTouchShrineUpgradeBlock;
import sirttas.elementalcraft.block.sorter.ISorterBlock;
import sirttas.elementalcraft.datagen.ECBlockFamilies;

public class ECBlockStateModelGenerator extends BlockModelGenerators implements ECModelGenerator {

    public static final MultiVariant CONTAINER_CONNECTOR         = plainVariant(decorateBlockModelLocation("container_connector"));
    public static final MultiVariant PEDESTAL_CONNECTOR          = plainVariant(decorateBlockModelLocation("pedestal_connector"));
    public static final MultiVariant AIR_MILL_UPPER              = plainVariant(decorateBlockModelLocation("air_mill_upper"));
    public static final MultiVariant AIR_MILL_LOWER              = plainVariant(decorateBlockModelLocation("air_mill_ulower"));
    public static final MultiVariant AIR_MILL_SYNTHESIZER_LOWER  = plainVariant(decorateBlockModelLocation("air_mill_synthesizer_lower"));
    public static final MultiVariant COVER_FRAME                 = plainVariant(decorateBlockModelLocation("cover_frame"));
    public static final MultiVariant SORTER_SOURCE               = plainVariant(decorateBlockModelLocation("sorter_source")).with(UV_LOCK);
    public static final MultiVariant SORTER_TARGET               = plainVariant(decorateBlockModelLocation("sorter_target")).with(UV_LOCK);

    public ECBlockStateModelGenerator(BlockModelGenerators blockModelGenerators) {
        super(blockModelGenerators.blockStateOutput, blockModelGenerators.itemModelOutput, blockModelGenerators.modelOutput);
    }

    @Override
    public void run() {
        createAirMillUpperModel();

        ECBlockFamilies.getAllFamilies()
                .filter(BlockFamily::shouldGenerateModel)
                .forEach(blockFamily -> this.family(blockFamily.getBaseBlock()).generateFor(blockFamily));

        // Containers
        createNonTemplateModelBlock(ECBlocks.SMALL_CONTAINER.get());
        createContainer(ECBlocks.CONTAINER.get());
        createReservoir(ECBlocks.FIRE_RESERVOIR.get());
        createReservoir(ECBlocks.WATER_RESERVOIR.get());
        createReservoir(ECBlocks.EARTH_RESERVOIR.get());
        createReservoir(ECBlocks.AIR_RESERVOIR.get());
        createContainer(ECBlocks.CREATIVE_CONTAINER.get());

        // Extractors
        createNonTemplateModelBlock(ECBlocks.RUDIMENTARY_EXTRACTOR.get());
        createExtractor();
        createNonTemplateModelBlock(ECBlocks.IMPROVED_EXTRACTOR.get());

        // Synthesizers
        createNonTemplateModelBlock(ECBlocks.CRACKING_SYNTHESIZER.get());
        createNonTemplateModelBlock(ECBlocks.COMBUSTION_SYNTHESIZER.get());
        createNonTemplateModelBlock(ECBlocks.DRAINING_SYNTHESIZER.get());
        createVibrationSynthesizer();
        createNonTemplateModelBlock(ECBlocks.SOLAR_SYNTHESIZER.get());
        createNonTemplateHorizontalBlock(ECBlocks.CULINARY_SYNTHESIZER.get());
        createNonTemplateModelBlock(ECBlocks.SCULK_CRACKING_SYNTHESIZER.get());
        createAirMillSynthesizer();

        // Instruments
        createNonTemplateModelBlock(ECBlocks.DIFFUSER.get());
        createNonTemplateModelBlock(ECBlocks.INFUSER.get());
        createNonTemplateModelBlock(ECBlocks.BINDER.get());
        createNonTemplateModelBlock(ECBlocks.BINDER_IMPROVED.get());
        createNonTemplateModelBlock(ECBlocks.CRYSTALLIZER.get());
        createNonTemplateHorizontalBlock(ECBlocks.INSCRIBER.get());
        createNonTemplateHorizontalBlock(ECBlocks.WATER_MILL_GRINDSTONE.get());
        createAirMill(ECBlocks.AIR_MILL_GRINDSTONE.get());
        createNonTemplateHorizontalBlock(ECBlocks.WATER_MILL_WOOD_SAW.get());
        createAirMill(ECBlocks.AIR_MILL_WOOD_SAW.get());
        createDoubleHalfBlock(ECBlocks.ENCHANTMENT_LIQUEFIER.get());
        createNonTemplateModelBlock(ECBlocks.PURE_INFUSER.get());
        createNonTemplateModelBlock(ECBlocks.FIRE_FURNACE.get());
        createNonTemplateModelBlock(ECBlocks.FIRE_BLAST_FURNACE.get());
        createNonTemplateHorizontalBlock(ECBlocks.PURIFIER.get());
        createNonTemplateHorizontalBlock(ECBlocks.SPELL_DESK.get());

        // Pedestals
        createPedestal(ECBlocks.FIRE_PEDESTAL.get());
        createPedestal(ECBlocks.WATER_PEDESTAL.get());
        createPedestal(ECBlocks.EARTH_PEDESTAL.get());
        createPedestal(ECBlocks.AIR_PEDESTAL.get());

        // Pipes and sorters
        createPipe(ECBlocks.PIPE_RUDIMENTARY.get());
        createPipe(ECBlocks.PIPE.get());
        createPipe(ECBlocks.PIPE_IMPROVED.get());
        createPipe(ECBlocks.PIPE_CREATIVE.get());
        createSorter(ECBlocks.RETRIEVER.get());
        createSorter(ECBlocks.ORDERED_SORTER.get());

        // Shrines
        createDoubleHalfBlock(ECBlocks.FIRE_PYLON.get());
        createNonTemplateModelBlock(ECBlocks.VACUUM_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.GROWTH_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.HARVEST_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.LUMBER_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.MELTING_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.ORE_SHRINE.get());
        createOverloadShrine();
        createNonTemplateModelBlock(ECBlocks.SWEET_SHRINE.get());
        createDoubleHalfBlock(ECBlocks.ENDER_LOCK_SHRINE.get());
        createBreedingShrine();
        createNonTemplateModelBlock(ECBlocks.GROVE_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.SPRING_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.BUDDING_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.SPAWNING_SHRINE.get());

        // Shrine upgrades
        createDirectionalShrineUpgrade(ECBlocks.ACCELERATION_SHRINE_UPGRADE.get());
        createOverclockedAccelerationShrineUpgrade();
        createDirectionalShrineUpgrade(ECBlocks.RANGE_SHRINE_UPGRADE.get());
        createDirectionalShrineUpgrade(ECBlocks.CAPACITY_SHRINE_UPGRADE.get());
        createDirectionalShrineUpgrade(ECBlocks.EFFICIENCY_SHRINE_UPGRADE.get());
        createDirectionalShrineUpgrade(ECBlocks.OPTIMIZATION_SHRINE_UPGRADE.get());
        createDirectionalShrineUpgrade(ECBlocks.STRENGTH_SHRINE_UPGRADE.get());
        createDirectionalShrineUpgrade(ECBlocks.TRANSLOCATION_SHRINE_UPGRADE.get());
        createFillingShrineUpgrade();
        createNonTemplateHorizontalBlock(ECBlocks.OVERWHELMING_STRENGTH_SHRINE_UPGRADE.get());
        createNonTemplateHorizontalBlock(ECBlocks.FORTUNE_SHRINE_UPGRADE.get());
        createNonTemplateHorizontalBlock(ECBlocks.GREATER_FORTUNE_SHRINE_UPGRADE.get());
        createSilkTouchShrineUpgrade();
        createNonTemplateHorizontalBlock(ECBlocks.CRYSTAL_HARVEST_SHRINE_UPGRADE.get());
        createNonTemplateHorizontalBlock(ECBlocks.NECTAR_SHRINE_UPGRADE.get());
        createNonTemplateHorizontalBlock(ECBlocks.PROTECTION_SHRINE_UPGRADE.get());
        createNonTemplateHorizontalBlock(ECBlocks.SPRINGALINE_SHRINE_UPGRADE.get());
        createNonTemplateHorizontalBlock(ECBlocks.CERTUS_QUARTZ_SHRINE_UPGRADE.get());
        createVerticalShrineUpgrade(ECBlocks.PLANTING_SHRINE_UPGRADE.get());
        createNonTemplateModelBlock(ECBlocks.BONELESS_GROWTH_SHRINE_UPGRADE.get());
        createNonTemplateModelBlock(ECBlocks.PICKUP_SHRINE_UPGRADE.get());
        createNonTemplateModelBlock(ECBlocks.VORTEX_SHRINE_UPGRADE.get());
        createNonTemplateModelBlock(ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE.get());
        createNonTemplateModelBlock(ECBlocks.STEM_POLLINATION_SHRINE_UPGRADE.get());
        createNonTemplateModelBlock(ECBlocks.CRYSTAL_GROWTH_SHRINE_UPGRADE.get());

        // Sources
        createSourceBlock(ECBlocks.FIRE_SOURCE.get());
        createSourceBlock(ECBlocks.WATER_SOURCE.get());
        createSourceBlock(ECBlocks.EARTH_SOURCE.get());
        createSourceBlock(ECBlocks.AIR_SOURCE.get());
        createDoubleHalfBlock(ECBlocks.SOURCE_BREEDER.get());
        createNonTemplateModelBlock(ECBlocks.SOURCE_BREEDER_PEDESTAL.get());

        // Ores
        createNonTemplateModelBlock(ECBlocks.CRYSTAL_ORE.get());
        createNonTemplateModelBlock(ECBlocks.DEEPSLATE_CRYSTAL_ORE.get());

        // Glass
        createGlassBlocks(ECBlocks.BURNT_GLASS.get(), ECBlocks.BURNT_GLASS_PANE.get());
        createGlassBlocks(ECBlocks.SPRINGALINE_GLASS.get(), ECBlocks.SPRINGALINE_GLASS_PANE.get());

        // Metal and crystal blocks
        createTrivialCube(ECBlocks.DRENCHED_IRON_BLOCK.get());
        createTrivialCube(ECBlocks.SWIFT_ALLOY_BLOCK.get());
        createTrivialCube(ECBlocks.FIREITE_BLOCK.get());
        createTrivialCube(ECBlocks.INERT_CRYSTAL_BLOCK.get());
        createTrivialCube(ECBlocks.FIRE_CRYSTAL_BLOCK.get());
        createTrivialCube(ECBlocks.WATER_CRYSTAL_BLOCK.get());
        createTrivialCube(ECBlocks.EARTH_CRYSTAL_BLOCK.get());
        createTrivialCube(ECBlocks.AIR_CRYSTAL_BLOCK.get());

        // Springaline
        createTrivialCube(ECBlocks.SPRINGALINE_BLOCK.get());
        createAmethystCluster(ECBlocks.SPRINGALINE_CLUSTER.get());
        createAmethystCluster(ECBlocks.LARGE_SPRINGALINE_BUD.get());
        createAmethystCluster(ECBlocks.MEDIUM_SPRINGALINE_BUD.get());
        createAmethystCluster(ECBlocks.SMALL_SPRINGALINE_BUD.get());
        createTrivialCube(ECBlocks.SPRINGALINE_LANTERN.get());

        // Misc
        createNonTemplateModelBlock(ECBlocks.ELEMENTAL_EMBER.get());
        createWhiteRockFence();
        createNonTemplateModelBlock(ECBlocks.TRANSLOCATION_ANCHOR.get());
    }

    private void createAirMillUpperModel() {
        ModelTemplates.PARTICLE_ONLY.create(
                decorateBlockModelLocation("air_mill_upper"),
                new TextureMapping().put(TextureSlot.PARTICLE, new Material(decorateBlockModelLocation("air_mill_blades"))),
                modelOutput);
    }

    public void createContainer(Block block) {
        createBlockWithConnectors(block, CONTAINER_CONNECTOR);
    }

    public void createReservoir(Block block) {
        var base      = plainVariant(ModelLocationUtils.getModelLocation(block));
        var connector = plainVariant(ModelLocationUtils.getModelLocation(block, "_connector"));

        blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(base)
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).term(BlockStateProperties.NORTH, true), CONTAINER_CONNECTOR)
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).term(BlockStateProperties.EAST, true),  CONTAINER_CONNECTOR.with(BlockModelGenerators.Y_ROT_90))
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).term(BlockStateProperties.SOUTH, true), CONTAINER_CONNECTOR.with(BlockModelGenerators.Y_ROT_180))
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).term(BlockStateProperties.WEST, true),  CONTAINER_CONNECTOR.with(BlockModelGenerators.Y_ROT_270))
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).term(BlockStateProperties.NORTH, true), connector)
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).term(BlockStateProperties.EAST, true),  connector.with(BlockModelGenerators.Y_ROT_90))
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).term(BlockStateProperties.SOUTH, true), connector.with(BlockModelGenerators.Y_ROT_180))
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).term(BlockStateProperties.WEST, true),  connector.with(BlockModelGenerators.Y_ROT_270)));
    }

    public void createPedestal(Block block) {
        createBlockWithConnectors(block, PEDESTAL_CONNECTOR);
    }

    public void createBlockWithConnectors(Block block, MultiVariant connector) {
        var base = plainVariant(ModelLocationUtils.getModelLocation(block));

        blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(base)
                .with(condition().term(BlockStateProperties.NORTH, true), connector)
                .with(condition().term(BlockStateProperties.EAST, true),  connector.with(BlockModelGenerators.Y_ROT_90))
                .with(condition().term(BlockStateProperties.SOUTH, true), connector.with(BlockModelGenerators.Y_ROT_180))
                .with(condition().term(BlockStateProperties.WEST, true),  connector.with(BlockModelGenerators.Y_ROT_270)));
    }

    public void createExtractor() {
        var block = ECBlocks.EXTRACTOR.get();

        var model = plainVariant(ModelLocationUtils.getModelLocation(block));

        blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_AXIS)
                        .select(Direction.Axis.X, model)
                        .select(Direction.Axis.Y, model.with(BlockModelGenerators.Y_ROT_90))));
    }

    public void createVibrationSynthesizer() {
        var block = ECBlocks.VIBRATION_SYNTHESIZER.get();

        var inactiveModel = ModelLocationUtils.getModelLocation(block, "_inactive");
        var inactive = plainVariant(ECModelTemplates.VIBRATION_SYNTHESIZER.create(
                inactiveModel,
                new TextureMapping().put(ECTextureSlots.TENDRIL, TextureMapping.getBlockTexture(block, "_tendril_inactive")),
                modelOutput));
        var active = plainVariant(ECModelTemplates.VIBRATION_SYNTHESIZER.createWithSuffix(
                block, "_active",
                new TextureMapping().put(ECTextureSlots.TENDRIL, TextureMapping.getBlockTexture(block, "_tendril_active")),
                modelOutput));

        registerSimpleItemModel(block, inactiveModel);
        blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(BlockStateProperties.SCULK_SENSOR_PHASE)
                        .generate(phase -> phase != SculkSensorPhase.ACTIVE && phase != SculkSensorPhase.COOLDOWN ? inactive : active)));
    }

    public void createAirMillSynthesizer() {
        createAirMill(ECBlocks.AIR_MILL_SYNTHESIZER.get(), AIR_MILL_SYNTHESIZER_LOWER);
    }

    public void createAirMill(Block block) {
        createAirMill(block, AIR_MILL_LOWER);
    }

    public void createAirMill(Block block, MultiVariant lower) {
        var brokenModel = plainVariant(ModelLocationUtils.getModelLocation(block, "_broken"));

        blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(BlockStateProperties.DOUBLE_BLOCK_HALF, AbstractAirMillBlock.BROKEN)
                        .generate((half, broken) -> {
                            if (half == DoubleBlockHalf.UPPER) {
                                return AIR_MILL_UPPER;
                            } else if (broken) {
                                return brokenModel;
                            }
                            return lower;
                        })));
    }

    public void createDoubleHalfBlock(@NotNull Block block) {
        createDoubleBlock(
                block,
                plainVariant(ModelLocationUtils.getModelLocation(block, "_upper")),
                plainVariant(ModelLocationUtils.getModelLocation(block, "_lower")));
    }

    public void createOverloadShrine() {
        var block = ECBlocks.OVERLOAD_SHRINE.get();

        var base = plainVariant(ModelLocationUtils.getModelLocation(block, "_base"));
        var top  = plainVariant(ModelLocationUtils.getModelLocation(block, "_top"));
        var side = plainVariant(ModelLocationUtils.getModelLocation(block, "_side"));

        blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(base)
                .with(condition().term(OverloadShrineBlock.FACING, Direction.UP),    top)
                .with(condition().term(OverloadShrineBlock.FACING, Direction.NORTH), side)
                .with(condition().term(OverloadShrineBlock.FACING, Direction.EAST),  side.with(BlockModelGenerators.Y_ROT_90))
                .with(condition().term(OverloadShrineBlock.FACING, Direction.SOUTH), side.with(BlockModelGenerators.Y_ROT_180))
                .with(condition().term(OverloadShrineBlock.FACING, Direction.WEST),  side.with(BlockModelGenerators.Y_ROT_270)));
    }

    public void createBreedingShrine() {
        var block = ECBlocks.BREEDING_SHRINE.get();

        var core = plainVariant(ModelLocationUtils.getModelLocation(block, "_core"));
        var bowl = plainVariant(ModelLocationUtils.getModelLocation(block, "_bowl"));

        blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(BreedingShrineBlock.FACING, BreedingShrineBlock.PART)
                        .generate((facing, part) -> {
                            var model = part == BreedingShrineBlock.Part.CORE ? core : bowl;
                            return switch (facing) {
                                case EAST  -> model.with(BlockModelGenerators.Y_ROT_90);
                                case SOUTH -> model.with(BlockModelGenerators.Y_ROT_180);
                                case WEST  -> model.with(BlockModelGenerators.Y_ROT_270);
                                default    -> model;
                            };
                        })));
    }

    public void createDirectionalShrineUpgrade(Block block) {
        var model = plainVariant(ModelLocationUtils.getModelLocation(block));

        blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(BlockStateProperties.FACING)
                        .select(Direction.UP,    model)
                        .select(Direction.DOWN,  model.with(BlockModelGenerators.X_ROT_180))
                        .select(Direction.NORTH, model.with(BlockModelGenerators.X_ROT_90))
                        .select(Direction.EAST,  model.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.SOUTH, model.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.WEST,  model.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_270))));
    }

    public void createVerticalShrineUpgrade(Block block) {
        var model = plainVariant(ModelLocationUtils.getModelLocation(block));

        blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(VerticalShrineUpgradeBlock.FACING)
                        .select(Direction.UP,   model)
                        .select(Direction.DOWN, model.with(BlockModelGenerators.X_ROT_180))));
    }

    public void createFillingShrineUpgrade() {
        var block = ECBlocks.FILLING_SHRINE_UPGRADE.get();

        var core = plainVariant(ModelLocationUtils.getModelLocation(block));
        var side = plainVariant(ModelLocationUtils.getModelLocation(block, "_side"));

        blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(BlockStateProperties.FACING)
                        .select(Direction.DOWN,  core)
                        .select(Direction.UP,    core.with(BlockModelGenerators.X_ROT_180))
                        .select(Direction.NORTH, side)
                        .select(Direction.EAST,  side.with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.SOUTH, side.with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.WEST,  side.with(BlockModelGenerators.Y_ROT_270))));
    }

    public void createOverclockedAccelerationShrineUpgrade() {
        var block = ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE.get();

        var lower     = plainVariant(ModelLocationUtils.getModelLocation(block, "_lower"));
        var upper     = plainVariant(ModelLocationUtils.getModelLocation(block, "_upper"));
        var connector = plainVariant(ModelLocationUtils.getModelLocation(block, "_connector"));

        blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(condition().term(OverclockedAccelerationShrineUpgradeBlock.HALF, DoubleBlockHalf.LOWER).term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH), lower.with(UV_LOCK))
                .with(condition().term(OverclockedAccelerationShrineUpgradeBlock.HALF, DoubleBlockHalf.LOWER).term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST),  lower.with(BlockModelGenerators.Y_ROT_90).with(UV_LOCK))
                .with(condition().term(OverclockedAccelerationShrineUpgradeBlock.HALF, DoubleBlockHalf.LOWER).term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH), lower.with(BlockModelGenerators.Y_ROT_180).with(UV_LOCK))
                .with(condition().term(OverclockedAccelerationShrineUpgradeBlock.HALF, DoubleBlockHalf.LOWER).term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST),  lower.with(BlockModelGenerators.Y_ROT_270).with(UV_LOCK))
                .with(condition().term(OverclockedAccelerationShrineUpgradeBlock.HALF, DoubleBlockHalf.UPPER).term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH), upper.with(UV_LOCK))
                .with(condition().term(OverclockedAccelerationShrineUpgradeBlock.HALF, DoubleBlockHalf.UPPER).term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST),  upper.with(BlockModelGenerators.Y_ROT_90).with(UV_LOCK))
                .with(condition().term(OverclockedAccelerationShrineUpgradeBlock.HALF, DoubleBlockHalf.UPPER).term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH), upper.with(BlockModelGenerators.Y_ROT_180).with(UV_LOCK))
                .with(condition().term(OverclockedAccelerationShrineUpgradeBlock.HALF, DoubleBlockHalf.UPPER).term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST),  upper.with(BlockModelGenerators.Y_ROT_270).with(UV_LOCK))
                .with(condition().term(OverclockedAccelerationShrineUpgradeBlock.HALF, DoubleBlockHalf.LOWER).term(OverclockedAccelerationShrineUpgradeBlock.CONNECTED, true).term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH), connector.with(UV_LOCK))
                .with(condition().term(OverclockedAccelerationShrineUpgradeBlock.HALF, DoubleBlockHalf.LOWER).term(OverclockedAccelerationShrineUpgradeBlock.CONNECTED, true).term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST),  connector.with(BlockModelGenerators.Y_ROT_90).with(UV_LOCK))
                .with(condition().term(OverclockedAccelerationShrineUpgradeBlock.HALF, DoubleBlockHalf.LOWER).term(OverclockedAccelerationShrineUpgradeBlock.CONNECTED, true).term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH), connector.with(BlockModelGenerators.Y_ROT_180).with(UV_LOCK))
                .with(condition().term(OverclockedAccelerationShrineUpgradeBlock.HALF, DoubleBlockHalf.LOWER).term(OverclockedAccelerationShrineUpgradeBlock.CONNECTED, true).term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST),  connector.with(BlockModelGenerators.Y_ROT_270).with(UV_LOCK)));
    }

    public void createSilkTouchShrineUpgrade() {
        var block = ECBlocks.SILK_TOUCH_SHRINE_UPGRADE.get();

        var core   = plainVariant(ModelLocationUtils.getModelLocation(block));
        var attach = plainVariant(ModelLocationUtils.getModelLocation(block, "_attach"));

        blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(condition().term(SilkTouchShrineUpgradeBlock.FACING, Direction.NORTH), core.with(UV_LOCK))
                .with(condition().term(SilkTouchShrineUpgradeBlock.FACING, Direction.EAST),  core.with(BlockModelGenerators.Y_ROT_90).with(UV_LOCK))
                .with(condition().term(SilkTouchShrineUpgradeBlock.FACING, Direction.SOUTH), core.with(BlockModelGenerators.Y_ROT_180).with(UV_LOCK))
                .with(condition().term(SilkTouchShrineUpgradeBlock.FACING, Direction.WEST),  core.with(BlockModelGenerators.Y_ROT_270).with(UV_LOCK))
                .with(condition().term(SilkTouchShrineUpgradeBlock.FACING, Direction.NORTH).term(BlockStateProperties.ATTACHED, true), attach.with(UV_LOCK))
                .with(condition().term(SilkTouchShrineUpgradeBlock.FACING, Direction.EAST) .term(BlockStateProperties.ATTACHED, true), attach.with(BlockModelGenerators.Y_ROT_90).with(UV_LOCK))
                .with(condition().term(SilkTouchShrineUpgradeBlock.FACING, Direction.SOUTH).term(BlockStateProperties.ATTACHED, true), attach.with(BlockModelGenerators.Y_ROT_180).with(UV_LOCK))
                .with(condition().term(SilkTouchShrineUpgradeBlock.FACING, Direction.WEST) .term(BlockStateProperties.ATTACHED, true), attach.with(BlockModelGenerators.Y_ROT_270).with(UV_LOCK)));
    }

    public void createSourceBlock(Block block) {
        ModelTemplates.PARTICLE_ONLY.create(
                ModelLocationUtils.getModelLocation(block),
                new TextureMapping().put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                modelOutput);
        createNonTemplateModelBlock(block);
    }

    public void createPipe(Block block) {
        var texture = switch (((ElementPipeBlock) block).getType()) {
            case RUDIMENTARY -> "iron";
            case STANDARD    -> "brass";
            case IMPROVED    -> "pure_iron";
            case CREATIVE    -> "creative_iron";
        };
        var core = plainVariant(ECModelTemplates.PIPE_CORE.createWithSuffix(
                block, "_core",
                new TextureMapping().put(TextureSlot.TEXTURE, new Material(decorateBlockModelLocation(texture))),
                modelOutput));

        registerSimpleItemModel(block, ECModelTemplates.PIPE_ITEM.createWithSuffix(
                block, "_core",
                new TextureMapping().put(TextureSlot.TEXTURE, new Material(decorateBlockModelLocation(texture))),
                modelOutput));
        blockStateOutput.accept(createCoverable(block, core));
    }

    public void createSorter(Block block) {
        var core = plainVariant(ModelLocationUtils.getModelLocation(block, "_core"));

        blockStateOutput.accept(createCoverable(block, core)
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.SOURCE, Direction.SOUTH), SORTER_SOURCE)
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.SOURCE, Direction.WEST),  SORTER_SOURCE.with(BlockModelGenerators.Y_ROT_90))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.SOURCE, Direction.NORTH), SORTER_SOURCE.with(BlockModelGenerators.Y_ROT_180))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.SOURCE, Direction.EAST),  SORTER_SOURCE.with(BlockModelGenerators.Y_ROT_270))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.SOURCE, Direction.DOWN),  SORTER_SOURCE.with(BlockModelGenerators.X_ROT_270))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.SOURCE, Direction.UP),    SORTER_SOURCE.with(BlockModelGenerators.X_ROT_90))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.TARGET, Direction.SOUTH), SORTER_TARGET.with(BlockModelGenerators.Y_ROT_180))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.TARGET, Direction.WEST),  SORTER_TARGET.with(BlockModelGenerators.Y_ROT_270))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.TARGET, Direction.NORTH), SORTER_TARGET)
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.TARGET, Direction.EAST),  SORTER_TARGET.with(BlockModelGenerators.Y_ROT_90))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.TARGET, Direction.DOWN),  SORTER_TARGET.with(BlockModelGenerators.X_ROT_90))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.TARGET, Direction.UP),    SORTER_TARGET.with(BlockModelGenerators.X_ROT_270)));
    }

    private static @NotNull MultiPartGenerator createCoverable(Block block, MultiVariant core) {
        return MultiPartGenerator.multiPart(block)
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME), core)
                .with(condition().term(CoverType.PROPERTY, CoverType.FRAME), COVER_FRAME);
    }
    public void createWhiteRockFence() {
        var block = ECBlocks.WHITE_ROCK_FENCE.get();

        var post = plainVariant(ModelTemplates.FENCE_POST.create(block, new TextureMapping().put(TextureSlot.TEXTURE, TextureMapping.getBlockTexture(ECBlocks.WHITE_ROCK.get())), modelOutput));
        var side = plainVariant(ModelTemplates.FENCE_SIDE.create(block, new TextureMapping().put(TextureSlot.TEXTURE, new Material(decorateBlockModelLocation("iron"))), modelOutput));

        createFence(block, post, side);
    }

    public static Identifier decorateBlockModelLocation(String id) {
        return ElementalCraftApi.createRL(id).withPrefix("block/");
    }

}
