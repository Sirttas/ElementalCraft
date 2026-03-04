package sirttas.elementalcraft.pureore;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import sirttas.elementalcraft.component.ECDataComponents;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record PureOre(
        Set<Holder<Item>> items,
        List<Ingredient> inputs,
        List<ItemStack> resultsForColor
) {

    public static final StreamCodec<RegistryFriendlyByteBuf, PureOre> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(HashSet::new, ByteBufCodecs.holderRegistry(Registries.ITEM)),
            PureOre::items,
            ByteBufCodecs.collection(ArrayList::new, Ingredient.CONTENTS_STREAM_CODEC),
            PureOre::inputs,
            ByteBufCodecs.collection(ArrayList::new, ItemStack.STREAM_CODEC),
            PureOre::resultsForColor,
            PureOre::new
    );

    public static Identifier getId(ItemStack stack) {
        return stack.get(ECDataComponents.PURE_ORE);
    }

    public PureOre(Set<Holder<Item>> items, List<Ingredient> inputs, List<ItemStack> resultsForColor) {
        this.items = Set.copyOf(items);
        this.inputs = List.copyOf(inputs);
        this.resultsForColor = List.copyOf(resultsForColor);
    }

    public boolean test(ItemStack stack) {
        return inputs.stream().anyMatch(ingredient -> ingredient.test(stack));
    }
}
