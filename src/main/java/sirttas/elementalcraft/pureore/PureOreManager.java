package sirttas.elementalcraft.pureore;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import sirttas.dpanvil.api.event.DataPackReloadCompleteEvent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.color.ECColorHelper;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.pureore.loader.IPureOreLoader;
import sirttas.elementalcraft.recipe.instrument.io.IPurifierRecipe;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PureOreManager {

	private final Map<ResourceLocation, Entry> pureOres = new HashMap<>();

	private Language language;

	public boolean isValidOre(ItemStack ore) {
		return pureOres.values().stream().anyMatch(e -> e.test(ore));
	}
	
	public ResourceLocation getPureOreId(ItemStack stack) {
		var nbt = NBTHelper.getECTag(stack);

		if (nbt != null) {
			return new ResourceLocation(nbt.getString(ECNames.ORE));
		}
		return null;
	}

	public IPurifierRecipe getRecipes(ItemStack ore, @Nonnull Level level) {
		return pureOres.values().stream()
				.filter(e -> e.test(ore))
				.<IPurifierRecipe>mapMulti((e, downstream) -> e.getRecipes().forEach(downstream))
				.filter(r -> r.matches(ore, level))
				.findAny()
				.orElse(null);
	}

	public static Collection<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> getInjectors() {
		return AbstractPureOreRecipeInjector.REGISTRY.getValues();
	}

	public Component getPureOreName(ItemStack stack) {
		var id = getPureOreId(stack);

		if (language == null) {
			language = Language.getInstance();
		}

		if (id == null) {
			return null;
		} else if (language.has("tooltip.elementalcraft.pure_ore." + id.getNamespace() + "." + id.getPath())) {
			return Component.translatable("tooltip.elementalcraft.pure_ore." + id.getNamespace() + "." + id.getPath());
		}

		var entry = pureOres.get(id);
		
		return entry != null ? entry.getDescription() : null;
	}

	public ItemStack createPureOre(ResourceLocation id) {
		if (this.pureOres.containsKey(id)) {
			ItemStack stack = new ItemStack(ECItems.PURE_ORE.get());
	
			NBTHelper.getOrCreateECTag(stack).putString(ECNames.ORE, id.toString());
			return stack;
		}
		return ItemStack.EMPTY;
	}

	@OnlyIn(Dist.CLIENT)
	public int[] getColors(ItemStack stack) {
		var entry = pureOres.get(getPureOreId(stack));

		return entry != null ? entry.getColors() : null;
	}

	public List<ResourceLocation> getOres() {
		return new ArrayList<>(pureOres.keySet());
	}

	public List<IPurifierRecipe> getRecipes() {
		return pureOres.values().stream()
				.<IPurifierRecipe>mapMulti((e, downstream) -> e.getRecipes().forEach(downstream))
				.toList();
	}

	public void reload(DataPackReloadCompleteEvent event) { // TODO use OnDatapackSyncEvent
		var start = Instant.now();
		var recipeManager = event.getRecipeManager();
		var injectors = getInjectors();
		var registry = event.getRegistry();

		ElementalCraftApi.LOGGER.info("Pure ore generation started.\n\r\tRecipe Types: {}",
				() -> injectors.stream()
						.map(AbstractPureOreRecipeInjector::toString)
						.collect(Collectors.joining(", ")));
		injectors.forEach(injector -> injector.init(recipeManager));
		this.pureOres.clear();
		ElementalCraft.PURE_ORE_LOADERS_MANAGER.getData().entrySet().stream()
				.map(Map.Entry::getValue)
				.sorted(Comparator.comparingInt(IPureOreLoader::getOrder))
				.forEach(l -> l.generate(registry, injectors).forEach(e -> this.pureOres.computeIfAbsent(e.getId(), i -> new Entry()).ores.put(l, e)));

		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreRecipeInjection.get())) {
			ElementalCraftApi.LOGGER.info("Pure ore recipe injection.");
			this.pureOres.values().removeIf(o -> !o.isProcessable());

			var entries = pureOres.values().stream().distinct().toList();
			var recipes = recipeManager.getRecipes().stream()
					.filter(r -> !isPureOreRecipe(r))
					.collect(Collectors.toList());
			var size = recipes.size();

			injectors.forEach(injector -> inject(registry, injector, recipes, entries));
			recipeManager.replaceRecipes(recipes);
			ElementalCraftApi.LOGGER.info("Pure ore recipe injection finished. {} recipes added.", () -> recipeManager.getRecipes().size() - size);
		}

		ElementalCraftApi.LOGGER.info("Pure ore generation ended in {}ms\r\n\tOres: {}.",
				() -> Duration.between(start, Instant.now()).toMillis(),
				() -> pureOres.keySet().stream()
						.map(ResourceLocation::toString)
						.collect(Collectors.joining(", ")));
	}

	private boolean isPureOreRecipe(Recipe<?> recipe) {
		var id = recipe.getId();

		return id.getNamespace().equals(ElementalCraftApi.MODID) && id.getPath().startsWith("pure_ore/");
	}

	private <C extends Container, T extends Recipe<C>> void inject(@Nonnull RegistryAccess registry, @Nonnull AbstractPureOreRecipeInjector<C, T> injector, @Nonnull Collection<Recipe<?>> recipes, @Nonnull List<Entry> entries) {
		entries.stream()
				.distinct()
				.<T>mapMulti((entry, downstream) -> entry.ores.values().forEach(v -> downstream.accept(this.injectEntry(registry, injector, v))))
				.filter(Objects::nonNull)
				.filter(ElementalCraftUtils.distinctBy(Recipe::getId))
				.forEach(recipes::add);
	}

	private <C extends Container, T extends Recipe<C>> T injectEntry(@Nonnull RegistryAccess registry, @Nonnull AbstractPureOreRecipeInjector<C, T> injector, @Nonnull PureOre entry) {
		RecipeType<T> recipeType = injector.getRecipeType();
		try {
			T recipe = entry.getRecipe(recipeType);

			return recipe != null ? injector.build(registry, recipe, StrictNBTIngredient.of(createPureOre(entry.getId()))) : null;
		} catch (Exception e) {
			ElementalCraftApi.LOGGER.error("Error in pure ore recipe injection", e);
			return null;
		}
	}

	private static class Entry {

		private int[] colors = null;
		private final Map<IPureOreLoader, PureOre> ores;

		public Entry() {
			ores = new Reference2ObjectArrayMap<>(ElementalCraft.PURE_ORE_LOADERS_MANAGER.getData().size());
		}

		public Component getDescription() {
			if (ores.isEmpty()) {
				return null;
			}
			return ores.values().iterator().next().getDescription();
		}

		public boolean test(ItemStack ore) {
			return !ores.isEmpty() && ores.values().stream().anyMatch(o -> o.getIngredient().test(ore));
		}

		public boolean isProcessable() {
			return !ores.isEmpty() && ores.values().stream().anyMatch(PureOre::isProcessable);
		}

		public List<IPurifierRecipe> getRecipes() {
			return ores.values().stream()
					.map(PureOre::getRecipe)
					.toList();
		}

		@OnlyIn(Dist.CLIENT)
		private int[] getColors() {
			if (colors == null) {
				colors = ores.values().stream()
						.map(o -> ECColorHelper.lookupColors(o.getResultForColor()))
						.findFirst()
						.orElse(null);
			}
			return colors;
		}

	}
}
