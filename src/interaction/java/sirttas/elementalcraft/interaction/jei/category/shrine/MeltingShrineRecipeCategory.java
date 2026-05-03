package sirttas.elementalcraft.interaction.jei.category.shrine;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.recipe.melting.MeltingRecipe;

import javax.annotation.Nonnull;

public class MeltingShrineRecipeCategory extends AbstractECRecipeCategory<MeltingRecipe> {

    private final BlockState meltingShrine;

    public MeltingShrineRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.melting_shrine", createDrawableStack(guiHelper, new ItemStack(ECBlocks.MELTING_SHRINE.get())), 121, 80);
        meltingShrine = ECBlocks.MELTING_SHRINE.get().defaultBlockState();
        addOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/extraction.png"), 0, 0, 24, 9), 72, 64);
    }

    @Nonnull
    @Override
    public IRecipeType<@NotNull MeltingRecipe> getRecipeType() {
        return ECJEIRecipeTypes.MELTING_SHRINE;
    }

    @Override
    public void draw(@Nonnull MeltingRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        submit(guiGraphics, (submitNodeStorage, poseStack) -> {
            submitBlock(submitNodeStorage, poseStack, meltingShrine);
            poseStack.translate(0, 1, 0);

            var inputs = recipeSlotsView.getSlotViews(RecipeIngredientRole.INPUT);

            if (inputs.isEmpty()) {
                return;
            }

            var stack = inputs.getFirst().getDisplayedItemStack().orElse(ItemStack.EMPTY);
            var block = stack.getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : Blocks.AIR;
            var state = block.defaultBlockState();

            if (!state.getFluidState().isEmpty()) {
                ECRendererHelper.submitFluid(state, poseStack, submitNodeStorage);
            } else if (!state.isAir()) {
                submitBlock(submitNodeStorage, poseStack, state);
            }
        });
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull MeltingRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 51, 60).addItemStacks(recipe.input().blocks().stream()
                .map(Holder::value)
                .filter(block -> !block.defaultBlockState().isAir())
                .map(ItemStack::new)
                .toList());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 101, 60).add(recipe.result().fluid().value(), 1000);
    }
}
