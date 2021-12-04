package sirttas.elementalcraft.item.pureore;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.fml.DistExecutor;
import sirttas.dpanvil.api.event.DataPackReloadCompletEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.recipe.instrument.io.PurifierRecipe;
import sirttas.elementalcraft.tag.ECTags;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PureOreManager {

	private static final Pattern ORE_PATTERN = Pattern.compile("_?ore$");
	
	private final Map<ResourceLocation, Entry> pureOres = new HashMap<>();

	public boolean isValidOre(ItemStack ore) {
		return pureOres.values().stream().anyMatch(e -> e.getIngredient().test(ore));
	}
	
	public ItemStack createPureOreFor(ItemStack ore) {
		return pureOres.values().stream()
				.filter(e -> e.getIngredient().test(ore))
				.map(e -> this.createPureOre(e.id))
				.findAny()
				.orElse(ItemStack.EMPTY);
	}
	
	private ResourceLocation getPureOreId(ItemStack stack) {
		var nbt = NBTHelper.getECTag(stack);

		if (nbt != null) {
			return new ResourceLocation(nbt.getString(ECNames.ORE));
		}
		return null;
	}
	
	Component getPureOreName(ItemStack stack) {
		var entry = pureOres.get(getPureOreId(stack));
		
		return entry != null ? entry.translationKey : null;
	}
	
	ItemStack createPureOre(ResourceLocation id) {
		if (this.pureOres.containsKey(id)) {
			ItemStack stack = new ItemStack(ECItems.PURE_ORE);
	
			NBTHelper.getOrCreateECTag(stack).putString(ECNames.ORE, id.toString());
			return stack;
		}
		return ItemStack.EMPTY;
	}

	public int getColor(ItemStack stack) {
		var entry = pureOres.get(getPureOreId(stack));
		
		return entry != null ? entry.color : -1;
	}

	public List<ResourceLocation> getOres() {
		return pureOres.keySet().stream().collect(Collectors.toList());
	}

	public List<PurifierRecipe> getRecipes() {
		return pureOres.values().stream()
				.map(e -> e.ores.stream().map(ItemStack::new).collect(Collectors.toList()))
				.filter(e -> !e.isEmpty())
				.map(JEIPurifierRecipe::new)
				.collect(Collectors.toList());
	}

	private Collection<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> getInjectors() {
		return AbstractPureOreRecipeInjector.REGISTRY.getValues();
	}

	private void generatePureOres(RecipeManager recipeManager) {
		Collection<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> injectors = getInjectors();

		ElementalCraftApi.LOGGER.info("Pure ore generation started.\r\n\tRecipe Types: {}\r\n\tOres found: {}",
				() -> injectors.stream().map(AbstractPureOreRecipeInjector::toString).collect(Collectors.joining(", ")),
				() -> ECTags.Items.PURE_ORES_ORE_SOURCE.getValues().stream().map(o -> o.getRegistryName().toString()).collect(Collectors.joining(", ")));
		injectors.forEach(injector -> injector.init(recipeManager));

		for (Item ore : ECTags.Items.PURE_ORES_ORE_SOURCE.getValues()) {
			Entry entry = findOrCreateEntry(ore);
			boolean isInBlacklist = ECTags.Items.PURE_ORES_MOD_PROCESSING_BLACKLIST.contains(ore);

			injectors.forEach(injector -> {
				if (!injector.isModProcessing() || !isInBlacklist) {
					injector.getRecipe(ore).ifPresent(entry::addRecipe);
				}
			});
		}

		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreRecipeInjection.get())) {
			ElementalCraftApi.LOGGER.info("Pure ore recipe injection");

			List<Entry> entries = pureOres.values().stream().distinct().collect(Collectors.toList());

			recipeManager.recipes = recipeManager.recipes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			injectors.forEach(injector -> inject(injector, entries));
			recipeManager.recipes = recipeManager.recipes.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
		}
		ElementalCraftApi.LOGGER.info("Pure ore generation ended");
	}

	private <C extends Container, T extends Recipe<C>> void inject(AbstractPureOreRecipeInjector<C, T> injector, List<PureOreManager.Entry> entries) {
		Map<ResourceLocation, T> map = injector.getRecipes();

		map.putAll(entries.stream().distinct().map(entry -> this.injectEntry(injector, entry)).filter(Objects::nonNull).collect(Collectors.toMap(Recipe::getId, o -> o, (recipe1, recipe2) -> {
			ElementalCraftApi.LOGGER.warn("Duplicated key for type {}: {}", injector.getRecipeType(), recipe1.getId());
			return recipe1;
		})));
		injector.inject(map);
	}

	public <C extends Container, T extends Recipe<C>> T injectEntry(AbstractPureOreRecipeInjector<C, T> injector, Entry entry) {
		RecipeType<T> recipeType = injector.getRecipeType();
		try {
			T recipe = entry.getRecipe(recipeType);

			return recipe != null ? injector.build(recipe, new PureOreIngredient(createPureOre(entry.id))) : null;
		} catch (Exception e) {
			ElementalCraftApi.LOGGER.error("Error in pure ore recipe injection", e);
			return null;
		}
	}

	private Entry findOrCreateEntry(Item ore) {
		var id = ore.getRegistryName();
		var matcher = ORE_PATTERN.matcher(id.getPath());
		Tag<Item> tag = null;
		
		if (matcher.matches()) {
			var path = matcher.replaceAll("");
			var namespace = id.getNamespace();
			var tagName = new ResourceLocation("forge", "ores/" + path);
			
			try {
				tag = ECTags.Items.getTag(tagName);
				if (tag != null) {
					namespace = "forge";
				}
			} catch (Exception e) {
				ElementalCraftApi.LOGGER.trace("Tag Not found {}: {}", tagName, e.getMessage());
			}
			id = new ResourceLocation(namespace, path);
		}
		
		var entry = pureOres.computeIfAbsent(id, Entry::new);

		if (entry.translationKey == null) {
			entry.translationKey = ore.getDescription();
		}
		entry.ores.add(ore);
		if (tag != null) {
			entry.ores.addAll(tag.getValues());
		}
		return entry;
	}

	public void reload(DataPackReloadCompletEvent event) {
		this.generatePureOres(event.getRecipeManager());
	}
	
	protected class Entry {
		
		final Set<Item> ores;
		final Map<RecipeType<?>, Recipe<?>> recipes;
		final ResourceLocation id;
		
		int color;
		Component translationKey;

		public Entry(ResourceLocation id) {
			this.ores = new HashSet<>();
			recipes = new HashMap<>();
			this.id = id;
			this.color = -1;
		}

		@SuppressWarnings("unchecked")
		public <C extends Container, T extends Recipe<C>> T getRecipe(RecipeType<T> recipeType) {
			return (T) recipes.get(recipeType);
		}

		@SuppressWarnings("unchecked")
		public <C extends Container, T extends Recipe<C>> void addRecipe(T recipe) {
			RecipeType<?> recipeType = recipe.getType();

			recipes.put(recipe.getType(), recipe);
			if (color == -1) {
				var result = getInjectors().stream()
						.filter(injector -> injector.getRecipeType().equals(recipeType))
						.map(injector -> ((AbstractPureOreRecipeInjector<C, T>) injector).getRecipeOutput(recipe))
						.filter(stack -> !stack.isEmpty())
						.findAny()
						.orElse(recipe.getResultItem());
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> color = ECItem.lookupColor(result));
			}
		}

		public boolean isValid() {
			return !recipes.isEmpty();
		}
		
		public Ingredient getIngredient() {
			return Ingredient.of(ores.stream().map(ItemStack::new));
		}
	}

	private static class PureOreIngredient extends NBTIngredient {

		public PureOreIngredient(ItemStack stack) {
			super(stack);
		}
	}

	private static class JEIPurifierRecipe extends PurifierRecipe {

		public JEIPurifierRecipe(List<ItemStack> ores) {
			super(ores.get(0));
			input = Ingredient.of(ores.stream().toArray(ItemStack[]::new));
		}
	}

}
