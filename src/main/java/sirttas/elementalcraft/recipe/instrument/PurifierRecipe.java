package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.purifier.TilePurifier;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.rune.Rune.BonusType;

public class PurifierRecipe implements IInstrumentRecipe<TilePurifier> {

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
	public boolean matches(TilePurifier tile) {
		return tile.getItemHandler().map(inv -> {
			ItemStack stack = inv.getStackInSlot(0);
			ItemStack output = inv.getStackInSlot(1);

			return tile.getTankElementType() == ElementType.EARTH && ElementalCraft.PURE_ORE_MANAGER.isValidOre(stack) && input.test(stack)
					&& (output.isEmpty() || (ItemHandlerHelper.canItemStacksStack(output, result) && output.getCount() + result.getCount() <= inv.getSlotLimit(1)));
		}).orElse(false);
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
	public void process(TilePurifier instrument) {
		IInventory inv = instrument.getInventory();
		ItemStack in = inv.getStackInSlot(0);
		ItemStack output = inv.getStackInSlot(1);
		ItemStack craftingResult = getCraftingResult(instrument);
		int luck = (int) (instrument.getRuneHandler().getBonus(BonusType.LUCK) * ECConfig.COMMON.purifierLuckRatio.get());

		if (craftingResult.isItemEqual(output) && output.getCount() + craftingResult.getCount() <= output.getMaxStackSize()) {
			in.shrink(1);
			output.grow(craftingResult.getCount());
		} else if (output.isEmpty()) {
			in.shrink(1);
			inv.setInventorySlotContents(1, craftingResult.copy());
		}
		if (luck > 0 && instrument.getWorld().rand.nextInt(100) < luck) {
			output.grow(1);
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
