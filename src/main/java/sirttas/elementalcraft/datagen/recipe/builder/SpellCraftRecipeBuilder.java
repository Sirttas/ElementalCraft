package sirttas.elementalcraft.datagen.recipe.builder;

import java.util.function.Consumer;

import com.google.gson.JsonObject;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.SpellCraftRecipe;

public class SpellCraftRecipeBuilder {

	private Ingredient gem;
	private Ingredient crystal;
	private final ResourceLocation output;
	private final IRecipeSerializer<?> serializer;

	private SpellCraftRecipeBuilder(IRecipeSerializer<?> serializerIn, ResourceLocation output) {
		this.serializer = serializerIn;
		this.output = output;
	}
	
	public static SpellCraftRecipeBuilder spellCraftRecipe(ResourceLocation output) {
		return new SpellCraftRecipeBuilder(SpellCraftRecipe.SERIALIZER, output);
	}
	
	public SpellCraftRecipeBuilder setGem(INamedTag<Item> tagIn) {
		return this.setGem(Ingredient.of(tagIn));
	}

	public SpellCraftRecipeBuilder setGem(IItemProvider itemIn) {
		return this.setGem(Ingredient.of(itemIn));
	}

	public SpellCraftRecipeBuilder setGem(Ingredient ingredientIn) {
		gem = ingredientIn;
		return this;
	}

	public SpellCraftRecipeBuilder setCrystal(INamedTag<Item> tagIn) {
		return this.setCrystal(Ingredient.of(tagIn));
	}

	public SpellCraftRecipeBuilder setCrystal(IItemProvider itemIn) {
		return this.setCrystal(Ingredient.of(itemIn));
	}

	public SpellCraftRecipeBuilder setCrystal(Ingredient ingredientIn) {
		crystal = ingredientIn;
		return this;
	}
	
	public void build(Consumer<IFinishedRecipe> consumerIn) {
		this.build(consumerIn, output.getPath());
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
		this.build(consumerIn, ElementalCraft.createRL(SpellCraftRecipe.NAME + '/' + save));
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.serializer, this.gem, this.crystal, this.output));
	}
	
	public static class Result extends AbstractFinishedRecipe {

		private final Ingredient gem;
		private final Ingredient crystal;
		private final ResourceLocation output;
		
		public Result(ResourceLocation id, IRecipeSerializer<?> serializer, Ingredient gem, Ingredient crystal, ResourceLocation output) {
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
