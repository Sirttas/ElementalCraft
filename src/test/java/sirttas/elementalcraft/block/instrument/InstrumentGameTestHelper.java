package sirttas.elementalcraft.block.instrument;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentGameTestHelper {

    private InstrumentGameTestHelper() {}

    public static <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(GameTestHelper helper, ItemStack input, ElementType elementType, Consumer<T> consumer) {
        runInstrument(helper, input, elementType, true, consumer);
    }

    public static <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(GameTestHelper helper, ItemStack input, ElementType elementType, boolean recipeAvailable, Consumer<T> consumer) {
        var instrument = (T) helper.getBlockEntity(new BlockPos(0, 2, 0));
        var container = ElementStorageHelper.get(helper.getBlockEntity(new BlockPos(0, 1, 0))).orElse(null);

        assertThat(instrument).isNotNull();
        assertThat(container).isNotNull();

        helper.startSequence().thenExecute(() -> {
                    instrument.getInventory().setItem(0, input);
                    container.insertElement(1000000, elementType, false);

                    assertThat(instrument.isRecipeAvailable()).isEqualTo(recipeAvailable);
                }).thenExecuteAfter(2, () -> consumer.accept(instrument))
                .thenSucceed();
    }
}
