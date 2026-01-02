package sirttas.elementalcraft.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.EmptyTemplate;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class SpectralToolGameTests {

    @GameTest
    @TestHolder(description = "Checks that the spectral tool break the designated block.")
    public static void should_breakDesignatedBlock(DynamicTest test) {
        test.registerGameTestTemplate(() -> StructureTemplateBuilder.withSize(3, 3, 3)
                        .set(0, 0, 0, Blocks.COBBLESTONE.defaultBlockState()));

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var tool = helper.createSpectralTool(null, new Vec3(2, 1, 1), new ItemStack(Items.DIAMOND_PICKAXE));

            helper.startSequence()
                    .thenExecute(() -> tool.digBlock(new BlockPos(0, 1, 0), Blocks.COBBLESTONE.defaultBlockState()))
                    .thenExecuteAfter(60, ECGameTestUtils.fixAssertions(() -> {
                        helper.assertBlockNotPresent(Blocks.COBBLESTONE, new BlockPos(0, 1, 0));
                    }))
                    .thenExecute(tool::discard)
                    .thenSucceed();
        });
    }

    @GameTest
    @EmptyTemplate(value = "5x5x5", floor = true)
    @TestHolder(description = "Checks that the spectral tool drops tool when killed.")
    public static void should_dropToolWhenKilled(ECGameTestHelper helper) {
        var tool = helper.createSpectralTool(null, new Vec3(3, 2, 3), new ItemStack(Items.DIAMOND_PICKAXE));

        helper.startSequence()
                .thenExecute(tool::kill)
                .thenExecuteAfter(5, ECGameTestUtils.fixAssertions(() -> {
                    assertThat(tool.isAlive()).as("Spectral tool should be removed").isFalse();
                    helper.assertItemEntityPresent(Items.DIAMOND_PICKAXE);
                }))
                .thenExecute(tool::discard)
                .thenSucceed();
    }

}
