package sirttas.elementalcraft.block.spelldesk;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
import sirttas.elementalcraft.inventory.container.AbstractECContainer;
import sirttas.elementalcraft.inventory.container.ECContainers;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.SpellCraftRecipe;
import sirttas.elementalcraft.tag.ECTags;

public class SpellDeskContainer extends AbstractECContainer {

	private final Container input;
	private final Container output;
	private final ContainerLevelAccess worldPosCallable;
	
	public SpellDeskContainer(int id, Inventory player) {
		this(id, player, ContainerLevelAccess.NULL);
	}
	
	public SpellDeskContainer(int id, Inventory player, ContainerLevelAccess worldPosCallable) {
		super(ECContainers.SPELL_DESK, id);
		this.worldPosCallable = worldPosCallable;
		input = new CraftingContainer(this, 3, 1);
		output = new SimpleContainer(6);
		
		this.addSlot(new InputSlot(0, 32, 35, s -> s.getItem() == ECItems.SCROLL_PAPER));
		this.addSlot(new InputSlot(1, 23, 53, s -> Tags.Items.GEMS.contains(s.getItem())));
		this.addSlot(new InputSlot(2, 41, 53, s -> ECTags.Items.CRYSTALS.contains(s.getItem())));
		this.addSlot(new OutputSlot(0, 108, 35));
		this.addSlot(new OutputSlot(1, 126, 35));
		this.addSlot(new OutputSlot(2, 144, 35));
		this.addSlot(new OutputSlot(3, 108, 53));
		this.addSlot(new OutputSlot(4, 126, 53));
		this.addSlot(new OutputSlot(5, 144, 53));
		this.addPlayerSlots(player, 84);
	}

	public static SpellDeskContainer create(int id, Inventory inventory, ContainerLevelAccess worldPosCallable) {
		return new SpellDeskContainer(id, inventory, worldPosCallable);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
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
				.map(r -> r.assemble(input))
				.collect(Collectors.toList());

		output.clearContent();
		for (int i = 0; i < stacks.size(); i++) {
			output.setItem(i, stacks.get(i));
		}
		this.broadcastChanges();
	}
	
	@Override
	public void slotsChanged(Container inventoryIn) {
		worldPosCallable.execute((world, pos) -> updateOutput(world));
	}

	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);
		worldPosCallable.execute((world, pos) -> clearContainer(playerIn, input));
	}
	
	private class OutputSlot extends Slot {

		public OutputSlot(int index, int xPosition, int yPosition) {
			super(output, index, xPosition, yPosition);
		}
		
		@Override
		public boolean mayPlace(ItemStack stack) {
			return false;
		}
		
		@Override
		public void onTake(Player player, ItemStack stack) {
			
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
		public boolean mayPlace(ItemStack stack) {
			return predicate.test(stack);
		}
	}
}
