package sirttas.elementalcraft.interaction.jei.category.shrine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.lava.LavaShrineBlock;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public class LavaShrineRecipeCategory extends AbstractShrineRecipeCategory<LavaShrineBlock> {

    private final BlockState lava;
    private final BlockState lavaShrine;
    private final Block[] liquifiables;
    private final ITickTimer timer;

    public LavaShrineRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.lavashrine", createDrawableStack(guiHelper, new ItemStack(ECBlocks.LAVA_SHRINE.get())), guiHelper.createBlankDrawable(121, 80));
        lava = Blocks.LAVA.defaultBlockState();
        lavaShrine = ECBlocks.LAVA_SHRINE.get().defaultBlockState();
        liquifiables = ECTags.Blocks.getTag(ECTags.Blocks.SHRINES_LAVA_LIQUIFIABLES).stream()
                .map(Holder::get)
                .toArray(Block[]::new);

        var l = liquifiables.length * 2;

        timer = guiHelper.createTickTimer(l * 20, l, false);
        setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/extraction.png"), 0, 0, 24, 9), 72, 64);
    }

    @Nonnull
    @Override
    public RecipeType<LavaShrineBlock> getRecipeType() {
        return ECJEIRecipeTypes.LAVA_SHRINE;
    }

    @Override
    public void draw(@Nonnull LavaShrineBlock recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull PoseStack poseStack, double mouseX, double mouseY) {
        render3D(poseStack, (p, b) -> {
            p.translate(-1.5, -2.3, 0);
            p.mulPose(Vector3f.XP.rotationDegrees(-30.0F));
            p.mulPose(Vector3f.YP.rotationDegrees(40.0F));
            renderBlock(lavaShrine, p, b);
            p.translate(0, 1, 0);

            var t = timer.getValue();

            if (t >= liquifiables.length) {
                renderFluid(lava, p, b);
            } else {
                renderBlock(liquifiables[t].defaultBlockState(), p, b);
            }
        });
        super.draw(recipe, recipeSlotsView, poseStack, mouseX, mouseY);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull LavaShrineBlock recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 51, 60).addIngredients(Ingredient.of(liquifiables));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 101, 60).addFluidStack(Fluids.LAVA, 1000);
    }
}
