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

	public static boolean calledFromJEI() { // TODO remove once JEI update to show tooltip images
		var stack = Thread.currentThread().getStackTrace();

		for (StackTraceElement stackTraceElement : stack) {
			if (stackTraceElement.getClassName().contains("mezz.jei")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSilentGearActive() {
		return ModList.get().isLoaded("silentgear");
	}
}
