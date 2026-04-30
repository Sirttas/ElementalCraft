package sirttas.elementalcraft.block.shrine.enderlock;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;
import static sirttas.elementalcraft.assertion.Assertions.within;

@ForEachTest(groups = ShrineGameTestHelper.GROUP)
public class EnderLockShrineGameTests {

    private static final Method TELEPORT = ObfuscationReflectionHelper.findMethod(EnderMan.class, "teleport", double.class, double.class, double.class);
    private static final String TEMPLATE = "elementalcraft:enderlockshrinegametests.should_preventendermanfromteleporting";

    @TestHolder
    @GameTest(template = TEMPLATE)
    public static void should_preventEnderManFromTeleporting(GameTestHelper helper) {
        var relativeVec = new Vec3(2.5, 0.5, -2.5);
        var enderman = helper.spawn(EntityType.ENDERMAN, relativeVec);
        var vec = helper.absoluteVec(relativeVec);

        helper.startSequence().thenExecute(() -> {
            ShrineGameTestHelper.getShrine(helper, BlockPos.ZERO).getElementStorage().fill();
        }).thenExecuteAfter(1, () -> {
            try {
                var to = vec.add(10, 0, 0);

                TELEPORT.invoke(enderman, to.x, to.y, to.z);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new GameTestAssertException(Component.literal(e.getMessage()), (int) helper.getTick());
            }
        }).thenExecuteAfter(1, () -> {
            assertThat(enderman.getPosition(0)).satisfies(p -> {
                assertThat(p.x()).isCloseTo(vec.x(), within(0.2));
                assertThat(p.z()).isCloseTo(vec.z(), within(0.2));
            });
            enderman.discard();
        }).thenSucceed();
    }

}
