package sirttas.elementalcraft.item.spell.book;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.spell.SpellHelper;

public class ItemSpellBook extends ItemEC {

	public static final String NAME = "spell_book";

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
		player.openContainer(new ContainerProvider(stack));
		return ActionResultType.CONSUME;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		SpellHelper.forEachSpell(stack, (spell, count) -> {
			if (count == 1) {
				tooltip.add(new StringTextComponent("").appendSibling(spell.getDisplayName()).mergeStyle(TextFormatting.GRAY));
			} else {
				tooltip.add(new StringTextComponent(count + " ").appendSibling(spell.getDisplayName()).mergeStyle(TextFormatting.GRAY));
			}
		});
	}

	@Override
	public int getDamage(ItemStack stack) {
		return ECConfig.COMMON.spellBookMaxSpell.get() - SpellHelper.getSpellCount(stack);
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return ECConfig.COMMON.spellBookMaxSpell.get();
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	private static class ContainerProvider implements INamedContainerProvider {

		private final ItemStack stack;

		private ContainerProvider(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public Container createMenu(int id, PlayerInventory inventory, PlayerEntity palyer) {
			return SpellBookContainer.create(id, inventory, stack);
		}

		@Override
		public ITextComponent getDisplayName() {
			return stack.getDisplayName();
		}

	}

}
