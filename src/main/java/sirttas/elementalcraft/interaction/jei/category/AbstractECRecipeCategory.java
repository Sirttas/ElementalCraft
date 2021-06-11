package sirttas.elementalcraft.interaction.jei.category;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;

public abstract class AbstractECRecipeCategory<T> implements IRecipeCategory<T> {

	private final String translationKey;
	private final IDrawable icon;
	private final IDrawable background;
	
	private IDrawable overlay;
	private int overlayX;
	private int overlayY;
	
	protected AbstractECRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
		this.background = background;
		this.icon = icon;
		this.translationKey = translationKey;
	}
	
	@Override
	public String getTitle() {
		return I18n.get(translationKey);
	}
	@Override
	public IDrawable getIcon() {
		return icon;
	}
	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	public void setOverlay(IDrawable overlay, int x, int y) {
		this.overlay = overlay;
		overlayX = x;
		overlayY = y;
	}

	@Override
	public void draw(T recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		if (overlay != null) {
			RenderSystem.enableBlend();
			overlay.draw(matrixStack, overlayX, overlayY);
			RenderSystem.disableBlend();
		}
	}
}
