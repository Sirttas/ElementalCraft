package sirttas.elementalcraft.spell;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.nbt.NBTHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SpellHelper {

	private SpellHelper() {}
	
	public static Spell getSpell(ItemStack stack) {
		return getSpellFromTag(NBTHelper.getECTag(stack));
	}

	public static void setSpell(ItemStack stack, Spell spell) {
		CompoundTag nbt = NBTHelper.getOrCreateECTag(stack);

		nbt.putString(ECNames.SPELL, spell.getRegistryName().toString());
	}

	public static void removeSpell(ItemStack stack, Spell spell) {
		ListTag list = getSpellList(stack);

		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				CompoundTag tag = (CompoundTag) list.get(i);

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

	public static ListTag getOrCreateSpellList(ItemStack stack) {
		CompoundTag nbt = NBTHelper.getOrCreateECTag(stack);
		ListTag list = nbt.getList(ECNames.SPELL_LIST, 10);

		if (!nbt.contains(ECNames.SPELL_LIST)) {
			nbt.put(ECNames.SPELL_LIST, list);
		}
		return list;
	}

	public static void forEachSpell(ItemStack stack, ObjIntConsumer<Spell> consumer) {
		ListTag list = getSpellList(stack);

		if (list != null && !list.isEmpty()) {
			list.forEach(t -> {
				CompoundTag tag = (CompoundTag) t;

				consumer.accept(getSpellFromTag(tag), tag.getInt(ECNames.COUNT));
			});
		}
	}

	public static List<Pair<Spell, Integer>> getSpellsAsMap(ItemStack stack) {
		ListTag list = getSpellList(stack);

		if (list != null && !list.isEmpty()) {
			List<Pair<Spell, Integer>> value = new ArrayList<>(list.size());

			list.forEach(t -> {
				CompoundTag tag = (CompoundTag) t;

				value.add(new Pair<>(getSpellFromTag(tag), tag.getInt(ECNames.COUNT)));
			});
			return value;
		}
		return Collections.emptyList();
	}

	public static void copySpells(ItemStack source, ItemStack target) {
		ListTag list = getSpellList(source);
		Spell spell = getSpell(source);

		if (list != null && !list.isEmpty()) {
			NBTHelper.getOrCreateECTag(target).put(ECNames.SPELL_LIST, list.copy());
		}
		if (spell.isValid()) {
			setSpell(target, spell);
		}
	}
	
	public static int getSpellCount(ItemStack stack) {
		ListTag list = getSpellList(stack);

		if (list != null && !list.isEmpty()) {
			return list.stream().mapToInt(t -> ((CompoundTag) t).getInt(ECNames.COUNT)).sum();
		}
		return 0;
	}

	public static void addSpell(ItemStack stack, Spell spell) {
		ListTag list = getOrCreateSpellList(stack);

		for (Tag value : list) {
			if (value instanceof CompoundTag tag && isSpellInTag(tag, spell)) {
				tag.putInt(ECNames.COUNT, tag.getInt(ECNames.COUNT) + 1);
				return;
			}
		}
		CompoundTag tag = new CompoundTag();

		tag.putString(ECNames.SPELL, spell.getRegistryName().toString());
		tag.putInt(ECNames.COUNT, 1);
		list.add(tag);
		if (list.size() == 1) {
			setSpell(stack, spell);
		}
	}

	public static void moveSelected(ItemStack stack, int i) {
		ListTag list = getSpellList(stack);
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
	
	@Nullable
	private static ListTag getSpellList(ItemStack stack) {
		CompoundTag nbt = NBTHelper.getECTag(stack);

		if (nbt != null && nbt.contains(ECNames.SPELL_LIST)) {
			return nbt.getList(ECNames.SPELL_LIST, 10);
		}
		return null;
	}

	private static Spell getSpellFromTag(Tag nbt) {
		if (nbt instanceof CompoundTag compoundTag) {
			return Spell.REGISTRY.getValue(new ResourceLocation(compoundTag.getString(ECNames.SPELL)));
		}
		return Spells.NONE;
	}

	private static boolean isSpellInTag(Tag nbt, Spell spell) {
		if (nbt instanceof CompoundTag && ((CompoundTag) nbt).contains(ECNames.SPELL)) {
			return ((CompoundTag) nbt).getString(ECNames.SPELL).equals(spell.getRegistryName().toString());
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
		List<Spell> list = spells.stream().filter(Spell::isValid).toList();
		int roll = rand.nextInt(list.stream().mapToInt(Spell::getWeight).sum());
		
		for (Spell spell : list) {
			roll -= spell.getWeight();
			if (roll < 0) {
				return spell;
			}
		}
		return list.get(list.size() - 1);
	}
}
