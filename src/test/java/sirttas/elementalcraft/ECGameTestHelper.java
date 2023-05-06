package sirttas.elementalcraft;

import net.minecraft.gametest.framework.GameTestBatch;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.level.block.Rotation;

import java.util.function.Consumer;

public class ECGameTestHelper {

    private ECGameTestHelper() {}

    public static TestFunction createTestFunction(String name, String template, Consumer<GameTestHelper> function) {
        return createTestFunction(name, template, Rotation.NONE, function);
    }

    public static TestFunction createTestFunction(String name, String template, Rotation rotation, Consumer<GameTestHelper> function) {
        return createTestFunction(GameTestBatch.DEFAULT_BATCH_NAME, name, template, rotation, function);
    }

    public static TestFunction createTestFunction(String batchName, String name, String template, Rotation rotation, Consumer<GameTestHelper> function) {
        return new TestFunction(batchName, name, template, rotation, 100, 0, true, function);
    }

}
