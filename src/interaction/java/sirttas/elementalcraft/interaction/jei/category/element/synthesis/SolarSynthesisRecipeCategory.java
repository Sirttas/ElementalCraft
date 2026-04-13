package sirttas.elementalcraft.interaction.jei.category.element.synthesis;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

import javax.annotation.Nonnull;
import java.util.List;

public class SolarSynthesisRecipeCategory extends AbstractECRecipeCategory<Ingredient> {

	public static final String NAME = "solar_fire_synthesis";

	private static final ItemStack SOLAR_SYNTHESIZER = new ItemStack(ECBlocks.SOLAR_SYNTHESIZER.get());
	private static final List<ItemStack> CONTAINERS = List.of(
			new ItemStack(ECBlocks.CONTAINER.get()),
			new ItemStack(ECBlocks.FIRE_RESERVOIR.get()));

	public SolarSynthesisRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.solar_fire_synthesis", createDrawableStack(guiHelper, SOLAR_SYNTHESIZER), 84, 66);
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/solar_fire_synthesis.png"), 0, 0, 49, 54), 8, 4);
	}

	@Nonnull
	@Override
	public IRecipeType<@NotNull Ingredient> getRecipeType() {
		return ECJEIRecipeTypes.SOLAR_SYNTHESIS;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull Ingredient ingredient, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 16, 0)
				.add(ingredient);

		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 15, 31)
				.add(SOLAR_SYNTHESIZER);
		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 15, 47)
				.addItemStacks(CONTAINERS);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 60, 44)
				.add(ECIngredientTypes.ELEMENT, new IngredientElementType(ElementType.FIRE, 1));
	}
}
