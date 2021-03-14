package sirttas.elementalcraft.item.pureore;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.fml.DistExecutor;
import sirttas.dpanvil.api.event.DataPackReloadCompletEvent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.ECInventoryHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.recipe.instrument.io.PurifierRecipe;
import sirttas.elementalcraft.tag.ECTags;

public class PureOreManager {

	private final Map<Item, Entry> pureOres = new HashMap<>();
	
	public ItemStack getOre(ItemStack stack) {
		return NBTHelper.readItemStack(NBTHelper.getECTag(stack), ECNames.ORE);
	}

	public boolean isValidOre(ItemStack ore) {
		return pureOres.containsKey(ore.getItem());
	}

	public ItemStack createPureOre(Item item) {
		ItemStack stack = new ItemStack(ECItems.PURE_ORE);

		NBTHelper.writeItemStack(NBTHelper.getOrCreateECTag(stack), ECNames.ORE, new ItemStack(pureOres.containsKey(item) ? pureOres.get(item).ore : item));
		return stack;
	}

	public int getColor(ItemStack stack) {
		return Optional.of(stack).map(this::getOre).map(i -> pureOres.get(i.getItem())).map(o -> o.color).orElse(-1);
	}

	public List<Item> getOres() {
		return pureOres.values().stream().distinct().map(o -> o.ore).distinct().collect(Collectors.toList());
	}

	public List<PurifierRecipe> getRecipes() {
		return pureOres.entrySet().stream().collect(Collectors.groupingBy(e -> e.getValue().ore, Collectors.mapping(e -> new ItemStack(e.getKey()), Collectors.toList()))).values()
				.stream().filter(e -> !e.isEmpty()).map(JEIPurifierRecipe::new).collect(Collectors.toList());
	}

	private Collection<AbstractPureOreRecipeInjector<?, ? extends IRecipe<?>>> getInjectors() {
		return AbstractPureOreRecipeInjector.REGISTRY.getValues();
	}

