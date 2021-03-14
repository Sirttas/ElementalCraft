package sirttas.elementalcraft.recipe.instrument.io;

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.purifier.TilePurifier;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.rune.Rune.BonusType;

public class PurifierRecipe implements IIOInstrumentRecipe<TilePurifier> {

	private final ResourceLocation id;
	private final ItemStack result;
	protected Ingredient input;

	public PurifierRecipe(ItemStack ore) {
		ResourceLocation oreName = ore.getItem().getRegistryName();

		this.input = Ingredient.fromStacks(ore);
		this.id = ElementalCraft.createRL(oreName.getNamespace() + '_' + oreName.getPath() + "_to_pure_ore");
		result = ElementalCraft.PURE_ORE_MANAGER.createPureOre(ore.getItem()).copy();
		result.setCount(ECConfig.COMMON.pureOreMultiplier.get());
	}

	@Override
	public int getElementAmount() {
		return ECConfig.COMMON.purifierBaseCost.get();
	}

	@Override
	public boolean matches(ItemStack stack) {
		return ElementalCraft.PURE_ORE_MANAGER.isValidOre(stack) && input.test(stack);
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.from(Ingredient.EMPTY, this.input);
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result.copy();
	}

	@Override
	public IRecipeType<?> getType() {
		return null;
	}

	@Override
	public ElementType getElementType() {
		return ElementType.EARTH;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public int getLuck(TilePurifier instrument) {
		return (int) Math.round(instrument.getRuneHandler().getBonus(BonusType.LUCK) * ECConfig.COMMON.purifierLuckRatio.get());
	}

	@Override
	public Random getRand(TilePurifier instrument) {
		return instrument.getWorld().getRandom();
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return null;
	}
}
