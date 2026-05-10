package sirttas.elementalcraft.spell;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.EmptyTemplate;
import net.neoforged.testframework.gametest.GameTest;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;

import static org.assertj.core.api.Assertions.assertThat;
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.itemList;
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.withValue;

@ForEachTest(groups = AirShieldSpellGameTests.GROUP)
public class AirShieldSpellGameTests {

    public static final String GROUP = "spell.air_shield";

    @GameTest
    @EmptyTemplate(value = "11x3x3", floor = true)
    @TestHolder(description = "Checks that the air shield spell blocks damage while channeling.")
    public static void should_blockDamage(ECGameTestHelper helper) {
        var player = helper.mockPlayerWithSpell(new Vec3(1, 1, 1), Spells.AIR_SHIELD);
        var zombie = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, new BlockPos(9, 1, 1));

        player.lookAt(EntityAnchorArgument.Anchor.EYES, helper.absoluteVec(new Vec3(9, 1, 1)));
        helper.startSequence()
                .thenExecute(() -> helper.useItem(player))
                .thenExecuteAfter(25, () -> player.hurt(helper.getLevel().damageSources().mobAttack(zombie), 5.0F))
                .thenExecuteAfter(1, () -> {
                    helper.assertEntityAlive(player);
                    assertThat(player.getHealth()).isEqualTo(player.getMaxHealth());
                    helper.assertElementUsed(player, ElementType.AIR);
                })
                .thenExecute(() -> {
                    player.discard();
                    zombie.discard();
                })
                .thenSucceed();
    }

    @GameTest
    @TestHolder(description = "Checks that the air shield spell blocks an arrow fired from a dispenser.")
    public static void should_blockArrow(DynamicTest test) {
        test.registerGameTestTemplate(() -> StructureTemplateBuilder.withSize(11, 3, 3)
                .fill(0, 0, 0, 10, 0, 2, Blocks.STONE)
                .set(9, 1, 1, Blocks.DISPENSER.defaultBlockState()
                        .setValue(DispenserBlock.FACING, Direction.WEST),
                        withValue(itemList(new ItemStack(Items.ARROW, 16))))
                .placeFloorLever(9, 2, 1, false)
                .set(9, 1, 1, Blocks.REDSTONE_LAMP.defaultBlockState()));

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var player = helper.mockPlayerWithSpell(new Vec3(1, 1, 1), Spells.AIR_SHIELD);

            player.lookAt(EntityAnchorArgument.Anchor.EYES, helper.absoluteVec(new Vec3(9, 1, 1)));
            helper.startSequence()
                    .thenExecute(() -> helper.useItem(player))
                    .thenExecuteAfter(25, () -> helper.pullLever(9, 2, 1))
                    .thenExecuteAfter(10, () -> {
                        helper.assertEntityAlive(player);
                        assertThat(player.getHealth()).isEqualTo(player.getMaxHealth());
                        helper.assertElementUsed(player, ElementType.AIR);
                    })
                    .thenExecute(player::discard)
                    .thenSucceed();
        });
    }
}