package sirttas.elementalcraft.spell;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.nbt.ECNBTTags;
import sirttas.elementalcraft.nbt.NBTHelper;

public class SpellHelper {

	public static Spell getSpell(ItemStack stack) {
		CompoundNBT nbt = NBTHelper.getECTag(stack);

		if (nbt.contains(ECNBTTags.SPELL)) {
			return Spell.REGISTRY.getValue(new ResourceLocation(nbt.getString(ECNBTTags.SPELL)));
		}
		return null;
	}

	public static void setSpell(ItemStack stack, Spell spell) {
		CompoundNBT nbt = NBTHelper.getECTag(stack);

		nbt.putString(ECNBTTags.SPELL, spell.getRegistryName().toString());
	}
}
