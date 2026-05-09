package sirttas.elementalcraft.interaction.jei.category.source;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.source.IngredientSource;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;

import javax.annotation.Nonnull;

public class DisplacementRecipeCategory extends AbstractECRecipeCategory<ElementType> {

	public static final String NAME = "displacement";


	public DisplacementRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.displacement", createDrawableStack(guiHelper, new ItemStack(ECItems.EMPTY_RECEPTACLE)), 64, 32);
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.identifier("textures/gui/overlay/extraction.png"), 0, 0, 24, 9), 21, 19);
	}

	@Nonnull
	@Override
	public IRecipeType<@NotNull ElementType> getRecipeType() {
		return ECJEIRecipeTypes.DISPLACEMENT;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ElementType type, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 0, 0).add(ECIngredientTypes.SOURCE, new IngredientSource(type));
		builder.addSlot(RecipeIngredientRole.INPUT, 0, 16).add( new ItemStack(ECItems.EMPTY_RECEPTACLE));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 47, 16).add(ReceptacleHelper.create(type));
	}
}
