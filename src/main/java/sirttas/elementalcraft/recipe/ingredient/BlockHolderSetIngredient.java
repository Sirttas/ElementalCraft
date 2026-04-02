package sirttas.elementalcraft.recipe.ingredient;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class BlockHolderSetIngredient implements ICustomIngredient {

    public static final MapCodec<@NotNull BlockHolderSetIngredient> CODEC = RegistryCodecs.homogeneousList(Registries.BLOCK)
            .xmap(BlockHolderSetIngredient::new, ingredient -> ingredient.blocks).fieldOf("blocks");
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull BlockHolderSetIngredient> STREAM_CODEC = ByteBufCodecs.holderSet(Registries.BLOCK)
            .map(BlockHolderSetIngredient::new, ingredient -> ingredient.blocks);

    protected final HolderSet<@NotNull Block> blocks;

    @Nullable
    protected HolderSet<@NotNull Item> items;

    public BlockHolderSetIngredient(HolderSet<@NotNull Block> blocks) {
        this.blocks = blocks;
    }

    protected HolderSet<@NotNull Item> dissolve() {
        if (items == null) {
            List<Holder<@NotNull Item>> list = new ArrayList<>();
            for (Holder<@NotNull Block> block : blocks) {
                var item = block.value().asItem();
                if (item != Items.AIR) {
                    list.add(item.builtInRegistryHolder());
                }
            }

            items = HolderSet.direct(list);
        }
        return items;
    }

    public HolderSet<@NotNull Block> blocks() {
        return blocks;
    }

    @Override
    public @NotNull Stream<Holder<@NotNull Item>> items() {
        return dissolve().stream();
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack == null)
            return false;

        return dissolve().contains(stack.typeHolder());
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public @NotNull IngredientType<?> getType() {
        return ECIngredientTypes.BLOCK_HOLDER_SET.get();
    }

    @Override
    public @NotNull SlotDisplay display() {
        return new SlotDisplay.Composite(dissolve().stream()
                .map(Ingredient::displayForSingleItem)
                .toList());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BlockHolderSetIngredient ingredient && Objects.equals(this.blocks, ingredient.blocks);
    }

    @Override
    public int hashCode() {
        return this.blocks.hashCode();
    }
}
