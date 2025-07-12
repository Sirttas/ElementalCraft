package sirttas.elementalcraft.item.source.receptacle;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.neoforged.testframework.Test;
import net.neoforged.testframework.annotation.ForEachTest;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ObjectAssert;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.assertion.Assertions;
import sirttas.elementalcraft.block.source.SourceBlock;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.source.SourceGameTestTemplates;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = ReceptacleGameTests.GROUP)
public class ReceptacleGameTests {

    public static final String GROUP = "stacks.receptacle";

    private static final InstanceOfAssertFactory<ReceptacleItem, ObjectAssert<ReceptacleItem>> RECEPTACLE_ITEM = new InstanceOfAssertFactory<>(ReceptacleItem.class, Assertions::assertThat);

    public static List<Test> collectTests() {
        var index = new AtomicInteger(0);

        return ElementType.ALL_VALID.stream()
                .<Test>mapMulti((t, downstream) -> {
                    var i = index.getAndIncrement();

                    downstream.accept(ECGameTestUtils.createTest(
                            GROUP,
                            "ReceptacleGameTests.should_pickupReceptacle#" + i,
                            "Check if a player picks-up a full receptacle when using an empty receptacle on it.",
                            SourceGameTestTemplates.getSourceTemplate(t),
                            h -> should_pickupReceptacle(h, t)));
                    downstream.accept(ECGameTestUtils.createTest(
                            GROUP,
                            "ReceptacleGameTests.should_pickupReceptacleAndDropStabilizer#" + i,
                            "Check if a player picks-up a full receptacle and a stabilizer when using an empty receptacle on it.",
                            SourceGameTestTemplates.getSourceWithStabilizerTemplate(t),
                            h -> should_pickupReceptacleAndDropStabilizer(h, t)));
                })
                .toList();
    }

    public static void should_pickupReceptacle(ECGameTestHelper helper, ElementType elementType) {
        var pos = new BlockPos(0, 1, 0);
        var player = helper.mockReceptaclePlayer();

        helper.startSequence()
                .thenExecute(() -> helper.useItemOn(player, pos))
                .thenExecuteAfter(1, ECGameTestUtils.fixAssertions(() -> {
                    helper.assertBlockNotPresent(SourceBlock.findSourceBlock(elementType), pos);
                    assertThat(helper.getEntities(EntityType.ITEM, pos, 1)).hasSize(1)
                            .allSatisfy(item -> assertThat(item.getItem())
                                    .is(ECTags.Items.RECEPTACLES)
                                    .hasCount(1)
                                    .hasDataComponentSatisfying(ECDataComponents.ELEMENT_AMOUNT, a -> assertThat(a).isPositive())
                                    .hasDataComponentSatisfying(ECDataComponents.SOURCE_TRAITS_HOLDER, h -> assertThat(h.getTraits()).isNotEmpty())
                                    .satisfies(s -> assertThat(s.getItem()).asInstanceOf(RECEPTACLE_ITEM)
                                            .satisfies(i -> assertThat(i.getElementType()).isEqualTo(elementType))));
                }))
                .thenExecute(player::discard)
                .thenSucceed();
    }

    public static void should_pickupReceptacleAndDropStabilizer(ECGameTestHelper helper, ElementType elementType) {
        var pos = new BlockPos(0, 1, 0);
        var player = helper.mockReceptaclePlayer();

        helper.startSequence()
                .thenExecute(() -> helper.useItemOn(player, pos))
                .thenExecuteAfter(1, ECGameTestUtils.fixAssertions(() -> {
                    helper.assertBlockNotPresent(SourceBlock.findSourceBlock(elementType), pos);
                    assertThat(helper.getEntities(EntityType.ITEM, pos, 1)).hasSize(2)
                            .anySatisfy(item -> assertThat(item.getItem())
                                    .is(ECItems.SOURCE_STABILIZER)
                                    .hasCount(1))
                            .anySatisfy(item -> assertThat(item.getItem())
                                    .is(ECTags.Items.RECEPTACLES)
                                    .hasCount(1)
                                    .hasDataComponentSatisfying(ECDataComponents.ELEMENT_AMOUNT, a -> assertThat(a).isPositive())
                                    .hasDataComponentSatisfying(ECDataComponents.SOURCE_TRAITS_HOLDER, h -> assertThat(h.getTraits()).isNotEmpty())
                                    .satisfies(s -> assertThat(s.getItem()).asInstanceOf(RECEPTACLE_ITEM)
                                            .satisfies(i -> assertThat(i.getElementType()).isEqualTo(elementType))));
                }))
                .thenExecute(player::discard)
                .thenSucceed();
    }
}
