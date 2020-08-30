package sirttas.elementalcraft.spell;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.nbt.NBTHelper;

public class SpellHelper {

	public static Spell getSpell(ItemStack stack) {
		CompoundNBT nbt = NBTHelper.getECTag(stack);

		if (nbt != null && nbt.contains(ECNames.SPELL)) {
			return Spell.REGISTRY.getValue(new ResourceLocation(nbt.getString(ECNames.SPELL)));
		}
		return null;
	}

	public static void setSpell(ItemStack stack, Spell spell) {
		CompoundNBT nbt = NBTHelper.getOrCreateECTag(stack);

		nbt.putString(ECNames.SPELL, spell.getRegistryName().toString());
	}
}
