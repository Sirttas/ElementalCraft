package sirttas.elementalcraft.interaction;

import net.minecraftforge.fml.ModList;
import sirttas.elementalcraft.config.ECConfig;

public class ECinteractions {

	private ECinteractions() {}
	
	public static boolean isMekanismActive() {
		return ModList.get().isLoaded("mekanism") && Boolean.TRUE.equals(ECConfig.COMMON.mekanismInteracionEnabled.get());
	}
	
	public static boolean isBotaniaActive() {
		return ModList.get().isLoaded("botania");
	}
}
