package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;

import javax.annotation.Nonnull;

public class BinderBlockEntity extends AbstractInstrumentBlockEntity<IBinder, AbstractBindingRecipe> implements IBinder {

	private static final Config<IBinder, AbstractBindingRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.BINDER,
			ECRecipeTypes.BINDING,
			ECConfig.COMMON.binderTransferSpeed,
			ECConfig.COMMON.binderMaxRunes,
			0,
			true,
			true
	);

	private final InstrumentContainer inventory;

	public BinderBlockEntity(BlockPos pos, BlockState state) {
		this(CONFIG, pos, state);
	}

	protected BinderBlockEntity(Config<IBinder, AbstractBindingRecipe> config, BlockPos pos, BlockState state) {
		super(config, pos, state);
		inventory = new InstrumentContainer(this::setChanged, 20);
		particleOffset = new Vec3(0, 0.2, 0);
	}

	@Override
	public int getItemCount() {
		return inventory.getItemCount();
	}
	
	@Nonnull
	@Override
	public Container getInventory() {
		return inventory;
	}

	@Override
	protected void assemble() {
		var remainingItem = recipe.getRemainingItems(getContainerWrapper()).get(0);

		super.assemble();
		if (!remainingItem.isEmpty()) {
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				if (inventory.getItem(i).isEmpty()) {
					inventory.setItem(i, remainingItem);
					break;
				}
			}
		}
	}
}
