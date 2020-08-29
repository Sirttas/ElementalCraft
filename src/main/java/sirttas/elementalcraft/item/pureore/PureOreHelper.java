package sirttas.elementalcraft.item.pureore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.common.Tags;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.nbt.ECNBTTags;
import sirttas.elementalcraft.nbt.NBTHelper;

public class PureOreHelper {

	protected static final Map<Item, PureOre> PURE_ORE_MAP = new HashMap<>();

	public static ItemStack getOre(ItemStack stack) {
		return NBTHelper.readItemStack(NBTHelper.getECTag(stack), ECNBTTags.ORE);
	}

	public static void setOre(ItemStack stack, ItemStack ore) {
		NBTHelper.writeItemStack(NBTHelper.getECTag(stack), ECNBTTags.ORE, ore);
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
		Collection<IRecipe<IInventory>> recipes = recipeManager.getRecipes(IRecipeType.SMELTING).values();

		for (Item ore : Tags.Items.ORES.getAllElements()) {
			recipes.stream().filter(r -> r.getIngredients().get(0).test(new ItemStack(ore))).findAny().ifPresent(r -> addOre(ore, r));
		}
	}

	private static void addOre(Item item, IRecipe<IInventory> recipe) {
		PureOre ore = PureOre.build(item, recipe);

		PURE_ORE_MAP.put(item, ore);
	}

	protected static class PureOre {
		Item ore;
		ItemStack result;
		int color;
		IRecipe<IInventory> smeltingRecipe;
		ItemStack pureOre;

		public static PureOre build(Item ore, IRecipe<IInventory> recipe) {
			PureOre pureOre = new PureOre();

			pureOre.ore = ore;
			pureOre.smeltingRecipe = recipe;
			pureOre.result = recipe.getRecipeOutput().copy();
			pureOre.color = ItemEC.lookupColor(pureOre.result);
			pureOre.pureOre = createPureOre(ore);
			return pureOre;
		}
	}
}
