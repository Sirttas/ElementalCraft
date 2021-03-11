package sirttas.elementalcraft.loot.function;

import net.minecraft.loot.functions.LootFunctionManager;
import sirttas.elementalcraft.ElementalCraft;

public class ECLootFunctions {

	private ECLootFunctions() {}
	
	public static final void setup() {
		RandomSpell.type = LootFunctionManager.func_237451_a_/* registerFunction */(ElementalCraft.MODID + ":random_spell", new RandomSpell.Serializer());
	}
}
