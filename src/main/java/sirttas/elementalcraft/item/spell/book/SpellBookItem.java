package sirttas.elementalcraft.item.spell.book;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.spell.SpellHelper;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

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
	public InteractionResult use(@Nonnull Level level, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		return open(level, player, stack);
	}
	
	public InteractionResult open(Level level, Player player, ItemStack stack) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		player.openMenu(new ContainerProvider(stack));
		return InteractionResult.CONSUME;
	}

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> builder, @NotNull TooltipFlag tooltipFlag) {
		SpellHelper.getSpellList(itemStack).forEachSpell((spell, count) -> {
			if (count == 1) {
                builder.accept(Component.empty().append(spell.value().getDisplayName()).withStyle(ChatFormatting.GRAY));
			} else {
                builder.accept(Component.literal(count + " ").append(spell.value().getDisplayName()).withStyle(ChatFormatting.GRAY));
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
