package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.INBTSerializable;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.container.IElementStorageBlocKEntity;
import sirttas.elementalcraft.container.IRuneableBlockEntity;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * IER = Inventory ElementStorage RuneHandler
 */
public abstract class AbstractIERBlockEntity extends AbstractECContainerBlockEntity implements IRuneableBlockEntity, IElementStorageBlocKEntity {

	protected AbstractIERBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void loadAdditional(@Nonnull ValueInput valueInput) {
		super.loadAdditional(compound, provider);
		IElementStorage elementStorage = getElementStorage();
		
		if (compound.contains(ECNames.ELEMENT_STORAGE) && elementStorage instanceof INBTSerializable) {
			((INBTSerializable<CompoundTag>) elementStorage).deserializeNBT(provider, compound.getCompound(ECNames.ELEMENT_STORAGE));
		}

        getRuneHandler().load(valueInput);
	}
	
	@Override
	public void saveAdditional(@Nonnull ValueOutput valueOutput) {
		super.saveAdditional(valueOutput);
		IElementStorage elementStorage = getElementStorage();
		
		if (elementStorage instanceof INBTSerializable<?> serializable) {
			compound.put(ECNames.ELEMENT_STORAGE, serializable.serializeNBT(provider));
		}

        getRuneHandler().save(valueOutput);
	}
}
