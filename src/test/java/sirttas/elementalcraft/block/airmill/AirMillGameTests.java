package sirttas.elementalcraft.block.airmill;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.item.ECItems;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

public class AirMillGameTests {

    private static final BlockPos AIR_MILL_POS = new BlockPos(0, 2, 0);

    public static List<Test> collectTests() {
        var index = new AtomicInteger(0);

        return AirMillTestCaseHolder.HOLDERS.stream()
                .<Test>mapMulti((holder, consumer) -> {
                    var i = index.getAndIncrement();

                    consumer.accept(holder.createTest(
                            "should_dropDamagedMill#" + i,
                            "Check if a damaged mill drops damaged",
                            AirMillGameTests::should_dropDamagedMill));
                    consumer.accept(holder.createTest(
                            "should_getRepairedByPlayer#" + i,
                            "Check if a damaged mill can be repaired by a player",
                            AirMillGameTests::should_getRepairedByPlayer));
                    consumer.accept(holder.createTest(
                            "should_getFullyRepairedByPlayer#" + i,
                            "Check if a damaged mill can be fully repaired by a player and the item is consumed",
                            AirMillGameTests::should_getFullyRepairedByPlayer));
                })
                .toList();

    }

    private static void should_dropDamagedMill(ECGameTestHelper helper, AirMillTestCaseHolder holder) {
        AirMill airMill = helper.getBlockEntity(AIR_MILL_POS);

        airMill.setDamage(100);

        helper.startSequence().thenExecuteAfter(1, () -> {
            helper.getLevel().destroyBlock(helper.absolutePos(AIR_MILL_POS), true);
        }).thenExecuteAfter(5, ECGameTestUtils.fixAssertions(() -> {
            var items = helper.findEntities(EntityType.ITEM, 0, 0, 0, Double.MAX_VALUE);
            var airMilItemEntity = items.stream()
                    .filter(i -> i.getItem().is(holder.block().get().asItem()))
                    .findFirst();

            assertThat(airMilItemEntity).hasValueSatisfying(item -> assertThat(item.getItem())
                    .is(holder.block())
                    .satisfies(stack -> assertThat(stack.getItem()).isInstanceOf(AirMillBlockItem.class))
                    .hasDataComponentSatisfying(ECDataComponents.AIR_MILL_DAMAGE, damage -> assertThat(damage).isGreaterThanOrEqualTo(100)));
        }))
        .thenExecute(() -> helper.discardItems(new BlockPos(0, 1, 0), 2))
        .thenSucceed();
    }

    private static void should_getRepairedByPlayer(ECGameTestHelper helper, AirMillTestCaseHolder holder) {
        AirMill airMill = helper.getBlockEntity(AIR_MILL_POS);

        airMill.setDamage(100);

        var player = helper.makeMockPlayer(GameType.SURVIVAL);

        player.moveTo(helper.absoluteVec(Vec3.ZERO));
        helper.getLevel().addFreshEntity(player);

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ECItems.AIR_MILL));
        helper.startSequence().thenExecuteAfter(1, ECGameTestUtils.fixAssertions(() -> {
            helper.useItemOn(player, AIR_MILL_POS);

            assertThat(airMill.getDamage()).isZero();
            assertThat(player.getMainHandItem())
                    .is(ECItems.AIR_MILL)
                    .hasDamageSatisfying(damage -> assertThat(damage).isGreaterThanOrEqualTo(100));
        }))
        .thenExecute(player::discard)
        .thenSucceed();
    }

    private static void should_getFullyRepairedByPlayer(ECGameTestHelper helper, AirMillTestCaseHolder holder) {
        int maxDamage = AirMill.getMaxDamage();
        AirMill airMill = helper.getBlockEntity(AIR_MILL_POS);

        airMill.setDamage(maxDamage);

        var player = helper.makeMockPlayer(GameType.SURVIVAL);

        player.moveTo(helper.absoluteVec(Vec3.ZERO));
        helper.getLevel().addFreshEntity(player);

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ECItems.AIR_MILL));
        helper.startSequence().thenExecuteAfter(1, ECGameTestUtils.fixAssertions(() -> {
                    helper.useItemOn(player, AIR_MILL_POS);

                    assertThat(airMill.getDamage()).isZero();
                    assertThat(player.getMainHandItem()).isEmpty();
                }))
                .thenExecute(player::discard)
                .thenSucceed();
    }
}
