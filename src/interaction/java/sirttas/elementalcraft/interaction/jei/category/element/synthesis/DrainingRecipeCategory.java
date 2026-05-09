package sirttas.elementalcraft.interaction.jei.category.element.synthesis;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

import javax.annotation.Nonnull;
import java.util.List;

public class DrainingRecipeCategory extends AbstractECRecipeCategory<IngredientElementType> {

	public static final String NAME = "draining";
	public static final Identifier TEXTURE = ElementalCraftApi.identifier("textures/gui/overlay/draining.png");

	private static final ItemStack DRAINING_SYNTHESIZER = new ItemStack(ECBlocks.DRAINING_SYNTHESIZER.get());
	private static final List<ItemStack> CONTAINERS = List.of(
			new ItemStack(ECBlocks.SMALL_CONTAINER.get()),
			new ItemStack(ECBlocks.CONTAINER.get()),
			new ItemStack(ECBlocks.WATER_RESERVOIR.get()));

	private final IGuiHelper guiHelper;

	public DrainingRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.draining", createDrawableStack(guiHelper, DRAINING_SYNTHESIZER), 55, 46);
		addOverlay(guiHelper.createDrawable(TEXTURE, 0, 0, 36, 9), 2, 3);
		this.guiHelper = guiHelper;
	}

	@Nonnull
	@Override
	public IRecipeType<@NotNull IngredientElementType> getRecipeType() {
		return ECJEIRecipeTypes.DRAINING;
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder acceptor, @NotNull IngredientElementType recipe, @NotNull IFocusGroup focuses) {
		acceptor.addWidget(new FoodWidget(guiHelper, new ScreenPosition(2, 3)));
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull IngredientElementType recipe, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 0, 15)
				.add(DRAINING_SYNTHESIZER);
		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 0, 30)
				.addItemStacks(CONTAINERS);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 39, 0)
				.add(ECIngredientTypes.ELEMENT, recipe);
	}

}
