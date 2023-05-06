package sirttas.elementalcraft.block.instrument.io;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.instrument.io.IIOInstrumentRecipe;

public abstract class AbstractIOInstrumentBlockEntity<T extends IInstrument, R extends IIOInstrumentRecipe<T>> extends AbstractInstrumentBlockEntity<T, R> {

	protected AbstractIOInstrumentBlockEntity(Config<T, R> config, BlockPos pos, BlockState state) {
		super(config, pos, state);
		outputSlot = 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void assemble() {
		T self = (T) this;
		Container inv = getInventory();
		ItemStack in = inv.getItem(0);
		ItemStack result = inv.getItem(1);
		ItemStack craftingResult = recipe.assemble(self);
		int inputSize = recipe.getInputSize();
		int luck = recipe.getLuck(self);

		if (luck > 0 && recipe.getRand(self).nextInt(100) < luck) {
			craftingResult.grow(1);
		}

		var count = craftingResult.getCount();

		if (craftingResult.sameItem(result) && result.getCount() + count <= result.getMaxStackSize()) {
			in.shrink(inputSize);
			result.grow(count);
		} else if (result.isEmpty()) {
			in.shrink(inputSize);
			inv.setItem(1, craftingResult);
		}
		if (in.isEmpty()) {
			inv.removeItemNoUpdate(0);
		}
	}
	
}
