package sirttas.elementalcraft.interaction.jei.category.shrine;

import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.spring.SpringShrineBlock;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class SpringShrineRecipeCategory extends AbstractShrineRecipeCategory<SpringShrineBlock> {

    private final BlockState water;
    private final BlockState springShrine;
    private final ITickTimer timer;

    public SpringShrineRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.springshrine", createDrawableStack(guiHelper, new ItemStack(ECBlocks.SPRING_SHRINE.get())), guiHelper.createBlankDrawable(110, 80));
        water = Blocks.WATER.defaultBlockState();
        springShrine = ECBlocks.SPRING_SHRINE.get().defaultBlockState();

        timer = guiHelper.createTickTimer(40, 1, false);
        setOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/extraction.png"), 0, 0, 24, 9), 61, 64);
    }

    @Nonnull
    @Override
    public RecipeType<SpringShrineBlock> getRecipeType() {
        return ECJEIRecipeTypes.SPRING_SHRINE;
    }

    @Override
    public void draw(@Nonnull SpringShrineBlock recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        render3D(guiGraphics, (p, b) -> {
            setupPose(p);
            ECRendererHelper.renderBlock(springShrine, p, b);
            p.translate(0, 1, 0);

            var t = timer.getValue();

            if (t == 0) {
                ECRendererHelper.renderFluid(water, p, b);
            }
        });
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull SpringShrineBlock recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 60).addFluidStack(Fluids.WATER, 1000);
    }
}
