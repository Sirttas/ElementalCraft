package sirttas.elementalcraft.spell;

import java.util.function.ObjIntConsumer;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.nbt.NBTHelper;

public class SpellHelper {

	public static Spell getSpell(ItemStack stack) {
		return getSpellFromTag(NBTHelper.getECTag(stack));
	}

	public static void setSpell(ItemStack stack, Spell spell) {
		CompoundNBT nbt = NBTHelper.getOrCreateECTag(stack);

		nbt.putString(ECNames.SPELL, spell.getRegistryName().toString());
	}

	public static void removeSpell(ItemStack stack, Spell spell) {
		ListNBT list = getSpellList(stack);

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				CompoundNBT tag = (CompoundNBT) list.get(i);

				if (tag.getString(ECNames.SPELL).equals(spell.getRegistryName().toString())) {
					int count = tag.getInt(ECNames.COUNT);

					if (count > 1) {
						tag.putInt(ECNames.COUNT, count - 1);
					} else {
						moveSelected(stack, -1);
						list.remove(i);
						if (list.isEmpty()) {
							setSpell(stack, Spells.none);
						}
					}
					return;
				}
			}
		}
	}

	public static ListNBT getOrCreateSpellList(ItemStack stack) {
		CompoundNBT nbt = NBTHelper.getOrCreateECTag(stack);
		ListNBT list = nbt.getList(ECNames.SPELL_LIST, 10);

		if (!nbt.contains(ECNames.SPELL_LIST)) {
			nbt.put(ECNames.SPELL_LIST, list);
		}
		return list;
	}

	public static void forEachSpell(ItemStack stack, ObjIntConsumer<Spell> consumer) {
		ListNBT list = getSpellList(stack);

		if (list != null) {
			list.forEach(t -> {
				CompoundNBT tag = (CompoundNBT) t;

				consumer.accept(getSpellFromTag(tag), tag.getInt(ECNames.COUNT));
			});
		}
	}

	public static ListNBT getSpellList(ItemStack stack) {
		CompoundNBT nbt = NBTHelper.getECTag(stack);

		if (nbt != null && nbt.contains(ECNames.SPELL_LIST)) {
			return nbt.getList(ECNames.SPELL_LIST, 10);
		}
		return null;
	}

	public static int getSpellCount(ItemStack stack) {
		ListNBT list = getSpellList(stack);

		if (list != null) {
			return list.stream().mapToInt(t -> ((CompoundNBT) t).getInt(ECNames.COUNT)).sum();
		}
		return 0;
	}

	public static void addSpell(ItemStack stack, Spell spell) {
		ListNBT list = getOrCreateSpellList(stack);

		for (int i = 0; i < list.size(); i++) {
			CompoundNBT tag = (CompoundNBT) list.get(i);

			if (isSpellInTag(tag, spell)) {
				tag.putInt(ECNames.COUNT, tag.getInt(ECNames.COUNT) + 1);
				return;
			}
		}
		CompoundNBT tag = new CompoundNBT();

		tag.putString(ECNames.SPELL, spell.getRegistryName().toString());
		tag.putInt(ECNames.COUNT, 1);
		list.add(tag);
		if (list.size() == 1) {
			setSpell(stack, spell);
		}
	}

	public static void moveSelected(ItemStack stack, int i) {
		ListNBT list = getSpellList(stack);
		Spell spell = getSpell(stack);

		if (list != null) {
			int selected = IntStream.range(0, list.size()).filter(j -> isSpellInTag(list.get(j), spell)).findFirst().orElse(0) + i;
			
			if (selected < 0) {
				selected = list.size() - 1;
			} else if (selected >= list.size()) {
				selected = 0;
			}
			setSpell(stack, getSpellFromTag(list.get(selected)));
		}
	}

	private static Spell getSpellFromTag(INBT nbt) {
		if (nbt instanceof CompoundNBT) {
			return Spell.REGISTRY.getValue(new ResourceLocation(((CompoundNBT) nbt).getString(ECNames.SPELL)));
		}
		return Spells.none;
	}

	private static boolean isSpellInTag(INBT nbt, Spell spell) {
		if (nbt instanceof CompoundNBT && ((CompoundNBT) nbt).contains(ECNames.SPELL)) {
			return ((CompoundNBT) nbt).getString(ECNames.SPELL).equals(spell.getRegistryName().toString());
		}
		return false;
	}
}
