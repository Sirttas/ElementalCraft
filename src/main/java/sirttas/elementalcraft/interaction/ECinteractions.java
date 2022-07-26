package sirttas.elementalcraft.interaction;

import net.minecraftforge.fml.ModList;
import sirttas.elementalcraft.config.ECConfig;

public class ECinteractions {

	private ECinteractions() {}
	
	public static boolean isMekanismActive() {
		return ModList.get().isLoaded("mekanism") && Boolean.TRUE.equals(ECConfig.COMMON.mekanismInteracionEnabled.get());
	}
	
	public static boolean isBotaniaActive() {
		return ModList.get().isLoaded("botania") && Boolean.TRUE.equals(ECConfig.COMMON.botaniaInteracionEnabled.get());
	}

	public static boolean isSilentGearActive() {
		return ModList.get().isLoaded("silentgear");
	}

	public static boolean isCuriosActive() {
		return ModList.get().isLoaded("curios");
	}
}
