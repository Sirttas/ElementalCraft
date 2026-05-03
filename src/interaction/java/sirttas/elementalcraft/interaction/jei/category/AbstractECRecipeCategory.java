package sirttas.elementalcraft.interaction.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2f;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
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
	public void draw(@Nonnull T recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
		if (overlays.isEmpty()) {
			return;
		}
		for (Overlay<T> overlay : overlays) {
			if (overlay.condition.test(recipe)) {
				overlay.drawable.draw(guiGraphics, overlay.x, overlay.y);
			}
		}
	}

	protected void submitPictureInPicture(@NonNull GuiGraphicsExtractor guiGraphics, PictureInPictureFactory factory) {
		Vector2f start = guiGraphics.pose().transformPosition(new Vector2f(0, 0));
		Vector2f end = guiGraphics.pose().transformPosition(new Vector2f(this.width, this.height));

		int startX = Math.round(start.x);
		int startY = Math.round(start.y);
		int endX = Math.round(end.x);
		int endY = Math.round(end.y);
		guiGraphics.submitPictureInPictureRenderState(factory.create(startX, startY, endX, endY));
	}


	private record Overlay<T>(
			IDrawable drawable,
			int x,
			int y,
			Predicate<T> condition
	) { }

	@FunctionalInterface
	public interface PictureInPictureFactory {
		PictureInPictureRenderState create(int startX, int startY, int endX, int endY);
	}
}
