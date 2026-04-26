package sirttas.elementalcraft.datagen.recipe.builder;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.spell.SpellCraftRecipe;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;

public class SpellCraftRecipeBuilder {

	private Ingredient gem;
	private Ingredient crystal;
	private final Identifier output;

	private SpellCraftRecipeBuilder(Identifier output) {
		this.output = output;
	}
	
	public static SpellCraftRecipeBuilder spellCraftRecipe(DeferredHolder<Spell, ? extends Spell> output) {
		return new SpellCraftRecipeBuilder(output.getId());
	}

	public SpellCraftRecipeBuilder setGem(ItemLike item) {
		return this.setGem(Ingredient.of(item));
	}

	public SpellCraftRecipeBuilder setGem(Ingredient ingredient) {
		gem = ingredient;
		return this;
	}

	public SpellCraftRecipeBuilder setCrystal(ItemLike item) {
		return this.setCrystal(Ingredient.of(item));
	}

	public SpellCraftRecipeBuilder setCrystal(Ingredient ingredient) {
		crystal = ingredient;
		return this;
	}
	
	public void save(RecipeOutput recipeOutput) {
		this.save(recipeOutput, output.getPath());
	}

	public void save(RecipeOutput recipeOutput, String save) {
		this.save(recipeOutput, ElementalCraftApi.createRL(SpellCraftRecipe.NAME + '/' + save));
	}

	public void save(RecipeOutput recipeOutput, Identifier id) {
		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, id), new SpellCraftRecipe(new Recipe.CommonInfo(false), this.gem, this.crystal, Spells.REGISTRY.get(output).orElseThrow().value().createItemStackTemplate()), null);
	}
}
