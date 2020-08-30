package sirttas.elementalcraft.item.pureore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.NBTIngredient;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.recipe.instrument.PurifierRecipe;

public class PureOreHelper {

	protected static final Map<Item, Entry> PURE_ORE_MAP = new HashMap<>();

	public static ItemStack getOre(ItemStack stack) {
		return NBTHelper.readItemStack(NBTHelper.getECTag(stack), ECNames.ORE);
	}

	public static boolean isValidOre(ItemStack ore) {
		return PURE_ORE_MAP.containsKey(ore.getItem());
	}

	public static ItemStack createPureOre(Item item) {
		ItemStack stack = new ItemStack(ECItems.pureOre);

		NBTHelper.writeItemStack(NBTHelper.getOrCreateECTag(stack), ECNames.ORE, new ItemStack(item));
		return stack;
	}

	public static int getColor(ItemStack stack) {
		return Optional.of(stack).map(PureOreHelper::getOre).map(i -> PURE_ORE_MAP.get(i.getItem())).map(o -> o.color).orElse(-1);
	}

	public static void generatePureOres(RecipeManager recipeManager) {
		Map<ResourceLocation, IRecipe<IInventory>> recipes = makeMutable(recipeManager.getRecipes(IRecipeType.SMELTING));

		for (Item ore : Tags.Items.ORES.getAllElements()) {
			recipes.values().stream().filter(r -> r.getIngredients().get(0).test(new ItemStack(ore))).filter(AbstractCookingRecipe.class::isInstance).map(AbstractCookingRecipe.class::cast).findAny()
					.ifPresent(r -> addOre(ore, r));
		}

		if (Boolean.TRUE.equals(ECConfig.CONFIG.pureOreSmeltingRecipeInjection.get())) {
			recipeManager.recipes = makeMutable(recipeManager.recipes);
			recipes.putAll(PURE_ORE_MAP.values().stream().distinct().map(PureOreHelper::buildRecipe).collect(Collectors.toMap(IRecipe::getId, o -> o)));
			recipeManager.recipes.put(IRecipeType.SMELTING, recipes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
		}
	}

	private static <K, V> Map<K, V> makeMutable(Map<K, V> map) {
		return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private static FurnaceRecipe buildRecipe(Entry entry) {
		return new FurnaceRecipe(new ResourceLocation(ElementalCraft.MODID, entry.smeltingRecipe.getId().getNamespace() + "_pure_" + entry.smeltingRecipe.getId().getPath()),
				entry.smeltingRecipe.getGroup(), new PureOreCompoundIngredient(entry.ingredients), entry.smeltingRecipe.getRecipeOutput().copy(), entry.smeltingRecipe.getExperience(),
				entry.smeltingRecipe.getCookTime());
	}

	private static void addOre(Item item, AbstractCookingRecipe recipe) {
		for (Entry entry : PURE_ORE_MAP.values()) {
			if (entry.smeltingRecipe.equals(recipe)) {
				PURE_ORE_MAP.put(item, entry);
				entry.ingredients.add(new PureOreIngredient(createPureOre(item)));
				return;
			}
		}
		PURE_ORE_MAP.put(item, new Entry(item, recipe));
	}

	protected static class Entry {

		Item ore;
		List<Ingredient> ingredients;
		ItemStack result;
		int color;
		AbstractCookingRecipe smeltingRecipe;

		public Entry(Item ore, AbstractCookingRecipe recipe) {
			this.ore = ore;
			ingredients = Lists.newArrayList(new PureOreIngredient(createPureOre(ore)));
			smeltingRecipe = recipe;
			result = recipe.getRecipeOutput().copy();
			color = ItemEC.lookupColor(result);
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

	public static List<PurifierRecipe> getRecipes() {
		return PURE_ORE_MAP.keySet().stream().map(k -> new PurifierRecipe(new ItemStack(k))).collect(Collectors.toList());
	}

}
