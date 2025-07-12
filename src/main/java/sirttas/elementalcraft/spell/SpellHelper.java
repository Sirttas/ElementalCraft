package sirttas.elementalcraft.spell;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.component.ECDataComponents;

import java.util.stream.StreamSupport;

public class SpellHelper {

	private SpellHelper() {}
	
	public static Holder<Spell> getSpell(ItemStack stack) {
		return stack.getOrDefault(ECDataComponents.SPELL, Spells.NONE);
	}

	public static void setSpell(ItemStack stack, Holder<Spell> spell) {
		stack.set(ECDataComponents.SPELL, spell);
	}

	public static void removeSpell(ItemStack stack, Holder<Spell> spell) {
		var list = getSpellList(stack);

		if (list.isEmpty()) {
			return;
		}

		var newList = list.mutable()
				.remove(spell)
				.immutable();

		stack.set(ECDataComponents.SPELL_LIST, newList);
		if (getSpell(stack).is(spell)) {
			setSpell(stack, newList.isEmpty() ? Spells.NONE : newList.getSpellAt(0));
		}
	}

	public static SpellList getSpellList(ItemStack stack) {
		return stack.getOrDefault(ECDataComponents.SPELL_LIST, SpellList.EMPTY);
	}

	public static void copySpells(ItemStack source, ItemStack target) {
		var list = getSpellList(source);

		if (!list.isEmpty()) {
			target.set(ECDataComponents.SPELL_LIST, list.mutable().immutable());
		}

		var spell = getSpell(source);

		if (isValid(spell)) {
			setSpell(target, spell);
		}
	}

	public static boolean isValid(Holder<Spell> spell) {
		return spell.value().isValid();
	}

	public static boolean isVisible(Holder<Spell> spell) {
		return spell.value().isVisible();
	}

	public static void addSpell(ItemStack stack, Holder<Spell> spell) {
		var list = getSpellList(stack);

		if (list.isFull()) {
			return;
		}

		var newList = list.mutable()
				.add(spell)
				.immutable();

		stack.set(ECDataComponents.SPELL_LIST, newList);
		if (newList.count() == 1) {
			setSpell(stack, spell);
		}
	}

	public static int getSelected(ItemStack stack) {
		var list = getSpellList(stack);

		if (list.isEmpty()) {
			return -1;
		}
		var spell = getSpell(stack);

		return list.getIndex(spell);
	}

	public static void setSelected(ItemStack stack, int i) {
		var list = getSpellList(stack);

		if (list.isEmpty()) {
			return;
		}
		setSpell(stack, list.getSpellAt(i));
	}

	public static Holder<Spell> randomSpell(RandomSource rand) {
		return randomSpell(Spells.REGISTRY.holders().toList(), rand);
	}

	public static Holder<Spell> randomSpell(ElementType type, RandomSource rand) {
		return randomSpell(Spells.REGISTRY.holders()
				.filter(spell -> {
					var value = spell.value();

					return value.getElementType() == type && value.isValid();
				})
				.toList(), rand);
	}

	public static Holder<Spell> randomSpell(Iterable<? extends Holder<Spell>> spells, RandomSource rand) {
		var list = StreamSupport.stream(spells.spliterator(), false)
				.filter(SpellHelper::isValid)
				.toList();

		if (list.isEmpty()) {
			return Spells.NONE;
		}

		int roll = rand.nextInt(list.stream()
				.mapToInt(h -> h.value().getWeight())
				.sum());
		
		for (var spell : list) {
			roll -= spell.value().getWeight();
			if (roll < 0) {
				return spell;
			}
		}
		return list.getLast();
	}

    public static Holder<Spell> getSpellInUse(Entity entity) {
		// TODO spell casting monster
		if (entity instanceof LivingEntity livingEntity && livingEntity.isUsingItem()) {
			return getSpell(livingEntity.getUseItem());
		}
		return Spells.NONE;
    }
}
