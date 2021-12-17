package sirttas.elementalcraft.recipe;

import com.google.gson.JsonObject;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;

public class SpellCraftRecipe implements IECRecipe<Container> {

	public static final String NAME = "spell_craft";
	public static final RecipeType<SpellCraftRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new RecipeType<SpellCraftRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});

	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final RecipeSerializer<SpellCraftRecipe> SERIALIZER = null;
	
	private static final Ingredient SCROLL_PAPER = Ingredient.of(ECItems.SCROLL_PAPER);
	
	private final Ingredient gem;
	private final Ingredient crystal;
	private final ItemStack output;
	private final ResourceLocation id;
	
	public SpellCraftRecipe(ResourceLocation id, ItemStack output, Ingredient gem, Ingredient crystal) {
		this.id = id;
		this.output = output;
		this.gem = gem;
		this.crystal = crystal;
	}
	
	@Override
	public boolean matches(Container inv, @Nonnull Level worldIn) {
		return SCROLL_PAPER.test(inv.getItem(0)) && gem.test(inv.getItem(1)) && crystal.test(inv.getItem(2));
	}

	@Nonnull
	@Override
	public ItemStack getResultItem() {
		return output;
	}
	
	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, SCROLL_PAPER, gem, crystal);
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Nonnull
	@Override
	public RecipeSerializer<SpellCraftRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Nonnull
	@Override
	public RecipeType<SpellCraftRecipe> getType() {
		return TYPE;
	}
	
	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<SpellCraftRecipe> {

		@Nonnull
		@Override
		public SpellCraftRecipe fromJson(@Nonnull ResourceLocation recipeId, JsonObject json) {
			Ingredient gem = Ingredient.fromJson(json.get(ECNames.GEM));
			Ingredient crystal = Ingredient.fromJson(json.get(ECNames.CRYSTAL));
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);

			return new SpellCraftRecipe(recipeId, output, gem, crystal);
		}

		@Override
		public SpellCraftRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
			Ingredient gem = Ingredient.fromNetwork(buffer);
			Ingredient crystal = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();

			return new SpellCraftRecipe(recipeId, output, gem, crystal);
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buffer, SpellCraftRecipe recipe) {
			recipe.gem.toNetwork(buffer);
			recipe.crystal.toNetwork(buffer);
			buffer.writeItem(recipe.getResultItem());
		}
	}

}
