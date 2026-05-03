package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.container.IContainerBlockEntity;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@Deprecated
public abstract class AbstractECContainerBlockEntity extends AbstractECBlockEntity implements IContainerBlockEntity {

	protected AbstractECContainerBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	@Override
	protected void loadAdditional(@Nonnull ValueInput valueInput) {
		super.loadAdditional(valueInput);
		Container inv = getInventory();

		if (inv instanceof ValueIOSerializable valueIOSerializable) {
			valueInput.readChild(ECNames.INVENTORY, valueIOSerializable);
		}
	}

	@Override
	protected void saveAdditional(@Nonnull ValueOutput valueOutput) {
		super.saveAdditional(valueOutput);
		Container inv = getInventory();

		if (inv instanceof ValueIOSerializable valueIOSerializable) {
			valueOutput.putChild(ECNames.INVENTORY, valueIOSerializable);
		}
	}
}
