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
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.SpellCraftRecipe;
import sirttas.elementalcraft.spell.Spell;

import java.util.function.Consumer;

public class SpellCraftRecipeBuilder {

	private Ingredient gem;
	private Ingredient crystal;
	private final ResourceLocation output;
	private final RecipeSerializer<?> serializer;

	private SpellCraftRecipeBuilder(RecipeSerializer<?> serializer, ResourceLocation output) {
		this.serializer = serializer;
		this.output = output;
	}
	
	public static SpellCraftRecipeBuilder spellCraftRecipe(RegistryObject<? extends Spell> output) {
		return new SpellCraftRecipeBuilder(ECRecipeSerializers.SPELL_CRAFT.get(), output.getId());
	}
	
	public SpellCraftRecipeBuilder setGem(TagKey<Item> tag) {
		return this.setGem(Ingredient.of(tag));
	}

	public SpellCraftRecipeBuilder setGem(ItemLike item) {
		return this.setGem(Ingredient.of(item));
	}

	public SpellCraftRecipeBuilder setGem(Ingredient ingredient) {
		gem = ingredient;
		return this;
	}

	public SpellCraftRecipeBuilder setCrystal(TagKey<Item> tag) {
		return this.setCrystal(Ingredient.of(tag));
	}

	public SpellCraftRecipeBuilder setCrystal(ItemLike item) {
		return this.setCrystal(Ingredient.of(item));
	}

	public SpellCraftRecipeBuilder setCrystal(Ingredient ingredient) {
		crystal = ingredient;
		return this;
	}
	
	public void save(Consumer<FinishedRecipe> consumer) {
		this.save(consumer, output.getPath());
	}

	public void save(Consumer<FinishedRecipe> consumer, String save) {
		this.save(consumer, ElementalCraft.createRL(SpellCraftRecipe.NAME + '/' + save));
	}

	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new Result(id, this.serializer, this.gem, this.crystal, this.output));
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
			
			json.addProperty(ECNames.ITEM, ForgeRegistries.ITEMS.getKey(ECItems.SCROLL.get()).toString());
			ecNbtJson.addProperty(ECNames.SPELL, this.output.toString());
			tagJson.add(ECNames.EC_NBT, ecNbtJson);
			json.add(ECNames.NBT, tagJson);
			return json;
		}
	}
}
