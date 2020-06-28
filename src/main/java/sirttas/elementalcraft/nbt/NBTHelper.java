package sirttas.elementalcraft.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class NBTHelper {

	public static ItemStack readItemStack(CompoundNBT cmp, String tag) {
		if (cmp.contains(tag)) {
			return ItemStack.read(cmp.getCompound(tag));
		}
		return ItemStack.EMPTY;
	}

	public static CompoundNBT writeItemStack(CompoundNBT cmp, String tag, ItemStack stack) {
		if (stack != null) {
			CompoundNBT stackNbt = new CompoundNBT();
			stack.write(stackNbt);
			cmp.put(tag, stackNbt);
			return stackNbt;
		} else if (cmp.contains(tag)) {
			cmp.remove(tag);
		}
		return null;
	}

	public static CompoundNBT getECTag(ItemStack stack) {
		CompoundNBT nbt = stack.getTag();

		if (nbt == null) {
			nbt = new CompoundNBT();
			stack.setTag(nbt);
		}
		if (!nbt.contains(ECNBTTags.EC_NBT)) {
			nbt.put(ECNBTTags.EC_NBT, new CompoundNBT());
		}
		return nbt.getCompound(ECNBTTags.EC_NBT);
	}

}
