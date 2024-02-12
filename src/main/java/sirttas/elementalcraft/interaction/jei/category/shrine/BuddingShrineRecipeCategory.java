package sirttas.elementalcraft.interaction.jei.category.shrine;

import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrineBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.AbstractHorizontalShrineUpgradeBlock;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class BuddingShrineRecipeCategory extends AbstractShrineRecipeCategory<BuddingShrineBlock.CrystalType> {

    private final ITickTimer timer;
    private final BlockState springalineShrineUpgrade;

    public BuddingShrineRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.buddingshrine", createDrawableStack(guiHelper, new ItemStack(ECBlocks.BUDDING_SHRINE.get())), guiHelper.createBlankDrawable(110, 66));
        timer = guiHelper.createTickTimer(100, 4, false);
        springalineShrineUpgrade = ECBlocks.SPRINGALINE_SHRINE_UPGRADE.get().defaultBlockState().setValue(AbstractHorizontalShrineUpgradeBlock.FACING, Direction.SOUTH);
        setOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/extraction.png"), 0, 0, 24, 9), 61, 44);
    }

    @Nonnull
    @Override
    public RecipeType<BuddingShrineBlock.CrystalType> getRecipeType() {
        return ECJEIRecipeTypes.BUDDING_SHRINE;
    }

    private BlockState getGrowthCrystal(BuddingShrineBlock.CrystalType type) {
        var t = timer.getValue();

        return switch (type) {
            case AMETHYST -> switch (t) {
                case 1 -> Blocks.SMALL_AMETHYST_BUD.defaultBlockState();
                case 2 -> Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState();
                case 3 -> Blocks.LARGE_AMETHYST_BUD.defaultBlockState();
                case 4 -> Blocks.AMETHYST_CLUSTER.defaultBlockState();
                default -> Blocks.AIR.defaultBlockState();
            };
            case SPRINGALINE -> switch (t) {
                case 1 -> ECBlocks.SMALL_SPRINGALINE_BUD.get().defaultBlockState();
                case 2 -> ECBlocks.MEDIUM_SPRINGALINE_BUD.get().defaultBlockState();
                case 3 -> ECBlocks.LARGE_SPRINGALINE_BUD.get().defaultBlockState();
                case 4 -> ECBlocks.SPRINGALINE_CLUSTER.get().defaultBlockState();
                default -> Blocks.AIR.defaultBlockState();
            };
        };
    }

    @Override
    public void draw(@Nonnull BuddingShrineBlock.CrystalType crystalType, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        render3D(guiGraphics, (p, b) -> {
            p.translate(0, 0.5, 0);
            setupPose(p);
            ECRendererHelper.renderBlock(ECBlocks.BUDDING_SHRINE.get().defaultBlockState().setValue(BuddingShrineBlock.CRYSTAL_TYPE, crystalType), p, b);
            if (crystalType == BuddingShrineBlock.CrystalType.SPRINGALINE) {
                p.pushPose();
                p.translate(0, 0, -1);
                ECRendererHelper.renderBlock(springalineShrineUpgrade, p, b);
                p.popPose();
            }
            p.translate(0, 1, 0);
            ECRendererHelper.renderBlock(getGrowthCrystal(crystalType), p, b);
        });
        super.draw(crystalType, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull BuddingShrineBlock.CrystalType crystalType, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 40).addItemStack(new ItemStack(switch (crystalType) {
            case AMETHYST -> Blocks.AMETHYST_CLUSTER;
            case SPRINGALINE -> ECBlocks.SPRINGALINE_CLUSTER.get();
        }));
    }
}
