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
import java.util.function.Consumer;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class ShrineRangeGameTests {

    public static final String BATCH_NAME = "shrineRange";

    @GameTestGenerator
    public static Collection<TestFunction> should_haveRange() {
        var index = new AtomicInteger(0);

        return List.of(
                createTestFunction(index.getAndIncrement(), "oreshrinegametests.should_mineinrange", h -> should_haveRange(h, new BlockPos(13, 2, 13), new AABB(1, -4, 1, 26, 2, 26))),
                createTestFunction(index.getAndIncrement(), "crystalgrowthshrineupgradegametests.should_growamethyst", h -> should_haveRange(h, new BlockPos(1, 2, 3), new AABB(-3, -2, -1, 6, 7, 8))),
                createTestFunction(index.getAndIncrement(), "vacuumshrinegametests.should_pullandpickup", h -> should_haveRange(h, new BlockPos(2, 2, 2), new AABB(-8, -8, -8, 13, 13, 13))),
                createTestFunction(index.getAndIncrement(), "lavashrinegametests.should_meltbasaltintolava", h -> should_haveRange(h, new BlockPos(2, 1, 2), new AABB(1, 2, 1, 4, 3, 4))),
                createTestFunction(index.getAndIncrement(), "lumbershrinegametests.should_cutoakblocks", h -> should_haveRange(h, new BlockPos(3, 2, 3), new AABB(-1, 2, -1, 8, 11, 8))),
                createTestFunction(index.getAndIncrement(), "harvestshrinegametests.should_harvestwheat", h -> should_haveRange(h, new BlockPos(3, 4, 3), new AABB(-1, 1, -1, 8, 4, 8))),
                createTestFunction(index.getAndIncrement(), "groveshrinegametests.should_generateflowers", h -> should_haveRange(h, new BlockPos(3, 2, 3), new AABB(-2, 1, -2, 9, 4, 9))),
                createTestFunction(index.getAndIncrement(), "sweetshrinegametests.should_feedplayer", h -> should_haveRange(h, new BlockPos(0, 1, 0), new AABB(-10, -9, -10, 11, 12, 11))),
                createTestFunction(index.getAndIncrement(), "overloadshrinegametests.should_speedupfurnace", h -> should_haveRange(h, new BlockPos(0, 1, 0), new AABB(0, 2, 0, 1, 3, 1))),
                createTestFunction(index.getAndIncrement(), "spawningshrinegametests.should_spawnmobs", h -> should_haveRange(h, new BlockPos(5, 2, 5), new AABB(1, 2, 1, 10, 3, 10))),
                createTestFunction(index.getAndIncrement(), "enderlockshrinegametests.should_preventendermanfromteleporting", h -> should_haveRange(h, new BlockPos(0, 1, 0), new AABB(-10, 1, -10, 11, 4, 11))),
                createTestFunction(index.getAndIncrement(), "breedingshrinegametests.should_breedcows", h -> should_haveRange(h, new BlockPos(0, 2, 3), new AABB(0, -8, -7, 21, 13, 14))),
                createTestFunction(index.getAndIncrement(), "breedingshrinegametests.should_breedcows", Rotation.CLOCKWISE_90, h -> should_haveRange(h, new BlockPos(0, 2, 3), new AABB(- 13, -8, 0, 8, 13, 21))),
                createTestFunction(index.getAndIncrement(), "breedingshrinegametests.should_breedcows", Rotation.CLOCKWISE_180, h -> should_haveRange(h, new BlockPos(0, 2, 3), new AABB(-20, -8, -13, 1, 13, 8))),
                createTestFunction(index.getAndIncrement(), "breedingshrinegametests.should_breedcows", Rotation.COUNTERCLOCKWISE_90, h -> should_haveRange(h, new BlockPos(0, 2, 3), new AABB(-7, -8, -20, 14, 13, 1))),
                createTestFunction(index.getAndIncrement(), "translocationshrineupgradegametests.should_growcropsaroundanchor", h -> should_haveRange(h, new BlockPos(4, 2, 4), new AABB(1, 2, 1, 8, 5, 8))),
                createTestFunction(index.getAndIncrement(), "translocationshrineupgradegametests.should_growcropsaroundanchor", helper -> {
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
        assertThat(shrine.getRange()).isEqualTo(range.move(helper.absolutePos(BlockPos.ZERO)));
        helper.succeed();
    }

    public static TestFunction createTestFunction(int index, String template, Consumer<GameTestHelper> function) {
        return createTestFunction(index, template, Rotation.NONE, function);
    }

    public static TestFunction createTestFunction(int index, String template, Rotation rotation, Consumer<GameTestHelper> function) {
        return ECGameTestHelper.createTestFunction(BATCH_NAME, "should_haveRange#" + index, template, rotation, function);
    }
}
