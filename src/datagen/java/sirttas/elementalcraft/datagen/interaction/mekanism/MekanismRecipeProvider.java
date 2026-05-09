package sirttas.elementalcraft.datagen.interaction.mekanism;

import mekanism.api.MekanismAPI;
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class MekanismRecipeProvider extends RecipeProvider {

    public MekanismRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@Nonnull RecipeOutput output) {
        ItemStackToItemStackRecipeBuilder.enriching(IngredientCreatorAccess.item().from(ECTags.Items.ORES_INERT_CRYSTAL), new ItemStack(ECItems.INERT_CRYSTAL, 2))
                .build(output.withConditions(new ModLoadedCondition(MekanismAPI.MEKANISM_MODID)), ElementalCraftApi.identifier("inert_crystal_from_mekanism_enriching"));
    }
}
