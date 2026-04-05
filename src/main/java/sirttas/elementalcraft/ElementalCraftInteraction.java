package sirttas.elementalcraft;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.pureore.factory.IPureOreRecipeFactoryType;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;

import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public interface ElementalCraftInteraction {

    static boolean isMekanismActive() {
        return ModList.get().isLoaded("mekanism");
    }

    static boolean isBotaniaActive() {
        return ModList.get().isLoaded("botania");
    }

    static boolean isSilentGearActive() {
        return ModList.get().isLoaded("silentgear");
    }

    static boolean isImmersiveEngineeringActive() {
        return ModList.get().isLoaded("immersiveengineering");
    }

    static boolean isAppliedEnergistics2Active() {
        return ModList.get().isLoaded("ae2");
    }

    boolean isActive();

    default GrindingRecipe lookupCrusherRecipe(@NotNull Level level, @NotNull SimpleIOInstrumentRecipeInput recipeInput) {
        return null;
    }

    default void registerPureOreRecipeInjectors(RegisterEvent.RegisterHelper<@NotNull IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry) {}

    default List<ItemStack> getHolders(LivingEntity entity) {
        return List.of();
    }

    default void registerTestFramework(IEventBus modBus, ModContainer container) {}

    default int[] lookupColors(ItemStack stack) {
        return null;
    }

    class Wrapper implements ElementalCraftInteraction {

        private final List<ElementalCraftInteraction> interactions;

        public Wrapper() {
            ServiceLoader<ElementalCraftInteraction> loader = ServiceLoader.load(ElementalCraftInteraction.class);

            interactions = loader.stream()
                    .map(ServiceLoader.Provider::get)
                    .filter(ElementalCraftInteraction::isActive)
                    .toList();
            ElementalCraftApi.LOGGER.info("Elemental Craft loaded {} interactions loaded: {}", interactions::size, () -> interactions.stream()
                    .map(interaction -> interaction.getClass().getName())
                    .collect(Collectors.joining(", ")));
            }

        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public GrindingRecipe lookupCrusherRecipe(@NotNull Level level, @NotNull SimpleIOInstrumentRecipeInput recipeInput) {
            return interactions.stream()
                    .map(interaction -> interaction.lookupCrusherRecipe(level, recipeInput))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void registerPureOreRecipeInjectors(RegisterEvent.RegisterHelper<@NotNull IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry) {
            interactions.forEach(interaction -> interaction.registerPureOreRecipeInjectors(registry));
        }

        @Override
        public List<ItemStack> getHolders(LivingEntity entity) {
            return interactions.stream()
                    .flatMap(interaction -> interaction.getHolders(entity).stream())
                    .toList();
        }

        @Override
        public void registerTestFramework(IEventBus modBus, ModContainer container) {
            interactions.forEach(interaction -> interaction.registerTestFramework(modBus, container));
        }

        @Override
        public int[] lookupColors(ItemStack stack) {
            return interactions.stream()
                    .map(interaction -> interaction.lookupColors(stack))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(new int[] { -1, -1, -1 });
        }
    }
}
