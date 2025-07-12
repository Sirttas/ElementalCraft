package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
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
	public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		IElementStorage elementStorage = getElementStorage();
		
		if (compound.contains(ECNames.ELEMENT_STORAGE) && elementStorage instanceof INBTSerializable) {
			((INBTSerializable<CompoundTag>) elementStorage).deserializeNBT(provider, compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(getRuneHandler(), compound.getList(ECNames.RUNE_HANDLER, Tag.OBJECT_HEADER));
		}
	}
	
	@Override
	public void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		IElementStorage elementStorage = getElementStorage();
		
		if (elementStorage instanceof INBTSerializable<?> serializable) {
			compound.put(ECNames.ELEMENT_STORAGE, serializable.serializeNBT(provider));
		}
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(getRuneHandler()));
	}
}
