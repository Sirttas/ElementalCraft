package sirttas.elementalcraft.block.instrument;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentGameTestHelper {

    public static final String BATCH_NAME = "instrument";

    private InstrumentGameTestHelper() {}

    public static <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(GameTestHelper helper, ItemStack input, ElementType elementType, Consumer<T> consumer) {
        runInstrument(helper, input, elementType, true, consumer);
    }

    public static <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(GameTestHelper helper, BlockPos pos, ItemStack input, ElementType elementType, Consumer<T> consumer) {
        runInstrument(helper, pos, input, elementType, true, consumer);
    }

    public static <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(GameTestHelper helper, ItemStack input, ElementType elementType, boolean recipeAvailable, Consumer<T> consumer) {
        runInstrument(helper, new BlockPos(0, 2, 0), input, elementType, recipeAvailable, consumer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(GameTestHelper helper, BlockPos pos, ItemStack input, ElementType elementType, boolean recipeAvailable, Consumer<T> consumer) {
        var instrument = (T) getInstrument(helper, pos);
        var container = getContainer(helper, pos.below());

        helper.startSequence().thenExecute(() -> {
                    instrument.getInventory().setItem(0, input);
                    container.fill(elementType);

                    assertThat(instrument.isRecipeAvailable()).isEqualTo(recipeAvailable);
                }).thenExecuteAfter(2, () -> consumer.accept(instrument))
                .thenSucceed();
    }

    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> T getInstrument(GameTestHelper helper, BlockPos pos) {
        try {
            var instrument = (T) helper.getBlockEntity(pos);

            assertThat(instrument).isNotNull();
            return instrument;
        } catch (AssertionError e) {
            throw new GameTestAssertException(e.getMessage());
        }
    }

    public static IElementStorage getContainer(GameTestHelper helper, BlockPos pos) {
        try {
            var be = helper.getBlockEntity(pos);

            assertThat(be).isNotNull();

            var container = BlockEntityHelper.getCapability(ElementalCraftCapabilities.ElementStorage.BLOCK, be, null);

            assertThat(container).isNotNull();
            return container;
        } catch (AssertionError e) {
            throw new GameTestAssertException(e.getMessage());
        }
    }

    public static TestFunction createTestFunction(String name, String template, Consumer<GameTestHelper> function) {
        return createTestFunction(name, template, Rotation.NONE, function);
    }

    public static TestFunction createTestFunction(String name, String template, Rotation rotation, Consumer<GameTestHelper> function) {
        return ECGameTestHelper.createTestFunction(BATCH_NAME, name, template, rotation, function);
    }
}
