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
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.RecipeHelper;
import sirttas.elementalcraft.rune.Rune;

public class InscriptionRecipe extends AbstractInstrumentRecipe<TileInscriber> {

	public static final String NAME = "inscription";
	public static final IRecipeType<InscriptionRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new IRecipeType<InscriptionRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static IRecipeSerializer<InscriptionRecipe> SERIALIZER;

	private final NonNullList<Ingredient> ingredients;
	private final ResourceLocation runeId;
	private final int elementAmount;
	private Rune output;

	public InscriptionRecipe(ResourceLocation id, ElementType type, int elementAmount, ResourceLocation runeId, List<Ingredient> ingredients) {
		super(id, type);
		this.ingredients = NonNullList.from(Ingredient.EMPTY, ingredients.stream().toArray(s -> new Ingredient[s]));
		this.runeId = runeId;
		this.elementAmount = elementAmount;
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
		if (output == null) {
			output = ElementalCraft.RUNE_MANAGER.get(runeId);
		}
		return output != null ? ECItems.rune.getRuneStack(output) : ItemStack.EMPTY;
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
		final IRecipeFactory factory;

		public Serializer(IRecipeFactory factory) {
			this.factory = factory;
		}

		@Override
		public InscriptionRecipe read(ResourceLocation recipeId, JsonObject json) {
			ElementType type = ElementType.byName(JSONUtils.getString(json, ECNames.ELEMENT_TYPE));
			int elementAmount = JSONUtils.getInt(json, ECNames.ELEMENT_AMOUNT);
			NonNullList<Ingredient> ingredients = RecipeHelper.readIngredients(JSONUtils.getJsonArray(json, ECNames.INGREDIENTS));
			ingredients.add(0, RecipeHelper.deserializeIngredient(json, "slate"));
			ResourceLocation runeId = new ResourceLocation(JSONUtils.getString(json, ECNames.OUTPUT));
			
			return this.factory.create(recipeId, type, elementAmount, runeId, ingredients);
		}


		@Override
		public InscriptionRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			ElementType type = ElementType.byName(buffer.readString(32767));
			int elementAmount = buffer.readInt();
			ResourceLocation runeId = buffer.readResourceLocation();
			int i = buffer.readInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < i; ++j) {
				ingredients.set(j, Ingredient.read(buffer));
			}

			return this.factory.create(recipeId, type, elementAmount, runeId, ingredients);
		}

		@Override
		public void write(PacketBuffer buffer, InscriptionRecipe recipe) {
			buffer.writeString(recipe.getElementType().getString());
			buffer.writeInt(recipe.getElementAmount());
			buffer.writeResourceLocation(recipe.runeId);
			buffer.writeInt(recipe.getIngredients().size());
			recipe.getIngredients().forEach(ingredient -> ingredient.write(buffer));
		}

		public interface IRecipeFactory {
			InscriptionRecipe create(ResourceLocation id, ElementType type, int elementAmount, ResourceLocation runeId, List<Ingredient> ingredients);
		}
	}
}
