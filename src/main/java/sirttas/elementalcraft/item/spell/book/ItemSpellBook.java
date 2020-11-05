package sirttas.elementalcraft.item.spell.book;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.property.ECProperties;

public class ItemSpellBook extends ItemEC {

	public ItemSpellBook() {
		super(ECProperties.Items.ITEM_UNSTACKABLE);
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);

		return new ActionResult<>(open(world, player, stack), stack);
	}

	public ActionResultType open(World world, PlayerEntity player, ItemStack stack) {
		if (world.isRemote) {
			return ActionResultType.SUCCESS;
		}
		player.openContainer(getContainer(stack));
		return ActionResultType.CONSUME;
	}

	private INamedContainerProvider getContainer(ItemStack stack) {
		return new ContainerProvider(stack);
	}


	private class ContainerProvider implements INamedContainerProvider {

		private final ItemStack stack;

		private ContainerProvider(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public Container createMenu(int id, PlayerInventory inventory, PlayerEntity palyer) {
			return ChestContainer.createGeneric9X3(id, inventory, null);
		}

		@Override
		public ITextComponent getDisplayName() {
			return ItemSpellBook.this.getDisplayName(stack);
		}

	}

}
