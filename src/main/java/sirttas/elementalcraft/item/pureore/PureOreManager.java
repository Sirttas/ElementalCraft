package sirttas.elementalcraft.item.pureore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.BlastingRecipe;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.fml.DistExecutor;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.ECInventoryHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.recipe.instrument.PurifierRecipe;

public class PureOreManager {

	private final Map<Item, Entry> pureOres = new HashMap<>();

	private List<PureOreRecipeInjector<?, ? extends IRecipe<?>>> injectors = Lists.newArrayList();
	
	public PureOreManager() {
		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreSmeltingRecipe.get())) {
			addInjector(PureOreRecipeInjector.create(IRecipeType.SMELTING, this::buildSmeltingRecipe));
		}
		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreBlastingRecipe.get())) {
			addInjector(PureOreRecipeInjector.create(IRecipeType.BLASTING, this::buildBlastingRecipe));
		}
		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreCampFireRecipe.get())) {
			addInjector(PureOreRecipeInjector.create(IRecipeType.CAMPFIRE_COOKING, this::buildCampFireRecipe));
		}
	}

	public ItemStack getOre(ItemStack stack) {
		return NBTHelper.readItemStack(NBTHelper.getECTag(stack), ECNames.ORE);
	}

	public boolean isValidOre(ItemStack ore) {
		return pureOres.containsKey(ore.getItem());
	}

	public ItemStack createPureOre(Item item) {
		ItemStack stack = new ItemStack(ECItems.pureOre);

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

	public void addInjector(PureOreRecipeInjector<?, ? extends IRecipe<?>> injector) {
		injectors.add(injector);
	}

	public void generatePureOres(RecipeManager recipeManager) {
		ElementalCraft.LOGGER.info("Pure ore generation started.\r\n\tRecipe Types: {}\r\n\tOres found: {}",
				() -> injectors.stream().map(PureOreRecipeInjector::toString).collect(Collectors.joining(", ")),
				() -> Tags.Items.ORES.getAllElements().stream().map(o -> o.getRegistryName().toString()).collect(Collectors.joining(", ")));
		injectors.forEach(injector -> injector.init(recipeManager));

		for (Item ore : Tags.Items.ORES.getAllElements()) {
			Entry entry = new Entry(ore);

			injectors.forEach(injector -> injector.getRecipe(ore).ifPresent(entry::addRecipe));
			addEntry(entry);
		}

		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreRecipeInjection.get())) {
			ElementalCraft.LOGGER.info("Pure ore recipe injection");

			List<Entry> entries = pureOres.values().stream().distinct().collect(Collectors.toList());

			recipeManager.recipes = recipeManager.recipes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			injectors.forEach(injector -> injector.inject(entries));
			recipeManager.recipes = recipeManager.recipes.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
		}
		ElementalCraft.LOGGER.info("Pure ore generation ended");
	}

	private void addEntry(Entry entry) {
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

	public String buildRecipeId(ResourceLocation source) {
		return source.getNamespace() + "_pure_" + source.getPath().replace('/', '_');
	}

	private FurnaceRecipe buildSmeltingRecipe(FurnaceRecipe sourceRecipe, Ingredient ingredient) {
		return new FurnaceRecipe(ElementalCraft.createRL(buildRecipeId(sourceRecipe.getId())), sourceRecipe.getGroup(), ingredient, sourceRecipe.getRecipeOutput(), sourceRecipe.getExperience(),
				sourceRecipe.getCookTime());
	}

	private BlastingRecipe buildBlastingRecipe(BlastingRecipe sourceRecipe, Ingredient ingredient) {
		return new BlastingRecipe(ElementalCraft.createRL(buildRecipeId(sourceRecipe.getId())), sourceRecipe.getGroup(), ingredient, sourceRecipe.getRecipeOutput(), sourceRecipe.getExperience(),
				sourceRecipe.getCookTime());
	}

	private CampfireCookingRecipe buildCampFireRecipe(CampfireCookingRecipe sourceRecipe, Ingredient ingredient) {
		return new CampfireCookingRecipe(ElementalCraft.createRL(buildRecipeId(sourceRecipe.getId())), sourceRecipe.getGroup(), ingredient, sourceRecipe.getRecipeOutput(),
				sourceRecipe.getExperience(), sourceRecipe.getCookTime());
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

		public <C extends IInventory, T extends IRecipe<C>> void addRecipe(T recipe) {
			recipes.put(recipe.getType(), recipe);
			if (result.isEmpty()) {
				result = recipe.getRecipeOutput();
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> color = ItemEC.lookupColor(result));
			}
		}

		public Ingredient getIngredient() {
			return new PureOreCompoundIngredient(ingredients);
		}

		private boolean match(Entry other) {
			if (ECInventoryHelper.stackEqualCount(other.result, result)) {
				return true;
			}
			for (IRecipe<?> recipe : other.recipes.values()) {
				if (recipes.containsValue(recipe)) {
					return true;
				}
			}
			return false;
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
