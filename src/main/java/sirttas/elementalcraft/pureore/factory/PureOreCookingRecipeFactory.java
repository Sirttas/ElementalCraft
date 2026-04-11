package sirttas.elementalcraft.pureore.factory;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.pureore.factory.AbstractPureOreRecipeFactory;
import sirttas.elementalcraft.tag.ECTags;

public class PureOreCookingRecipeFactory<T extends AbstractCookingRecipe> extends AbstractPureOreRecipeFactory<SingleRecipeInput, T> {

	private final Factory<T> factory;

	public PureOreCookingRecipeFactory(RecipeManager recipeManager, RecipeType<@NotNull T> recipeType, Factory<T> factory) {
		super(recipeManager, recipeType);
		this.factory = factory;
	}

	@Override
	public T create(@NotNull RegistryAccess registry, @NotNull T recipe, @NotNull Ingredient ingredient) {
		return factory.create(
                new Recipe.CommonInfo(recipe.showNotification()),
                new AbstractCookingRecipe.CookingBookInfo(recipe.category(), recipe.group()),
                ingredient,
                ItemStackTemplate.fromNonEmptyStack(recipe.assemble(new SingleRecipeInput(ItemStack.EMPTY))),
                recipe.experience(),
                recipe.cookingTime());
	}

	public interface Factory<T extends AbstractCookingRecipe> {
		T create(Recipe.CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime);
	}

	@Override
	public boolean filter(RecipeHolder<@NotNull T> holder, ItemStack stack) {
		return super.filter(holder, stack) && (stack.is(ECTags.Items.PURE_ORES_SOURCES_ORES) || stack.is(ECTags.Items.PURE_ORES_SPECIFICS));
	}

    @Override
    @Deprecated
    public ItemStack getRecipeOutput(@NotNull RegistryAccess registry, @NotNull T recipe) {
        return recipe.assemble(new SingleRecipeInput(ItemStack.EMPTY));
    }
}
