package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * IER = Inventory ElementStorage RuneHandler
 */
public abstract class AbstractIERBlockEntity extends AbstractECContainerBlockEntity {

	protected AbstractIERBlockEntity(RegistryObject<? extends BlockEntityType<?>> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}
	
	public abstract IElementStorage getElementStorage();
	public abstract IRuneHandler getRuneHandler();
	
	@SuppressWarnings("unchecked")
	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		IElementStorage elementStorage = getElementStorage();
		
		if (compound.contains(ECNames.ELEMENT_STORAGE) && elementStorage instanceof INBTSerializable) {
			((INBTSerializable<CompoundTag>) elementStorage).deserializeNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(getRuneHandler(), compound.getList(ECNames.RUNE_HANDLER, 8));
		}
	}
	
	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		IElementStorage elementStorage = getElementStorage();
		
		if (elementStorage instanceof INBTSerializable) {
			compound.put(ECNames.ELEMENT_STORAGE, ((INBTSerializable<?>) elementStorage).serializeNBT());
		}
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(getRuneHandler()));
	}
	
	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if (!this.remove) {
			if (cap == ElementalCraftCapabilities.ELEMENT_STORAGE) {
				IElementStorage elementStorage = getElementStorage();
				
				return LazyOptional.of(elementStorage != null ? () -> elementStorage : null).cast();
			} else if (cap == ElementalCraftCapabilities.RUNE_HANDLE) {
				IRuneHandler runeHandler = getRuneHandler();
				
				return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
			}
		}
		return super.getCapability(cap, side);
	}	
}
