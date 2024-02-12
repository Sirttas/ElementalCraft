package sirttas.elementalcraft.loot.function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class RandomSpell extends LootItemConditionalFunction implements IElementTypeProvider {

	public static final Codec<RandomSpell> CODEC = RecordCodecBuilder.create(builder -> commonFields(builder).and(
			ElementType.CODEC.optionalFieldOf(ECNames.ELEMENT_TYPE, ElementType.NONE).forGetter(RandomSpell::getElementType)
	).apply(builder, RandomSpell::new));

	private ElementType elementType;

	private RandomSpell(List<LootItemCondition> condition, ElementType elementType) {
		super(condition);
		this.elementType = elementType;
	}

	@Nonnull
    @Override
	public ItemStack run(@Nonnull ItemStack stack, LootContext context) {
		var random = context.getRandom();
		Spell spell;

		if (this.elementType != ElementType.NONE) {
			spell = SpellHelper.randomSpell(this.elementType, random);
		} else {
			spell = SpellHelper.randomSpell(random);
		}
		SpellHelper.setSpell(stack, spell);
		return stack;
	}

	public static LootItemConditionalFunction.Builder<?> builder() {
		return builder(ElementType.NONE);
	}


	public static LootItemConditionalFunction.Builder<?> builder(ElementType elementType) {
		return simpleBuilder(l -> new RandomSpell(l, elementType));
	}

	@Nonnull
    @Override
	public LootItemFunctionType getType() {
		return ECLootFunctions.RANDOM_SPELL.get();
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}
}
