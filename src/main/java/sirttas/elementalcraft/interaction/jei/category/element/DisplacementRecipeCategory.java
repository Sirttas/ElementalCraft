package sirttas.elementalcraft.interaction.jei.category.element;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.source.IngredientSource;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;

import javax.annotation.Nonnull;

public class DisplacementRecipeCategory extends AbstractECRecipeCategory<ElementType> {

	public static final String NAME = "displacement";


	public DisplacementRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.displacement", createDrawableStack(guiHelper, new ItemStack(ECBlocks.FIRE_SOURCE_DISPLACEMENT_PLATE.get())), guiHelper.createBlankDrawable(64, 32));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/extraction.png"), 0, 0, 24, 9), 21, 19);
	}

	@Nonnull
	@Override
	public RecipeType<ElementType> getRecipeType() {
		return ECJEIRecipeTypes.DISPLACEMENT;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ElementType type, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 0, 0).addIngredient(ECIngredientTypes.SOURCE, new IngredientSource(type));
		builder.addSlot(RecipeIngredientRole.INPUT, 0, 16).addItemStack(switch (type) {
			case AIR -> new ItemStack(ECBlocks.AIR_SOURCE_DISPLACEMENT_PLATE.get());
			case EARTH -> new ItemStack(ECBlocks.EARTH_SOURCE_DISPLACEMENT_PLATE.get());
			case FIRE -> new ItemStack(ECBlocks.FIRE_SOURCE_DISPLACEMENT_PLATE.get());
			case WATER -> new ItemStack(ECBlocks.WATER_SOURCE_DISPLACEMENT_PLATE.get());
			default -> ItemStack.EMPTY;
		});
		builder.addSlot(RecipeIngredientRole.OUTPUT, 47, 0).addItemStack(ReceptacleHelper.create(type));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 47, 16).addItemStack(new ItemStack(ECBlocks.BROKEN_SOURCE_DISPLACEMENT_PLATE.get()));
	}
}
