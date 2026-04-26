package sirttas.elementalcraft.datagen.interaction.ae2;

import appeng.api.ids.AEConstants;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.grinding.GrindingRecipeBuilder;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class Ae2RecipeProvider extends RecipeProvider {

    public Ae2RecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@Nonnull RecipeOutput output) {
        shaped(RecipeCategory.MISC, ECBlocks.CERTUS_QUARTZ_SHRINE_UPGRADE.get())
                .define('C', ECItems.SHRINE_UPGRADE_CORE.get())
                .define('f', AEItems.FLUIX_CRYSTAL)
                .define('q', AEBlocks.QUARTZ_BLOCK)
                .define('w', ECBlocks.WHITE_ROCK.get())
                .define('c', ECItems.PURE_CRYSTAL.get())
                .pattern("qfq")
                .pattern("wCw")
                .pattern(" c ")
                .unlockedBy("has_shrine_upgrade_core", has(ECItems.SHRINE_UPGRADE_CORE))
                .save(output.withConditions(new ModLoadedCondition(AEConstants.MOD_ID)));

        GrindingRecipeBuilder.grindingRecipe(AEItems.CERTUS_QUARTZ_CRYSTAL)
                .withCount(6)
                .withIngredient(AEBlocks.QUARTZ_CLUSTER)
                .withLuckRatio(5)
                .save(output.withConditions(new ModLoadedCondition(AEConstants.MOD_ID)));
    }
}