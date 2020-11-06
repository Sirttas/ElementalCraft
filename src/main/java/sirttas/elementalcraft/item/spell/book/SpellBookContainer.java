package sirttas.elementalcraft.item.spell.book;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.container.ECContainers;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;

public class SpellBookContainer extends Container {

	static final int ROW_COUNT = 3;

	private final ItemStack book;
	private final int bookSize;

	public SpellBookContainer(int id, PlayerInventory player) {
		this(id, player, ItemStack.EMPTY, ROW_COUNT);
	}

	private SpellBookContainer(int id, PlayerInventory playerInventoryIn, ItemStack book, int rows) {
		super(ECContainers.spellBook, id);
		this.book = book;
		this.bookSize = 9 * rows;
		addSlots(playerInventoryIn, new Inventory(bookSize), rows);
	}

	public static SpellBookContainer create(int id, PlayerInventory playerInventoryIn, ItemStack book) {
		return new SpellBookContainer(id, playerInventoryIn, book, ROW_COUNT);
	}

	private void addSlots(PlayerInventory playerInventoryIn, IInventory inv, int rows) {
		List<Pair<Spell, Integer>> spells = SpellHelper.getSpellsAsMap(book);

		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < 9; ++j) {
				int index = j + i * 9;
				Slot slot = this.addSlot(new Slot(inv, index, 8 + j * 18, 18 + i * 18));
				if (index < spells.size()) {
					ItemStack scroll = new ItemStack(ECItems.scroll);
					Pair<Spell, Integer> pair = spells.get(index);

					SpellHelper.setSpell(scroll, pair.getFirst());
					scroll.setCount(pair.getSecond());
					slot.putStack(scroll);
				}
			}
		}
		addPlayerSlots(playerInventoryIn, (rows - 4) * 18);
	}

	private void addPlayerSlots(PlayerInventory playerInventoryIn, int yOffset) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 103 + i * 18 + yOffset));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventoryIn, i, 8 + i * 18, 161 + yOffset));
		}
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this
	 * moves the stack between the player inventory and the other inventory(s).
	 */
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			ItemStack old = stack.copy();

			if (index < bookSize) {
				removeSpell(stack);
				return ItemStack.EMPTY;
			} else if (!addSpell(stack)) {
				return ItemStack.EMPTY;
			}

			if (stack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			return old;
		}
		return ItemStack.EMPTY;
	}
	
	private boolean removeSpell(ItemStack stack) {
		Spell spell = SpellHelper.getSpell(stack);

		if (spell.isValid()) {
			for (int i = 0; i < this.inventorySlots.size(); i++) {
				Slot slot = this.inventorySlots.get(i + bookSize);

				if (slot != null && !slot.getHasStack()) {
					ItemStack scroll = new ItemStack(ECItems.scroll);

					SpellHelper.setSpell(scroll, spell);
					slot.putStack(scroll);
					stack.shrink(1);
					SpellHelper.removeSpell(book, spell);
					slot.onSlotChanged();
					return true;
				}
			}
		}
		return false;
	}

	private boolean addSpell(ItemStack stack) {
		Spell spell = SpellHelper.getSpell(stack);

		if (stack.getItem() == ECItems.scroll && spell.isValid() && SpellHelper.getSpellCount(stack) < ECConfig.COMMON.spellBookMaxSpell.get()) {
			for (int i = 0; i < bookSize; i++) {
				Slot slot = this.inventorySlots.get(i);

				if (slot != null) {
					ItemStack stackInSlot = slot.getStack();

					if (stackInSlot.isEmpty()) {
						slot.putStack(stack.copy());
						processAddSpell(stack, spell, slot);
						return true;
					} else if (spell.equals(SpellHelper.getSpell(stackInSlot))) {
						stackInSlot.grow(1);
						processAddSpell(stack, spell, slot);
						return true;
					}
				}
			}
		}
		return false;
	}

	public void processAddSpell(ItemStack stack, Spell spell, Slot slot) {
		stack.shrink(1);
		SpellHelper.addSpell(book, spell);
		slot.onSlotChanged();
	}

}
