package sirttas.elementalcraft.recipe.ingredient;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECIngredientTypes {

    private static final DeferredRegister<@NotNull IngredientType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, ElementalCraftApi.MODID);

    public static final DeferredHolder<@NotNull IngredientType<?>, @NotNull IngredientType<@NotNull BlockHolderSetIngredient>> BLOCK_HOLDER_SET = DEFERRED_REGISTER.register("bloc_holder_set", () -> new IngredientType<>(BlockHolderSetIngredient.CODEC));

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }
}
