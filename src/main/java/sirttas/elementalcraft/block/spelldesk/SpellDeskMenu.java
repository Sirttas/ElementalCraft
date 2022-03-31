package sirttas.elementalcraft.block.spelldesk;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import sirttas.elementalcraft.container.menu.AbstractECMenu;
import sirttas.elementalcraft.container.menu.ECMenus;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.SpellCraftRecipe;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;

public class SpellDeskMenu extends AbstractECMenu {

	private final Container input;
	private final Container output;
	private final ContainerLevelAccess worldPosCallable;
	
	public SpellDeskMenu(int id, Inventory player) {
		this(id, player, ContainerLevelAccess.NULL);
	}
	
	public SpellDeskMenu(int id, Inventory player, ContainerLevelAccess worldPosCallable) {
		super(ECMenus.SPELL_DESK, id);
		this.worldPosCallable = worldPosCallable;
		input = new CraftingContainer(this, 3, 1);
		output = new SimpleContainer(6);
		
		this.addSlot(new InputSlot(0, 32, 35, s -> s.getItem() == ECItems.SCROLL_PAPER));
		this.addSlot(new InputSlot(1, 23, 53, s -> s.is(Tags.Items.GEMS)));
		this.addSlot(new InputSlot(2, 41, 53, s -> s.is(ECTags.Items.CRYSTALS)));
		this.addSlot(new OutputSlot(0, 108, 35));
		this.addSlot(new OutputSlot(1, 126, 35));
		this.addSlot(new OutputSlot(2, 144, 35));
		this.addSlot(new OutputSlot(3, 108, 53));
		this.addSlot(new OutputSlot(4, 126, 53));
		this.addSlot(new OutputSlot(5, 144, 53));
		this.addPlayerSlots(player, 84);
	}

	public static SpellDeskMenu create(int id, Inventory inventory, ContainerLevelAccess worldPosCallable) {
		return new SpellDeskMenu(id, inventory, worldPosCallable);
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
	
	private void updateOutput(Level world) {
		List<ItemStack> stacks = world.getRecipeManager().getRecipesFor(SpellCraftRecipe.TYPE, input, world).stream()
                .limit(6)
                .map(r -> r.assemble(input)).toList();

		output.clearContent();
		for (int i = 0; i < stacks.size(); i++) {
			output.setItem(i, stacks.get(i));
		}
		this.broadcastChanges();
	}
	
	@Override
	public void slotsChanged(@Nonnull Container inventoryIn) {
		worldPosCallable.execute((world, pos) -> updateOutput(world));
	}

	@Override
	public void removed(@Nonnull Player playerIn) {
		super.removed(playerIn);
		worldPosCallable.execute((world, pos) -> clearContainer(playerIn, input));
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
			updateOutput(player.level);
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
