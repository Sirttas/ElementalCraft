package sirttas.elementalcraft.recipe.instrument.io;

import java.util.Random;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

public class PurifierRecipe implements IIOInstrumentRecipe<PurifierBlockEntity> {

	private final ResourceLocation id;
	private final ItemStack result;
	protected Ingredient input;

	public PurifierRecipe(ItemStack ore) {
		ResourceLocation oreName = ore.getItem().getRegistryName();

		this.input = Ingredient.of(ore);
		this.id = ElementalCraft.createRL(oreName.getNamespace() + '_' + oreName.getPath() + "_to_pure_ore");
		result = ElementalCraft.PURE_ORE_MANAGER.createPureOreFor(ore).copy();
		result.setCount(ECConfig.COMMON.pureOreMultiplier.get());
	}

	@Override
	public int getElementAmount() {
		return ECConfig.COMMON.purifierBaseCost.get();
	}

	@Override
	public boolean matches(ItemStack stack) {
		return !result.isEmpty() && input.test(stack);
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, this.input);
	}

	@Override
	public ItemStack getResultItem() {
		return result;
	}

	@Override
	public RecipeType<?> getType() {
		return null;
	}

	@Override
	public ElementType getElementType() {
		return ElementType.EARTH;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public int getLuck(PurifierBlockEntity instrument) {
		return (int) Math.round(instrument.getRuneHandler().getBonus(BonusType.LUCK) * ECConfig.COMMON.purifierLuckRatio.get());
	}

	@Override
	public Random getRand(PurifierBlockEntity instrument) {
		return instrument.getLevel().getRandom();
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return null;
	}
}
