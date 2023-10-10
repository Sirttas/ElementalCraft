package sirttas.elementalcraft.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlockEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class PureInfusionRecipe implements IContainerBlockEntityRecipe<PureInfuserBlockEntity> {

	public static final String NAME = "pureinfusion";

	private final NonNullList<Ingredient> ingredients;
	private final ItemStack output;
	private final int elementAmount;
	private final ResourceLocation id;

	public PureInfusionRecipe(ResourceLocation id, int elementAmount, ItemStack output, List<Ingredient> ingredients) {
		this.id = id;
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new));
		this.output = output;
		this.elementAmount = elementAmount;
	}

	@Nonnull
	@Override
	public ItemStack getResultItem(@Nonnull RegistryAccess registry) {
		return output;
	}

	@Override
	public boolean matches(@Nonnull PureInfuserBlockEntity inv, @Nonnull Level level) {
		return ingredients.get(0).test(inv.getItem()) 
				&& ingredients.get(1).test(inv.getStackInPedestal(ElementType.WATER)) 
				&& ingredients.get(2).test(inv.getStackInPedestal(ElementType.FIRE))
				&& ingredients.get(3).test(inv.getStackInPedestal(ElementType.EARTH)) 
				&& ingredients.get(4).test(inv.getStackInPedestal(ElementType.AIR));
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializers.PURE_INFUSION.get();
	}

	@Nonnull
	@Override
	public RecipeType<?> getType() {
		return ECRecipeTypes.PURE_INFUSION.get();
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public @NotNull ItemStack assemble(@Nonnull PureInfuserBlockEntity inv, @Nonnull RegistryAccess registry) {
		return this.getResultItem(registry).copy(); // TODO get element from ingredients
	}

	public int getElementAmount() {
		return elementAmount;
	}

	public static class Serializer implements RecipeSerializer<PureInfusionRecipe> {

		@Nonnull
		@Override
		public PureInfusionRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			int elementAmount = GsonHelper.getAsInt(json, ECNames.ELEMENT_AMOUNT);
			NonNullList<Ingredient> ingredients = RecipeHelper.readIngredients(GsonHelper.getAsJsonArray(json, ECNames.INGREDIENTS));
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);

			return new PureInfusionRecipe(recipeId, elementAmount, output, ingredients);
		}

		@Override
		public PureInfusionRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
			var elementAmount = buffer.readInt();
			var output = buffer.readItem();
			var i = buffer.readVarInt();
			var ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buffer));
			return new PureInfusionRecipe(recipeId, elementAmount, output, ingredients);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, PureInfusionRecipe recipe) {
			buffer.writeInt(recipe.elementAmount);
			buffer.writeItem(recipe.output);
			buffer.writeVarInt(recipe.getIngredients().size());

			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}
		}
	}
}
