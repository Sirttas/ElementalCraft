package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.container.IContainerBlockEntity;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class AbstractECContainerBlockEntity extends AbstractECBlockEntity implements IContainerBlockEntity {

	protected AbstractECContainerBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	@Override
	protected void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		Container inv = getInventory();

		if (inv instanceof INBTSerializable nbtInv && compound.contains(ECNames.INVENTORY)) {
			nbtInv.deserializeNBT(provider, compound.get(ECNames.INVENTORY));
		}
	}

	@Override
	protected void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		Container inv = getInventory();

		if (inv instanceof INBTSerializable<?> nbtInv) {
			compound.put(ECNames.INVENTORY, nbtInv.serializeNBT(provider));
		}
	}
}
