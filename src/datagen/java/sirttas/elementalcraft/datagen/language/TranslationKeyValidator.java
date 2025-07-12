package sirttas.elementalcraft.datagen.language;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class TranslationKeyValidator {

    private static final Field DATA = ObfuscationReflectionHelper.findField(LanguageProvider.class, "data");
    private static final Field LOCALE = ObfuscationReflectionHelper.findField(LanguageProvider.class, "locale");

    private final List<LanguageProvider> providers;

    public TranslationKeyValidator(List<LanguageProvider> providers) {
        this.providers = providers;
    }

    public boolean hasKey(String key) {
        return doHasKey(key).isEmpty();
    }

    @SuppressWarnings("unchecked")
    private List<String> doHasKey(String key) {
        if ("chat.square_brackets".equals(key)) { // advancements has this set as default name
            return List.of();
        }
        return providers.stream()
                .filter(provider -> {
                    try {
                        return !((Map<String, String>) DATA.get(provider)).containsKey(key);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .map(provider -> {
                    try {
                        return (String) LOCALE.get(provider);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .toList();
    }

    public boolean hasComponent(Component component) {
        if (component.getContents() instanceof TranslatableContents translatableContents) {
            return hasKey(translatableContents.getKey());
        }
        return true;
    }

    public void checkHasKey(String key) {
        var list = doHasKey(key);

        if (!list.isEmpty()) {
            throw new IllegalStateException("Missing translation in [" + String.join(", ", list) + "] for key: " + key);
        }
    }

    public void checkHasComponent(Component component) {
        if (component.getContents() instanceof TranslatableContents translatableContents) {
            checkHasKey(translatableContents.getKey());
        }
    }
}
