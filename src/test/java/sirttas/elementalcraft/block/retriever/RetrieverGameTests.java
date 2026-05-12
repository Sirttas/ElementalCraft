package sirttas.elementalcraft.block.retriever;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.binder.BinderBlockEntity;
import sirttas.elementalcraft.block.instrument.infuser.InfuserBlockEntity;
import sirttas.elementalcraft.block.sorter.ISorterBlock;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.rune.Runes;
import sirttas.elementalcraft.template.ECStructureTemplateBuilder;

import java.util.function.Supplier;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.runeHandler;
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.withValue;

@ForEachTest(groups = RetrieverGameTests.GROUP)
public class RetrieverGameTests {

    public static final String GROUP = "level.blocks.retriever";

    public static final String INFUSER_TEMPLATE_NAME = "elementalcraft:retriever_from_infuser";
    public static final String BINDER_TEMPLATE_NAME = "elementalcraft:retriever_from_binder";

    @RegisterStructureTemplate(INFUSER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> INFUSER_TEMPLATE = createTemplate(ECBlocks.INFUSER);

    @RegisterStructureTemplate(BINDER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> BINDER_TEMPLATE = createTemplate(ECBlocks.BINDER);

    private static Supplier<StructureTemplate> createTemplate(Holder<Block> instrumentBlock) {
        return ECStructureTemplateBuilder.lazy(2, 3, 2, builder -> builder
                .fill(0, 0, 0, 1, 0, 1, ECBlocks.WHITE_ROCK_BRICKS.get().defaultBlockState())
                .placeFloorLever(1, 2, 1, true)
                .set(0, 1, 0, ECBlocks.CONTAINER.get().defaultBlockState())
                .set(0, 1, 1, Blocks.CHEST.defaultBlockState()
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH))
                .set(0, 2, 0, instrumentBlock.value().defaultBlockState(), withValue(runeHandler(Runes.CREATIVE)))
                .set(0, 2, 1, ECBlocks.RETRIEVER.get().defaultBlockState()
                        .setValue(ISorterBlock.SOURCE, Direction.NORTH)
                        .setValue(ISorterBlock.TARGET, Direction.DOWN)));
    }

    @GameTest(template = INFUSER_TEMPLATE_NAME)
    @TestHolder
    public static void should_extractFromInfuser_afterActivation(ECGameTestHelper helper) {
        var infuser = helper.getBlockEntity(new BlockPos(0, 2, 0), InfuserBlockEntity.class);
        var container = helper.requireElementContainer(new BlockPos(0, 1, 0));

        helper.startSequence().thenExecute(() -> {
                    infuser.getInventory().setItem(0, new ItemStack(Items.IRON_INGOT));
                    container.fill(ElementType.WATER);
                }).thenExecuteAfter(2, () -> {
                    assertThat(infuser.getInventory().getItem(0))
                            .is(ECItems.DRENCHED_IRON_INGOT.get())
                            .hasCount(1);
                    helper.pullLever(1, 2, 1);
                }).thenExecuteAfter(2, () -> {
                    helper.assertContainerContains(new BlockPos(0, 1, 1), ECItems.DRENCHED_IRON_INGOT.get());
                }).thenSucceed();
    }

    @GameTest(template = INFUSER_TEMPLATE_NAME)
    @TestHolder
    public static void should_extractFromInfuser(ECGameTestHelper helper) {
        var infuser = helper.getBlockEntity(new BlockPos(0, 2, 0), InfuserBlockEntity.class);
        var container = helper.requireElementContainer(new BlockPos(0, 1, 0));

        helper.startSequence().thenExecute(() -> {
            infuser.getInventory().setItem(0, new ItemStack(Items.IRON_INGOT));
            container.fill(ElementType.WATER);
            helper.pullLever(1, 2, 1);
        }).thenExecuteAfter(2, () -> {
            helper.assertContainerContains(new BlockPos(0, 1, 1), ECItems.DRENCHED_IRON_INGOT.get());
        }).thenSucceed();
    }

    @GameTest(template = BINDER_TEMPLATE_NAME)
    @TestHolder
    public static void should_extractFromBinder(ECGameTestHelper helper) {
        var binder = helper.getBlockEntity(new BlockPos(0, 2, 0), BinderBlockEntity.class);
        var container = helper.requireElementContainer(new BlockPos(0, 1, 0));

        helper.startSequence().thenExecute(() -> {
            var inv = binder.getInventory();

            inv.setItem(0, new ItemStack(Items.GOLD_INGOT));
            inv.setItem(1, new ItemStack(ECItems.DRENCHED_IRON_INGOT));
            inv.setItem(2, new ItemStack(Items.COPPER_INGOT));
            inv.setItem(3, new ItemStack(Items.REDSTONE));
            inv.setItem(4, new ItemStack(ECItems.AIR_CRYSTAL));
            container.fill(ElementType.AIR);
            helper.pullLever(1, 2, 1);
        }).thenExecuteAfter(2, () -> {
            helper.assertContainerContains(new BlockPos(0, 1, 1), ECItems.SWIFT_ALLOY_INGOT.get());
        }).thenSucceed();
    }

    @GameTest(template = BINDER_TEMPLATE_NAME)
    @TestHolder
    public static void should_extractOutputAndRemainingFromBinder(ECGameTestHelper helper) {
        var binder = helper.getBlockEntity(new BlockPos(0, 2, 0), BinderBlockEntity.class);
        var container = helper.requireElementContainer(new BlockPos(0, 1, 0));

        helper.startSequence().thenExecute(() -> {
            var inv = binder.getInventory();

            inv.setItem(0, new ItemStack(ECItems.SHRINE_BASE));
            inv.setItem(1, new ItemStack(ECItems.FIRE_CRYSTAL));
            inv.setItem(2, new ItemStack(Items.LAVA_BUCKET));
            inv.setItem(3, new ItemStack(Items.GOLD_INGOT));
            container.fill(ElementType.FIRE);
            helper.pullLever(1, 2, 1);
        }).thenExecuteAfter(2, () -> {
            helper.assertContainerContains(new BlockPos(0, 1, 1), ECBlocks.FIRE_PYLON.get().asItem());
            helper.assertContainerContains(new BlockPos(0, 1, 1), Items.BUCKET);
        }).thenSucceed();
    }

}
