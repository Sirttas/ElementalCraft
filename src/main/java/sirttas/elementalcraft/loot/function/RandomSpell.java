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
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;

public class RandomSpell extends LootFunction {
	private final List<Spell> spellList;

	private static final String SPELLS = "spells";

	private RandomSpell(ILootCondition[] condition, Collection<Spell> spellList) {
		super(condition);
		this.spellList = ImmutableList.copyOf(spellList);
	   }

	@Override
	public ItemStack doApply(ItemStack stack, LootContext context) {
		Random random = context.getRandom();
		Spell spell;

		if (this.spellList.isEmpty()) {
			List<Spell> list = Lists.newArrayList();

			for (Spell sp : Spell.REGISTRY) {
				list.add(sp);
			}
			spell = list.get(random.nextInt(list.size()));
		} else {
			spell = this.spellList.get(random.nextInt(this.spellList.size()));
		}
		SpellHelper.setSpell(stack, spell);
		return stack;
	}

	public static LootFunction.Builder<?> builder() { // NOSONAR
		return builder(ImmutableList.of());
	}

	public static LootFunction.Builder<?> builder(Collection<Spell> spellList) { // NOSONAR
		return builder(l -> new RandomSpell(l, spellList));
	}

	public static class Serializer extends LootFunction.Serializer<RandomSpell> {
		public Serializer() {
			super(new ResourceLocation(ElementalCraft.MODID, "random_spell"), RandomSpell.class);
		}

		@Override
		public void serialize(JsonObject object, RandomSpell functionClazz, JsonSerializationContext serializationContext) {
			super.serialize(object, functionClazz, serializationContext);
			if (!functionClazz.spellList.isEmpty()) {
				JsonArray jsonarray = new JsonArray();

				for (Spell spell : functionClazz.spellList) {
					ResourceLocation resourcelocation = Spell.REGISTRY.getKey(spell);
					if (resourcelocation == null) {
						throw new IllegalArgumentException("Don't know how to serialize spell " + spell);
					}
					jsonarray.add(new JsonPrimitive(resourcelocation.toString()));
				}
				object.add(SPELLS, jsonarray);
			}
		}

		@Override
		public RandomSpell deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
			List<Spell> list = Lists.newArrayList();

			if (object.has(SPELLS)) {
				for (JsonElement jsonelement : JSONUtils.getJsonArray(object, SPELLS)) {
					String s = JSONUtils.getString(jsonelement, "spell");
					Spell spell = Spell.REGISTRY.getValue(new ResourceLocation(s));

					if (spell == null) {
						throw new JsonSyntaxException("Unknown spell '" + s + "'");
					}
					list.add(spell);
				}
			}
			return new RandomSpell(conditionsIn, list);
		}
	}
}