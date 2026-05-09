package sirttas.elementalcraft.template;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueOutput;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;

import java.util.Collection;
import java.util.function.Consumer;

public class StructureTemplateNbtHelper {

    private StructureTemplateNbtHelper() { }

    @SafeVarargs
    public static CompoundTag withValue(Consumer<ValueOutput>... values) {
        var valueOutput = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, ECGameTestUtils.registryAccess());

        for (var rune : values) {
            rune.accept(valueOutput);
        }
        return valueOutput.buildResult();
    }

    @SuppressWarnings("unchecked")
    public static Consumer<ValueOutput> runeHandler(Collection<ResourceKey<Rune>> runes) {
        return runeHandler(runes.toArray(ResourceKey[]::new));
    }

    @SafeVarargs
    public static Consumer<ValueOutput> runeHandler(ResourceKey<Rune>... runes) {
        return output -> {
            var handler = new RuneHandler(runes.length);

            for (var rune : runes) {
                handler.addRune(ElementalCraftApi.RUNE_MANAGER.getOrCreateHolder(rune));
            }
            output.putChild(ECNames.RUNE_HANDLER, handler);
        };
    }

    public static Consumer<ValueOutput> itemList(Collection<ItemStack> stacks) {
        return itemList(stacks.toArray(ItemStack[]::new));
    }

    public static Consumer<ValueOutput> itemList(ItemStack... stacks) {
        return output -> ContainerHelper.saveAllItems(output, NonNullList.of(ItemStack.EMPTY, stacks), false);
    }


    public static Consumer<ValueOutput> elementStorage(ElementType type, int amount) {
        return elementStorage(type, amount, amount);
    }

    public static Consumer<ValueOutput> elementStorage(ElementType type, int amount, int capacity) {
        return output -> output.putChild(ECNames.ELEMENT_STORAGE, new SingleElementStorage(type, amount, capacity, null));
    }
}
