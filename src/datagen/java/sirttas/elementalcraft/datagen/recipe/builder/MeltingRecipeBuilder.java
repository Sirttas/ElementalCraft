package sirttas.elementalcraft.datagen.recipe.builder;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.ingredient.BlockHolderSetIngredient;
import sirttas.elementalcraft.recipe.melting.MeltingRecipe;

public class MeltingRecipeBuilder {

    private final HolderSet<Block> input;
    private final Fluid result;
    private int cooldown;
    private int elementAmount;
    private int fillingAmount;

    private MeltingRecipeBuilder(HolderSet<Block> input, Fluid result) {
        this.input = input;
        this.result = result;
    }

    public static MeltingRecipeBuilder melting(HolderSet<Block> input, Fluid result) {
        return new MeltingRecipeBuilder(input, result);
    }

    public MeltingRecipeBuilder cooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public MeltingRecipeBuilder elementAmount(int elementAmount) {
        this.elementAmount = elementAmount;
        return this;
    }

    public MeltingRecipeBuilder fillingAmount(int fillingAmount) {
        this.fillingAmount = fillingAmount;
        return this;
    }

    public void save(RecipeOutput recipeOutput) {
        var id = BuiltInRegistries.FLUID.getKey(this.result);

        this.save(recipeOutput, Identifier.fromNamespaceAndPath(id.getNamespace(), MeltingRecipe.NAME + '/' + id.getPath()));
    }

    public void save(RecipeOutput recipeOutput, String save) {
        var Identifier = BuiltInRegistries.FLUID.getKey(this.result);

        if (Identifier.parse(save).equals(Identifier)) {
            throw new IllegalStateException("Melting Recipe " + save + " should remove its 'save' argument");
        } else {
            this.save(recipeOutput, ElementalCraftApi.identifier(MeltingRecipe.NAME + '/' + save));
        }
    }

    public void save(RecipeOutput recipeOutput, Identifier id) {
        recipeOutput.accept(ResourceKey.create(Registries.RECIPE, id), new MeltingRecipe(new Recipe.CommonInfo(false), new BlockHolderSetIngredient(input), new FluidStackTemplate(result, fillingAmount), cooldown, elementAmount), null);
    }
}
