package sirttas.elementalcraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestBatch;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.function.Consumer;

public class ECGameTestHelper {

    public static final String EMPTY_TEMPLATE = "empty";
    public static final String EMPTY_CONTAINER_TEMPLATE = "empty_container";

    private ECGameTestHelper() {}

    public static TestFunction createTestFunction(String name, String template, Consumer<GameTestHelper> function) {
        return createTestFunction(name, template, Rotation.NONE, function);
    }

    public static TestFunction createTestFunction(String name, String template, Rotation rotation, Consumer<GameTestHelper> function) {
        return createTestFunction(GameTestBatch.DEFAULT_BATCH_NAME, name, template, rotation, function);
    }

    public static TestFunction createTestFunction(String batchName, String name, String template, Rotation rotation, Consumer<GameTestHelper> function) {
        if (!template.startsWith("elementalcraft:")) {
            template = "elementalcraft:" + template;
        }
        if (!name.endsWith(":" + template)) {
            name += ":" + template;
        }

        return new TestFunction(batchName, name, template, rotation, 100, 0, true, fixAssertions(function));
    }

    public static Consumer<GameTestHelper> fixAssertions(Consumer<GameTestHelper> function) {
        return helper -> {
            try {
                function.accept(helper);
            } catch (AssertionError e) {
                logAssertionError(e);
                helper.fail(e.getMessage());
            }
        };
    }

    public static Runnable fixAssertions(Runnable function) {
        return () -> {
            try {
                function.run();
            } catch (AssertionError e) {
                logAssertionError(e);
                throw new GameTestAssertException(e.getMessage());
            }
        };
    }

    private static void logAssertionError(AssertionError e) {
        ElementalCraftApi.LOGGER.error("Assertion failed: ", e);
    }

    public static void useItemOn(GameTestHelper helper, Player player, BlockPos pos) {
        useItemOn(helper, player, pos, Direction.NORTH);
    }

    public static void useItemOn(GameTestHelper helper, Player player, BlockPos pos, Direction direction) {
        var absolutePos = helper.absolutePos(pos);
        var result = new BlockHitResult(Vec3.atCenterOf(absolutePos), direction, absolutePos, true);
        var stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (player.isShiftKeyDown() && !stack.isEmpty()) {
            UseOnContext useoncontext = new UseOnContext(player, InteractionHand.MAIN_HAND, result);
            stack.useOn(useoncontext);
            return;
        }
        helper.useBlock(pos, player, result);
    }

    public static void discardItems(GameTestHelper helper, BlockPos pos, int expansionAmount) {
        helper.getLevel().getEntities(EntityType.ITEM, new AABB(helper.absolutePos(pos)).inflate(expansionAmount), Entity::isAlive).forEach(e -> e.remove(Entity.RemovalReason.DISCARDED));
    }
}
