package sirttas.elementalcraft.pureore;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.common.util.Lazy;
import sirttas.dpanvil.api.event.DataPackReloadCompleteEvent;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.recipe.instrument.io.IPurifierRecipe;
import sirttas.elementalcraft.tag.ECTags;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PureOreManager {

	private static final Lazy<List<PureOreLoader>> LOADERS = Lazy.of(() -> List.of(PureOreLoader.create(ECTags.Items.PURE_ORES_ORE_SOURCE)
					.pattern("_?ore$")
					.tagFolder("ores")
					.inputSize(ECConfig.COMMON.pureOreOresInput.get())
					.outputSize(ECConfig.COMMON.pureOreOresOutput.get())
					.luckRatio(ECConfig.COMMON.pureOreOresLuckRatio.get()),
			PureOreLoader.create(ECTags.Items.PURE_ORES_RAW_MATERIALS_SOURCE)
					.pattern("^raw_?")
					.tagFolder("raw_materials")
					.inputSize(ECConfig.COMMON.pureOreRawMaterialsInput.get())
					.outputSize(ECConfig.COMMON.pureOreRawMaterialsOutput.get())
					.luckRatio(ECConfig.COMMON.pureOreRawMaterialsLuckRatio.get()),
			PureOreLoader.create(ECTags.Items.PURE_ORES_GEORE_SHARDS_SOURCE)
					.pattern("_?shard$")
					.tagFolder("geore_shards")
					.inputSize(ECConfig.COMMON.pureOreGeoreShardsInput.get())
					.outputSize(ECConfig.COMMON.pureOreGeoreShardsOutput.get())
					.luckRatio(ECConfig.COMMON.pureOreGeoreShardsLuckRatio.get())));

	private final Map<ResourceLocation, Entry> pureOres = new HashMap<>();

	public boolean isValidOre(ItemStack ore) {
		return pureOres.values().stream().anyMatch(e -> e.test(ore));
	}
	
	private ResourceLocation getPureOreId(ItemStack stack) {
		var nbt = NBTHelper.getECTag(stack);

		if (nbt != null) {
			return new ResourceLocation(nbt.getString(ECNames.ORE));
		}
		return null;
	}

	public IPurifierRecipe getRecipes(ItemStack ore) {
		return pureOres.values().stream()
				.filter(e -> e.test(ore))
				.<IPurifierRecipe>mapMulti((e, downstream) -> e.getRecipes().forEach(downstream))
				.filter(r -> r.matches(ore))
				.findAny()
				.orElse(null);
	}

	public static Collection<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> getInjectors() {
		return AbstractPureOreRecipeInjector.REGISTRY.getValues();
	}

	public Component getPureOreName(ItemStack stack) {
		var entry = pureOres.get(getPureOreId(stack));
		
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
	public int getColor(ItemStack stack) {
		var entry = pureOres.get(getPureOreId(stack));

		return entry != null ? entry.getColor() : -1;
	}

	public List<ResourceLocation> getOres() {
		return new ArrayList<>(pureOres.keySet());
	}

	public List<IPurifierRecipe> getRecipes() {
		return pureOres.values().stream()
				.<IPurifierRecipe>mapMulti((e, downstream) -> e.getRecipes().forEach(downstream))
				.toList();
	}

	public void reload(DataPackReloadCompleteEvent event) {
		var start = Instant.now();
		var recipeManager = event.getRecipeManager();
		var injectors = getInjectors();

		ElementalCraftApi.LOGGER.info("Pure ore generation started.\n\r\tRecipe Types: {}",
				() -> injectors.stream()
						.map(AbstractPureOreRecipeInjector::toString)
						.collect(Collectors.joining(", ")));
		injectors.forEach(injector -> injector.init(recipeManager));
		this.pureOres.clear();
		LOADERS.get().forEach(l -> l.generate(injectors).forEach(e -> this.pureOres.computeIfAbsent(e.getId(), i -> new Entry()).ores.put(l, e)));

		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreRecipeInjection.get())) {
			ElementalCraftApi.LOGGER.info("Pure ore recipe injection.");
			this.pureOres.values().removeIf(o -> !o.isProcessable());

			var entries = pureOres.values().stream().distinct().toList();
			var recipes = recipeManager.getRecipes().stream()
					.filter(r -> !isPureOreRecipe(r))
					.collect(Collectors.toList());
			var size = recipes.size();

			injectors.forEach(injector -> inject(injector, recipes, entries));
			recipeManager.replaceRecipes(recipes);
			ElementalCraftApi.LOGGER.info("Pure ore recipe injection finished. {} recipes added.", () -> recipeManager.getRecipes().size() - size);
		}

		ElementalCraftApi.LOGGER.info("Pure ore generation ended\r\n\tOres: {} in {}.", () -> pureOres.keySet().stream()
				.map(ResourceLocation::toString)
				.collect(Collectors.joining(", ")),
				() -> Duration.between(start, Instant.now()));
	}

	private boolean isPureOreRecipe(Recipe<?> recipe) {
		var id = recipe.getId();

		return id.getNamespace().equals(ElementalCraftApi.MODID) && id.getPath().startsWith("pure_ore/");
	}

	private <C extends Container, T extends Recipe<C>> void inject(AbstractPureOreRecipeInjector<C, T> injector, Collection<Recipe<?>> recipes, List<Entry> entries) {
		entries.stream()
				.distinct()
				.<T>mapMulti((entry, downstream) -> entry.ores.values().forEach(v -> downstream.accept(this.injectEntry(injector, v))))
				.filter(Objects::nonNull)
				.filter(ElementalCraftUtils.distinctBy(Recipe::getId))
				.forEach(recipes::add);
	}

	private <C extends Container, T extends Recipe<C>> T injectEntry(AbstractPureOreRecipeInjector<C, T> injector, PureOre entry) {
		RecipeType<T> recipeType = injector.getRecipeType();
		try {
			T recipe = entry.getRecipe(recipeType);

			return recipe != null ? injector.build(recipe, StrictNBTIngredient.of(createPureOre(entry.getId()))) : null;
		} catch (Exception e) {
			ElementalCraftApi.LOGGER.error("Error in pure ore recipe injection", e);
			return null;
		}
	}

	private static class Entry {

		private int color = -1;
		private final Map<PureOreLoader, PureOre> ores;

		public Entry() {
			ores = new Reference2ObjectArrayMap<>(LOADERS.get().size());
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
		private int getColor() {
			if (color == -1) {
				color = ores.values().stream()
						.map(o -> ECItem.lookupColor(o.getResultForColor()))
						.findFirst()
						.orElse(-1);
			}
			return color;
		}

	}
}
