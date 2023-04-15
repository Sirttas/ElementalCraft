package sirttas.elementalcraft;

import net.minecraft.gametest.framework.GameTestBatch;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;

import java.util.function.Consumer;

public class ECGameTestHelper {

    private ECGameTestHelper() {}

    public static TestFunction createTestFunction(String name, String template, Consumer<GameTestHelper> function) {
        return new TestFunction(GameTestBatch.DEFAULT_BATCH_NAME, name, template, 100, 0, true, function);
    }

}
