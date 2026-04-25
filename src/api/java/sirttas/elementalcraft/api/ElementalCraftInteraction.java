package sirttas.elementalcraft.api;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.pureore.factory.IPureOreRecipeFactoryType;

import java.util.List;
import java.util.function.BiConsumer;

public interface ElementalCraftInteraction {

    String IMC_METHOD = "elementalcraft_interaction";

    static boolean isMekanismActive() {
        return ModList.get().isLoaded("mekanism");
    }

    static boolean isBotaniaActive() {
        return ModList.get().isLoaded("botania");
    }

    static boolean isAppliedEnergistics2Active() {
        return ModList.get().isLoaded("ae2");
    }

    boolean isActive();

    default <I extends RecipeInput, T extends Recipe<I>> T lookupRecipe(@NotNull Level level, @NotNull RecipeType<T> type, @NotNull I recipeInput) {
        return null;
    }

    default void registerPureOreRecipeInjectors(RegisterEvent.RegisterHelper<@NotNull IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry) {}

    default List<ItemStack> getHolders(LivingEntity entity) {
        return List.of();
    }

    default void addCraftingStations(BiConsumer<Object, ItemStack> consumer) {}

    @ApiStatus.Internal
    default void registerTestFramework(IEventBus modBus, ModContainer container) {}

    @Deprecated
    default int[] lookupColors(ItemStack stack) {
        return null;
    }


}