	private void generatePureOres(RecipeManager recipeManager) {
		Collection<AbstractPureOreRecipeInjector<?, ? extends IRecipe<?>>> injectors = getInjectors();

		ElementalCraft.LOGGER.info("Pure ore generation started.\r\n\tRecipe Types: {}\r\n\tOres found: {}",
				() -> injectors.stream().map(AbstractPureOreRecipeInjector::toString).collect(Collectors.joining(", ")),
				() -> ECTags.Items.PURE_ORES.getAllElements().stream().map(o -> o.getRegistryName().toString()).collect(Collectors.joining(", ")));
		injectors.forEach(injector -> injector.init(recipeManager));

		for (Item ore : ECTags.Items.PURE_ORES.getAllElements()) {
			Entry entry = new Entry(ore);
			boolean isInBlacklist = ECTags.Items.PURE_ORES_MOD_PROCESSING_BLACKLIST.contains(ore);

			injectors.forEach(injector -> {
				if (!injector.isModProcessing() || !isInBlacklist) {
					injector.getRecipe(ore).ifPresent(entry::addRecipe);
				}
			});
			addEntry(entry);
		}

		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreRecipeInjection.get())) {
			ElementalCraft.LOGGER.info("Pure ore recipe injection");

			List<Entry> entries = pureOres.values().stream().distinct().collect(Collectors.toList());

			recipeManager.recipes = recipeManager.recipes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			injectors.forEach(injector -> inject(injector, entries));
			recipeManager.recipes = recipeManager.recipes.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
		}
		ElementalCraft.LOGGER.info("Pure ore generation ended");
	}

	private <C extends IInventory, T extends IRecipe<C>> void inject(AbstractPureOreRecipeInjector<C, T> injector, List<PureOreManager.Entry> entries) {
		Map<ResourceLocation, T> map = injector.getRecipes();

		map.putAll(entries.stream().distinct().map(entry -> this.injectEntry(injector, entry)).filter(Objects::nonNull).collect(Collectors.toMap(IRecipe::getId, o -> o, (recipe1, recipe2) -> {
			ElementalCraft.LOGGER.warn("Duplicated key for type {}: {}", injector.getRecipeType(), recipe1.getId());
			return recipe1;
		})));
		injector.inject(map);
	}

	public <C extends IInventory, T extends IRecipe<C>> T injectEntry(AbstractPureOreRecipeInjector<C, T> injector, Entry entry) {
		IRecipeType<T> recipeType = injector.getRecipeType();
		try {
			T recipe = entry.getRecipe(recipeType);

			return recipe != null ? injector.build(recipe, entry.getIngredient()) : null;
		} catch (Exception e) {
			ElementalCraft.LOGGER.error("Error in pure ore recipe injection", e);
			return null;
		}
	}

	private void addEntry(Entry entry) {
		if (entry.isValid()) {
			for (Entry other : pureOres.values()) {
				if (entry.match(other)) {
					pureOres.put(entry.ore, other);
					entry.recipes.forEach((type, recipe) -> {
						if (!other.recipes.containsKey(type)) {
							other.addRecipe(recipe);
						}
					});
					other.ingredients.add(new PureOreIngredient(createPureOre(entry.ore)));
					return;
				}
			}
			pureOres.put(entry.ore, entry);
		}
	}

	public void reload(DataPackReloadCompletEvent event) {
		this.generatePureOres(event.getRecipeManager());
	}

	protected class Entry {

		Item ore;
		List<Ingredient> ingredients;
		ItemStack result;
		int color;
		Map<IRecipeType<?>, IRecipe<?>> recipes = new HashMap<>();

		public Entry(Item ore) {
			this.ore = ore;
			ingredients = Lists.newArrayList(new PureOreIngredient(createPureOre(ore)));
			result = ItemStack.EMPTY;
		}


		@SuppressWarnings("unchecked")
		public <C extends IInventory, T extends IRecipe<C>> T getRecipe(IRecipeType<T> recipeType) {
			return (T) recipes.get(recipeType);
		}

		@SuppressWarnings("unchecked")
		public <C extends IInventory, T extends IRecipe<C>> void addRecipe(T recipe) {
			IRecipeType<?> recipeType = recipe.getType();

			recipes.put(recipe.getType(), recipe);
			if (result.isEmpty()) {
				result = getInjectors().stream().filter(injector -> injector.getRecipeType().equals(recipeType)).findAny()
						.map(injector -> ((AbstractPureOreRecipeInjector<C, T>) injector).getRecipeOutput(recipe)).filter(stack -> !stack.isEmpty()).orElse(recipe.getRecipeOutput());
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> color = ItemEC.lookupColor(result));
			}
		}

		public Ingredient getIngredient() {
			return new PureOreCompoundIngredient(ingredients);
		}

		public boolean match(Entry other) {
			if (other.isValid() && isValid()) {
				if (!other.result.isEmpty() && !result.isEmpty() && ECInventoryHelper.stackEqualCount(other.result, result)) {
					return true;
				}
				for (IRecipe<?> recipe : other.recipes.values()) {
					if (recipes.containsValue(recipe)) {
						return true;
					}
				}
			}
			return false;
		}

		public boolean isValid() {
			return !recipes.isEmpty();
		}
	}

	private static class PureOreIngredient extends NBTIngredient {

		public PureOreIngredient(ItemStack stack) {
			super(stack);
		}
	}

	private static class PureOreCompoundIngredient extends CompoundIngredient {

		protected PureOreCompoundIngredient(List<Ingredient> children) {
			super(children);
		}

	}

	private static class JEIPurifierRecipe extends PurifierRecipe {

		public JEIPurifierRecipe(List<ItemStack> ores) {
			super(ores.get(0));
			input = Ingredient.fromStacks(ores.stream().toArray(ItemStack[]::new));
		}
	}

}
