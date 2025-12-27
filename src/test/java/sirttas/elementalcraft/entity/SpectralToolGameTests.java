package sirttas.elementalcraft.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.entity.spectral.SpectralTool;
import sirttas.elementalcraft.spell.Spells;

public class SpectralToolGameTests {

    @GameTest
    @TestHolder(description = "Checks that the spectral tool break the designated block.")
    public static void should_breakDesignatedBlock(DynamicTest test) {
        test.registerGameTestTemplate(() -> StructureTemplateBuilder.withSize(3, 3, 3)
                        .set(0, 0, 0, Blocks.COBBLESTONE.defaultBlockState()));

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var player = helper.mockPlayerWithSpell(new Vec3(1, 1, 1), Spells.SPECTRAL_TOOL);
            var tool = new SpectralTool(player, new ItemStack(Items.DIAMOND_PICKAXE));

            tool.moveTo(helper.absoluteVec(new Vec3(2, 1, 1)));
            helper.getLevel().addFreshEntity(tool);
            helper.startSequence()
                    .thenExecute(() -> tool.digBlock(new BlockPos(0, 1, 0), Blocks.COBBLESTONE.defaultBlockState()))
                    .thenExecuteAfter(60, ECGameTestUtils.fixAssertions(() -> {
                        helper.assertBlockNotPresent(Blocks.COBBLESTONE, new BlockPos(0, 1, 0));
                    }))
                    .thenExecute(() -> {
                        player.discard();
                        tool.discard();
                    })
                    .thenSucceed();
        });
    }

}
