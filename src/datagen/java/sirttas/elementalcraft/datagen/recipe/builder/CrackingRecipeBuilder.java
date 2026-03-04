package sirttas.elementalcraft.datagen.recipe.builder;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.cracking.AbstractCrackingRecipe;
import sirttas.elementalcraft.recipe.cracking.CrackingRecipe;
import sirttas.elementalcraft.recipe.cracking.SculkCrackingRecipe;

public class CrackingRecipeBuilder {

    private final AbstractCrackingRecipe.Factory<?> factory;
    private final HolderSet<Block> input;
    private Block result;
    private int elementAmount;

    private CrackingRecipeBuilder(AbstractCrackingRecipe.Factory<?> factory, HolderSet<Block> input) {
        this.factory = factory;
        this.input = input;
        this.result = Blocks.AIR;
        this.elementAmount = 200;
    }

    public static CrackingRecipeBuilder crackingRecipe(HolderSet<Block> input) {
        return new CrackingRecipeBuilder(CrackingRecipe::new, input);
    }

    public static CrackingRecipeBuilder crackingRecipe(Block input) {
        return new CrackingRecipeBuilder(CrackingRecipe::new, HolderSet.direct(BuiltInRegistries.BLOCK.wrapAsHolder(input)));
    }

    public static CrackingRecipeBuilder sculkCrackingRecipe(HolderSet<Block> input) {
        return new CrackingRecipeBuilder(SculkCrackingRecipe::new, input);
    }

    public static CrackingRecipeBuilder sculkCrackingRecipe(Block input) {
        return new CrackingRecipeBuilder(SculkCrackingRecipe::new, HolderSet.direct(BuiltInRegistries.BLOCK.wrapAsHolder(input)));
    }

    public CrackingRecipeBuilder result(Block result) {
        this.result = result;
        return this;
    }

    public CrackingRecipeBuilder elementAmount(int elementAmount) {
        this.elementAmount = elementAmount;
        return this;
    }

    public void save(RecipeOutput recipeOutput) {
        var id = BuiltInRegistries.BLOCK.getKey(this.result);

        this.save(recipeOutput, Identifier.fromNamespaceAndPath(id.getNamespace(), CrackingRecipe.NAME + '/' + id.getPath()));
    }

    public void save(RecipeOutput recipeOutput, String save) {
        var Identifier = BuiltInRegistries.BLOCK.getKey(this.result);

        if (Identifier.parse(save).equals(Identifier)) {
            throw new IllegalStateException("Cracking Recipe " + save + " should remove its 'save' argument");
        } else {
            this.save(recipeOutput, ElementalCraftApi.createRL(CrackingRecipe.NAME + '/' + save));
        }
    }

    public void save(RecipeOutput recipeOutput, Identifier id) {
        recipeOutput.accept(id, factory.create(input, result, elementAmount), null);
    }
}
