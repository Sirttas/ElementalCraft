package sirttas.elementalcraft.range;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.testframework.Test;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.block.diffuser.DiffuserBlockEntity;
import sirttas.elementalcraft.block.diffuser.DiffuserGameTests;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.block.shrine.melting.MeltingShrineGameTests;
import sirttas.elementalcraft.block.shrine.upgrade.directional.RangeShrineUpgradeTemplates;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.CrystalHarvestShrineUpgradeGameTests;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlockEntity;
import sirttas.elementalcraft.block.synthesizer.cracking.CrackingSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.cracking.CrackingSynthesizerGameTests;
import sirttas.elementalcraft.block.synthesizer.cracking.sculk.SculkCrackingSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.cracking.sculk.SculkCrackingSynthesizerGameTests;
import sirttas.elementalcraft.block.synthesizer.vibration.VibrationSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.vibration.VibrationSynthesizerGameTests;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

public class RangeGameTests {

    public static final String GROUP = "range";

    public static Collection<Test> should_haveRange() {
        var i = 0;

        return List.of(
                createTest(i++, DiffuserGameTests.TEMPLATE_23x23_NAME, helper -> should_haveRange(helper, new BlockPos(11, 3, 11), new AABB(1, -7, 1, 22, 14, 22), DiffuserBlockEntity::getRange)),
                createTest(i++, CrackingSynthesizerGameTests.CRACKING_SYNTHESIZER_TEMPLATE_NAME, helper -> should_haveRange(helper, new BlockPos(5, 3, 5), new AABB(0, 0, 0, 11, 2,  11), CrackingSynthesizerBlockEntity::getRange)),
                createTest(i++, CrackingSynthesizerGameTests.CRACKING_SYNTHESIZER_WITH_RUNE_TEMPLATE_NAME, helper -> should_haveRange(helper, new BlockPos(6, 3, 6), new AABB(0, 0, 0, 13, 2,  13), CrackingSynthesizerBlockEntity::getRange)),
                createTest(i++, VibrationSynthesizerGameTests.TEMPLATE_NAME, helper -> should_haveRange(helper, new BlockPos(10, 3, 10), new AABB(0, -7, 0, 21, 14,  21), VibrationSynthesizerBlockEntity::getRange)),
                createTest(i++, SculkCrackingSynthesizerGameTests.SCULK_CRACKING_SYNTHESIZER_TEMPLATE_NAME, helper -> should_haveRange(helper, new BlockPos(8, 3, 8), new AABB(0, -5, 0, 17, 12,  17), SculkCrackingSynthesizerBlockEntity::getRange)),
                createTest(i++, MeltingShrineGameTests.MELTING_SHRINE_TEMPLATE_NAME, helper -> should_haveRange(helper, new BlockPos(1, 1, 1), new AABB(1, 2, 1, 2, 3, 2))),
                createTest(i++, MeltingShrineGameTests.MELTING_SHRINE_WITH_FILLING_TEMPLATE_NAME, helper -> should_haveRange(helper, new BlockPos(1, 1, 1), new AABB(1, 2, 1, 2, 3, 2))),
                createTest(i++, CrystalHarvestShrineUpgradeGameTests.TEMPLATE_NAME,helper -> should_haveRange(helper, new BlockPos(11, 2, 11), new AABB(6, -3, 6, 17, 8, 17))),
                createTest(i++, "oreshrinegametests.should_mineinrange", helper -> should_haveRange(helper, new BlockPos(13, 2, 13), new AABB(1, -4, 1, 26, 2, 26))),
                createTest(i++, "crystalgrowthshrineupgradegametests.should_growamethyst", helper -> should_haveRange(helper, new BlockPos(1, 2, 3), new AABB(-3, -2, -1, 6, 7, 8))),
                createTest(i++, "vacuumshrinegametests.should_pullandpickup", helper -> should_haveRange(helper, new BlockPos(2, 2, 2), new AABB(-8, -4 /* TODO -8 */, -8, 13, 13, 13))),
                createTest(i++, "lumbershrinegametests.should_cutoakblocks", helper -> should_haveRange(helper, new BlockPos(3, 2, 3), new AABB(-1, 2, -1, 8, 11, 8))),
                createTest(i++, "harvestshrinegametests.should_harvestwheat", helper -> should_haveRange(helper, new BlockPos(3, 4, 3), new AABB(-1, 1, -1, 8, 4, 8))),
                createTest(i++, "groveshrinegametests.should_generateflowers", helper -> should_haveRange(helper, new BlockPos(3, 2, 3), new AABB(-2, 1, -2, 9, 4, 9))),
                createTest(i++, "sweetshrinegametests.should_feedplayer", helper -> should_haveRange(helper, new BlockPos(0, 1, 0), new AABB(-10, -4 /* TODO -9 */, -10, 11, 12, 11))),
                createTest(i++, "overloadshrinegametests.should_speedupfurnace", helper -> should_haveRange(helper, new BlockPos(0, 1, 0), new AABB(0, 2, 0, 1, 3, 1))),
                createTest(i++, "spawningshrinegametests.should_spawnmobs", helper -> should_haveRange(helper, new BlockPos(5, 2, 5), new AABB(1, 2, 1, 10, 3, 10))),
                createTest(i++, "enderlockshrinegametests.should_preventendermanfromteleporting", helper -> should_haveRange(helper, new BlockPos(0, 1, 0), new AABB(-10, 1, -10, 11, 4, 11))),
                createTest(i++, "breedingshrinegametests.should_breedcows", helper -> should_haveRange(helper, new BlockPos(0, 2, 3), new AABB(0, -4 /* TODO -8 */, -7, 21, 13, 14))),
                createTest(i++, "breedingshrinegametests.should_breedcows", Rotation.CLOCKWISE_90, helper -> should_haveRange(helper, new BlockPos(0, 2, 3), new AABB(- 13, -4 /* TODO -8 */, 0, 8, 13, 21))),
                createTest(i++, "breedingshrinegametests.should_breedcows", Rotation.CLOCKWISE_180, helper -> should_haveRange(helper, new BlockPos(0, 2, 3), new AABB(-20, -4 /* TODO -8 */, -13, 1, 13, 8))),
                createTest(i++, "breedingshrinegametests.should_breedcows", Rotation.COUNTERCLOCKWISE_90, helper -> should_haveRange(helper, new BlockPos(0, 2, 3), new AABB(-7, -4 /* TODO -8 */, -20, 14, 13, 1))),
                createTest(i++, "growthshrinegametests.should_growcrops", helper -> should_haveRange(helper, new BlockPos(5, 2, 5), new AABB(1, 2, 1, 10, 5, 10))),
                createTest(i++, "translocationshrineupgradegametests.should_growcropsaroundanchor", helper -> should_haveRange(helper, new BlockPos(4, 2, 4), new AABB(1, 2, 1, 8, 5, 8))),
                createTest(i++, "translocationshrineupgradegametests.should_growcropsaroundanchor", helper -> {
                    TranslocationShrineUpgradeBlockEntity upgrade = helper.getBlockEntity(new BlockPos(5, 2, 4));
                    var shrine = ShrineGameTestHelper.getShrine(helper, new BlockPos(4, 2, 4));
                    var targetPos = helper.absolutePos(new BlockPos(9, 2, 4));

                    upgrade.setTarget(targetPos);
                    should_haveRange(helper, shrine, new AABB(6, 2, 1, 13, 5, 8));
                }),
                createTest(i++, RangeShrineUpgradeTemplates.HARVEST_SHRINE_WITH_1_RANGE_TEMPLATE_NAME, helper -> should_haveRange(helper, new BlockPos(0, 2, 0), new AABB(-7, -1, -7, 8, 2, 8)))
        );
    }

