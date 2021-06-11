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
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberBlockEntity;
import sirttas.elementalcraft.recipe.RecipeHelper;

public class InscriptionRecipe extends AbstractInstrumentRecipe<InscriberBlockEntity> {

	public static final String NAME = "inscription";
	public static final IRecipeType<InscriptionRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new IRecipeType<InscriptionRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});
	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final IRecipeSerializer<InscriptionRecipe> SERIALIZER = null;

	private final NonNullList<Ingredient> ingredients;
	private final int elementAmount;
	private final ItemStack output;

	public InscriptionRecipe(ResourceLocation id, ElementType type, int elementAmount, ItemStack output, List<Ingredient> ingredients) {
		super(id, type);
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.stream().toArray(s -> new Ingredient[s]));
		this.elementAmount = elementAmount;
		this.output = output;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public boolean matches(InscriberBlockEntity inv) {
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
	public ItemStack getResultItem() {
		return output;
	}

	@Override
	public IRecipeType<?> getType() {
		return TYPE;
	}

	@Override
	public void process(InscriberBlockEntity instrument) {
		instrument.clearContent();
		super.process(instrument);
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InscriptionRecipe> {

		@Override
		public InscriptionRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			ElementType type = ElementType.byName(JSONUtils.getAsString(json, ECNames.ELEMENT_TYPE));
			int elementAmount = JSONUtils.getAsInt(json, ECNames.ELEMENT_AMOUNT);
			NonNullList<Ingredient> ingredients = RecipeHelper.readIngredients(JSONUtils.getAsJsonArray(json, ECNames.INGREDIENTS));
			ingredients.add(0, RecipeHelper.deserializeIngredient(json, "slate"));
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);
			
			return new InscriptionRecipe(recipeId, type, elementAmount, output, ingredients);
		}


		@Override
		public InscriptionRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
			ElementType type = ElementType.byName(buffer.readUtf());
			int elementAmount = buffer.readInt();
			ItemStack output = buffer.readItem();
			int i = buffer.readInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < i; ++j) {
				ingredients.set(j, Ingredient.fromNetwork(buffer));
			}

			return new InscriptionRecipe(recipeId, type, elementAmount, output, ingredients);
		}

		@Override
		public void toNetwork(PacketBuffer buffer, InscriptionRecipe recipe) {
			buffer.writeUtf(recipe.getElementType().getSerializedName());
			buffer.writeInt(recipe.getElementAmount());
			buffer.writeItem(recipe.getResultItem());
			buffer.writeInt(recipe.getIngredients().size());
			recipe.getIngredients().forEach(ingredient -> ingredient.toNetwork(buffer));
		}
	}
}
