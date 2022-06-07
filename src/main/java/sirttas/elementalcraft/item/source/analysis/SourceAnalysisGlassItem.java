package sirttas.elementalcraft.item.source.analysis;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.property.ECProperties;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.TreeMap;

public class SourceAnalysisGlassItem extends ECItem implements ISourceInteractable {

	public static final String NAME = "source_analysis_glass";

	
	public SourceAnalysisGlassItem() {
		super(ECProperties.Items.ITEM_UNSTACKABLE);
	}
	
	public static boolean consumeSpringaline(Player player) {
		if (player.isCreative()) {
			return true;
		}

		var inv = player.getInventory();
		var slot = inv.findSlotMatchingItem(new ItemStack(ECItems.SPRINGALINE_SHARD));
		
		if (slot >= 0) {
			var stack = inv.getItem(slot);
			
			if (!stack.isEmpty()) {
				stack.shrink(1);
				if (stack.isEmpty()) {
					inv.setItem(slot, ItemStack.EMPTY);
				}
				return true;
			}
		}
		player.displayClientMessage(new TranslatableComponent("message.elementalcraft.missing_springaline"), true);
		return false;
	}
	
	@Nonnull
    @Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		
		return BlockEntityHelper.getBlockEntityAs(level, pos, SourceBlockEntity.class)
				.map(source -> {
					if (source.isAnalyzed() || consumeSpringaline(player)) {
						source.setAnalyzed(true);
						return open(level, player, source.getTraits());
					}
					return InteractionResult.PASS;
				}).orElse(InteractionResult.PASS);
	}
	
	/**
	 * Called when the equipped item is right clicked.
	 */
	@Nonnull
    @Override
	public InteractionResultHolder<ItemStack> use(@Nonnull Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		return new InteractionResultHolder<>(open(world, player, new TreeMap<>()), stack);
	}
	
	public InteractionResult open(Level world, Player player, Map<SourceTrait, ISourceTraitValue> traiMap) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		player.openMenu(new Menu(traiMap));
		return InteractionResult.CONSUME;
	}

	private class Menu implements MenuProvider {

		private final Map<SourceTrait, ISourceTraitValue> traits;
		
		private Menu(Map<SourceTrait, ISourceTraitValue> traits) {
			this.traits = traits;
		}
		
		@Override
		public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inventory, Player player) {
			return new SourceAnalysisGlassMenu(id, inventory, traits, ContainerLevelAccess.create(player.level, player.getOnPos()));
		}

		@Nonnull
        @Override
		public Component getDisplayName() {
			return SourceAnalysisGlassItem.this.getDescription();
		}
	}
}
