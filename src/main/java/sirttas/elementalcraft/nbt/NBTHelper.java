package sirttas.elementalcraft.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.name.ECNames;

import javax.annotation.Nonnull;

public class NBTHelper {

	private NBTHelper() {}
	
	public static ItemStack readItemStack(CompoundTag cmp, String tag) {
		if (cmp != null && cmp.contains(tag)) {
			return ItemStack.of(cmp.getCompound(tag));
		}
		return ItemStack.EMPTY;
	}

	public static CompoundTag writeItemStack(CompoundTag cmp, String tag, ItemStack stack) {
		if (cmp != null) {
			if (stack != null) {
				CompoundTag stackNbt = new CompoundTag();
				stack.save(stackNbt);
				cmp.put(tag, stackNbt);
				return stackNbt;
			} else if (cmp.contains(tag)) {
				cmp.remove(tag);
			}
		}
		return null;
	}

	public static CompoundTag getECTag(ItemStack stack) {
		CompoundTag nbt = stack.getTag();

		if (nbt == null || !nbt.contains(ECNames.EC_NBT)) {
			return null;
		}
		return nbt.getCompound(ECNames.EC_NBT);
	}

	public static CompoundTag getOrCreateECTag(ItemStack stack) {
		CompoundTag nbt = stack.getTag();

		if (nbt == null) {
			nbt = new CompoundTag();
			stack.setTag(nbt);
		}
		if (!nbt.contains(ECNames.EC_NBT)) {
			nbt.put(ECNames.EC_NBT, new CompoundTag());
		}
		return nbt.getCompound(ECNames.EC_NBT);
	}

	public static CompoundTag getOrCreate(@Nonnull CompoundTag cmp, String tag) {
		if (cmp.contains(tag)) {
			return cmp.getCompound(tag);
		}
		cmp.put(tag, new CompoundTag());
		return cmp.getCompound(tag);
	}

}
