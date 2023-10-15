package sirttas.elementalcraft.block.instrument;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Rotation;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentGameTestHelper {

    public static final String BATCH_NAME = "instrument";

    private InstrumentGameTestHelper() {}

    public static <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(GameTestHelper helper, ItemStack input, ElementType elementType, Consumer<T> consumer) {
        runInstrument(helper, input, elementType, true, consumer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(GameTestHelper helper, ItemStack input, ElementType elementType, boolean recipeAvailable, Consumer<T> consumer) {
        var instrument = (T) helper.getBlockEntity(new BlockPos(0, 2, 0));
        var container = ElementStorageHelper.get(helper.getBlockEntity(new BlockPos(0, 1, 0))).orElse(null);

        assertThat(instrument).isNotNull();
        assertThat(container).isNotNull();

        helper.startSequence().thenExecute(() -> {
                    instrument.getInventory().setItem(0, input);
                    container.fill(elementType);

                    assertThat(instrument.isRecipeAvailable()).isEqualTo(recipeAvailable);
                }).thenExecuteAfter(2, () -> consumer.accept(instrument))
                .thenSucceed();
    }

    public static TestFunction createTestFunction(String name, String template, Consumer<GameTestHelper> function) {
        return createTestFunction(name, template, Rotation.NONE, function);
    }

    public static TestFunction createTestFunction(String name, String template, Rotation rotation, Consumer<GameTestHelper> function) {
        return ECGameTestHelper.createTestFunction(BATCH_NAME, name, template, rotation, function);
    }
}
