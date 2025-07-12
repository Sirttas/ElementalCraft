package sirttas.elementalcraft.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.data.DataManagerCodecs;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.component.ECDataComponents;

import javax.annotation.Nonnull;
import java.util.List;

public class SetRuneFunction extends LootItemConditionalFunction {

    public static final MapCodec<SetRuneFunction> CODEC = RecordCodecBuilder.mapCodec(builder -> commonFields(builder).and(
            DataManagerCodecs.holderCodec(ElementalCraftApi.RUNE_MANAGER_KEY, Rune.CODEC, false).fieldOf("rune").forGetter(r -> r.rune)
    ).apply(builder, SetRuneFunction::new));

    private final Holder<Rune> rune;

    private SetRuneFunction(List<LootItemCondition> condition, Holder<Rune> rune) {
        super(condition);
        this.rune = rune;
    }

    @Nonnull
    @Override
    public ItemStack run(@Nonnull ItemStack stack, @NotNull LootContext context) {
        stack.set(ECDataComponents.RUNE, rune);
        return stack;
    }

    public static Builder<?> builder(Holder<Rune> rune) {
        return simpleBuilder(l -> new SetRuneFunction(l, rune));
    }

    @Nonnull
    @Override
    public LootItemFunctionType<SetRuneFunction> getType() {
        return ECLootFunctions.SET_RUNE.get();
    }
}
