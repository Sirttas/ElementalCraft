package sirttas.elementalcraft.loot.function;

import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECLootFunctions {

	private ECLootFunctions() {}
	
	public static final void setup() {
		RandomSpell.type = LootItemFunctions.register(ElementalCraftApi.MODID + ":random_spell", new RandomSpell.Serializer());
	}
}
