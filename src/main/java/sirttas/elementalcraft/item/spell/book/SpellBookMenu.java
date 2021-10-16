package sirttas.elementalcraft.item.spell.book;

import java.util.List;
import java.util.stream.IntStream;

import com.mojang.datafixers.util.Pair;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.menu.AbstractECMenu;
import sirttas.elementalcraft.container.menu.ECMenus;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.network.message.MessageHelper;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;

public class SpellBookMenu extends AbstractECMenu {

	static final int ROW_COUNT = (Spell.REGISTRY.getEntries().size() + 9 - 1) / 9;
	static final int SLOT_COUNT = ROW_COUNT * 9;

	private ItemStack book;
	private final Container inventory;
	private final Player player;

	public SpellBookMenu(int id, Inventory player) {
		this(id, player, new ItemStack(ECItems.SPELL_BOOK));
	}

	private SpellBookMenu(int id, Inventory playerInventoryIn, ItemStack book) {
		super(ECMenus.SPELL_BOOK, id);
		this.book = book;
		this.inventory = new SimpleContainer(SLOT_COUNT);
		this.player = playerInventoryIn.player;
		addSlots(playerInventoryIn);
	}

	public static SpellBookMenu create(int id, Inventory playerInventoryIn, ItemStack book) {
		return new SpellBookMenu(id, playerInventoryIn, book);
	}

	private void addSlots(Inventory playerInventoryIn) {
		for (int i = 0; i < ROW_COUNT; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new ScrollSlot(inventory, j + i * 9, 8 + j * 18, 18 + i * 18));
			}
		}
		addPlayerSlots(playerInventoryIn, 103 + (ROW_COUNT - 4) * 18);
	}

	@Override
	public boolean canDragTo(Slot slotIn) {
		return slotIn.index > SLOT_COUNT;
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this
	 * moves the stack between the player inventory and the other inventory(s).
	 */
	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		Slot slot = this.slots.get(index);

		if (slot.hasItem()) {
			ItemStack stack = slot.getItem();
			ItemStack old = stack.copy();
			Spell spell = SpellHelper.getSpell(stack);

			if (stack.getItem() == ECItems.SCROLL && spell.isValid()) {
				if (index < SLOT_COUNT) {
					removeSpell(stack);
					return ItemStack.EMPTY;
				} else if (canAddSpell(stack, spell)) {
					addSpell(stack, spell);
					return ItemStack.EMPTY;
				}

				if (stack.isEmpty()) {
					slot.set(ItemStack.EMPTY);
				} else {
					slot.setChanged();
				}
				return old;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
		Slot slot = slotId >= 0 ? this.slots.get(slotId) : null;

		if (slot == null || slot.getItem().getItem() != ECItems.SPELL_BOOK) {
			if (slotId < 0 || slotId >= SLOT_COUNT || clickTypeIn == ClickType.THROW || clickTypeIn == ClickType.QUICK_MOVE || clickTypeIn == ClickType.PICKUP_ALL) {
				super.clicked(slotId, dragType, clickTypeIn, player);
			} else if (clickTypeIn == ClickType.CLONE && player.isCreative() && getCarried().isEmpty()) {
				if (slot != null && slot.hasItem()) {
					ItemStack scroll = slot.getItem().copy();

					scroll.setCount(1);
					setCarried(scroll);
				}
			} else if (clickTypeIn == ClickType.PICKUP) {
				if (getCarried().isEmpty()) {
					if (slot != null && slot.hasItem()) {
						ItemStack stack = slot.getItem();
						ItemStack scroll = stack.copy();

						stack.shrink(1);
						scroll.setCount(1);
						SpellHelper.removeSpell(book, SpellHelper.getSpell(stack));
						setCarried(scroll);
						this.refresh();
					}
				} else {
					ItemStack stack = getCarried();
					Spell spell = SpellHelper.getSpell(stack);

					if (stack.getItem() == ECItems.SCROLL && spell.isValid()) {
						SpellHelper.addSpell(book, spell);
						setCarried(ItemStack.EMPTY);
						this.refresh();
					}
				}
			}
		}
	}

	public int getSpellCount() {
		int spellCount = SpellHelper.getSpellCount(book);

		return spellCount > 0 ? spellCount : IntStream.range(0, SLOT_COUNT).map(i -> {
			ItemStack stack = inventory.getItem(i);

			return stack.isEmpty() ? 0 : stack.getCount();
		}).sum();
	}

	public void setBook(ItemStack book) {
		this.book = book;
		this.refresh();

	}

	public boolean canAddSpell(ItemStack stack, Spell spell) {
		return stack.getItem() == ECItems.SCROLL && spell.isValid() && SpellHelper.getSpellCount(stack) < ECConfig.COMMON.spellBookMaxSpell.get();
	}
	
	@Override
	public void onOpen(Player player) {
		refresh();
	}
	
	private void refresh() {
		List<Pair<Spell, Integer>> spells = SpellHelper.getSpellsAsMap(book);

		for (int i = 0; i < SLOT_COUNT; ++i) {
			if (i < spells.size()) {
				ItemStack scroll = new ItemStack(ECItems.SCROLL);
				Pair<Spell, Integer> pair = spells.get(i);

				SpellHelper.setSpell(scroll, pair.getFirst());
				scroll.setCount(pair.getSecond());
				inventory.setItem(i, scroll);
			} else {
				inventory.setItem(i, ItemStack.EMPTY);
			}
			this.slots.forEach(Slot::setChanged);
		}
		if (player instanceof ServerPlayer) {
			MessageHelper.sendToPlayer((ServerPlayer) player, new SpellBookMessage(book));
		}
	}

	private void removeSpell(ItemStack stack) {
		Spell spell = SpellHelper.getSpell(stack);

		if (spell.isValid()) {
			for (int i = 0; i < this.slots.size(); i++) {
				Slot slot = this.slots.get(i + SLOT_COUNT);

				if (!slot.hasItem()) {
					ItemStack scroll = new ItemStack(ECItems.SCROLL);

					SpellHelper.setSpell(scroll, spell);
					slot.set(scroll);
					stack.shrink(1);
					SpellHelper.removeSpell(book, spell);
					slot.setChanged();
					refresh();
					return;
				}
			}
		}
	}

	private void addSpell(ItemStack stack, Spell spell) {
		for (int i = 0; i < SLOT_COUNT; i++) {
			Slot slot = this.slots.get(i);

			ItemStack stackInSlot = slot.getItem();

			if (stackInSlot.isEmpty() || spell.equals(SpellHelper.getSpell(stackInSlot))) {
				stack.shrink(1);
				SpellHelper.addSpell(book, spell);
				refresh();
				return;
			}
		}
	}

	private static class ScrollSlot extends Slot {

		public ScrollSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return stack.getItem() == ECItems.SCROLL && getItem().isEmpty();
		}

		@Override
		public int getMaxStackSize() {
			return ECConfig.COMMON.spellBookMaxSpell.get();
		}
	}
}
