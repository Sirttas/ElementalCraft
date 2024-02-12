package sirttas.elementalcraft.interaction.jei.category.instrument;

import com.google.common.collect.Lists;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.enchantment.liquefier.EnchantmentLiquefierBlockEntity;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.enchantment.liquefaction.EnchantmentLiquefactionRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnchantmentLiquefactionRecipeCategory extends AbstractInstrumentRecipeCategory<EnchantmentLiquefierBlockEntity, EnchantmentLiquefactionRecipeCategory.RecipeWrapper> {

	private static final ItemStack ENCHANTMENT_LIQUEFIER = new ItemStack(ECBlocks.ENCHANTMENT_LIQUEFIER.get());

	public EnchantmentLiquefactionRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.enchantment_liquefaction", createDrawableStack(guiHelper, ENCHANTMENT_LIQUEFIER), guiHelper.createBlankDrawable(86, 100));
		setOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/enchantment_liquefaction.png"), 0, 0, 46, 13), 20, 20);
	}

	@Nonnull
	@Override
	public RecipeType<EnchantmentLiquefactionRecipeCategory.RecipeWrapper> getRecipeType() {
		return ECJEIRecipeTypes.ENCHANTMENT_LIQUEFACTION;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull EnchantmentLiquefactionRecipeCategory.RecipeWrapper recipe, @Nonnull IFocusGroup focuses) {
		var stacks = recipe.stacks;
		var enchantmentStacks = recipe.enchantmentStacks;

		builder.createFocusLink(
				builder.addSlot(RecipeIngredientRole.INPUT, 17, 4)
						.addItemStacks(enchantmentStacks),
				builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 54, 4)
						.addItemStacks(stacks)
		);
		builder.createFocusLink(
				builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 17, 33)
						.addItemStacks(Lists.reverse(stacks)),
				builder.addSlot(RecipeIngredientRole.OUTPUT, 54, 33)
						.addItemStacks(Lists.reverse(enchantmentStacks))
		);

		builder.addSlot(RecipeIngredientRole.CATALYST, 35, 42)
				.addItemStack(ENCHANTMENT_LIQUEFIER);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 35, 58)
				.addItemStack(container);

		builder.addSlot(RecipeIngredientRole.INPUT, 36, 76)
				.addIngredients(ECIngredientTypes.ELEMENT, getElementTypeIngredients(recipe));
	}

	public static class RecipeWrapper implements IInstrumentRecipe<EnchantmentLiquefierBlockEntity> {

		private final EnchantmentLiquefactionRecipe recipe;
		private final List<ItemStack> stacks;
		private final List<ItemStack> enchantmentStacks;

        public RecipeWrapper(EnchantmentLiquefactionRecipe recipe, int level, List<ItemStack> stacks) {
            this.recipe = recipe;
			this.stacks = Util.make(() -> {
				var list = new ArrayList<ItemStack>(stacks.size() + 1);

				list.add(new ItemStack(Items.BOOK));
				list.addAll(stacks);
				return list;
			});
			this.enchantmentStacks = Util.make(() -> {
				var map = Map.of(recipe.getEnchantment(), level);
				var list = new ArrayList<ItemStack>(stacks.size() + 1);
				var book = new ItemStack(Items.ENCHANTED_BOOK);

				EnchantmentHelper.setEnchantments(map, book);
				list.add(book);
				stacks.stream()
						.map(s -> {
							var copy = s.copy();
							EnchantmentHelper.setEnchantments(map, copy);
							return copy;
						}).forEach(list::add);
				return list;
			});
        }

        @Override
		public boolean matches(EnchantmentLiquefierBlockEntity inv, @NotNull Level level) {
			return recipe.matches(inv, level);
		}

		@Override
		public List<ElementType> getValidElementTypes() {
			return recipe.getValidElementTypes();
		}

		@Override
		public int getElementAmount() {
			return recipe.getElementAmount();
		}

		@Override
		public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
			return recipe.getResultItem(registryAccess);
		}

		@Override
		public @NotNull RecipeSerializer<?> getSerializer() {
			return recipe.getSerializer();
		}

		@Override
		public net.minecraft.world.item.crafting.@NotNull RecipeType<?> getType() {
			return recipe.getType();
		}
	}
}
