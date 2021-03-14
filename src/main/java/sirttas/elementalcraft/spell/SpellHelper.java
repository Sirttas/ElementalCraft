package sirttas.elementalcraft.spell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mojang.datafixers.util.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.nbt.NBTHelper;

public class SpellHelper {

	private SpellHelper() {}
	
	public static Spell getSpell(ItemStack stack) {
		return getSpellFromTag(NBTHelper.getECTag(stack));
	}

	public static void setSpell(ItemStack stack, Spell spell) {
		CompoundNBT nbt = NBTHelper.getOrCreateECTag(stack);

		nbt.putString(ECNames.SPELL, spell.getRegistryName().toString());
	}

	public static void removeSpell(ItemStack stack, Spell spell) {
		ListNBT list = getSpellList(stack);

		if (list != null && !list.isEmpty()) {
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
							setSpell(stack, Spells.NONE);
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

		if (list != null && !list.isEmpty()) {
			list.forEach(t -> {
				CompoundNBT tag = (CompoundNBT) t;

				consumer.accept(getSpellFromTag(tag), tag.getInt(ECNames.COUNT));
			});
		}
	}

	public static List<Pair<Spell, Integer>> getSpellsAsMap(ItemStack stack) {
		ListNBT list = getSpellList(stack);

		if (list != null && !list.isEmpty()) {
			List<Pair<Spell, Integer>> value = new ArrayList<>(list.size());

			list.forEach(t -> {
				CompoundNBT tag = (CompoundNBT) t;

				value.add(new Pair<>(getSpellFromTag(tag), tag.getInt(ECNames.COUNT)));
			});
			return value;
		}
		return Collections.emptyList();
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

		if (list != null && !list.isEmpty()) {
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

		if (list != null && !list.isEmpty()) {
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
		return Spells.NONE;
	}

	private static boolean isSpellInTag(INBT nbt, Spell spell) {
		if (nbt instanceof CompoundNBT && ((CompoundNBT) nbt).contains(ECNames.SPELL)) {
			return ((CompoundNBT) nbt).getString(ECNames.SPELL).equals(spell.getRegistryName().toString());
		}
		return false;
	}

	public static Spell randomSpell(Random rand) {
		return randomSpell(Spell.REGISTRY.getValues(), rand);
	}

	public static Spell randomSpell(ElementType type, Random rand) {
		return randomSpell(Spell.REGISTRY.getValues().stream().filter(spell -> spell.getElementType() == type && spell.isValid()).collect(Collectors.toList()), rand);
	}

	public static Spell randomSpell(Collection<Spell> spells, Random rand) {
		List<Spell> list = spells.stream().filter(Spell::isValid).collect(Collectors.toList());
		int roll = rand.nextInt(list.stream().mapToInt(Spell::getWeight).sum());
		
		for (Spell spell : spells) {
			roll -= spell.getWeight();
			if (roll < 0) {
				return spell;
			}
		}
		return list.get(list.size() - 1);
	}
}
