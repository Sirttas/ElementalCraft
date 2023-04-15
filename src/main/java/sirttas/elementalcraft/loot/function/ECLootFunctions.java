package sirttas.elementalcraft.loot.function;

import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECLootFunctions {

	private static final DeferredRegister<LootItemFunctionType> DEFERRED_REGISTER = DeferredRegister.create(Registry.LOOT_FUNCTION_REGISTRY, ElementalCraftApi.MODID);

	public static final RegistryObject<LootItemFunctionType> RANDOM_SPELL = register("random_spell", new RandomSpell.Serializer());

	private ECLootFunctions() {}

	public static RegistryObject<LootItemFunctionType> register(String name, Serializer<? extends LootItemFunction> serializer) {
		return DEFERRED_REGISTER.register(name, () -> new LootItemFunctionType(serializer));
	}

	public static void register(IEventBus modBus) {
		DEFERRED_REGISTER.register(modBus);
	}
}
