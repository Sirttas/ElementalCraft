package sirttas.elementalcraft.pureore;

import com.google.common.collect.Iterables;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.pureore.PureOreException;
import sirttas.elementalcraft.api.pureore.factory.IPureOreRecipeFactory;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.pureore.factory.PureOreRecipeFactoryTypes;
import sirttas.elementalcraft.pureore.loader.IPureOreLoader;
import sirttas.elementalcraft.pureore.loader.LoadedPureOre;
import sirttas.elementalcraft.recipe.instrument.io.purification.OrePurificationRecipe;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class PureOreGenerator {

    private PureOreGenerator() { }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void reload(OnDatapackSyncEvent event) {
        var player = event.getPlayer();

        if (player != null) {
            // FIXME use configuration task instead
            PacketDistributor.sendToPlayer(player, new PureOreSyncPayload(PureOreManager.getInstance().getPureOres()));
        } else {
            new PureOreGenerator().reload(event.getPlayerList().getServer());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void reload(ServerStartedEvent event) {
        new PureOreGenerator().reload(event.getServer());
    }

    public void reload(MinecraftServer server) {
        var start = Instant.now();
        var recipeManager = server.getRecipeManager();
        var factories = createFactories(recipeManager);
        var registry = server.registryAccess();
        var pureOreSets = new HashMap<Identifier, LoadedPureOreSet>();

        ElementalCraftApi.LOGGER.info("Pure ore generation started.\n\r\tRecipe Types: {}",
                () -> factories.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")));
        ElementalCraft.PURE_ORE_LOADERS_MANAGER.holders()
                .sorted(Comparator.comparingInt(holder -> holder.value().getOrder()))
                .forEach(holder -> holder.value().generate(registry).forEach(e -> {
                    factories.forEach(factory -> addRecipes(e, factory));
                    pureOreSets.computeIfAbsent(e.getId(), i -> new LoadedPureOreSet()).ores.put(holder, e);
                }));

        pureOreSets.values().removeIf(o -> !o.isProcessable());

        var pureOres = pureOreSets.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toPureOre()));

        PureOreManager.getInstance().replacePureOres(pureOres);
        PacketDistributor.sendToAllPlayers(new PureOreSyncPayload(pureOres));

        if (ECConfig.SERVER.pureOreRecipeInjection.get()) {
            ElementalCraftApi.LOGGER.info("Building pure ore recipes.");

            var loadedPureOreSets = pureOreSets.values().stream().distinct().toList();
            var recipes = recipeManager.getRecipes().stream()
                    .filter(r -> !isPureOreRecipe(r))
                    .toList();

            var processingRecipes = factories.stream()
                    .<RecipeHolder<?>>mapMulti((factory, downstream) -> buildRecipes(registry, factory, loadedPureOreSets).forEach(downstream))
                    .filter(ElementalCraftUtils.distinctBy(RecipeHolder::id))
                    .toList();
            var orePurificationRecipes = pureOreSets.entrySet().stream()
                    .<RecipeHolder<@NotNull OrePurificationRecipe>>mapMulti((entry, consumer) -> entry.getValue().getOrePurificationRecipes(entry.getKey()).forEach(consumer))
                    .toList();

            ElementalCraftApi.LOGGER.info("Injecting pure ore recipes.");
            replaceRecipes(recipeManager, Iterables.concat(recipes, processingRecipes, orePurificationRecipes), server.getWorldData().enabledFeatures());
            ElementalCraftApi.LOGGER.info("Pure ore recipe injection finished: {} processing recipes added and {} ore purification added.", processingRecipes::size, orePurificationRecipes::size);
        }

        ElementalCraftApi.LOGGER.info("Pure ore generation ended in {}ms\r\n\tOres: {}.",
                () -> Duration.between(start, Instant.now()).toMillis(),
                () -> pureOreSets.keySet().stream()
                        .map(Identifier::toString)
                        .collect(Collectors.joining(", ")));
    }

    private void replaceRecipes(RecipeManager recipeManager, Iterable<RecipeHolder<?>> recipes, FeatureFlagSet enabledFlags) {
        recipeManager.recipes = RecipeMap.create(recipes);
        recipeManager.finalizeRecipeLoading(enabledFlags);
    }

    private Collection<? extends IPureOreRecipeFactory<?, ? extends Recipe<?>>> createFactories(@Nonnull RecipeManager recipeManager) {
        return PureOreRecipeFactoryTypes.REGISTRY.stream()
                .map(t -> t.create(recipeManager))
                .toList();
    }

    private <C extends RecipeInput, T extends Recipe<@NotNull C>> void addRecipes(LoadedPureOre ore, IPureOreRecipeFactory<C, T> factory) {
        factory.getRecipes(ore.getOres()).forEach(h -> ore.addRecipe(h.value()));
    }

    private boolean isPureOreRecipe(RecipeHolder<?> holder) {
        var id = holder.id();
        var identifier = id.identifier();

        return identifier.getNamespace().equals(ElementalCraftApi.MODID) && (identifier.getPath().startsWith("pure_ore/") || identifier.getPath().startsWith("ore_purification/generated/"));
    }

    private <C extends RecipeInput, T extends Recipe<@NotNull C>> Stream<RecipeHolder<@NotNull T>> buildRecipes(@Nonnull RegistryAccess registry, @Nonnull IPureOreRecipeFactory<C, T> factory, @Nonnull List<LoadedPureOreSet> entries) {
        return entries.stream()
                .distinct()
                .<RecipeHolder<@NotNull T>>mapMulti((set, downstream) -> set.ores.values().forEach(v -> downstream.accept(this.buildRecipe(registry, factory, v))))
                .filter(Objects::nonNull);
    }

    private <C extends RecipeInput, T extends Recipe<@NotNull C>> RecipeHolder<@NotNull T> buildRecipe(@Nonnull RegistryAccess registry, @Nonnull IPureOreRecipeFactory<C, T> factory, @Nonnull LoadedPureOre entry) {
        var recipeType = factory.getRecipeType();
        var key = BuiltInRegistries.RECIPE_TYPE.getKey(factory.getRecipeType());

        if (key == null) {
            throw new PureOreException("Cannot build pure ore recipe as its RecipeType is absent in registry.");
        }

        try {
            var recipe = entry.getRecipe(recipeType);
            var id = entry.getId();

            return recipe != null ? new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, buildRecipeId(key, id)), factory.create(registry, recipe, DataComponentIngredient.of(true, PureOreManager.getInstance().createPureOre(id)))) : null;
        } catch (Exception e) {
            ElementalCraftApi.LOGGER.error("Error building pure ore recipe", e);
            return null;
        }
    }

    private static Identifier buildRecipeId(@Nonnull Identifier factoryId, @Nonnull Identifier sourceId) {
        return ElementalCraftApi.createRL("pure_ore/" + factoryId.getNamespace() + "/" + factoryId.getPath() + "/" + sourceId.getNamespace() + "/" + sourceId.getPath());
    }

    private static class LoadedPureOreSet {

        private final Map<Holder<@NotNull IPureOreLoader>, LoadedPureOre> ores;

        public LoadedPureOreSet() {
            ores = new Reference2ObjectArrayMap<>(ElementalCraft.PURE_ORE_LOADERS_MANAGER.getData().size());
        }

        public boolean isProcessable() {
            return !ores.isEmpty() && ores.values().stream().anyMatch(LoadedPureOre::isProcessable);
        }

        public List<RecipeHolder<@NotNull OrePurificationRecipe>> getOrePurificationRecipes(Identifier pureOreId) {
            var result = new ArrayList<RecipeHolder<@NotNull OrePurificationRecipe>>(ores.size());

            ores.forEach((holder, ore) -> {
                var recipe = ore.getOrePurificationRecipe();

                if (recipe != null) {
                    result.add(new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, buildOrePurificationRecipeId(holder.getKey(), pureOreId)), recipe));
                }
            });
            return result;
        }

        private static Identifier buildOrePurificationRecipeId(@Nullable ResourceKey<@NotNull IPureOreLoader> loaderKey, @Nonnull Identifier sourceId) {
            if (loaderKey == null) {
                ElementalCraftApi.LOGGER.warn("Unknown loader for pure ore {}.", sourceId);
                return ElementalCraftApi.createRL("ore_purification/generated/unknown_loader/" + sourceId.getNamespace() + "/" + sourceId.getPath());
            }

            var loaderId = loaderKey.identifier();

            return ElementalCraftApi.createRL("ore_purification/generated/" + loaderId.getNamespace() + "/" + loaderId.getPath() + "/" + sourceId.getNamespace() + "/" + sourceId.getPath());
        }

        public PureOre toPureOre() {
            var items = new HashSet<Holder<@NotNull Item>>(ores.size());
            var inputs = new ArrayList<Ingredient>(ores.size());
            var recipeDisplays = new ArrayList<RecipeDisplay>(ores.size());

            for (var ore : ores.values()) {
                items.addAll(ore.getOres());
                inputs.add(ore.getInput());
                recipeDisplays.addAll(ore.recipeDisplays());
            }

            return new PureOre(items, inputs, recipeDisplays.stream()
                    .map(RecipeDisplay::result)
                    .findFirst()
                    .orElse(SlotDisplay.Empty.INSTANCE));
        }
    }
}
