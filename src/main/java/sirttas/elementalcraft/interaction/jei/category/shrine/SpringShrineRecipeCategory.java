package sirttas.elementalcraft.interaction.jei.category.shrine;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.spring.SpringShrineBlock;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;

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
        setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/extraction.png"), 0, 0, 24, 9), 61, 64);
    }

    @Nonnull
    @Override
    public RecipeType<SpringShrineBlock> getRecipeType() {
        return ECJEIRecipeTypes.SPRING_SHRINE;
    }

    @Override
    public void draw(@Nonnull SpringShrineBlock recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull PoseStack poseStack, double mouseX, double mouseY) {
        render3D(poseStack, (p, b) -> {
            setupPose(p);
            renderBlock(springShrine, p, b);
            p.translate(0, 1, 0);

            var t = timer.getValue();

            if (t == 0) {
                renderFluid(water, p, b);
            }
        });
        super.draw(recipe, recipeSlotsView, poseStack, mouseX, mouseY);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull SpringShrineBlock recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 60).addFluidStack(Fluids.WATER, 1000);
    }
}
