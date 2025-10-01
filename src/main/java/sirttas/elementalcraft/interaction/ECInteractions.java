package sirttas.elementalcraft.interaction;

import net.neoforged.fml.ModList;

public class ECInteractions {

	private ECInteractions() {}
	
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

	public static boolean isTestFrameworkActive() {
		return ModList.get().isLoaded("testframework");
	}

    public static boolean isAppliedEnergistics2Active() {
        return ModList.get().isLoaded("ae2");
    }
}
