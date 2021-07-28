package sirttas.elementalcraft.block.shrine;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;

public abstract class AbstractShrineBlock extends AbstractECEntityBlock {

	private final ElementType elementType;

	protected AbstractShrineBlock(ElementType elementType) {
		this.elementType = elementType;
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntityHelper.getTileEntityAs(worldIn, pos, AbstractShrineBlockEntity.class).ifPresent(shrine -> shrine.getUpgradeDirections().forEach(direction -> {
				BlockPos newPos = pos.relative(direction);
				BlockState upgradeState = worldIn.getBlockState(newPos);
				Block block = upgradeState.getBlock();

				if (block instanceof AbstractShrineUpgradeBlock && ((AbstractShrineUpgradeBlock) block).getFacing(upgradeState) == direction.getOpposite()) {
					worldIn.destroyBlock(newPos, true);
				}
			}));
		}
		super.onRemove(state, worldIn, pos, newState, isMoving);
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		final AbstractShrineBlockEntity shrine = (AbstractShrineBlockEntity) world.getBlockEntity(pos);

		if (shrine != null && player.getItemInHand(hand).isEmpty() && player.isShiftKeyDown()) {
			if (world.isClientSide) {
				shrine.startShowingRange();
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(new TranslatableComponent("tooltip.elementalcraft.consumes", elementType.getDisplayName()).withStyle(ChatFormatting.YELLOW));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		BlockEntityHelper.getTileEntityAs(world, pos, AbstractShrineBlockEntity.class).filter(AbstractShrineBlockEntity::isRunning).ifPresent(s -> this.doAnimateTick(s, state, world, pos, rand));
	}

	@OnlyIn(Dist.CLIENT)
	protected void doAnimateTick(AbstractShrineBlockEntity shrine, BlockState state, Level world, BlockPos pos, Random rand) {
		BoneMealItem.addGrowthParticles(world, pos, 1);
	}
	
	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createShrineTicker(Level level, BlockEntityType<T> type, BlockEntityType<? extends AbstractShrineBlockEntity> shrineType) {
		return level.isClientSide ? null : createECTicker(level, type, shrineType, AbstractShrineBlockEntity::serverTick);
	}

}
