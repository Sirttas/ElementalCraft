package sirttas.elementalcraft;

import net.minecraft.gametest.framework.GameTestAssertException;
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
        if (!template.startsWith("elementalcraft:")) {
            template = "elementalcraft:" + template;
        }
        if (!name.endsWith(":" + template)) {
            name += ":" + template;
        }

        return new TestFunction(batchName, name, template, rotation, 100, 0, true, fixAssertions(function));
    }

    public static Consumer<GameTestHelper> fixAssertions(Consumer<GameTestHelper> function) {
        return helper -> {
            try {
                function.accept(helper);
            } catch (AssertionError e) {
                helper.fail(e.getMessage());
            }
        };
    }

    public static Runnable fixAssertions(Runnable function) {
        return () -> {
            try {
                function.run();
            } catch (AssertionError e) {
                throw new GameTestAssertException(e.getMessage());
            }
        };
    }

}
