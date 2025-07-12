package sirttas.elementalcraft.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.spell.SpellHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class RandomSpellFunction extends LootItemConditionalFunction implements IElementTypeProvider {

	public static final MapCodec<RandomSpellFunction> CODEC = RecordCodecBuilder.mapCodec(builder -> commonFields(builder).and(
			ElementType.CODEC.optionalFieldOf(ECNames.ELEMENT_TYPE, ElementType.NONE).forGetter(RandomSpellFunction::getElementType)
	).apply(builder, RandomSpellFunction::new));

	private final ElementType elementType;

	private RandomSpellFunction(List<LootItemCondition> condition, ElementType elementType) {
		super(condition);
		this.elementType = elementType;
	}

	@Nonnull
    @Override
	public ItemStack run(@Nonnull ItemStack stack, LootContext context) {
		var random = context.getRandom();
		var spell = this.elementType != ElementType.NONE ? SpellHelper.randomSpell(this.elementType, random) : SpellHelper.randomSpell(random);

		SpellHelper.setSpell(stack, spell);
		return stack;
	}

	public static LootItemConditionalFunction.Builder<?> builder() {
		return builder(ElementType.NONE);
	}


	public static LootItemConditionalFunction.Builder<?> builder(ElementType elementType) {
		return simpleBuilder(l -> new RandomSpellFunction(l, elementType));
	}

	@Nonnull
    @Override
	public LootItemFunctionType<RandomSpellFunction> getType() {
		return ECLootFunctions.RANDOM_SPELL.get();
	}

	@Override
	public @NotNull ElementType getElementType() {
		return elementType;
	}
}
