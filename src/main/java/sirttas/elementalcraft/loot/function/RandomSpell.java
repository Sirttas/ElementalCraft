package sirttas.elementalcraft.loot.function;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;

public class RandomSpell extends LootFunction {

	private ElementType elementType;
	private final List<Spell> spellList;

	static LootFunctionType type;
	
	private RandomSpell(ILootCondition[] condition, Collection<Spell> spellList) {
		super(condition);
		this.spellList = ImmutableList.copyOf(spellList);
		this.elementType = ElementType.NONE;
	}

	private RandomSpell(ILootCondition[] condition, ElementType elementType) {
		this(condition, ImmutableList.of());
		this.elementType = elementType;
	}

	@Override
	public ItemStack doApply(ItemStack stack, LootContext context) {
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

	public static LootFunction.Builder<?> builder() {
		return builder(ImmutableList.of());
	}

	public static LootFunction.Builder<?> builder(Collection<Spell> spellList) {
		return builder(l -> new RandomSpell(l, spellList));
	}

	public static LootFunction.Builder<?> builder(ElementType elementType) {
		return builder(l -> new RandomSpell(l, elementType));
	}

	public static class Serializer extends LootFunction.Serializer<RandomSpell> {
		@Override
		public void serialize(JsonObject object, RandomSpell function, JsonSerializationContext serializationContext) {
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
				object.addProperty(ECNames.ELEMENT_TYPE, function.elementType.getString());
			}
		}

		@Override
		public RandomSpell deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
			List<Spell> list = Lists.newArrayList();

			if (object.has(ECNames.SPELL_LIST)) {
				for (JsonElement jsonelement : JSONUtils.getJsonArray(object, ECNames.SPELL_LIST)) {
					String s = JSONUtils.getString(jsonelement, ECNames.SPELL);
					Spell spell = Spell.REGISTRY.getValue(new ResourceLocation(s));

					if (spell == null) {
						throw new JsonSyntaxException("Unknown spell '" + s + "'");
					}
					list.add(spell);
				}
				return new RandomSpell(conditionsIn, list);
			} else if (object.has(ECNames.ELEMENT_TYPE)) {
				return new RandomSpell(conditionsIn, ElementType.byName(JSONUtils.getString(object, ECNames.ELEMENT_TYPE)));
			}
			return new RandomSpell(conditionsIn, list);
		} 
	}

	@Override
	public LootFunctionType getFunctionType() {
		return type;
	}
}