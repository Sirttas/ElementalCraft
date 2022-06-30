package sirttas.elementalcraft.interaction.jei.category;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

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

	protected static IDrawable createDrawableStack(IGuiHelper guiHelper, ItemStack stack) {
		return guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, stack);
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

	@Nonnull
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
	public void draw(@Nonnull T recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull PoseStack matrixStack, double mouseX, double mouseY) {
		if (overlay != null) {
			RenderSystem.enableBlend();
			overlay.draw(matrixStack, overlayX, overlayY);
			RenderSystem.disableBlend();
		}
	}
}
