package sirttas.elementalcraft.mixin;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class Connector implements IMixinConnector {
	@Override
	public void connect() {
		Mixins.addConfigurations("assets/elementalcraft/elementalcraft.mixins.json");
	}
}