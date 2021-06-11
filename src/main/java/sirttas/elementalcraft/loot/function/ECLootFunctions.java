package sirttas.elementalcraft.loot.function;

import net.minecraft.loot.functions.LootFunctionManager;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECLootFunctions {

	private ECLootFunctions() {}
	
	public static final void setup() {
		RandomSpell.type = LootFunctionManager.register/* registerFunction */(ElementalCraftApi.MODID + ":random_spell", new RandomSpell.Serializer());
	}
}
