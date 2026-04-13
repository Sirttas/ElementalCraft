package sirttas.elementalcraft.interaction.jei.category.element.synthesis;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

import javax.annotation.Nonnull;
import java.util.List;

public class VibrationRecipeCategory extends AbstractECRecipeCategory<IngredientElementType> {

	public static final String NAME = "vibration";

	private static final Identifier TEXTURE = ElementalCraftApi.createRL("textures/gui/overlay/vibration.png");

	private static final ItemStack VIBRATION_SYNTHESIZER = new ItemStack(ECBlocks.VIBRATION_SYNTHESIZER.get());
	private static final List<ItemStack> CONTAINERS = List.of(
			new ItemStack(ECBlocks.SMALL_CONTAINER.get()),
			new ItemStack(ECBlocks.CONTAINER.get()),
			new ItemStack(ECBlocks.AIR_RESERVOIR.get()));

	private final IGuiHelper guiHelper;

	public VibrationRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.vibration", createDrawableStack(guiHelper, VIBRATION_SYNTHESIZER), 61, 54);
		addOverlay(guiHelper.createDrawable(TEXTURE, 0, 0, 23, 9), 21, 9);
		this.guiHelper = guiHelper;
	}

	@Nonnull
	@Override
	public IRecipeType<@NotNull IngredientElementType> getRecipeType() {
		return ECJEIRecipeTypes.VIBRATION;
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder acceptor, @NotNull IngredientElementType recipe, @NotNull IFocusGroup focuses) {
		acceptor.addWidget(new VibrationWidget(guiHelper, new ScreenPosition(2, 3)));
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull IngredientElementType recipe, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 6, 19)
				.add(VIBRATION_SYNTHESIZER);
		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 6, 36)
				.addItemStacks(CONTAINERS);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 45, 6)
				.add(ECIngredientTypes.ELEMENT, recipe);
	}

	static class VibrationWidget implements IRecipeWidget {

		private final IDrawable[] steps;
		private final ScreenPosition position;

		private int ticks = 0;

		VibrationWidget(IGuiHelper guiHelper, ScreenPosition position) {
			steps = new IDrawable[] {
					guiHelper.createDrawable(TEXTURE, 108, 9, 18, 18),
					guiHelper.createDrawable(TEXTURE, 90, 9, 18, 18),
					guiHelper.createDrawable(TEXTURE, 72, 9, 18, 18),
					guiHelper.createDrawable(TEXTURE, 54, 9, 18, 18),
					guiHelper.createDrawable(TEXTURE, 36, 9, 18, 18),
					guiHelper.createDrawable(TEXTURE, 18, 9, 18, 18),
					guiHelper.createDrawable(TEXTURE, 0, 9, 18, 18)
			};
			this.position = position;
		}

		@Override
		@NotNull
		public ScreenPosition getPosition() {
			return position;
		}

		@Override
		public void drawWidget(@NotNull GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
			int stepIndex = ticks % steps.length;
			steps[stepIndex].draw(guiGraphics, 0, 0);
		}

		@Override
		public void tick() {
			ticks++;
			if (ticks >= 7) {
				ticks = 0;
			}
		}
	}


}
