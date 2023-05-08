package sirttas.elementalcraft.block.shrine.properties;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.entity.BlockEntityGameTestHelper;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlockEntity;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class ShrineRangeGameTests {

    @GameTestGenerator
    public static Collection<TestFunction> should_haveRange() {
        var index = new AtomicInteger(0);

        return List.of(
                ECGameTestHelper.createTestFunction("should_haveRange#" + index.getAndIncrement(), "elementalcraft:oreshrinegametests.should_mineinrange", h -> should_haveRange(h, new BlockPos(13, 2, 13), new AABB(1, -4, 1, 26, 2, 26))),
                ECGameTestHelper.createTestFunction("should_haveRange#" + index.getAndIncrement(), "elementalcraft:crystalgrowthshrineupgradegametests.should_growamethyst", h -> should_haveRange(h, new BlockPos(1, 2, 3), new AABB(-3, -2, -1, 6, 7, 8))),
                ECGameTestHelper.createTestFunction("should_haveRange#" + index.getAndIncrement(), "elementalcraft:breedingshrinegametests.should_breedcows", h -> should_haveRange(h, new BlockPos(0, 2, 3), new AABB(0, -8, -7, 21, 13, 14))),
                ECGameTestHelper.createTestFunction("should_haveRange#" + index.getAndIncrement(), "elementalcraft:breedingshrinegametests.should_breedcows", Rotation.CLOCKWISE_90, h -> should_haveRange(h, new BlockPos(0, 2, 3), new AABB(- 13, -8, 0, 8, 13, 21))),
                ECGameTestHelper.createTestFunction("should_haveRange#" + index.getAndIncrement(), "elementalcraft:breedingshrinegametests.should_breedcows", Rotation.CLOCKWISE_180, h -> should_haveRange(h, new BlockPos(0, 2, 3), new AABB(-20, -8, -13, 1, 13, 8))),
                ECGameTestHelper.createTestFunction("should_haveRange#" + index.getAndIncrement(), "elementalcraft:breedingshrinegametests.should_breedcows", Rotation.COUNTERCLOCKWISE_90, h -> should_haveRange(h, new BlockPos(0, 2, 3), new AABB(-7, -8, -20, 14, 13, 1))),
                ECGameTestHelper.createTestFunction("should_haveRange#" + index.getAndIncrement(), "elementalcraft:translocationshrineupgradegametests.should_growcropsaroundanchor", h -> should_haveRange(h, new BlockPos(4, 2, 4), new AABB(1, 2, 1, 8, 5, 8))),
                ECGameTestHelper.createTestFunction("should_haveRange#" + index.getAndIncrement(), "elementalcraft:translocationshrineupgradegametests.should_growcropsaroundanchor", helper -> {
                    var upgrade = (TranslocationShrineUpgradeBlockEntity) BlockEntityGameTestHelper.getBlockEntity(helper, new BlockPos(5, 2, 4));
                    var shrine = ShrineGameTestHelper.getShrine(helper, new BlockPos(4, 2, 4));
                    var targetPos = helper.absolutePos(new BlockPos(9, 2, 4));

                    upgrade.setTarget(targetPos);
                    shrine.refresh();

                    should_haveRange(helper, shrine, new AABB(6, 2, 1, 13, 5, 8));
                })
        );
    }


    private static void should_haveRange(GameTestHelper helper, BlockPos pos, AABB range) {
        should_haveRange(helper, ShrineGameTestHelper.getShrine(helper, pos), range);
    }

    private static void should_haveRange(GameTestHelper helper, AbstractShrineBlockEntity shrine, AABB range) {
        assertThat(shrine).isNotNull();
        assertThat(shrine.getRangeBoundingBox()).isEqualTo(range.move(helper.absolutePos(BlockPos.ZERO)));
        helper.succeed();
    }
}
