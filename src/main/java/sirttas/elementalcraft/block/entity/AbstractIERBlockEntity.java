package sirttas.elementalcraft.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;

public abstract class AbstractIERBlockEntity extends AbstractECContainerBlockEntity {

	protected AbstractIERBlockEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}
	
	public abstract IElementStorage getElementStorage();
	public abstract IRuneHandler getRuneHandler();
	
	@SuppressWarnings("unchecked")
	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		IElementStorage elementStorage = getElementStorage();
		
		if (compound.contains(ECNames.ELEMENT_STORAGE) && elementStorage instanceof INBTSerializable) {
			((INBTSerializable<CompoundNBT>) elementStorage).deserializeNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(getRuneHandler(), compound.getList(ECNames.RUNE_HANDLER, 8));
		}
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		IElementStorage elementStorage = getElementStorage();
		
		if (elementStorage instanceof INBTSerializable) {
			compound.put(ECNames.ELEMENT_STORAGE, ((INBTSerializable<?>) elementStorage).serializeNBT());
		}
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(getRuneHandler()));
		return compound;
	}
	
	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.remove) {
			if (cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY) {
				IElementStorage elementStorage = getElementStorage();
				
				return LazyOptional.of(elementStorage != null ? () -> elementStorage : null).cast();
			} else if (cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
				IRuneHandler runeHandler = getRuneHandler();
				
				return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
			}
		}
		return super.getCapability(cap, side);
	}	
}
