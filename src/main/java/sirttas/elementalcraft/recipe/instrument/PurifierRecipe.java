package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.inventory.IInventory;
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

public class PurifierRecipe implements IInstrumentRecipe<TilePurifier> {

	private final ResourceLocation id;
	private final ItemStack ore;
	protected Ingredient input;

	public PurifierRecipe(ItemStack ore) {
		ResourceLocation oreName = ore.getItem().getRegistryName();

		this.ore = ore;
		this.input = Ingredient.fromStacks(ore);
		this.id = ElementalCraft.createRL(oreName.getNamespace() + '_' + oreName.getPath() + "_to_pure_ore");
	}

	@Override
	public int getElementPerTick() {
		return ECConfig.COMMON.purifierConsumeAmount.get();
	}

	@Override
	public int getDuration() {
		return ECConfig.COMMON.purifierDuration.get();
	}

	@Override
	public boolean matches(TilePurifier inv) {
		ItemStack stack = inv.getInventory().getStackInSlot(0);

		return inv.getTankElementType() == ElementType.EARTH && ElementalCraft.PURE_ORE_MANAGER.isValidOre(stack) && input.test(stack);
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.from(Ingredient.EMPTY, this.input);
	}

	@Override
	public ItemStack getRecipeOutput() {
		ItemStack result = ElementalCraft.PURE_ORE_MANAGER.createPureOre(ore.getItem()).copy();

		result.setCount(ECConfig.COMMON.pureOreMultiplier.get());
		return result;
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
	public void process(TilePurifier instrument) {
		IInventory inv = instrument.getInventory();
		ItemStack in = inv.getStackInSlot(0);
		ItemStack output = inv.getStackInSlot(1);
		ItemStack result = getCraftingResult(instrument);

		if (result.isItemEqual(output) && output.getCount() + result.getCount() <= output.getMaxStackSize()) {
			in.shrink(1);
			output.grow(result.getCount());
		} else if (output.isEmpty()) {
			in.shrink(1);
			inv.setInventorySlotContents(1, result.copy());
		}
		if (in.isEmpty()) {
			inv.removeStackFromSlot(0);
		}
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return null;
	}
}
