package sirttas.elementalcraft.block.container;

import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;

public abstract class AbstractElementContainerBlockEntity extends AbstractECBlockEntity implements IElementContainer {

	protected final SingleElementStorage elementStorage;

	protected AbstractElementContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, Function<Runnable, SingleElementStorage> elementStorage) {
		super(blockEntityType, pos, state);
		this.elementStorage = elementStorage.apply(this::setChanged);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.deserializeNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.serializeNBT());
		return compound;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY) {
			return LazyOptional.of(elementStorage != null ? () -> elementStorage : null).cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

}