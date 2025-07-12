package sirttas.elementalcraft.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

public class RandomSpellListFunction extends LootItemConditionalFunction {

	public static final MapCodec<RandomSpellListFunction> CODEC = RecordCodecBuilder.mapCodec(builder -> commonFields(builder).and(
			RegistryCodecs.homogeneousList(Spells.REGISTRY_KEY).fieldOf("spells").forGetter(r -> r.spellList)
	).apply(builder, RandomSpellListFunction::new));

	private final HolderSet<Spell> spellList;

	private RandomSpellListFunction(List<LootItemCondition> condition, HolderSet<Spell> spellList) {
		super(condition);
		this.spellList = spellList;
	}

	@Nonnull
    @Override
	public ItemStack run(@Nonnull ItemStack stack, LootContext context) {
		SpellHelper.setSpell(stack, SpellHelper.randomSpell(spellList, context.getRandom()));
		return stack;
	}

	public static Builder<?> builder(HolderSet<Spell> spellList) {
		return simpleBuilder(l -> new RandomSpellListFunction(l, spellList));
	}

	@Nonnull
    @Override
	public LootItemFunctionType<RandomSpellListFunction> getType() {
		return ECLootFunctions.RANDOM_SPELL_LIST.get();
	}
}
