package sirttas.elementalcraft.datagen.recipe.builder;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.SpellCraftRecipe;

import java.util.function.Consumer;

public class SpellCraftRecipeBuilder {

	private Ingredient gem;
	private Ingredient crystal;
	private final ResourceLocation output;
	private final RecipeSerializer<?> serializer;

	private SpellCraftRecipeBuilder(RecipeSerializer<?> serializerIn, ResourceLocation output) {
		this.serializer = serializerIn;
		this.output = output;
	}
	
	public static SpellCraftRecipeBuilder spellCraftRecipe(ResourceLocation output) {
		return new SpellCraftRecipeBuilder(SpellCraftRecipe.SERIALIZER, output);
	}
	
	public SpellCraftRecipeBuilder setGem(TagKey<Item> tagIn) {
		return this.setGem(Ingredient.of(tagIn));
	}

	public SpellCraftRecipeBuilder setGem(ItemLike itemIn) {
		return this.setGem(Ingredient.of(itemIn));
	}

	public SpellCraftRecipeBuilder setGem(Ingredient ingredientIn) {
		gem = ingredientIn;
		return this;
	}

	public SpellCraftRecipeBuilder setCrystal(TagKey<Item> tagIn) {
		return this.setCrystal(Ingredient.of(tagIn));
	}

	public SpellCraftRecipeBuilder setCrystal(ItemLike itemIn) {
		return this.setCrystal(Ingredient.of(itemIn));
	}

	public SpellCraftRecipeBuilder setCrystal(Ingredient ingredientIn) {
		crystal = ingredientIn;
		return this;
	}
	
	public void build(Consumer<FinishedRecipe> consumerIn) {
		this.build(consumerIn, output.getPath());
	}

	public void build(Consumer<FinishedRecipe> consumerIn, String save) {
		this.build(consumerIn, ElementalCraft.createRL(SpellCraftRecipe.NAME + '/' + save));
	}

	public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.serializer, this.gem, this.crystal, this.output));
	}
	
	public static class Result extends AbstractFinishedRecipe {

		private final Ingredient gem;
		private final Ingredient crystal;
		private final ResourceLocation output;
		
		public Result(ResourceLocation id, RecipeSerializer<?> serializer, Ingredient gem, Ingredient crystal, ResourceLocation output) {
			super(id, serializer);
			this.gem = gem;
			this.crystal = crystal;
			this.output = output;
		}
		
		@Override
		public void serializeRecipeData(JsonObject json) {
			json.add(ECNames.GEM, gem.toJson());
			json.add(ECNames.CRYSTAL, crystal.toJson());
			json.add(ECNames.OUTPUT, getJsonOutput());
		}

		private JsonObject getJsonOutput() {
			JsonObject json = new JsonObject();
			JsonObject tagJson = new JsonObject();
			JsonObject ecNbtJson = new JsonObject();
			
			json.addProperty(ECNames.ITEM, ForgeRegistries.ITEMS.getKey(ECItems.SCROLL).toString());
			ecNbtJson.addProperty(ECNames.SPELL, this.output.toString());
			tagJson.add(ECNames.EC_NBT, ecNbtJson);
			json.add(ECNames.NBT, tagJson);
			return json;
		}
	}
}
