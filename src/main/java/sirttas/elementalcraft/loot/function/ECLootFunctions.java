package sirttas.elementalcraft.loot.function;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECLootFunctions {

	private static final DeferredRegister<@NotNull MapCodec<? extends LootItemFunction>> DEFERRED_REGISTER = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, ElementalCraftApi.MODID);

	public static final DeferredHolder<@NotNull MapCodec<? extends LootItemFunction>, @NotNull MapCodec<RandomSpellFunction>> RANDOM_SPELL = register("random_spell", RandomSpellFunction.CODEC);
	public static final DeferredHolder<@NotNull MapCodec<? extends LootItemFunction>, @NotNull MapCodec<RandomSpellListFunction>> RANDOM_SPELL_LIST = register("random_spell_list", RandomSpellListFunction.CODEC);
	public static final DeferredHolder<@NotNull MapCodec<? extends LootItemFunction>, @NotNull MapCodec<SetRuneFunction>> SET_RUNE = register("set_rune", SetRuneFunction.CODEC);

	private ECLootFunctions() {}

	private static <T extends LootItemFunction> DeferredHolder<@NotNull MapCodec<? extends LootItemFunction>, @NotNull MapCodec<T>> register(String name, MapCodec<T> codec) {
		return DEFERRED_REGISTER.register(name, () -> codec);
	}

	public static void register(IEventBus modBus) {
		DEFERRED_REGISTER.register(modBus);
	}
}
