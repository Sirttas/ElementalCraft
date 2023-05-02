package sirttas.elementalcraft.loot.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import sirttas.elementalcraft.api.rune.handler.RuneHandlerHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.loot.parameter.ECLootContextParams;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class LootRunes extends LootPoolSingletonContainer {
    private LootRunes(int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions) {
        super(weight, quality, conditions, functions);
    }

    @Override
    protected void createItemStack(@Nonnull Consumer<ItemStack> output, @Nonnull LootContext context) {
        var be = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);

        if (be == null) {
            return;
        }
        RuneHandlerHelper.get(be, context.getParamOrNull(ECLootContextParams.DIRECTION)).getRunes().forEach(rune -> output.accept(ECItems.RUNE.get().getRuneStack(rune)));
    }

    public static LootPoolSingletonContainer.Builder<?> builder() {
        return simpleBuilder(LootRunes::new);
    }

    @Nonnull
    @Override
    public LootPoolEntryType getType() {
        return ECLootPoolEntries.RUNES.get();
    }

    public static class Serializer extends LootPoolSingletonContainer.Serializer<LootRunes> {

        @Nonnull
        @Override
        protected LootRunes deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext context, int weight, int quality, @Nonnull LootItemCondition[] conditions, @Nonnull LootItemFunction[] functions) {
            return new LootRunes(weight, quality, conditions, functions);
        }
    }
}
