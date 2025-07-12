package sirttas.elementalcraft.block.instrument.io;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.recipe.instrument.io.IOInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.io.IOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;

import java.util.function.Supplier;

public abstract class AbstractIOInstrumentBlockEntity<I extends IOInstrumentRecipeInput, R extends IOInstrumentRecipe<I>> extends AbstractInstrumentBlockEntity<I, R> {

	protected AbstractIOInstrumentBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, Holder<IConfigurableBlockEntityProperties> properties, BlockPos pos, BlockState state) {
		super(blockEntityType, properties, pos, state);
	}

	@NotNull
	protected SimpleIOInstrumentRecipeInput createSimpleIORecipeInput() {
		var inv = getInventory();
		var container = getContainer();

		return new SimpleIOInstrumentRecipeInput(
				inv.getItem(0),
				inv.getItem(1),
				level.getRandom(),
				container.getElementType(),
				container.getElementAmount(),
				getRuneHandler().getBonuses());
	}

	@Override
	public void assemble() {
		var input = createRecipeInput();
		var craftingResult = recipe.assemble(input, level.registryAccess());
		var inputSize = recipe.getInputSize();
		var luck = recipe.getLuck(input);

		if (luck > 0 && recipe.getRandomSource(input).nextInt(100) < luck) {
			craftingResult.grow(1);
		}

		var count = craftingResult.getCount();
		var inv = getInventory();
		var in = inv.getItem(0);
		var result = inv.getItem(1);

		if (ItemStack.isSameItem(craftingResult, result) && result.getCount() + count <= result.getMaxStackSize()) {
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
