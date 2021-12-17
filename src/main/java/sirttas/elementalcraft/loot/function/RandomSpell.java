package sirttas.elementalcraft.loot.function;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;

import javax.annotation.Nonnull;

public class RandomSpell extends LootItemConditionalFunction {

	private ElementType elementType;
	private final List<Spell> spellList;

	static LootItemFunctionType type;
	
	private RandomSpell(LootItemCondition[] condition, Collection<Spell> spellList) {
		super(condition);
		this.spellList = List.copyOf(spellList);
		this.elementType = ElementType.NONE;
	}

	private RandomSpell(LootItemCondition[] condition, ElementType elementType) {
		this(condition, List.of());
		this.elementType = elementType;
	}

	@Nonnull
    @Override
	public ItemStack run(@Nonnull ItemStack stack, LootContext context) {
		Random random = context.getRandom();
		Spell spell;

		if (!this.spellList.isEmpty()) {
			spell = SpellHelper.randomSpell(this.spellList, random);
		} else if (this.elementType != ElementType.NONE) {
			spell = SpellHelper.randomSpell(this.elementType, random);
		} else {
			spell = SpellHelper.randomSpell(random);
		}
		SpellHelper.setSpell(stack, spell);
		return stack;
	}

	public static LootItemConditionalFunction.Builder<?> builder() {
		return builder(List.of());
	}

	public static LootItemConditionalFunction.Builder<?> builder(Collection<Spell> spellList) {
		return simpleBuilder(l -> new RandomSpell(l, spellList));
	}

	public static LootItemConditionalFunction.Builder<?> builder(ElementType elementType) {
		return simpleBuilder(l -> new RandomSpell(l, elementType));
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<RandomSpell> {
		@Override
		public void serialize(@Nonnull JsonObject object, @Nonnull RandomSpell function, @Nonnull JsonSerializationContext serializationContext) {
			super.serialize(object, function, serializationContext);
			if (!function.spellList.isEmpty()) {
				JsonArray jsonarray = new JsonArray();

				for (Spell spell : function.spellList) {
					ResourceLocation resourcelocation = Spell.REGISTRY.getKey(spell);
					if (resourcelocation == null) {
						throw new IllegalArgumentException("Don't know how to serialize spell " + spell);
					}
					jsonarray.add(new JsonPrimitive(resourcelocation.toString()));
				}
				object.add(ECNames.SPELL_LIST, jsonarray);
			} else if (function.elementType != ElementType.NONE) {
				object.addProperty(ECNames.ELEMENT_TYPE, function.elementType.getSerializedName());
			}
		}

		@Nonnull
        @Override
		public RandomSpell deserialize(JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootItemCondition[] conditionsIn) {
			List<Spell> list = Lists.newArrayList();

			if (object.has(ECNames.SPELL_LIST)) {
				for (JsonElement jsonelement : GsonHelper.getAsJsonArray(object, ECNames.SPELL_LIST)) {
					String s = GsonHelper.convertToString(jsonelement, ECNames.SPELL);
					Spell spell = Spell.REGISTRY.getValue(new ResourceLocation(s));

					if (spell == null) {
						throw new JsonSyntaxException("Unknown spell '" + s + "'");
					}
					list.add(spell);
				}
				return new RandomSpell(conditionsIn, list);
			} else if (object.has(ECNames.ELEMENT_TYPE)) {
				return new RandomSpell(conditionsIn, ElementType.byName(GsonHelper.getAsString(object, ECNames.ELEMENT_TYPE)));
			}
			return new RandomSpell(conditionsIn, list);
		} 
	}

	@Nonnull
    @Override
	public LootItemFunctionType getType() {
		return type;
	}
}
