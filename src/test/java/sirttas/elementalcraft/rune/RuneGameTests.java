package sirttas.elementalcraft.rune;

import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.ECItems;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;


@GameTestHolder(ElementalCraftApi.MODID)
public class RuneGameTests {

    @GameTestGenerator
    public static Collection<TestFunction> should_dropRunes() {
        var index = new AtomicInteger(0);

        return RuneTestHolder.HOLDERS.stream()
                .map(t -> t.createTestFunction("should_dropRunes#" + index.getAndIncrement(), RuneGameTests::should_dropRunes))
                .toList();
    }

    private static void should_dropRunes(GameTestHelper helper, RuneTestHolder holder) {
        var pos = holder.pos();
        var runes = holder.runes();

        helper.getLevel().destroyBlock(helper.absolutePos(pos), true, null);

        var items = helper.getEntities(EntityType.ITEM, pos, 1);

        assertThat(items).hasSizeGreaterThanOrEqualTo(runes.size())
                .anySatisfy(item -> assertThat(item.getItem()) // we must use anySatisfy because we also have other items
                        .is(ECItems.RUNE)
                        .satisfies(stack -> assertThat(runes).anySatisfy(rune -> RuneGameTestHelper.assertRuneIs(stack, rune))));
        items.forEach(Entity::discard);
        helper.succeed();
    }

}
