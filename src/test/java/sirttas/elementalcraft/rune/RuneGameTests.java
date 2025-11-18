package sirttas.elementalcraft.rune;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.rune.RuneItem;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;


public class RuneGameTests {

    public static Collection<Test> should_dropRunes() {
        var index = new AtomicInteger(0);

        return RuneTestCaseHolder.HOLDERS.stream()
                .map(t -> t.createTest(
                        "should_dropRunes_" + index.getAndIncrement(),
                        "Check if the rune is dropped when the block is destroyed",
                        RuneGameTests::should_dropRunes))
                .toList();
    }

    private static void should_dropRunes(ECGameTestHelper helper, RuneTestCaseHolder holder) {
        var pos = holder.pos();
        var runes = holder.runes();

        helper.getLevel().destroyBlock(helper.absolutePos(pos), true, null);

        var itemEntities = helper.getEntities(EntityType.ITEM, pos, 1);

        assertThat(itemEntities).hasSizeGreaterThanOrEqualTo(runes.size());

        var droppedRunes = itemEntities.stream()
                .map(ItemEntity::getItem)
                .filter(stack -> stack.is(ECItems.RUNE))
                .map(RuneItem::getRune)
                .collect(Collectors.toList());

        assertThat(droppedRunes)
                .describedAs("The dropped runes are not the expected ones")
                .hasSize(runes.size());

        for (var runeKey : runes) {
            for (var rune : droppedRunes) {
                if (rune.is(runeKey)) {
                    droppedRunes.remove(rune);
                    break;
                }
            }
        }

        assertThat(droppedRunes)
                .describedAs("The dropped runes are not the expected ones")
                .isEmpty();

        itemEntities.forEach(Entity::discard);
        helper.succeed();
    }

}
