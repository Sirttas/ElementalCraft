package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.container.IContainerBlockEntity;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class AbstractECContainerBlockEntity extends AbstractECBlockEntity implements Clearable, IContainerBlockEntity {

	protected AbstractECContainerBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		Container inv = getInventory();

		if (inv instanceof INBTSerializable nbtInv && compound.contains(ECNames.INVENTORY)) {
			nbtInv.deserializeNBT(compound.get(ECNames.INVENTORY));
		}
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		Container inv = getInventory();

		if (inv instanceof INBTSerializable nbtInv) {
			compound.put(ECNames.INVENTORY, nbtInv.serializeNBT());
		}
	}
}
