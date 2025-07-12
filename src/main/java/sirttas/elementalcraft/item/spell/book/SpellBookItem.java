package sirttas.elementalcraft.item.spell.book;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.spell.SpellHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SpellBookItem extends Item {

	public static final String NAME = "spell_book";
	
	public SpellBookItem(Item.Properties properties) {
		super(properties);
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Nonnull
    @Override
	public InteractionResultHolder<ItemStack> use(@Nonnull Level level, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		return new InteractionResultHolder<>(open(level, player, stack), stack);
	}
	
	public InteractionResult open(Level level, Player player, ItemStack stack) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		player.openMenu(new ContainerProvider(stack));
		return InteractionResult.CONSUME;
	}

	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Item.TooltipContext tooltipContext, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		SpellHelper.getSpellList(stack).forEachSpell((spell, count) -> {
			if (count == 1) {
				tooltip.add(Component.empty().append(spell.value().getDisplayName()).withStyle(ChatFormatting.GRAY));
			} else {
				tooltip.add(Component.literal(count + " ").append(spell.value().getDisplayName()).withStyle(ChatFormatting.GRAY));
			}
		});
	}


	@Override
	public int getBarWidth(@Nonnull ItemStack stack) {
		return Math.round(ECConfig.SERVER.spellBookMaxSpell.get() - SpellHelper.getSpellList(stack).count() * 13F / ECConfig.SERVER.spellBookMaxSpell.get());
	}
	
	private static class ContainerProvider implements MenuProvider {

		private final ItemStack stack;

		private ContainerProvider(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inventory, @Nonnull Player palyer) {
			return SpellBookMenu.create(id, inventory, stack);
		}

		@Nonnull
        @Override
		public Component getDisplayName() {
			return stack.getHoverName();
		}

	}

}
