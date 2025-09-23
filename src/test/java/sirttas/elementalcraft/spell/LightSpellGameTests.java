package sirttas.elementalcraft.spell;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.jewel.JewelTestHelper;
import sirttas.elementalcraft.jewel.StriderJewelGameTests;

@ForEachTest(groups = StriderJewelGameTests.GROUP)
public class LightSpellGameTests {

    public static final String GROUP = "spell.light";

    @GameTest
    @TestHolder(description = "Checks that the light spell spawn an elemental ember.")
    public static void should_placeElementalEmber(DynamicTest test) {
        test.registerGameTestTemplate(() -> StructureTemplateBuilder.withSize(10, 3, 3)
                .fill(9, 0, 0, 9, 2, 2, ECBlocks.WHITE_ROCK_BRICK.get()));

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var player = helper.mockPlayerWithSpell(new Vec3(1, 1, 1), Spells.LIGHT);

            player.lookAt(EntityAnchorArgument.Anchor.EYES, helper.absoluteVec(new Vec3(9, 1, 1)));
            helper.startSequence()
                    .thenExecute(() -> helper.useItem(player))
                    .thenExecuteAfter(2, ECGameTestUtils.fixAssertions(() -> {
                        helper.assertBlockPresent(ECBlocks.ELEMENTAL_EMBER.get(), new BlockPos(8, 1, 1));
                        JewelTestHelper.assertElementUsed(player, ElementType.FIRE);
                    }))
                    .thenExecute(player::discard)
                    .thenSucceed();
        });
    }

}