    private static void should_haveRange(GameTestHelper helper, BlockPos pos, AABB range) {
        should_haveRange(helper, ShrineGameTestHelper.getShrine(helper, pos), range);
    }

    private static void should_haveRange(GameTestHelper helper, AbstractShrineBlockEntity shrine, AABB range) {
        assertThat(shrine).isNotNull();

        shrine.refresh();

        assertThat(moveRange(helper, shrine.getRange())).isEqualTo(range);
        helper.succeed();
    }

    private static <T extends BlockEntity> void should_haveRange(GameTestHelper helper, BlockPos pos, AABB range, Function<T, AABB> rangeGetter) {
        T blockEntity = helper.getBlockEntity(pos);

        assertThat(blockEntity).isNotNull();
        assertThat(moveRange(helper, rangeGetter.apply(blockEntity))).isEqualTo(range);
        helper.succeed();
    }

    private static AABB moveRange(GameTestHelper helper, AABB range) {
        var absolutePos = helper.absolutePos(BlockPos.ZERO);

        return range.move(new BlockPos(-absolutePos.getX(), -absolutePos.getY(), -absolutePos.getZ()));
    }

    public static Test createTest(int index, String template, Consumer<ECGameTestHelper> function) {
        return createTest(index, template, Rotation.NONE, function);
    }

    public static Test createTest(int index, Supplier<StructureTemplateBuilder> template, Consumer<ECGameTestHelper> function) {
        return createTest(index, template, Rotation.NONE, function);
    }

    public static Test createTest(int index, String template, Rotation rotation, Consumer<ECGameTestHelper> function) {
        return ECGameTestUtils.createTest(
                GROUP,
                "should_haveRange_" + index,
                "Check range for block entity in template: " + template,
                template,
                rotation,
                function);
    }

    public static Test createTest(int index, Supplier<StructureTemplateBuilder> template, Rotation rotation, Consumer<ECGameTestHelper> function) {
        return ECGameTestUtils.createTest(
                GROUP,
                "should_haveRange_" + index,
                "Check range for block entity in anonymous template",
                template,
                rotation,
                function);
    }
}
