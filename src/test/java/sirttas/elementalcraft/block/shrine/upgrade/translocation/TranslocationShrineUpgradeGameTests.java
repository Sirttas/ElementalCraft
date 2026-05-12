package sirttas.elementalcraft.block.shrine.upgrade.translocation;


import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineGameUpgradeTests;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.template.ECStructureTemplateBuilder;

import java.util.List;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = ShrineGameUpgradeTests.GROUP)
public class TranslocationShrineUpgradeGameTests {

    private static final List<BlockPos> CROPS = List.of(
            new BlockPos(9, 1, 1),
            new BlockPos(9, 1, 2),
            new BlockPos(9, 1, 3),
            new BlockPos(9, 1, 5),
            new BlockPos(9, 1, 6),
            new BlockPos(9, 1, 7),
            new BlockPos(10, 1, 1),
            new BlockPos(10, 1, 2),
            new BlockPos(10, 1, 3),
            new BlockPos(10, 1, 5),
            new BlockPos(10, 1, 6),
            new BlockPos(10, 1, 7),
            new BlockPos(11, 1, 1),
            new BlockPos(11, 1, 2),
            new BlockPos(11, 1, 3),
            new BlockPos(11, 1, 4),
            new BlockPos(11, 1, 5),
            new BlockPos(11, 1, 6),
            new BlockPos(11, 1, 7)
    );
    public static final String TEMPLATE = "elementalcraft:translocationshrineupgradegametests.should_growcropsaroundanchor";

    @TestHolder(description = "Checks that the translocation shrine upgrade grows crops around the anchor")
    @GameTest(template = TEMPLATE)
    public static void should_growCropsAroundAnchor(ECGameTestHelper helper) {
        helper.withTranslocationAnchorAt(new BlockPos(9, 1, 4), anchorPos -> {
            var upgrade = helper.getBlockEntity(new BlockPos(5, 1, 4), TranslocationShrineUpgradeBlockEntity.class);
            var shrine = ShrineGameTestHelper.getShrine(helper, new BlockPos(4, 1, 4));

            upgrade.setTarget(anchorPos);
            shrine.refresh();

            assertThat(upgrade.getTarget()).isEqualTo(anchorPos);
            assertThat(shrine.getTargetPos()).isEqualTo(anchorPos);
            assertThat(shrine.getUpgradeCount(ShrineUpgrades.TRANSLOCATION)).isEqualTo(1);

            ShrineGameTestHelper.forcePeriods(shrine, CROPS.size() * 7);
            helper.succeedIf(() -> CROPS.forEach(pos -> helper.assertBlockState(pos, b -> b.getValue(CropBlock.AGE) == 7, _ -> Component.literal("Crop has not been grown"))));
        });
    }

    @TestHolder(description = "Checks that right-clicking a translocation anchor with the upgrade item stores the anchor position on the item")
    @GameTest(template = TEMPLATE)
    public static void should_registerPosWhenRightClickingAnchor(DynamicTest test) {
        test.registerGameTestTemplate(() -> ECStructureTemplateBuilder.withSize(1, 2, 1)
                .set(0, 0, 0, ECBlocks.WHITE_ROCK.get().defaultBlockState())
                .set(0, 1, 0, ECBlocks.TRANSLOCATION_ANCHOR.get().defaultBlockState())
                .unpack());

        test.onGameTest(ECGameTestHelper.class, helper -> helper.withTranslocationAnchorAt(new BlockPos(0, 1, 0), anchorPos -> {
            var player = helper.mockPlayerWithItem(Vec3.ZERO, new ItemStack(ECItems.TRANSLOCATION_SHRINE_UPGRADE.get()));

            helper.useItemOn(player, new BlockPos(0, 1, 0));
            assertThat(player.getMainHandItem()).hasDataComponentWithValue(ECDataComponents.TARGET_ANCHOR, anchorPos);
            helper.succeed();
        }));
    }
}
