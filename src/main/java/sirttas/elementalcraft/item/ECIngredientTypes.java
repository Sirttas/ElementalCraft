package sirttas.elementalcraft.item;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.source.receptacle.NaturalSourceIngredient;

public class ECIngredientTypes {

    private static final DeferredRegister<IngredientType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, ElementalCraftApi.MODID);

    public static final DeferredHolder<IngredientType<?>, IngredientType<NaturalSourceIngredient>> NATURAL_SOURCE = DEFERRED_REGISTER.register("natural_source", () -> new IngredientType<>(NaturalSourceIngredient.CODEC));

    private ECIngredientTypes() {}

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }

}
