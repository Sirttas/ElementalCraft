package sirttas.elementalcraft.block.container.reservoir;

import net.minecraft.world.entity.player.Player;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;

public class ReservoirElementStorage extends StaticElementStorage {

	public ReservoirElementStorage(ElementType elementType, int elementCapacity, Runnable syncCallback) {
		super(elementType, elementCapacity, syncCallback);
	}
	
	@Override
	public boolean doesRenderGauge(Player player) {
		return true;
	}
}
