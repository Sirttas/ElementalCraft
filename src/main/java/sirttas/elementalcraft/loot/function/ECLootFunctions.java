package sirttas.elementalcraft.loot.function;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECLootFunctions {

	private static final DeferredRegister<LootItemFunctionType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, ElementalCraftApi.MODID);

	public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<RandomSpellFunction>> RANDOM_SPELL = register("random_spell", RandomSpellFunction.CODEC);
	public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<RandomSpellListFunction>> RANDOM_SPELL_LIST = register("random_spell_list", RandomSpellListFunction.CODEC);
	public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<SetRuneFunction>> SET_RUNE = register("set_rune", SetRuneFunction.CODEC);

	private ECLootFunctions() {}

	private static <T extends LootItemFunction> DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<T>> register(String name, MapCodec<T> codec) {
		return DEFERRED_REGISTER.register(name, () -> new LootItemFunctionType<>(codec));
	}

	public static void register(IEventBus modBus) {
		DEFERRED_REGISTER.register(modBus);
	}
}
