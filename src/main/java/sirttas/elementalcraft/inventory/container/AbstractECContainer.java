package sirttas.elementalcraft.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public abstract class AbstractECContainer extends Container {
	
	protected AbstractECContainer(ContainerType<?> type, int id) {
		super(type, id);
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return true;
	}
	
	protected void addPlayerSlots(PlayerInventory playerInventory, int yOffset) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, i * 18 + yOffset));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 58 + yOffset));
		}
	}

	   /**
	    * Called when the container is closed.
	    */
	   @Override
	public void removed(PlayerEntity playerIn) {
	      PlayerInventory playerinventory = playerIn.inventory;
	      
	      if (!playerinventory.getCarried().isEmpty()) {
	         playerIn.drop(playerinventory.getCarried(), false);
	         playerinventory.setCarried(ItemStack.EMPTY);
	      }

	   }
	
}
