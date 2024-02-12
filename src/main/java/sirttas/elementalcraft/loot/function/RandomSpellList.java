package sirttas.elementalcraft.loot.function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;

import javax.annotation.Nonnull;
import java.util.List;

public class RandomSpellList extends LootItemConditionalFunction {

	public static final Codec<RandomSpellList> CODEC = RecordCodecBuilder.create(builder -> commonFields(builder).and(
			RegistryCodecs.homogeneousList(Spells.KEY).fieldOf("spells").forGetter(r -> r.spellList)
	).apply(builder, RandomSpellList::new));

	private final HolderSet<Spell> spellList;

	private RandomSpellList(List<LootItemCondition> condition, HolderSet<Spell> spellList) {
		super(condition);
		this.spellList = spellList;
	}

	@Nonnull
    @Override
	public ItemStack run(@Nonnull ItemStack stack, LootContext context) {
		var random = context.getRandom();
		var list = spellList.stream()
				.map(Holder::value)
				.toList();

		if (list.isEmpty()) {
			return ItemStack.EMPTY;
		}

		var spell = SpellHelper.randomSpell(list, random);

		SpellHelper.setSpell(stack, spell);
		return stack;
	}

	public static Builder<?> builder(HolderSet<Spell> spellList) {
		return simpleBuilder(l -> new RandomSpellList(l, spellList));
	}

	@Nonnull
    @Override
	public LootItemFunctionType getType() {
		return ECLootFunctions.RANDOM_SPELL_LIST.get();
	}
}
