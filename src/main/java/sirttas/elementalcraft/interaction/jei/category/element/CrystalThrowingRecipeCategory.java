package sirttas.elementalcraft.interaction.jei.category.element;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.elemental.ElementalItemHelper;

import javax.annotation.Nonnull;

public class CrystalThrowingRecipeCategory extends AbstractECRecipeCategory<ElementType> {

    public static final String NAME = "crystal_throwing";

    public CrystalThrowingRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.crystal_throwing", createDrawableStack(guiHelper, new ItemStack(ECItems.INERT_CRYSTAL.get())), guiHelper.createBlankDrawable(100, 45));
        setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/crystal_throwing.png"), 0, 0, 73, 24), 10, 2);
    }

    @Nonnull
    @Override
    public RecipeType<ElementType> getRecipeType() {
        return ECJEIRecipeTypes.CRYSTAL_THROWING;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ElementType type, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 2, 27).addItemStack(new ItemStack(ElementalItemHelper.getCrystalForType(type)));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 67, 27).addItemStack(new ItemStack(ElementalItemHelper.getPowerfulShardForType(type)));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 83, 27).addItemStack(new ItemStack(ElementalItemHelper.getShardForType(type)));
    }
}
