package sirttas.elementalcraft.interaction.jei.category.element;

import com.google.common.collect.Lists;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class ExtractionRecipeCategory extends AbstractECRecipeCategory<ElementType> {

	public static final String NAME = "extraction";

	private final int amount;
	private final ItemStack extractor;
	protected final List<ItemStack> tanks;

	public ExtractionRecipeCategory(IGuiHelper guiHelper) {
		this(guiHelper, "elementalcraft.jei.extraction", new ItemStack(ECBlocks.EXTRACTOR.get()), Lists.newArrayList(new ItemStack(ECBlocks.CONTAINER.get()), new ItemStack(ECBlocks.SMALL_CONTAINER.get())), 1);
	}

	protected ExtractionRecipeCategory(IGuiHelper guiHelper, String translationKey, ItemStack extractor, List<ItemStack> tanks, int amount) {
		super(translationKey, createDrawableStack(guiHelper, extractor), guiHelper.createBlankDrawable(64, 48));
		this.extractor = extractor;
		this.tanks = tanks;
		this.amount = amount;
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/extraction.png"), 0, 0, 45, 44), 0, 0);
	}

	@Nonnull
	@Override
	public RecipeType<ElementType> getRecipeType() {
		return ECJEIRecipeTypes.EXTRACTION;
	}

	@Nonnull
    @Override
	public List<Component> getTooltipStrings(@Nonnull ElementType recipe, @Nonnull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (mouseX > 0 && mouseX < 16 && mouseY > 0 && mouseY < 16) {
			return Lists.newArrayList(Component.translatable("block.elementalcraft.source." + recipe.getSerializedName()));
		}
		return Collections.emptyList();
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ElementType type, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.CATALYST, 0, 16)
				.addItemStack(extractor);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 0, 32)
				.addItemStacks(tanks);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 47, 32)
				.addIngredient(ECIngredientTypes.ELEMENT, new IngredientElementType(type, amount));
	}
}
