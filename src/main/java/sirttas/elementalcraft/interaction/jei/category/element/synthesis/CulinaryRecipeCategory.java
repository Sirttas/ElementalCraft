package sirttas.elementalcraft.interaction.jei.category.element.synthesis;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

import javax.annotation.Nonnull;
import java.util.List;

public class CulinaryRecipeCategory extends AbstractECRecipeCategory<ItemStack> {

	public static final String NAME = "culinary";

	private static final ItemStack CULINARY_SYNTHESIZER = new ItemStack(ECBlocks.CULINARY_SYNTHESIZER.get());
	private static final List<ItemStack> CONTAINERS = List.of(
			new ItemStack(ECBlocks.CONTAINER.get()),
			new ItemStack(ECBlocks.WATER_RESERVOIR.get()));

	private final IGuiHelper guiHelper;

	public CulinaryRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.culinary", createDrawableStack(guiHelper, CULINARY_SYNTHESIZER), guiHelper.createBlankDrawable(55, 63));
		addOverlay(guiHelper.createDrawable(DrainingRecipeCategory.TEXTURE, 0, 0, 36, 9), 2, 20);
		this.guiHelper = guiHelper;
	}

	@Nonnull
	@Override
	public RecipeType<ItemStack> getRecipeType() {
		return ECJEIRecipeTypes.CULINARY;
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder acceptor, @NotNull ItemStack recipe, @NotNull IFocusGroup focuses) {
		acceptor.addWidget(new FoodWidget(guiHelper, new ScreenPosition(2, 20)));
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ItemStack recipe, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
				.addItemStack(recipe);
		builder.addSlot(RecipeIngredientRole.CATALYST, 0, 32)
				.addItemStack(CULINARY_SYNTHESIZER);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 0, 47)
				.addItemStacks(CONTAINERS);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 39, 17)
				.addIngredient(ECIngredientTypes.ELEMENT, new IngredientElementType(ElementType.WATER, 2));
	}

}
