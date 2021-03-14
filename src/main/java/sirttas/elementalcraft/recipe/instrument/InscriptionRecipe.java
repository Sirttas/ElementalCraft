package sirttas.elementalcraft.recipe.instrument;

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
import sirttas.elementalcraft.block.instrument.inscriber.TileInscriber;
import sirttas.elementalcraft.recipe.RecipeHelper;

public class InscriptionRecipe extends AbstractInstrumentRecipe<TileInscriber> {

	public static final String NAME = "inscription";
	public static final IRecipeType<InscriptionRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new IRecipeType<InscriptionRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static final IRecipeSerializer<InscriptionRecipe> SERIALIZER = null;

	private final NonNullList<Ingredient> ingredients;
	private final int elementAmount;
	private final ItemStack output;

	public InscriptionRecipe(ResourceLocation id, ElementType type, int elementAmount, ItemStack output, List<Ingredient> ingredients) {
		super(id, type);
		this.ingredients = NonNullList.from(Ingredient.EMPTY, ingredients.stream().toArray(s -> new Ingredient[s]));
		this.elementAmount = elementAmount;
		this.output = output;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public boolean matches(TileInscriber inv) {
		if (inv.getTankElementType() == getElementType()) {
			return RecipeHelper.matchesUnordered(inv.getInventory(), ingredients);
		}
		return false;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return output.copy();
	}

	@Override
	public IRecipeType<?> getType() {
		return TYPE;
	}

	@Override
	public void process(TileInscriber instrument) {
		instrument.clear();
		super.process(instrument);
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InscriptionRecipe> {

		@Override
		public InscriptionRecipe read(ResourceLocation recipeId, JsonObject json) {
			ElementType type = ElementType.byName(JSONUtils.getString(json, ECNames.ELEMENT_TYPE));
			int elementAmount = JSONUtils.getInt(json, ECNames.ELEMENT_AMOUNT);
			NonNullList<Ingredient> ingredients = RecipeHelper.readIngredients(JSONUtils.getJsonArray(json, ECNames.INGREDIENTS));
			ingredients.add(0, RecipeHelper.deserializeIngredient(json, "slate"));
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);
			
			return new InscriptionRecipe(recipeId, type, elementAmount, output, ingredients);
		}


		@Override
		public InscriptionRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			ElementType type = ElementType.byName(buffer.readString(32767));
			int elementAmount = buffer.readInt();
			ItemStack output = buffer.readItemStack();
			int i = buffer.readInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < i; ++j) {
				ingredients.set(j, Ingredient.read(buffer));
			}

			return new InscriptionRecipe(recipeId, type, elementAmount, output, ingredients);
		}

		@Override
		public void write(PacketBuffer buffer, InscriptionRecipe recipe) {
			buffer.writeString(recipe.getElementType().getString());
			buffer.writeInt(recipe.getElementAmount());
			buffer.writeItemStack(recipe.getRecipeOutput());
			buffer.writeInt(recipe.getIngredients().size());
			recipe.getIngredients().forEach(ingredient -> ingredient.write(buffer));
		}
	}
}
