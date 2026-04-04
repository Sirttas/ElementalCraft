package sirttas.elementalcraft.block.shrine.budding;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.neoforged.testframework.gametest.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import java.util.function.Supplier;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = ShrineGameTestHelper.GROUP)
public class BuddingShrineGameTests {

    public static final String BUDDING_SHRINE_TEMPLATE_NAME = "elementalcraft:budding_shrine";
    public static final String BUDDING_SHRINE_WITH_SPRINGALINE_UPGRADE_TEMPLATE_NAME = "elementalcraft:budding_shrine_with_springaline_upgrade";


    @RegisterStructureTemplate(BUDDING_SHRINE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> BUDDING_SHRINE_TEMPLATE = StructureTemplateBuilder.lazy(1, 2, 1, builder -> builder
            .set(0, 0, 0, ECBlocks.BUDDING_SHRINE.get().defaultBlockState()));

    @RegisterStructureTemplate(BUDDING_SHRINE_WITH_SPRINGALINE_UPGRADE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> BUDDING_SHRINE_WITH_SPRINGALINE_UPGRADE_TEMPLATE = StructureTemplateBuilder.lazy(2, 2, 1, builder -> builder
            .set(0, 0, 0, ECBlocks.BUDDING_SHRINE.get().defaultBlockState())
            .set(1, 0, 0, ECBlocks.SPRINGALINE_SHRINE_UPGRADE.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)));


    @TestHolder
    @GameTest(template = BUDDING_SHRINE_TEMPLATE_NAME)
    public static void should_growAmethyst(GameTestHelper helper) {
        helper.startSequence().thenExecuteAfter(1, () -> {
            BuddingShrineBlockEntity shrine = helper.getBlockEntity(new BlockPos(0, 1, 0));

            assertThat(shrine.getBudType().requiredUpgrade()).isNull();
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(0, 1, 0));

            helper.assertBlockPresent(Blocks.SMALL_AMETHYST_BUD, new BlockPos(0, 2, 0));
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(0, 1, 0));

            helper.assertBlockPresent(Blocks.MEDIUM_AMETHYST_BUD, new BlockPos(0, 2, 0));
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(0, 1, 0));

            helper.assertBlockPresent(Blocks.LARGE_AMETHYST_BUD, new BlockPos(0, 2, 0));
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(0, 1, 0));

            helper.assertBlockPresent(Blocks.AMETHYST_CLUSTER, new BlockPos(0, 2, 0));
        }).thenSucceed();
    }

    @TestHolder
    @GameTest(template = BUDDING_SHRINE_WITH_SPRINGALINE_UPGRADE_TEMPLATE_NAME)
    public static void should_growSpringalineWhenUpgradedWithSpringalineShrineUpgrade(GameTestHelper helper) {
        helper.startSequence().thenExecuteAfter(1, () -> {
            BuddingShrineBlockEntity shrine = helper.getBlockEntity(new BlockPos(0, 1, 0));

            assertThat(shrine.getBudType().requiredUpgrade().getKey()).isEqualTo(ShrineUpgrades.SPRINGALINE);
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(0, 1, 0));

            helper.assertBlockPresent(ECBlocks.SMALL_SPRINGALINE_BUD.get(), new BlockPos(0, 2, 0));
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(0, 1, 0));

            helper.assertBlockPresent(ECBlocks.MEDIUM_SPRINGALINE_BUD.get(), new BlockPos(0, 2, 0));
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(0, 1, 0));

            helper.assertBlockPresent(ECBlocks.LARGE_SPRINGALINE_BUD.get(), new BlockPos(0, 2, 0));
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(0, 1, 0));

            helper.assertBlockPresent(ECBlocks.SPRINGALINE_CLUSTER.get(), new BlockPos(0, 2, 0));
        }).thenSucceed();
    }
}
