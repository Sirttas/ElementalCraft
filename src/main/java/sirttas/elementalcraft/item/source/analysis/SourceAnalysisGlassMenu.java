package sirttas.elementalcraft.item.source.analysis;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.source.trait.SourceTraitHelper;
import sirttas.elementalcraft.container.menu.AbstractECMenu;
import sirttas.elementalcraft.container.menu.ECMenus;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.network.message.MessageHelper;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class SourceAnalysisGlassMenu extends AbstractECMenu {

	private final Container input;
	private final Container output;
	private final Player player;
	private final ContainerLevelAccess worldPosCallable;
	private Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits;
	
	public SourceAnalysisGlassMenu(int id, Inventory inventory) {
		this(id, inventory, new HashMap<>(), ContainerLevelAccess.NULL);
	}
	
	public SourceAnalysisGlassMenu(int id, Inventory inventory, Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits, ContainerLevelAccess worldPosCallable) {
		super(ECMenus.SOURCE_ANALYSIS_GLASS, id);
		this.player = inventory.player;
		input = new CraftingContainer(this, 1, 1);
		output = new SimpleContainer(1);
		this.worldPosCallable = worldPosCallable;
		this.traits = traits;
		addSlots(inventory);
	}
	
	private void addSlots(Inventory inventory) {
		this.addSlot(new InputSlot(0, 152, 18));
		this.addSlot(new OutputSlot(0, 152, 66));
		addPlayerSlots(inventory, 98);
	}

	public Map<ResourceKey<SourceTrait>, ISourceTraitValue> getTraits() {
		return traits;
	}

	public void setTraits(Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits) {
		this.traits = traits;
	}
	
	@Override
	public void onOpen(Player player) {
		sendTraits();
	}
	
	@Nonnull
	@Override
	public ItemStack quickMoveStack(@Nonnull Player player, int index) {
		Slot slot = this.slots.get(index);

		if (slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			ItemStack stack = slotStack.copy();

			if (index < 1) {
				if (!this.moveItemStackTo(slotStack, 2, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (index < 2) {
				if (!this.moveItemStackTo(slotStack, 2, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
				slot.onTake(player, slotStack);
				return ItemStack.EMPTY;
			} else if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
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
	
	private void analyseReceptacle() {
		var stack = input.getItem(0);
		
		if (!stack.isEmpty() && output.getItem(0).isEmpty()) {
			var blockEntityTag = stack.getTagElement(ECNames.BLOCK_ENTITY_TAG);
				
			if (blockEntityTag != null) {
				boolean analyzed = blockEntityTag.getBoolean(ECNames.ANALYZED);
				
				if (analyzed || SourceAnalysisGlassItem.consumeSpringaline(player)) {
					blockEntityTag.putBoolean(ECNames.ANALYZED, true);
					SourceTraitHelper.loadTraits(blockEntityTag.getCompound(ECNames.TRAITS), traits);
					output.setItem(0, stack);
					input.setItem(0, ItemStack.EMPTY);
					sendTraits();
				}
			}
		}
	}
	
	@Override
	public void slotsChanged(@Nonnull Container inv) {
		worldPosCallable.execute((world, pos) -> analyseReceptacle());
	}
	
	@Override
	public void removed(@Nonnull Player player) {
		super.removed(player);
		worldPosCallable.execute((level, pos) -> {
			clearContainer(player, input);
			clearContainer(player, output);
		});
	}
	
	private void sendTraits() {
		if (player instanceof ServerPlayer serverPlayer) {
			MessageHelper.sendToPlayer(serverPlayer, new SourceAnalysisGlassMessage(traits));
		}
	}
	
	private class OutputSlot extends Slot {

		public OutputSlot(int index, int xPosition, int yPosition) {
			super(output, index, xPosition, yPosition);
		}
		
		@Override
		public boolean mayPlace(@Nonnull ItemStack stack) {
			return false;
		}
	}
	
	private class InputSlot extends Slot {
		
		public InputSlot(int index, int xPosition, int yPosition) {
			super(input, index, xPosition, yPosition);
		}
		
		@Override
		public boolean mayPlace(ItemStack stack) {
			return stack.is(ECItems.RECEPTACLE.get());
		}
		
		@Override
		public void setChanged() {
			super.setChanged();
			worldPosCallable.execute((level, pos) -> analyseReceptacle());
		}
	}
}
