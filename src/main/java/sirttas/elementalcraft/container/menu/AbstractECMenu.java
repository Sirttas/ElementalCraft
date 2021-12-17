package sirttas.elementalcraft.container.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public abstract class AbstractECMenu extends AbstractContainerMenu implements IMenuOpenListener {

	protected AbstractECMenu(MenuType<?> type, int id) {
		super(type, id);
	}

	@Override
	public boolean stillValid(@Nonnull Player playerIn) {
		return true;
	}

	protected void addPlayerSlots(Inventory playerInventory, int yOffset) {
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
	public void removed(@Nonnull Player player) {
		var carried = this.getCarried();

		if (!carried.isEmpty()) {
			player.drop(carried, false);
			setCarried(ItemStack.EMPTY);
		}
	}
	
	@Override
	public void onOpen(Player player) {}
}
