package sirttas.elementalcraft.block.instrument;

import net.minecraft.inventory.IInventory;

public interface IInstrument extends IInventory {

	boolean canProgress();

	boolean isReciptAvalable();

	void process();

	float getProgress();

	boolean isRunning();

}