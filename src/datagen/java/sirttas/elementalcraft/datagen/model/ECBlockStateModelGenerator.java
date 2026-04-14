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
import sirttas.elementalcraft.block.sorter.ISorterBlock;

public class ECBlockStateModelGenerator extends BlockModelGenerators implements ECModelGenerator {

    public static final MultiVariant CONTAINER_CONNECTOR = plainVariant(decorateBlockModelLocation("container_connector"));
    public static final MultiVariant PEDESTAL_CONNECTOR = plainVariant(decorateBlockModelLocation("pedestal_connector"));
    public static final MultiVariant AIR_MILL_UPPER = plainVariant(decorateBlockModelLocation("air_mill_upper"));
    public static final MultiVariant AIR_MILL_LOWER = plainVariant(decorateBlockModelLocation("air_mill_ulower"));
    public static final MultiVariant COVER_FRAME = plainVariant(decorateBlockModelLocation("cover_frame"));
    public static final MultiVariant SORTER_SOURCE = plainVariant(decorateBlockModelLocation("sorter_source")).with(UV_LOCK);
    public static final MultiVariant SORTER_TARGET = plainVariant(decorateBlockModelLocation("sorter_target")).with(UV_LOCK);

    public ECBlockStateModelGenerator(BlockModelGenerators blockModelGenerators) {
        super(blockModelGenerators.blockStateOutput, blockModelGenerators.itemModelOutput, blockModelGenerators.modelOutput);
    }

