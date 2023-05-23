package sirttas.elementalcraft.block.shrine.enderlock;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import java.lang.reflect.InvocationTargetException;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;
import static sirttas.elementalcraft.assertion.Assertions.within;

@GameTestHolder(ElementalCraftApi.MODID)
public class EnderLockShrineGameTests {

    // elementalcraft:enderlockshrinegametests.should_preventendermanfromteleporting
    @GameTest(batch = ShrineGameTestHelper.BATCH_NAME)
    public static void should_preventEnderManFromTeleporting(GameTestHelper helper) {
        var level = helper.getLevel();
        var enderman = EntityType.ENDERMAN.create(helper.getLevel());
        var vec = helper.absoluteVec(new Vec3(2.5, 1.5, -2.5));

        level.addFreshEntity(enderman);
        helper.startSequence().thenExecute(() -> {
            ShrineGameTestHelper.getShrine(helper, new BlockPos(0, 1, 0)).getElementStorage().fill();
            enderman.moveTo(vec);
        }).thenExecuteAfter(1, () -> {
            try {
                ObfuscationReflectionHelper.findMethod(EnderMan.class, "m_32543_" /* teleport */, double.class, double.class, double.class).invoke(enderman, 0, 0, 0);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new GameTestAssertException(e.getMessage());
            }
        }).thenExecuteAfter(1, ECGameTestHelper.fixAssertions(() -> {
            assertThat(enderman.getPosition(0)).satisfies(p -> {
                assertThat(p.x()).isCloseTo(vec.x(), within(0.2));
                assertThat(p.z()).isCloseTo(vec.z(), within(0.2));
            });
            enderman.discard();
        })).thenSucceed();
    }

}
