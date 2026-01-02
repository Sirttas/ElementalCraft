package sirttas.elementalcraft.spell;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.EmptyTemplate;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.entity.ECEntities;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = SpectralToolSpellGameTests.GROUP)
public class SpectralToolSpellGameTests {

    public static final String GROUP = "spell.spectral_tool";

    @GameTest
    @EmptyTemplate(floor = true)
    @TestHolder(description = "Checks that spectral tool spell create a spectral tool entity.")
    public static void should_createSpectralToolEntity(ECGameTestHelper helper) {
        var player = helper.mockPlayerWithSpell(new Vec3(1, 1, 1), Spells.SPECTRAL_TOOL);

        player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.DIAMOND_PICKAXE));
        player.lookAt(EntityAnchorArgument.Anchor.EYES, helper.absoluteVec(new Vec3(9, 1, 1)));
        helper.startSequence()
                .thenExecute(() -> helper.useItem(player))
                .thenExecuteAfter(2, ECGameTestUtils.fixAssertions(() -> {
                    assertThat(player.getItemInHand(InteractionHand.OFF_HAND))
                            .isEmpty();
                    helper.assertEntitiesPresent(ECEntities.SPECTRAL_TOOL.get(), 1);
                    helper.assertElementUsed(player, ElementType.AIR);
                }))
                .thenExecute(() -> {
                    player.discard();
                    helper.findEntities(ECEntities.SPECTRAL_TOOL.get(), 0, 0, 0, 3).forEach(Entity::discard);
                })
                .thenSucceed();
    }

    @GameTest
    @EmptyTemplate(value = "5x3x3", floor = true)
    @TestHolder(description = "Checks that spectral tool spell can be used to unsummon tools.")
    public static void should_unsummonSpectralToolEntity(ECGameTestHelper helper) {
        var player = helper.mockPlayerWithSpell(new Vec3(1, 3, 2), Spells.SPECTRAL_TOOL);
        var tool = helper.createSpectralTool(player, new Vec3(4, 2, 2), new ItemStack(Items.DIAMOND_PICKAXE));

        helper.startSequence()
                .thenExecute(() -> {
                    player.lookAt(EntityAnchorArgument.Anchor.EYES, tool.position());
                    helper.useItem(player);
                })
                .thenExecuteAfter(5, ECGameTestUtils.fixAssertions(() -> {
                    assertThat(tool.isAlive()).as("Spectral tool should be removed").isFalse();
                    assertThat(player.getInventory().contains(new ItemStack(Items.DIAMOND_PICKAXE))).as("Pickaxe should return to player").isTrue();
                }))
                .thenExecute(() -> {
                    player.discard();
                    tool.discard();
                })
                .thenSucceed();
    }
}