    @Override
    public void run() {
        createAirMillUpperModel();

        createNonTemplateModelBlock(ECBlocks.SMALL_CONTAINER.get());
        createContainer(ECBlocks.CONTAINER.get());
        createReservoir(ECBlocks.FIRE_RESERVOIR.get());
        createReservoir(ECBlocks.WATER_RESERVOIR.get());
        createReservoir(ECBlocks.EARTH_RESERVOIR.get());
        createReservoir(ECBlocks.AIR_RESERVOIR.get());
        createContainer(ECBlocks.CREATIVE_CONTAINER.get());
        createNonTemplateModelBlock(ECBlocks.RUDIMENTARY_EXTRACTOR.get());
        createExtractor();
        createNonTemplateModelBlock(ECBlocks.IMPROVED_EXTRACTOR.get());
        createNonTemplateModelBlock(ECBlocks.CRACKING_SYNTHESIZER.get());
        createNonTemplateModelBlock(ECBlocks.COMBUSTION_SYNTHESIZER.get());
        createNonTemplateModelBlock(ECBlocks.DRAINING_SYNTHESIZER.get());
        createVibrationSynthesizer();
        createNonTemplateModelBlock(ECBlocks.SOLAR_SYNTHESIZER.get());
        createNonTemplateHorizontalBlock(ECBlocks.CULINARY_SYNTHESIZER.get());
        createNonTemplateModelBlock(ECBlocks.SCULK_CRACKING_SYNTHESIZER.get());
        createAirMillSynthesizer();
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
        createPedestal(ECBlocks.FIRE_PEDESTAL.get());
        createPedestal(ECBlocks.WATER_PEDESTAL.get());
        createPedestal(ECBlocks.EARTH_PEDESTAL.get());
        createPedestal(ECBlocks.AIR_PEDESTAL.get());
        createNonTemplateModelBlock(ECBlocks.PURE_INFUSER.get());
        createNonTemplateModelBlock(ECBlocks.FIRE_FURNACE.get());
        createNonTemplateModelBlock(ECBlocks.FIRE_BLAST_FURNACE.get());
        createNonTemplateHorizontalBlock(ECBlocks.PURIFIER.get());
        createPipe(ECBlocks.PIPE_RUDIMENTARY.get());
        createPipe(ECBlocks.PIPE.get());
        createPipe(ECBlocks.PIPE_IMPROVED.get());
        createPipe(ECBlocks.PIPE_CREATIVE.get());
        createSorter(ECBlocks.RETRIEVER.get());
        createSorter(ECBlocks.ORDERED_SORTER.get());
        createNonTemplateHorizontalBlock(ECBlocks.SPELL_DESK.get());
        createDoubleHalfBlock(ECBlocks.FIRE_PYLON.get());
        createNonTemplateModelBlock(ECBlocks.VACUUM_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.GROWTH_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.HARVEST_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.LUMBER_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.MELTING_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.ORE_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.OVERLOAD_SHRINE.get());
        createNonTemplateModelBlock(ECBlocks.SWEET_SHRINE.get());
        createDoubleHalfBlock(ECBlocks.ENDER_LOCK_SHRINE.get());
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
        var base = plainVariant(ModelLocationUtils.getModelLocation(block));
        var connector = plainVariant(ModelLocationUtils.getModelLocation(block, "_connector"));

        blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(base)
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).term(BlockStateProperties.NORTH, true), CONTAINER_CONNECTOR)
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).term(BlockStateProperties.EAST, true), CONTAINER_CONNECTOR.with(BlockModelGenerators.Y_ROT_90))
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).term(BlockStateProperties.SOUTH, true), CONTAINER_CONNECTOR.with(BlockModelGenerators.Y_ROT_180))
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).term(BlockStateProperties.WEST, true), CONTAINER_CONNECTOR.with(BlockModelGenerators.Y_ROT_270))
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).term(BlockStateProperties.NORTH, true), connector)
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).term(BlockStateProperties.EAST, true), connector.with(BlockModelGenerators.Y_ROT_90))
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).term(BlockStateProperties.SOUTH, true), connector.with(BlockModelGenerators.Y_ROT_180))
                .with(condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).term(BlockStateProperties.WEST, true), connector.with(BlockModelGenerators.Y_ROT_270)));
    }

    public void createExtractor() {
        var model = plainVariant(ModelLocationUtils.getModelLocation(ECBlocks.EXTRACTOR.get()));

        blockStateOutput.accept(MultiVariantGenerator.dispatch(ECBlocks.EXTRACTOR.get())
                .with(PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_AXIS)
                        .select(Direction.Axis.X, model)
                        .select(Direction.Axis.Y, model.with(BlockModelGenerators.Y_ROT_90))));
    }

    public void createVibrationSynthesizer() {
        var inactiveModel = ModelLocationUtils.getModelLocation(ECBlocks.VIBRATION_SYNTHESIZER.get(), "_inactive");
        var inactive = plainVariant(ECModelTemplates.VIBRATION_SYNTHESIZER.create(
                inactiveModel,
                new TextureMapping().put(ECTextureSlots.TENDRIL, TextureMapping.getBlockTexture(ECBlocks.VIBRATION_SYNTHESIZER.get(), "_tendril_inactive")),
                modelOutput));
        var active = plainVariant(ECModelTemplates.VIBRATION_SYNTHESIZER.createWithSuffix(
                ECBlocks.VIBRATION_SYNTHESIZER.get(), "_active",
                new TextureMapping().put(ECTextureSlots.TENDRIL, TextureMapping.getBlockTexture(ECBlocks.VIBRATION_SYNTHESIZER.get(), "_tendril_active")),
                modelOutput));

        registerSimpleItemModel(ECBlocks.VIBRATION_SYNTHESIZER.get(), inactiveModel);
        blockStateOutput.accept(MultiVariantGenerator.dispatch(ECBlocks.VIBRATION_SYNTHESIZER.get())
                .with(PropertyDispatch.initial(BlockStateProperties.SCULK_SENSOR_PHASE)
                        .generate(phase -> phase != SculkSensorPhase.ACTIVE && phase != SculkSensorPhase.COOLDOWN ? inactive : active)));
    }

    public void createAirMillSynthesizer() {
        createAirMill(ECBlocks.AIR_MILL_SYNTHESIZER.get(), plainVariant(decorateBlockModelLocation("air_mill_synthesizer_lower")));
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
                plainVariant(ModelLocationUtils.getModelLocation(ECBlocks.VIBRATION_SYNTHESIZER.get(), "_upper")),
                plainVariant(ModelLocationUtils.getModelLocation(ECBlocks.VIBRATION_SYNTHESIZER.get(), "_lower")));
    }

    public void createPedestal(Block block) {
        createBlockWithConnectors(block, PEDESTAL_CONNECTOR);
    }

    public void createBlockWithConnectors(Block block, MultiVariant connector) {
        var base = plainVariant(ModelLocationUtils.getModelLocation(block));

        blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(base)
                .with(condition().term(BlockStateProperties.NORTH, true), connector)
                .with(condition().term(BlockStateProperties.EAST, true), connector.with(BlockModelGenerators.Y_ROT_90))
                .with(condition().term(BlockStateProperties.SOUTH, true), connector.with(BlockModelGenerators.Y_ROT_180))
                .with(condition().term(BlockStateProperties.WEST, true), connector.with(BlockModelGenerators.Y_ROT_270)));
    }

    public void createPipe(Block block) {
        var texture = switch (((ElementPipeBlock) block).getType()) {
            case RUDIMENTARY -> "iron";
            case STANDARD -> "brass";
            case IMPROVED -> "pure_iron";
            case CREATIVE -> "creative_iron";
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
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.SOURCE, Direction.WEST), SORTER_SOURCE.with(BlockModelGenerators.Y_ROT_90))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.SOURCE, Direction.NORTH), SORTER_SOURCE.with(BlockModelGenerators.Y_ROT_180))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.SOURCE, Direction.EAST), SORTER_SOURCE.with(BlockModelGenerators.Y_ROT_270))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.SOURCE, Direction.DOWN), SORTER_SOURCE.with(BlockModelGenerators.X_ROT_270))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.SOURCE, Direction.UP), SORTER_SOURCE.with(BlockModelGenerators.X_ROT_90))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.TARGET, Direction.SOUTH), SORTER_TARGET.with(BlockModelGenerators.Y_ROT_180))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.TARGET, Direction.WEST), SORTER_TARGET.with(BlockModelGenerators.Y_ROT_270))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.TARGET, Direction.NORTH), SORTER_TARGET)
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.TARGET, Direction.EAST), SORTER_TARGET.with(BlockModelGenerators.Y_ROT_90))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.TARGET, Direction.DOWN), SORTER_TARGET.with(BlockModelGenerators.X_ROT_90))
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME).term(ISorterBlock.TARGET, Direction.UP), SORTER_TARGET.with(BlockModelGenerators.X_ROT_270))
        );
    }

    private static @NotNull MultiPartGenerator createCoverable(Block block, MultiVariant core) {
        return MultiPartGenerator.multiPart(block)
                .with(condition().term(CoverType.PROPERTY, CoverType.NONE, CoverType.FRAME), core)
                .with(condition().term(CoverType.PROPERTY, CoverType.FRAME), COVER_FRAME);
    }

    public static Identifier decorateBlockModelLocation(String id) {
        return ElementalCraftApi.createRL(id).withPrefix("block/");
    }

}
