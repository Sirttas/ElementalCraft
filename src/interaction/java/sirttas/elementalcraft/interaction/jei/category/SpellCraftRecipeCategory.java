package sirttas.elementalcraft.interaction.jei.category;

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
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.spell.SpellCraftRecipe;
import sirttas.elementalcraft.recipe.spell.SpellCraftRecipeDisplay;

import javax.annotation.Nonnull;

public class SpellCraftRecipeCategory extends AbstractECRecipeCategory<SpellCraftRecipe> {

	public SpellCraftRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.spell_craft", createDrawableStack(guiHelper, new ItemStack(ECBlocks.SPELL_DESK.get())), 123, 55);
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.identifier("textures/gui/overlay/spell_craft.png"), 0, 0, 103, 36), 10, 10);
	}

	@Nonnull
	@Override
	public IRecipeType<@NotNull SpellCraftRecipe> getRecipeType() {
		return ECJEIRecipeTypes.SPELL_CRAFTING;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull SpellCraftRecipe recipe, @Nonnull IFocusGroup focuses) {
        if (!(recipe.display().getFirst() instanceof SpellCraftRecipeDisplay display)) {
            return;
        }

		builder.addSlot(RecipeIngredientRole.INPUT, 20, 11)
				.add(new ItemStack(ECItems.SCROLL_PAPER));
		builder.addSlot(RecipeIngredientRole.INPUT, 11, 29)
				.add(display.gem());
		builder.addSlot(RecipeIngredientRole.INPUT, 29, 29)
				.add(display.crystal());

		builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 20)
				.add(display.result());
	}
}
