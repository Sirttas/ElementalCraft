package sirttas.elementalcraft.interaction.jei.category.source;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.elemental.ElementalItem;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;

import javax.annotation.Nonnull;

public class SourceBreedingRecipeCategory extends AbstractECRecipeCategory<ElementalItem> {

	public static final String NAME = "source_breeding";


	public SourceBreedingRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.source_breeding", createDrawableStack(guiHelper, new ItemStack(ECBlocks.SOURCE_BREEDER.get())), 67, 80);
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/source_breeding.png"), 0, 0, 47, 33), 10, 10);
	}

	@Nonnull
	@Override
	public IRecipeType<@NotNull ElementalItem> getRecipeType() {
		return ECJEIRecipeTypes.SOURCE_BREEDING;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ElementalItem seed, @Nonnull IFocusGroup focuses) {
		var type = seed.getElementType();
		var sourceReceptacle = ReceptacleHelper.create(type);

		builder.addSlot(RecipeIngredientRole.INPUT, 25, 46).add(new ItemStack(seed));
		builder.addSlot(RecipeIngredientRole.INPUT, 25, 62).add(ECIngredientTypes.ELEMENT, new IngredientElementType(type, 4));

		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 4, 38).add(sourceReceptacle);
		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 48, 38).add(sourceReceptacle);

		builder.addSlot(RecipeIngredientRole.OUTPUT, 25, 2).add(ReceptacleHelper.create(type));
	}
}
