package sirttas.elementalcraft.block.container;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;

import javax.annotation.Nonnull;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractElementContainerBlockEntity extends AbstractECBlockEntity implements IElementContainer {

	protected final SingleElementStorage elementStorage;

	protected AbstractElementContainerBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, BlockPos pos, BlockState state, Function<AbstractElementContainerBlockEntity, SingleElementStorage> elementStorage) {
		super(blockEntityType, pos, state);
		this.elementStorage = elementStorage.apply(this);
	}

	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.deserializeNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.serializeNBT());
	}

	@Override
	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

}
