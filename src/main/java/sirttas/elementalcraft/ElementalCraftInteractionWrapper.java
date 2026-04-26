package sirttas.elementalcraft;

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
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.ElementalCraftInteraction;
import sirttas.elementalcraft.api.pureore.factory.IPureOreRecipeFactoryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ElementalCraftInteractionWrapper implements ElementalCraftInteraction {

    private final List<ElementalCraftInteraction> interactions;

    public ElementalCraftInteractionWrapper() {
        ServiceLoader<ElementalCraftInteraction> loader = ServiceLoader.load(ElementalCraftInteraction.class);

        interactions = new ArrayList<>(loader.stream()
                .map(ServiceLoader.Provider::get) // TODO try catch
                .filter(ElementalCraftInteraction::isActive)
                .toList());
        injectTests();
        ElementalCraftApi.LOGGER.info("Elemental Craft loaded {} interactions loaded: {}", interactions::size, () -> interactions.stream()
                .map(interaction -> interaction.getClass().getName())
                .collect(Collectors.joining(", ")));
    }

    private void injectTests() {
        if (!ModList.get().isLoaded("testframework")) {
            return;
        }

        try {
            var testInteractionClass = Class.forName("sirttas.elementalcraft.ElementalCraftTests");
            if (interactions.stream().noneMatch(interaction -> interaction.getClass().equals(testInteractionClass))) {
                var testInteraction = (ElementalCraftInteraction) testInteractionClass.getConstructor().newInstance();

                if (testInteraction.isActive()) {
                    interactions.add(testInteraction);
                }
            }
        } catch (Exception e) {
            ElementalCraftApi.LOGGER.error("Failed to inject tests from interactions", e);
        }
    }

    @Override
    public boolean isActive() {
        return !interactions.isEmpty();
    }

    @Override
    public <I extends RecipeInput, T extends Recipe<I>> T lookupRecipe(@NotNull Level level, @NotNull RecipeType<T> type, @NotNull I recipeInput) {
        return interactions.stream()
                .map(interaction -> interaction.lookupRecipe(level, type, recipeInput))
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
    public void addCraftingStations(BiConsumer<Object, ItemStack> consumer) {
        interactions.forEach(interaction -> interaction.addCraftingStations(consumer));
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
                .orElse(null);
    }

    <T> void addInteractionFromIMC(Supplier<?> supplier) {
        var interaction = (ElementalCraftInteraction) supplier.get();

        if (interaction != null) {
            interactions.add(interaction);
        }
    }
}
