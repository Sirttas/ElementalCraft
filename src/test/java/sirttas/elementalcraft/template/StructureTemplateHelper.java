package sirttas.elementalcraft.template;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ECGameTestUtils;
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
        if (runes.length == 0) {
            return tag;
        }

        var handler = new RuneHandler(runes.length);

        for (var rune : runes) {
            handler.addRune(ElementalCraftApi.RUNE_MANAGER.getOrCreateHolder(rune));
        }
        tag.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(handler));
        return tag;
    }

    public static @NotNull CompoundTag withContainerContent(ItemStack...stacks) {
        return withContainerContent(new CompoundTag(), stacks);
    }

    public static @NotNull CompoundTag withContainerContent(CompoundTag tag, ItemStack...stacks) {
        return ContainerHelper.saveAllItems(tag, NonNullList.of(ItemStack.EMPTY, stacks), ECGameTestUtils.registryAccess());
    }

    public static @NotNull CompoundTag withStackList(String tagName, ItemStack...stacks) {
        return withStackList(new CompoundTag(), tagName, stacks);
    }


    public static @NotNull CompoundTag withStackList(CompoundTag tag, String tagName, ItemStack...stacks) {
        var list = new ListTag();

        for (var stack : stacks) {
            list.add(stack.save(ECGameTestUtils.registryAccess()));
        }
        tag.put(tagName, list);
        return tag;
    }
}
