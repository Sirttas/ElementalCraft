package sirttas.elementalcraft.loot.function;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECLootFunctions {

	private static final DeferredRegister<LootItemFunctionType> DEFERRED_REGISTER = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, ElementalCraftApi.MODID);

	public static final DeferredHolder<LootItemFunctionType, LootItemFunctionType> RANDOM_SPELL = register("random_spell", RandomSpell.CODEC);
	public static final DeferredHolder<LootItemFunctionType, LootItemFunctionType> RANDOM_SPELL_LIST = register("random_spell_list", RandomSpellList.CODEC);

	private ECLootFunctions() {}

	public static DeferredHolder<LootItemFunctionType, LootItemFunctionType> register(String name, Codec<? extends LootItemFunction> codec) {
		return DEFERRED_REGISTER.register(name, () -> new LootItemFunctionType(codec));
	}

	public static void register(IEventBus modBus) {
		DEFERRED_REGISTER.register(modBus);
	}
}
