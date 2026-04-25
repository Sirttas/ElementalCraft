package sirttas.elementalcraft.block.shrine.growth;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.CropBlock;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import java.util.List;
import java.util.stream.IntStream;

@ForEachTest(groups = ShrineGameTestHelper.GROUP)
public class GrowthShrineGameTests {

    private static final List<BlockPos> CROPS = IntStream.rangeClosed(1, 9)
            .mapToObj(z -> IntStream.rangeClosed(1, 9).mapToObj(x -> new BlockPos(x, 2, z)))
            .flatMap(s -> s)
            .filter(p -> p.getX() != 5 || p.getZ() != 5)
            .toList();
    private static final String TEMPLATE = "elementalcraft:growthshrinegametests.should_growcrops";

    @TestHolder
    @GameTest(template = TEMPLATE)
    public static void should_growCrops(GameTestHelper helper) {
        helper.startSequence()
                .thenExecuteAfter(1, () -> ShrineGameTestHelper.forcePeriods(helper, new BlockPos(5, 2, 5), CROPS.size() * 7))
                .thenExecuteAfter(1, () -> CROPS.forEach(pos -> helper.assertBlockState(pos, b -> b.getValue(CropBlock.AGE) == 7, _ -> Component.literal("Crop has not been grown"))))
                .thenSucceed();
    }

}
