package sirttas.elementalcraft.interaction.jei.category.shrine;

import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.spring.SpringShrineBlock;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;

import javax.annotation.Nonnull;

public class SpringShrineRecipeCategory extends AbstractECRecipeCategory<SpringShrineBlock> {

    private final BlockState water;
    private final BlockState springShrine;
    private final ITickTimer timer;

    public SpringShrineRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.springshrine", createDrawableStack(guiHelper, new ItemStack(ECBlocks.SPRING_SHRINE.get())), 110, 80);
        water = Blocks.WATER.defaultBlockState();
        springShrine = ECBlocks.SPRING_SHRINE.get().defaultBlockState();

        timer = guiHelper.createTickTimer(40, 1, false);
        addOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/extraction.png"), 0, 0, 24, 9), 61, 64);
    }

    @Nonnull
    @Override
    public IRecipeType<@NotNull SpringShrineBlock> getRecipeType() {
        return ECJEIRecipeTypes.SPRING_SHRINE;
    }

    @Override
    public void draw(@Nonnull SpringShrineBlock recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        submit(guiGraphics, (submitNodeStorage, poseStack) -> {
            submitBlock(submitNodeStorage, poseStack, springShrine);

            var t = timer.getValue();

            if (t == 0) {
                poseStack.translate(0, 1, 0);
                ECRendererHelper.submitFluid(water, poseStack, submitNodeStorage);
            }
        });
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull SpringShrineBlock recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 60).add(Fluids.WATER, 1000);
    }
}
