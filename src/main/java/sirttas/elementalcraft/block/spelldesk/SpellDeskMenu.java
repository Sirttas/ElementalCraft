package sirttas.elementalcraft.block.spelldesk;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import sirttas.elementalcraft.container.menu.AbstractECMenu;
import sirttas.elementalcraft.container.menu.ECMenus;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class SpellDeskMenu extends AbstractECMenu {

	private final Container input;
	private final Container output;
	private final Level level;

	private final DataSlot page = DataSlot.standalone();
	private final DataSlot pageCount = DataSlot.standalone();

	private List<ItemStack> stacks;

	public SpellDeskMenu(int id, Inventory player) {
		super(ECMenus.SPELL_DESK, id);
		this.level = player.player.level();
		input = new TransientCraftingContainer(this, 3, 1);
		output = new SimpleContainer(6);
		stacks = Collections.emptyList();
		
		this.addSlot(new InputSlot(0, 32, 35, s -> s.is(ECItems.SCROLL_PAPER.get())));
		this.addSlot(new InputSlot(1, 23, 53, s -> s.is(Tags.Items.GEMS)));
		this.addSlot(new InputSlot(2, 41, 53, s -> s.is(ECTags.Items.CRYSTALS)));
		this.addSlot(new OutputSlot(0, 108, 35));
		this.addSlot(new OutputSlot(1, 126, 35));
		this.addSlot(new OutputSlot(2, 144, 35));
		this.addSlot(new OutputSlot(3, 108, 53));
		this.addSlot(new OutputSlot(4, 126, 53));
		this.addSlot(new OutputSlot(5, 144, 53));
		this.addPlayerSlots(player, 84);
		this.addDataSlot(this.page);
		this.addDataSlot(this.pageCount);
	}

	@Nonnull
    @Override
	public ItemStack quickMoveStack(@Nonnull Player player, int index) {
		Slot slot = this.slots.get(index);

		if (slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			ItemStack stack = slotStack.copy();

			if (index < 3) {
				if (!this.moveItemStackTo(slotStack, 9, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (index < 9) {
				if (!this.moveItemStackTo(slotStack.copy(), 9, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
				slot.onTake(player, slotStack);
				return ItemStack.EMPTY;
			} else if (!this.moveItemStackTo(slotStack, 0, 3, false)) {
				return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			return stack;
		}
		return ItemStack.EMPTY;
	}
	
	private void updateRecipeList(Level level) {
		stacks = level.getRecipeManager().getRecipesFor(ECRecipeTypes.SPELL_CRAFT.get(), input, level).stream()
                .map(h -> h.value().assemble(input, level.registryAccess()))
				.toList();

		this.page.set(0);
		this.pageCount.set(Math.max(1, (int) Math.ceil(stacks.size() / 6.0)));
		setOutput();
	}

	private void setOutput() {
		output.clearContent();
		var size = Math.min(stacks.size(), 6);
		var index = page.get() * 6;

		for (int i = 0; i < size; i++) {
			output.setItem(i, stacks.get(i + index));
		}
		this.broadcastChanges();
	}

	@Override
	public void slotsChanged(@Nonnull Container container) {
		updateRecipeList(level);
	}

	@Override
	public void removed(@Nonnull Player player) {
		super.removed(player);
		clearContainer(player, input);
	}

	public int getPage() {
		return page.get();
	}

	public int getPageCount() {
		return pageCount.get();
	}

	public void nextPage() {
		page.set(Math.min(getPage() + 1, getPageCount() - 1));
		setOutput();
	}

	public void previousPage() {
		page.set(Math.max(getPage() - 1, 0));
		setOutput();
	}


	private class OutputSlot extends Slot {

		public OutputSlot(int index, int xPosition, int yPosition) {
			super(output, index, xPosition, yPosition);
		}
		
		@Override
		public boolean mayPlace(@Nonnull ItemStack stack) {
			return false;
		}
		
		@Override
		public void onTake(@Nonnull Player player, @Nonnull ItemStack stack) {
			checkTakeAchievements(stack);
			for (int i = 0; i < input.getContainerSize(); i++) {
				input.removeItem(i, 1);
			}
			updateRecipeList(player.level());
		}
	}
	
	private class InputSlot extends Slot {

		private final Predicate<ItemStack> predicate;
		
		public InputSlot(int index, int xPosition, int yPosition, Predicate<ItemStack> predicate) {
			super(input, index, xPosition, yPosition);
			this.predicate = predicate;
		}
		
		
		@Override
		public boolean mayPlace(@Nonnull ItemStack stack) {
			return predicate.test(stack);
		}
	}
}
