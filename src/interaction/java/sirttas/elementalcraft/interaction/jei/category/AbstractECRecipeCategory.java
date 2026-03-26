package sirttas.elementalcraft.interaction.jei.category;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public abstract class AbstractECRecipeCategory<T> implements IRecipeCategory<T> {

	private final String translationKey;
	private final IDrawable icon;
	private final int width;
	private final int height;
	private final List<Overlay<T>> overlays;
	
	protected AbstractECRecipeCategory(String translationKey, IDrawable icon, int width, int height) {
		this.translationKey = translationKey;
		this.icon = icon;
		this.width = width;
		this.height = height;
		this.overlays = new ArrayList<>();
	}

	protected static IDrawable createDrawableStack(IGuiHelper guiHelper, ItemStack stack) {
		return guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, stack);
	}

	public static void render3D(@Nonnull GuiGraphics guiGraphics, BiConsumer<PoseStack, MultiBufferSource> render) {
		var poseStack = guiGraphics.pose();
		var modelViewStack = RenderSystem.getModelViewStack();

		modelViewStack.pushMatrix();
		modelViewStack.mul(poseStack.last().pose());
		modelViewStack.translate(0, 0, 1050);
		modelViewStack.scale(1.0F, 1.0F, -1.0F);
		RenderSystem.applyModelViewMatrix();

		var stack = new PoseStack();

		stack.translate(0, 0, 1000);
		stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		stack.scale(30,30, 30);
		Lighting.setupForEntityInInventory();

		var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

		RenderSystem.runAsFancy(() -> render.accept(stack, bufferSource));
		bufferSource.endBatch();
		modelViewStack.popMatrix();
		RenderSystem.applyModelViewMatrix();
		Lighting.setupFor3DItems();
	}

	protected static void setupPose(PoseStack p) {
		p.translate(-1.5, -2.3, 0);
		p.mulPose(Axis.XP.rotationDegrees(-30.0F));
		p.mulPose(Axis.YP.rotationDegrees(40.0F));
	}

	@Nonnull
    @Override
	public Component getTitle() {
		return Component.translatable(translationKey);
	}

	@Nonnull
    @Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	public void addOverlay(IDrawable overlay, int x, int y) {
		this.addOverlay(overlay, x, y, t -> true);
	}

	public void addOverlay(IDrawable overlay, int x, int y, Predicate<T> condition) {
		this.overlays.add(new Overlay<>(overlay, x, y, condition));
	}


	@Override
	public void draw(@Nonnull T recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
		if (overlays.isEmpty()) {
			return;
		}
		RenderSystem.enableBlend();
		for (Overlay<T> overlay : overlays) {
			if (overlay.condition.test(recipe)) {
				overlay.drawable.draw(guiGraphics, overlay.x, overlay.y);
			}
		}
		RenderSystem.disableBlend();
	}

	private record Overlay<T>(
			IDrawable drawable,
			int x,
			int y,
			Predicate<T> condition
	) {


	}
}
