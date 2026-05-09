package sirttas.elementalcraft.block.shrine.upgrade.fortune.greater;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.block.shrine.upgrade.HorizontalShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineGameUpgradeTests;
import sirttas.elementalcraft.rune.Runes;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.runeHandler;
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.withValue;

@ForEachTest(groups = ShrineGameUpgradeTests.GROUP)
public class GreaterFortuneShrineUpgradeGameTests {

    public static final String TEMPLATE_NAME = "elementalcraft:ore_shrine_with_greater_fortune_with_upgrade";

    @RegisterStructureTemplate(TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> TEMPLATE = StructureTemplateBuilder.lazy(25, 2, 25, builder -> builder
            .fill(0, 0, 0, 24, 0, 24, ECBlocks.WHITE_ROCK_BRICKS.get().defaultBlockState())
            .set(12, 0, 11, Blocks.IRON_ORE.defaultBlockState())
            .set(12, 0, 13, Blocks.IRON_ORE.defaultBlockState())
            .set(11, 0, 12, Blocks.IRON_ORE.defaultBlockState())
            .set(13, 0, 12, Blocks.IRON_ORE.defaultBlockState())
            .set(12, 1, 12, ECBlocks.ORE_SHRINE.get().defaultBlockState())
            .set(12, 1, 13, ECBlocks.GREATER_FORTUNE_SHRINE_UPGRADE.get().defaultBlockState().setValue(HorizontalShrineUpgradeBlock.FACING, Direction.NORTH), withValue(runeHandler(Runes.TZEENTCH))));

    @TestHolder
    @GameTest(template = TEMPLATE_NAME, required = false)
    public static void should_increaseOreLoot(ECGameTestHelper helper) {
        ShrineGameTestHelper.forcePeriods(helper, new BlockPos(12, 1, 12), 4);
        helper.succeedIf(() -> {
            helper.assertBlockState(new BlockPos(12, 0, 11), b -> b.is(Blocks.STONE), _ -> Component.literal("Block has not been mined"));
            helper.assertBlockState(new BlockPos(12, 0, 13), b -> b.is(Blocks.STONE), _ -> Component.literal("Block has not been mined"));
            helper.assertBlockState(new BlockPos(11, 0, 12), b -> b.is(Blocks.STONE), _ -> Component.literal("Block has not been mined"));
            helper.assertBlockState(new BlockPos(13, 0, 12), b -> b.is(Blocks.STONE), _ -> Component.literal("Block has not been mined"));
            var count = helper.getEntities(EntityType.ITEM, new BlockPos(12, 0, 12), 2).stream()
                    .map(ItemEntity::getItem)
                    .filter(i -> i.is(Items.RAW_IRON))
                    .mapToInt(ItemStack::getCount)
                    .sum();

            assertThat(count).isGreaterThan(4);
        });
    }

}
