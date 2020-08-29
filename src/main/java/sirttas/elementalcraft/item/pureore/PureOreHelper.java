package sirttas.elementalcraft.item.pureore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.NBTIngredient;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.nbt.ECNBTTags;
import sirttas.elementalcraft.nbt.NBTHelper;

public class PureOreHelper {

	protected static final Map<Item, PureOreEntry> PURE_ORE_MAP = new HashMap<>();

	public static ItemStack getOre(ItemStack stack) {
		return NBTHelper.readItemStack(NBTHelper.getECTag(stack), ECNBTTags.ORE);
	}

	public static void setOre(ItemStack stack, ItemStack ore) {
		NBTHelper.writeItemStack(NBTHelper.getECTag(stack), ECNBTTags.ORE, ore);
	}

	public static boolean isValidOre(ItemStack ore) {
		return PURE_ORE_MAP.containsKey(ore.getItem());
	}

	public static ItemStack createPureOre(Item item) {
		ItemStack stack = new ItemStack(ECItems.pureOre);

		setOre(stack, new ItemStack(item));
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
		recipeManager.recipes = makeMutable(recipeManager.recipes);
		recipes.putAll(PURE_ORE_MAP.values().stream().map(PureOreHelper::buildRecipe).collect(Collectors.toMap(IRecipe::getId, o -> o)));
		recipeManager.recipes.put(IRecipeType.SMELTING, recipes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
	}

	private static <K, V> Map<K, V> makeMutable(Map<K, V> map) {
		return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private static FurnaceRecipe buildRecipe(PureOreEntry entry) {
		return new FurnaceRecipe(new ResourceLocation(ElementalCraft.MODID, entry.smeltingRecipe.getId().getNamespace() + "_pure_" + entry.smeltingRecipe.getId().getPath()),
				entry.smeltingRecipe.getGroup(), new PureOreIngredient(createPureOre(entry.ore)), entry.smeltingRecipe.getRecipeOutput().copy(), entry.smeltingRecipe.getExperience(),
				entry.smeltingRecipe.getCookTime());
	}

	private static void addOre(Item item, AbstractCookingRecipe recipe) {
		PureOreEntry entry = PureOreEntry.build(item, recipe);

		PURE_ORE_MAP.put(item, entry);
	}

	protected static class PureOreEntry {

		Item ore;
		ItemStack result;
		int color;
		AbstractCookingRecipe smeltingRecipe;
		ItemStack pureOre;

		public static PureOreEntry build(Item ore, AbstractCookingRecipe recipe) {
			PureOreEntry entry = new PureOreEntry();

			entry.ore = ore;
			entry.smeltingRecipe = recipe;
			entry.result = recipe.getRecipeOutput().copy();
			entry.color = ItemEC.lookupColor(entry.result);
			entry.pureOre = createPureOre(ore);
			return entry;
		}
	}

	private static class PureOreIngredient extends NBTIngredient {

		public PureOreIngredient(ItemStack stack) {
			super(stack);
		}

	}

}
