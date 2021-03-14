package sirttas.elementalcraft.recipe;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.pureinfuser.TilePureInfuser;

public class PureInfusionRecipe implements IInventoryTileRecipe<TilePureInfuser> {

	public static final String NAME = "pureinfusion";
	public static final IRecipeType<PureInfusionRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new IRecipeType<PureInfusionRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});

	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static final IRecipeSerializer<PureInfusionRecipe> SERIALIZER = null;

	private NonNullList<Ingredient> ingredients;
	private ItemStack output;
	private int elementAmount;
	private ResourceLocation id;

	public PureInfusionRecipe(ResourceLocation id, int elementAmount, ItemStack output, List<Ingredient> ingredients) {
		this.id = id;
		this.ingredients = NonNullList.from(Ingredient.EMPTY, ingredients.stream().toArray(s -> new Ingredient[s]));
		this.output = output;
		this.elementAmount = elementAmount;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Override
	public boolean matches(TilePureInfuser inv) {
		return ingredients.get(0).test(inv.getItem()) 
				&& ingredients.get(1).test(inv.getStackInPedestal(ElementType.WATER)) 
				&& ingredients.get(2).test(inv.getStackInPedestal(ElementType.FIRE))
				&& ingredients.get(3).test(inv.getStackInPedestal(ElementType.EARTH)) 
				&& ingredients.get(4).test(inv.getStackInPedestal(ElementType.AIR));
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType() {
		return TYPE;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public ItemStack getCraftingResult(TilePureInfuser inv) {
		return this.getRecipeOutput().copy();
	}

	@Override
	public void process(TilePureInfuser instrument) {
		instrument.getInventory().setInventorySlotContents(0, this.getCraftingResult(instrument));
		instrument.emptyPedestals();
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PureInfusionRecipe> {

		@Override
		public PureInfusionRecipe read(ResourceLocation recipeId, JsonObject json) {
			int elementAmount = JSONUtils.getInt(json, ECNames.ELEMENT_AMOUNT);
			NonNullList<Ingredient> ingredients = RecipeHelper.readIngredients(JSONUtils.getJsonArray(json, ECNames.INGREDIENTS));
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);

			return new PureInfusionRecipe(recipeId, elementAmount, output, ingredients);
		}

		@Override
		public PureInfusionRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			int elementAmount = buffer.readInt();
			ItemStack output = buffer.readItemStack();
			int i = buffer.readVarInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < ingredients.size(); ++j) {
				ingredients.set(j, Ingredient.read(buffer));
			}

			return new PureInfusionRecipe(recipeId, elementAmount, output, ingredients);
		}

		@Override
		public void write(PacketBuffer buffer, PureInfusionRecipe recipe) {
			buffer.writeInt(recipe.elementAmount);
			buffer.writeItemStack(recipe.getRecipeOutput());
			buffer.writeVarInt(recipe.getIngredients().size());

			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.write(buffer);
			}
		}
	}
}
