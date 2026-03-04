package sirttas.elementalcraft.datagen.recipe.builder;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.melting.MeltingRecipe;

public class MeltingRecipeBuilder {

    private final HolderSet<Block> input;
    private final Fluid result;
    private int cooldown;
    private int elementAmount;
    private float fillingAmount;

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

    public MeltingRecipeBuilder fillingAmount(float fillingAmount) {
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
            this.save(recipeOutput, ElementalCraftApi.createRL(MeltingRecipe.NAME + '/' + save));
        }
    }

    public void save(RecipeOutput recipeOutput, Identifier id) {
        recipeOutput.accept(id, new MeltingRecipe(input, result, cooldown, elementAmount, fillingAmount), null);
    }
}
