package sirttas.elementalcraft.interaction.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.recipe.pure.infusion.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.pure.infusion.PureInfusionRecipeDisplay;

import javax.annotation.Nonnull;

public class PureInfusionRecipeCategory extends AbstractECRecipeCategory<PureInfusionRecipe> {

	public PureInfusionRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.pureinfusion", createDrawableStack(guiHelper, new ItemStack(ECBlocks.PURE_INFUSER.get())), 177, 134);
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.identifier("textures/gui/overlay/pureinfusion.png"), 0, 0, 142, 83), 27, 27);
	}

	@Nonnull
	@Override
	public IRecipeType<@NotNull PureInfusionRecipe> getRecipeType() {
		return ECJEIRecipeTypes.PURE_INFUSION;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull PureInfusionRecipe recipe, @Nonnull IFocusGroup focuses) {
        if (!(recipe.display().getFirst() instanceof PureInfusionRecipeDisplay display)) {
            return;
        }

		var elementAmount = IngredientElementType.getGaugeValue(display.elementAmount());

		builder.addSlot(RecipeIngredientRole.INPUT, 60, 61)
				.add(display.pureInfuserInput());

		// Left
		builder.addSlot(RecipeIngredientRole.INPUT, 26, 61)
				.add(display.fireInput());
		builder.addSlot(RecipeIngredientRole.INPUT, 9, 61)
				.add(ECIngredientTypes.ELEMENT, new IngredientElementType(ElementType.FIRE, elementAmount));

		// Top
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 27)
				.add(display.waterInput());
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 10)
				.add(ECIngredientTypes.ELEMENT, new IngredientElementType(ElementType.WATER, elementAmount));

		// Bottom
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 95)
				.add(display.earthInput());
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 112)
				.add(ECIngredientTypes.ELEMENT, new IngredientElementType(ElementType.EARTH, elementAmount));

		// Right
		builder.addSlot(RecipeIngredientRole.INPUT, 94, 61)
				.add(display.airInput());
		builder.addSlot(RecipeIngredientRole.INPUT, 111, 61)
				.add(ECIngredientTypes.ELEMENT, new IngredientElementType(ElementType.AIR, elementAmount));

		builder.addSlot(RecipeIngredientRole.OUTPUT, 154, 61)
				.add(display.result());
	}
}
