package sirttas.elementalcraft.interaction.jei.category.shrine;

import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.jspecify.annotations.NonNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrinePlateModelResolver;
import sirttas.elementalcraft.client.model.ECModelResolver;
import sirttas.elementalcraft.client.renderer.pip.GuiBlockRenderState;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import vazkii.patchouli.client.multiblock.MultiblockPiPRenderState;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuddingShrineRecipeCategory extends AbstractECRecipeCategory<BuddingShrineBudType> {

    private static final int WIDTH = 99;
    private static final int HEIGHT = 99;

    private final ITickTimer timer;
    private final BlockState shrineState;
    private final Map<ResourceKey<@NotNull ShrineUpgrade>, BlockState> upgradeStates;
    private final BlockModelResolver blockModelResolver;
    private final BuddingShrinePlateModelResolver buddingShrinePlateModelResolver;

    public BuddingShrineRecipeCategory(IGuiHelper guiHelper) {
        super("elementalcraft.jei.buddingshrine", createDrawableStack(guiHelper, new ItemStack(ECBlocks.BUDDING_SHRINE.get())), 110, 66);
        timer = guiHelper.createTickTimer(100, 4, false);
        shrineState = ECBlocks.BUDDING_SHRINE.get().defaultBlockState();
        blockModelResolver = Minecraft.getInstance().getBlockModelResolver();
        upgradeStates = new HashMap<>();
        addOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/extraction.png"), 0, 0, 24, 9), 61, 44);

        buddingShrinePlateModelResolver = ECModelResolver.get(BuddingShrinePlateModelResolver.IDENTIFIER);
    }

    @Nonnull
    @Override
    public IRecipeType<@NotNull BuddingShrineBudType> getRecipeType() {
        return ECJEIRecipeTypes.BUDDING_SHRINE;
    }

    private BlockState getGrowthCrystal(BuddingShrineBudType type) {
        var t = timer.getValue();

        if (t < type.sequence().size()) {
            return type.sequence().get(t).defaultBlockState();
        }
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public void draw(@Nonnull BuddingShrineBudType budType, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        renderShrine(guiGraphics);

        /*render3D(guiGraphics, (p, b) -> {
            p.translate(0, 0.5, 0);
            setupPose(p);
            ECRendererHelper.renderBlock(shrineState, p, b);
            ECRendererHelper.renderModel(buddingShrinePlateModelResolver.getModel(budType), p, b, shrineState, 15728880, OverlayTexture.NO_OVERLAY);

            var upgradeState = getUpgradeState(budType);

            if (!upgradeState.isAir()) {
                p.pushPose();
                p.translate(0, 0, -1);
                ECRendererHelper.renderBlock(upgradeState, p, b);
                p.popPose();
            }
            p.translate(0, 1, 0);
            ECRendererHelper.renderBlock(getGrowthCrystal(budType), p, b);
        });*/
        super.draw(budType, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    private void renderShrine(@NonNull GuiGraphicsExtractor guiGraphics) {
        var blockModelRenderState = new BlockModelRenderState();

        blockModelResolver.update(blockModelRenderState, shrineState, BlockDisplayContext.create());

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(-49, 11);
        guiGraphics.enableScissor(0, 0, WIDTH, HEIGHT);

        final float offsetX = (WIDTH / 2f);
        final float offsetY = (HEIGHT / 2f);
        guiGraphics.pose().translate(offsetX, offsetY);

        renderBlock(guiGraphics, -offsetX, -offsetY, blockModelRenderState);
        guiGraphics.disableScissor();
        guiGraphics.pose().popMatrix();
    }

    private static void renderBlock(@NonNull GuiGraphicsExtractor guiGraphics, float x, float y, BlockModelRenderState blockModelRenderState) {
        Vector2f start = guiGraphics.pose().transformPosition(new Vector2f(x, y));
        Vector2f end = guiGraphics.pose().transformPosition(new Vector2f(WIDTH, HEIGHT));

        int startX = Math.round(start.x);
        int startY = Math.round(start.y);
        int endX = Math.round(end.x);
        int endY = Math.round(end.y);

        guiGraphics.submitPictureInPictureRenderState(new GuiBlockRenderState(blockModelRenderState, startX, startY, endX, endY, 1, guiGraphics.peekScissorStack()));
        guiGraphics.submitPictureInPictureRenderState(new MultiblockPiPRenderState(startX, startY, endX, endY, 1, guiGraphics.peekScissorStack(), new Matrix4f(), List.of(new MultiblockPiPRenderState.BlockRenderState(BlockPos.ZERO, blockModelRenderState))));
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull BuddingShrineBudType budType, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX < 34 || mouseX > 50 || mouseY < 40 || mouseY > 56) {
            return;
        }

        var upgradeStack = getUpgradeStack(budType);

        if (upgradeStack.isEmpty()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);

        tooltip.addAll(upgradeStack.getTooltipLines(tooltipContext, player, TooltipFlag.Default.NORMAL));
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull BuddingShrineBudType budType, @Nonnull IFocusGroup focuses) {
        var upgradeStack = getUpgradeStack(budType);

        if (!upgradeStack.isEmpty()) {
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).add(upgradeStack);
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 40).add(new ItemStack(budType.sequence().getLast()));
    }

    private ItemStack getUpgradeStack(BuddingShrineBudType type) {
        var upgradeState = getUpgradeState(type);

        if (upgradeState.isAir()) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(upgradeState.getBlock());
    }

    private BlockState getUpgradeState(BuddingShrineBudType type) {
        var upgrade = type.requiredUpgrade();

        if (upgrade.isEmpty()) {
            return Blocks.AIR.defaultBlockState();
        }
        return upgrade.get().unwrap().map(k -> upgradeStates.computeIfAbsent(k, k2 -> {
            var state = BuiltInRegistries.BLOCK.get(k.identifier()).get().value().defaultBlockState();

            if (state.isAir()) {
                return Blocks.AIR.defaultBlockState();
            } else if (state.hasProperty(BlockStateProperties.FACING)) {
                return state.setValue(BlockStateProperties.FACING, Direction.SOUTH);
            } else if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                return state.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
            }
            return state;
        }), u -> Blocks.AIR.defaultBlockState());
    }
}
