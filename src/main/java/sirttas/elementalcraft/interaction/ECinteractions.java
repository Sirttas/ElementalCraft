package sirttas.elementalcraft.interaction;

import net.minecraftforge.fml.ModList;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.interaction.mekanism.MekanismInteraction;

public class ECinteractions {

	public static void setup() {
		if (isMekanismActive()) {
			MekanismInteraction.setup();
		}
	}

	public static boolean isMekanismActive() {
		return ModList.get().isLoaded("mekanism") && Boolean.TRUE.equals(ECConfig.COMMON.mekanismInteracionEnabled.get());
	}
}
