package sirttas.elementalcraft;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Rotation;
import net.neoforged.testframework.Test;
import net.neoforged.testframework.TestFramework;
import net.neoforged.testframework.gametest.GameTestData;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import net.neoforged.testframework.impl.TestFrameworkImpl;
import net.neoforged.testframework.impl.test.AbstractTest;
import org.apache.commons.lang3.StringUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ECGameTestUtils {

    private ECGameTestUtils() {}

    public static Test createTest(String group, String id, String description, String template, Consumer<ECGameTestHelper> function) {
        return createTest(group, id, description, template, Rotation.NONE, function);
    }

    public static Test createTest(String group, String id, String description, String template, Rotation rotation, Consumer<ECGameTestHelper> function) {
        if (!template.startsWith("elementalcraft:")) {
            template = "elementalcraft:" + template;
        }

        var suffix = "_" + template.replace(":", "_");

        if (!id.endsWith(suffix)) {
            id += suffix;
        }

        return new ECTest(id, group, description, Either.left(template), rotation, function);
    }

    public static Test createTest(String group, String id, String description, Supplier<StructureTemplateBuilder> template, Rotation rotation, Consumer<ECGameTestHelper> function) {
        return new ECTest(id, group, description, Either.right(template), rotation, function);
    }

    public static RegistryAccess registryAccess() {
        return ElementalCraftTests.server.registryAccess();
    }

    public static void logAssertionError(AssertionError e) {
        ElementalCraftApi.LOGGER.error("Assertion failed: ", e);
    }

    private static class ECTest extends AbstractTest.Dynamic {
        private final String group;
        private final Either<String, Supplier<StructureTemplateBuilder>> template;
        private final Rotation rotation;
        private final Consumer<ECGameTestHelper> function;


        private ECTest(
                String id,
                String group,
                String description,
                Either<String, Supplier<StructureTemplateBuilder>> template,
                Rotation rotation,
                Consumer<ECGameTestHelper> function) {
            this.id = id;
            this.group = group;
            this.template = template;
            this.rotation = rotation;
            this.function = function;
            this.enabledByDefault = true;
            this.groups.clear();
            if (StringUtils.isNotBlank(group)) {
                this.groups.add(group);
            }
            this.visuals = new Visuals(
                    Component.literal(TestFrameworkImpl.capitaliseWords(id, "_")),
                    StringUtils.isNotBlank(description) ? List.of(Component.literal(description)) : Collections.emptyList());
        }

        @Override
        public void init(TestFramework framework) {
                super.init(framework);
            var templateName = template.map(Function.identity(), builder -> createModId() + ":gametest_template");

            this.gameTestData = new GameTestData(
                    StringUtils.isNotBlank(group) ? group : null,
                    templateName,
                    true,
                    1,
                    1,
                    this::onGameTest,
                    100,
                    0,
                    rotation,
                    true,
                    0,
                    false);

            this.onGameTest(ECGameTestHelper.class, helper -> {
                try {
                    function.accept(helper);
                } catch (AssertionError e) {
                    logAssertionError(e);
                    helper.fail(e.getMessage());
                }
            });
            template.right().ifPresent(this::registerGameTestTemplate);
        }
    }
}
