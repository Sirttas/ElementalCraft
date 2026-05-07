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
import org.jspecify.annotations.NonNull;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.GrindingRecipeBuilder;
import sirttas.elementalcraft.item.ECItems;

import java.util.concurrent.CompletableFuture;

public class Ae2RecipeProvider extends RecipeProvider {

    public Ae2RecipeProvider(HolderLookup.@NonNull Provider registries, @NonNull RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.MISC, ECBlocks.CERTUS_QUARTZ_SHRINE_UPGRADE.get())
                .define('C', ECItems.SHRINE_UPGRADE_CORE.get())
                .define('f', AEItems.FLUIX_CRYSTAL)
                .define('q', AEBlocks.QUARTZ_BLOCK)
                .define('w', ECBlocks.WHITE_ROCK.get())
                .define('c', ECItems.PURE_CRYSTAL.get())
                .pattern("qfq")
                .pattern("wCw")
                .pattern(" c ")
                .unlockedBy("has_shrine_upgrade_core", has(ECItems.SHRINE_UPGRADE_CORE.get()))
                .save(output);

        GrindingRecipeBuilder.grindingRecipe(AEItems.CERTUS_QUARTZ_CRYSTAL)
                .withCount(6)
                .withIngredient(AEBlocks.QUARTZ_CLUSTER)
                .withLuckRatio(5)
                .save(output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registries, @NonNull RecipeOutput output) {
            return new Ae2RecipeProvider(registries, output.withConditions(new ModLoadedCondition(AEConstants.MOD_ID)));
        }

        @Override
        public @NonNull String getName() {
            return "Elementalcraft Recipes (AE2 interaction)";
        }
    }
}