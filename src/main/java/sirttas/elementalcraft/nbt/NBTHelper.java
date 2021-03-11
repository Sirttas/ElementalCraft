package sirttas.elementalcraft.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import sirttas.elementalcraft.api.name.ECNames;

public class NBTHelper {

	private NBTHelper() {}
	
	public static ItemStack readItemStack(CompoundNBT cmp, String tag) {
		if (cmp != null && cmp.contains(tag)) {
			return ItemStack.read(cmp.getCompound(tag));
		}
		return ItemStack.EMPTY;
	}

	public static CompoundNBT writeItemStack(CompoundNBT cmp, String tag, ItemStack stack) {
		if (cmp != null) {
			if (stack != null) {
				CompoundNBT stackNbt = new CompoundNBT();
				stack.write(stackNbt);
				cmp.put(tag, stackNbt);
				return stackNbt;
			} else if (cmp.contains(tag)) {
				cmp.remove(tag);
			}
		}
		return null;
	}

	public static CompoundNBT getECTag(ItemStack stack) {
		CompoundNBT nbt = stack.getTag();

		if (nbt == null || !nbt.contains(ECNames.EC_NBT)) {
			return null;
		}
		return nbt.getCompound(ECNames.EC_NBT);
	}

	public static CompoundNBT getOrCreateECTag(ItemStack stack) {
		CompoundNBT nbt = stack.getTag();

		if (nbt == null) {
			nbt = new CompoundNBT();
			stack.setTag(nbt);
		}
		if (!nbt.contains(ECNames.EC_NBT)) {
			nbt.put(ECNames.EC_NBT, new CompoundNBT());
		}
		return nbt.getCompound(ECNames.EC_NBT);
	}

}
