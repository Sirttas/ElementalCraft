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
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlockEntity;

public class PureInfusionRecipe implements IInventoryTileRecipe<PureInfuserBlockEntity> {

	public static final String NAME = "pureinfusion";
	public static final IRecipeType<PureInfusionRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new IRecipeType<PureInfusionRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});

	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final IRecipeSerializer<PureInfusionRecipe> SERIALIZER = null;

	private final NonNullList<Ingredient> ingredients;
	private final ItemStack output;
	private final int elementAmount;
	private final ResourceLocation id;

	public PureInfusionRecipe(ResourceLocation id, int elementAmount, ItemStack output, List<Ingredient> ingredients) {
		this.id = id;
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.stream().toArray(s -> new Ingredient[s]));
		this.output = output;
		this.elementAmount = elementAmount;
	}

	@Override
	public ItemStack getResultItem() {
		return output;
	}

	@Override
	public boolean matches(PureInfuserBlockEntity inv) {
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
	public ItemStack assemble(PureInfuserBlockEntity inv) {
		return this.getResultItem().copy();
	}

	@Override
	public void process(PureInfuserBlockEntity instrument) {
		instrument.getInventory().setItem(0, this.assemble(instrument));
		instrument.emptyPedestals();
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PureInfusionRecipe> {

		@Override
		public PureInfusionRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			int elementAmount = JSONUtils.getAsInt(json, ECNames.ELEMENT_AMOUNT);
			NonNullList<Ingredient> ingredients = RecipeHelper.readIngredients(JSONUtils.getAsJsonArray(json, ECNames.INGREDIENTS));
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);

			return new PureInfusionRecipe(recipeId, elementAmount, output, ingredients);
		}

		@Override
		public PureInfusionRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
			int elementAmount = buffer.readInt();
			ItemStack output = buffer.readItem();
			int i = buffer.readVarInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < ingredients.size(); ++j) {
				ingredients.set(j, Ingredient.fromNetwork(buffer));
			}

			return new PureInfusionRecipe(recipeId, elementAmount, output, ingredients);
		}

		@Override
		public void toNetwork(PacketBuffer buffer, PureInfusionRecipe recipe) {
			buffer.writeInt(recipe.elementAmount);
			buffer.writeItem(recipe.getResultItem());
			buffer.writeVarInt(recipe.getIngredients().size());

			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}
		}
	}
}
