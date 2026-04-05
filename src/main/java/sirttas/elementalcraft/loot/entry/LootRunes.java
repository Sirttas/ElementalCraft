package sirttas.elementalcraft.loot.entry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.loot.parameter.ECLootContextParams;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

public class LootRunes extends LootPoolSingletonContainer {

    public static final MapCodec<LootRunes> CODEC = RecordCodecBuilder.mapCodec(builder -> singletonFields(builder).apply(builder, LootRunes::new));
    private LootRunes(int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
    }

    @Override
    public @NotNull MapCodec<? extends LootPoolSingletonContainer> codec() {
        return CODEC;
    }

    @Override
    protected void createItemStack(@Nonnull Consumer<ItemStack> output, @Nonnull LootContext context) {
        var be = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);

        if (be == null) {
            return;
        }

        var runeHandler = BlockEntityHelper.getCapability(ElementalCraftCapabilities.RuneHandlers.BLOCK, be, context.getParamOrNull(ECLootContextParams.DIRECTION));

        if (runeHandler == null) {
            return;
        }
        runeHandler.getRunes().forEach(rune -> output.accept(ECItems.RUNE.get().getRuneStack(rune)));
    }

    public static LootPoolSingletonContainer.Builder<?> builder() {
        return simpleBuilder(LootRunes::new);
    }
}
