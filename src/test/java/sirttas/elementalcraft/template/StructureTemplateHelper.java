package sirttas.elementalcraft.template;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;

import java.util.function.Consumer;

public class StructureTemplateHelper {

    private StructureTemplateHelper() { }

    @SafeVarargs
    public static @NotNull CompoundTag withTag(Consumer<CompoundTag>...runes) {
        var tag = new CompoundTag();

        for (var rune : runes) {
            rune.accept(tag);
        }
        return tag;
    }

    @SafeVarargs
    public static @NotNull CompoundTag addRuneHandler(ResourceKey<Rune>...runes) {
        return addRuneHandler(new CompoundTag(), runes);
    }

    @SafeVarargs
    public static @NotNull CompoundTag addRuneHandler(CompoundTag tag, ResourceKey<Rune>...runes) {
        var handler = new RuneHandler(runes.length);

        for (var rune : runes) {
            handler.addRune(ElementalCraftApi.RUNE_MANAGER.get(rune));
        }
        tag.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(handler));
        return tag;
    }
}
