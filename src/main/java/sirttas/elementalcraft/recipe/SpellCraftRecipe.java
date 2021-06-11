package sirttas.elementalcraft.recipe;

import com.google.gson.JsonObject;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.ECItems;

public class SpellCraftRecipe implements IECRecipe<IInventory> {

	public static final String NAME = "spell_craft";
	public static final IRecipeType<SpellCraftRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new IRecipeType<SpellCraftRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});

	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final IRecipeSerializer<SpellCraftRecipe> SERIALIZER = null;
	
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
	public boolean matches(IInventory inv, World worldIn) {
		return SCROLL_PAPER.test(inv.getItem(0)) && gem.test(inv.getItem(1)) && crystal.test(inv.getItem(2));
	}

	@Override
	public ItemStack getResultItem() {
		return output;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, SCROLL_PAPER, gem, crystal);
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public IRecipeSerializer<SpellCraftRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public IRecipeType<SpellCraftRecipe> getType() {
		return TYPE;
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SpellCraftRecipe> {

		@Override
		public SpellCraftRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			Ingredient gem = Ingredient.fromJson(json.get(ECNames.GEM));
			Ingredient crystal = Ingredient.fromJson(json.get(ECNames.CRYSTAL));
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);

			return new SpellCraftRecipe(recipeId, output, gem, crystal);
		}

		@Override
		public SpellCraftRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
			Ingredient gem = Ingredient.fromNetwork(buffer);
			Ingredient crystal = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();

			return new SpellCraftRecipe(recipeId, output, gem, crystal);
		}

		@Override
		public void toNetwork(PacketBuffer buffer, SpellCraftRecipe recipe) {
			recipe.gem.toNetwork(buffer);
			recipe.crystal.toNetwork(buffer);
			buffer.writeItem(recipe.getResultItem());
		}
	}

}
