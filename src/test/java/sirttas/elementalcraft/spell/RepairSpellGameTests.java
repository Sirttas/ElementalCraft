package sirttas.elementalcraft.spell;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.EmptyTemplate;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = RepairSpellGameTests.GROUP)
public class RepairSpellGameTests {

    public static final String GROUP = "spell.repair";

    @GameTest
    @EmptyTemplate(floor = true)
    @TestHolder(description = "Checks that repair spell repair spell in other hand.")
    public static void should_repairItemInOtherHand(ECGameTestHelper helper) {
        var player = helper.mockPlayerWithSpell(new Vec3(1, 0, 1), Spells.REPAIR);
        var damagedItem = new ItemStack(Items.DIAMOND_PICKAXE);

        damagedItem.setDamageValue(10);
        player.setItemInHand(InteractionHand.OFF_HAND, damagedItem);
        player.lookAt(EntityAnchorArgument.Anchor.EYES, helper.absoluteVec(new Vec3(9, 0, 1)));
        helper.startSequence()
                .thenExecuteFor(20, () -> helper.useItem(player))
                .thenExecuteAfter(2, () -> {
                    assertThat(player.getItemInHand(InteractionHand.OFF_HAND))
                            .is(Items.DIAMOND_PICKAXE)
                            .hasDamage(0);
                    helper.assertElementUsed(player, ElementType.FIRE);
                })
                .thenExecute(player::discard)
                .thenSucceed();
    }
}
