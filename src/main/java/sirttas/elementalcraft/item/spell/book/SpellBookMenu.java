package sirttas.elementalcraft.item.spell.book;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.menu.AbstractECMenu;
import sirttas.elementalcraft.container.menu.ECMenus;
import sirttas.elementalcraft.container.menu.IMenuOpenListener;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class SpellBookMenu extends AbstractECMenu implements IMenuOpenListener {

	static final int ROW_COUNT = (Spells.REGISTRY.size() + 9 - 1) / 9;
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
	@Nonnull
    @Override
	public ItemStack quickMoveStack(@Nonnull Player player, int index) {
		Slot slot = this.slots.get(index);

		if (slot.hasItem()) {
			var stack = slot.getItem();
			var old = stack.copy();
			var spell = SpellHelper.getSpell(stack);

			if (stack.is(ECItems.SCROLL.get()) && SpellHelper.isValid(spell)) {
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
	public void clicked(int slotId, int dragType, @Nonnull ClickType clickType, @Nonnull Player player) {
		Slot slot = slotId >= 0 ? this.slots.get(slotId) : null;

		if (slot == null || !slot.getItem().is(ECItems.SPELL_BOOK.get())) {
			if (slotId < 0 || slotId >= SLOT_COUNT || clickType == ClickType.THROW || clickType == ClickType.QUICK_MOVE || clickType == ClickType.PICKUP_ALL) {
				super.clicked(slotId, dragType, clickType, player);
			} else if (clickType == ClickType.CLONE && player.getAbilities().instabuild && getCarried().isEmpty()) {
				if (slot != null && slot.hasItem()) {
					ItemStack scroll = slot.getItem().copy();

					scroll.setCount(1);
					setCarried(scroll);
				}
			} else if (clickType == ClickType.PICKUP) {
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
					var stack = getCarried();
					var spell = SpellHelper.getSpell(stack);

					if (stack.is(ECItems.SCROLL.get()) && SpellHelper.isValid(spell)) {
						SpellHelper.addSpell(book, spell);
						setCarried(ItemStack.EMPTY);
						this.refresh();
					}
				}
			}
		}
	}

	public int getSpellCount() {
		int spellCount = SpellHelper.getSpellList(book).count();

		return spellCount > 0 ? spellCount : IntStream.range(0, SLOT_COUNT).map(i -> {
			ItemStack stack = inventory.getItem(i);

			return stack.isEmpty() ? 0 : stack.getCount();
		}).sum();
	}

	public void setBook(ItemStack book) {
		this.book = book;
		this.refresh();

	}

	public boolean canAddSpell(ItemStack stack, Holder<Spell> spell) {
		return stack.is(ECItems.SCROLL.get()) && SpellHelper.isValid(spell) && !SpellHelper.getSpellList(stack).isFull();
	}
	
	@Override
	public void onOpen(Player player) {
		refresh();
	}
	
	private void refresh() {
		var spells = SpellHelper.getSpellList(book).getSpells();
		var i = new AtomicInteger(0);

		spells.forEach((s, c) -> {
			var scroll = new ItemStack(ECItems.SCROLL);

			SpellHelper.setSpell(scroll, s);
			scroll.setCount(c);
			inventory.setItem(i.getAndIncrement(), scroll);
		});
		for (var j = i.get(); j < SLOT_COUNT; j++) {
			inventory.setItem(j, ItemStack.EMPTY);
		}
		this.slots.forEach(Slot::setChanged);

		if (player instanceof ServerPlayer serverPlayer) {
			PacketDistributor.sendToPlayer(serverPlayer, new SpellBookPayload(book));
		}
	}

	private void removeSpell(ItemStack stack) {
		var spell = SpellHelper.getSpell(stack);

		if (SpellHelper.isValid(spell)) {
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

	private void addSpell(ItemStack stack, Holder<Spell> spell) {
		for (int i = 0; i < SLOT_COUNT; i++) {
			Slot slot = this.slots.get(i);

			ItemStack stackInSlot = slot.getItem();

			if (stackInSlot.isEmpty() || spell.is(SpellHelper.getSpell(stackInSlot))) {
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
			return stack.is(ECItems.SCROLL.get()) && getItem().isEmpty();
		}

		@Override
		public int getMaxStackSize() {
			return ECConfig.SERVER.spellBookMaxSpell.get();
		}
	}
}
