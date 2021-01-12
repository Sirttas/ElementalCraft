package sirttas.elementalcraft.api.element.storage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.tile.TileEntityHelper;

public class CapabilityElementStorage {
	@CapabilityInject(IElementStorage.class) public static final Capability<IElementStorage> ELEMENT_STORAGE_CAPABILITY = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(IElementStorage.class, new Capability.IStorage<IElementStorage>() {
			@Override
			public INBT writeNBT(Capability<IElementStorage> capability, IElementStorage instance, Direction side) {
				CompoundNBT compound = new CompoundNBT();

				compound.putString(ECNames.ELEMENT_TYPE, instance.getElementType().getString());
				compound.putInt(ECNames.ELEMENT_AMOUNT, instance.getElementAmount());
				compound.putInt(ECNames.ELEMENT_CAPACITY, instance.getElementCapacity());
				return compound;
			}

			@Override
			public void readNBT(Capability<IElementStorage> capability, IElementStorage instance, Direction side, INBT base) {
				if (instance instanceof ElementStorage && base instanceof CompoundNBT) {
					ElementStorage storage = (ElementStorage) instance;
					CompoundNBT compound = (CompoundNBT) base;

					storage.elementType = ElementType.byName(compound.getString(ECNames.ELEMENT_TYPE));
					storage.elementAmount = compound.getInt(ECNames.ELEMENT_AMOUNT);
					storage.elementCapacity = compound.getInt(ECNames.ELEMENT_CAPACITY);
				} else {
					throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
				}
			}
		}, () -> new ElementStorage(100000));
	}

	public static IElementStorage getElementStorageAt(IWorldReader world, BlockPos pos) {
		return TileEntityHelper.getTileEntity(world, pos).map(t -> get(t).orElse(EmptyElementStorage.INSTANCE)).orElse(EmptyElementStorage.INSTANCE);
	}

	public static LazyOptional<IElementStorage> get(TileEntity tileEntity) {
		return tileEntity.getCapability(ELEMENT_STORAGE_CAPABILITY);
	}
}
