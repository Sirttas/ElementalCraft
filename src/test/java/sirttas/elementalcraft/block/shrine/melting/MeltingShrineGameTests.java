package sirttas.elementalcraft.block.shrine.melting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.template.ECStructureTemplateBuilder;

import java.util.function.Supplier;

@ForEachTest(groups = ShrineGameTestHelper.GROUP)
public class MeltingShrineGameTests {

    public static final String MELTING_SHRINE_TEMPLATE_NAME = "elementalcraft:melting_shrine";
    public static final String MELTING_SHRINE_WITH_FILLING_TEMPLATE_NAME = "elementalcraft:melting_shrine_with_filling";

    @RegisterStructureTemplate(MELTING_SHRINE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> MELTING_SHRINE_TEMPLATE = ECStructureTemplateBuilder.lazy(3, 3, 3, builder -> builder
            .fill(0, 0, 0, 2, 2, 2, ECBlocks.WHITE_ROCK_BRICKS.get().defaultBlockState())
            .set(1, 0, 1, ECBlocks.MELTING_SHRINE.get().defaultBlockState())
            .set(1, 1, 1, Blocks.AIR.defaultBlockState()));

    @RegisterStructureTemplate(MELTING_SHRINE_WITH_FILLING_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> MELTING_SHRINE_WITH_FILLING_TEMPLATE = ECStructureTemplateBuilder.lazy(4, 3, 3, builder -> builder
            .fill(0, 0, 0, 2, 2, 2, ECBlocks.WHITE_ROCK_BRICKS.get().defaultBlockState())
            .set(1, 0, 1, ECBlocks.MELTING_SHRINE.get().defaultBlockState())
            .set(2, 0, 1, ECBlocks.FILLING_SHRINE_UPGRADE.get().defaultBlockState()
                    .setValue(BlockStateProperties.FACING, Direction.WEST))
            .set(3, 0, 1, Blocks.CAULDRON.defaultBlockState())
            .set(1, 1, 1, Blocks.AIR.defaultBlockState()));

    @TestHolder
    @GameTest(template = MELTING_SHRINE_TEMPLATE_NAME)
    public static void should_meltBasaltIntoLava(ECGameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            helper.setBlock(new BlockPos(1, 1, 1), Blocks.BASALT);
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(1, 0, 1));
        }).thenExecuteAfter(1, () -> {
            helper.assertBlockState(new BlockPos(1, 1, 1), b -> b.is(Blocks.LAVA), _ -> Component.literal("Block has not been melted"));
        }).thenSucceed();
    }

    @TestHolder
    @GameTest(template = MELTING_SHRINE_TEMPLATE_NAME)
    public static void should_meltIceIntoWater(ECGameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            helper.setBlock(new BlockPos(1, 1, 1), Blocks.PACKED_ICE);
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(1, 0, 1));
        }).thenExecuteAfter(1, () -> {
            helper.assertBlockState(new BlockPos(1, 1, 1), b -> b.is(Blocks.WATER), _ -> Component.literal("Block has not been melted"));
        }).thenSucceed();
    }

    @TestHolder
    @GameTest(template = MELTING_SHRINE_WITH_FILLING_TEMPLATE_NAME)
    public static void should_fillCauldronWithLava(GameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            helper.setBlock(new BlockPos(1, 1, 1), Blocks.BASALT);
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(1, 0, 1));
        }).thenExecuteAfter(1, () -> {
            helper.assertBlockState(new BlockPos(3, 0, 1), b -> b.is(Blocks.LAVA_CAULDRON), _ -> Component.literal("Cauldron has not been filled"));
            helper.assertBlockState(new BlockPos(1, 1, 1), BlockBehaviour.BlockStateBase::isAir, _ -> Component.literal("Basalt has not been removed"));
        }).thenSucceed();
    }

    @TestHolder
    @GameTest(template = MELTING_SHRINE_WITH_FILLING_TEMPLATE_NAME)
    public static void should_fillCauldronWithWater(GameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            helper.setBlock(new BlockPos(1, 1, 1), Blocks.PACKED_ICE);
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(1, 0, 1));
        }).thenExecuteAfter(1, () -> {
            helper.assertBlockState(new BlockPos(3, 0, 1), b -> b.is(Blocks.WATER_CAULDRON), _ -> Component.literal("Cauldron has not been filled"));
            helper.assertBlockState(new BlockPos(1, 1, 1), BlockBehaviour.BlockStateBase::isAir, _ -> Component.literal("Ice has not been removed"));
        }).thenSucceed();
    }

}
