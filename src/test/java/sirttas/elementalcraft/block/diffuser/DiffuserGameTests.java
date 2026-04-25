package sirttas.elementalcraft.block.diffuser;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.Test;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.item.holder.ElementHolderTestCaseHolder;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

public class DiffuserGameTests {

    public static final String TEMPLATE_23x23_NAME = "elementalcraft:diffuser_23x23";

    @RegisterStructureTemplate(TEMPLATE_23x23_NAME)
    public static final Supplier<StructureTemplate> TEMPLATE_23x23 = StructureTemplateBuilder.lazy(23, 4, 23, b -> b
            .fill(0, 0, 0, 22, 0, 22, ECBlocks.WHITE_ROCK_BRICKS.get().defaultBlockState())
            .set(11, 1, 11, ECBlocks.CONTAINER.get().defaultBlockState())
            .set(11, 2, 11, ECBlocks.DIFFUSER.get().defaultBlockState()));

    public static Collection<Test> should_fillHolder() {
        var index = new AtomicInteger(0);

        return ElementHolderTestCaseHolder.HOLDERS.stream()
                .map(t -> t.createTest(
                        "diffusergametests.should_fillHolder_" + index.getAndIncrement(),
                        "Check if a diffuser can fill holders in a player inventory.",
                        TEMPLATE_23x23_NAME,
                        DiffuserGameTests::should_fillHolder))
                .toList();
    }

    private static void should_fillHolder(ECGameTestHelper helper, ElementHolderTestCaseHolder holder) {
        var elementType = holder.type();
        var player = holder.mockPlayer(helper, new Vec3(9, 1, 9));
        var storage = helper.getBlockEntity(new BlockPos(11, 2, 11), ElementContainerBlockEntity.class).getElementStorage();
        var playerStorage = player.getCapability(ElementalCraftCapabilities.ElementStorages.ENTITY_FOR_ELEMENT, elementType);
        var ticks = new AtomicInteger(0);

        assertThat(playerStorage).isNotNull();

        helper.startSequence()
                .thenExecute(() -> storage.fill(elementType))
                .thenIdle(1)
                .thenExecuteFor(10, () -> {
                    var i = ticks.incrementAndGet();

                    assertThat(storage.getElementAmount(elementType)).isEqualTo(100000 - (5 * i));
                    assertThat(playerStorage.getElementAmount(elementType)).isEqualTo(5 * i);
                })
                .thenExecute(player::discard)
                .thenSucceed();
    }

}
