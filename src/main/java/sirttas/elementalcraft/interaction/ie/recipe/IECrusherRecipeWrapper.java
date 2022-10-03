package sirttas.elementalcraft.interaction.ie.recipe;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AirMillGrindstoneBlockEntity;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

import javax.annotation.Nonnull;

public class IECrusherRecipeWrapper implements IGrindingRecipe {

    private final CrusherRecipe crushingRecipe;

    public IECrusherRecipeWrapper(CrusherRecipe crushingRecipe) {
        this.crushingRecipe = crushingRecipe;
    }

    @Override
    public int getElementAmount() {
        return 1000;
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return crushingRecipe.getIngredients();
    }

    @Nonnull
    @Override
    public ItemStack getResultItem() {
        return crushingRecipe.getResultItem();
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return crushingRecipe.getId();
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return crushingRecipe.getSerializer();
    }

    @Override
    public boolean matches(ItemStack stack) {
        return crushingRecipe.input.test(stack) && IGrindingRecipe.super.matches(stack);
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull AirMillGrindstoneBlockEntity instrument) {
        var luck = instrument.getRuneHandler().getBonus(Rune.BonusType.LUCK);
        var result = IGrindingRecipe.super.assemble(instrument);
        var level = instrument.getLevel();

        if (level == null) {
            return result;
        }

        var rand = level.getRandom();

        crushingRecipe.secondaryOutputs.forEach(output -> {
            var stack = output.stack().get();

            if (ItemStack.isSame(stack, result) && rand.nextFloat() < output.chance() * luck) {
                result.grow(stack.getCount());
            }
        });
        return result;
    }
}
