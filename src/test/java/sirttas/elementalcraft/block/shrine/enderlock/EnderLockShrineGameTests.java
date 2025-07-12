package sirttas.elementalcraft.block.shrine.enderlock;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.testframework.annotation.TestHolder;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;
import static sirttas.elementalcraft.assertion.Assertions.within;

public class EnderLockShrineGameTests {

    private static final Method TELEPORT = ObfuscationReflectionHelper.findMethod(EnderMan.class, "teleport", double.class, double.class, double.class);
    private static final String TEMPLATE = "elementalcraft:enderlockshrinegametests.should_preventendermanfromteleporting";

    @TestHolder
    @GameTest(template = TEMPLATE)
    public static void should_preventEnderManFromTeleporting(GameTestHelper helper) {
        var relativeVec = new Vec3(2.5, 1.5, -2.5);
        var enderman = helper.spawn(EntityType.ENDERMAN, relativeVec);
        var vec = helper.absoluteVec(relativeVec);

        helper.startSequence().thenExecute(() -> {
            ShrineGameTestHelper.getShrine(helper, new BlockPos(0, 1, 0)).getElementStorage().fill();
        }).thenExecuteAfter(1, () -> {
            try {
                var to = vec.add(10, 0, 0);

                TELEPORT.invoke(enderman, to.x, to.y, to.z);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new GameTestAssertException(e.getMessage());
            }
        }).thenExecuteAfter(1, ECGameTestUtils.fixAssertions(() -> {
            assertThat(enderman.getPosition(0)).satisfies(p -> {
                assertThat(p.x()).isCloseTo(vec.x(), within(0.2));
                assertThat(p.z()).isCloseTo(vec.z(), within(0.2));
            });
            enderman.discard();
        })).thenSucceed();
    }

}
