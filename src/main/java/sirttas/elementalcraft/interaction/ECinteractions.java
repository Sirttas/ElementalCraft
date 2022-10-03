package sirttas.elementalcraft.interaction;

import net.minecraftforge.fml.ModList;

public class ECinteractions {

	private ECinteractions() {}
	
	public static boolean isMekanismActive() {
		return ModList.get().isLoaded("mekanism");
	}
	
	public static boolean isBotaniaActive() {
		return ModList.get().isLoaded("botania");
	}

	public static boolean isSilentGearActive() {
		return ModList.get().isLoaded("silentgear");
	}

	public static boolean isCuriosActive() {
		return ModList.get().isLoaded("curios");
	}
	public static boolean isImmersiveEngineeringActive() {
		return ModList.get().isLoaded("immersiveengineering");
	}

}
