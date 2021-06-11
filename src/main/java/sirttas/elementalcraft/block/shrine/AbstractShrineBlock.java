package sirttas.elementalcraft.block.shrine;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.AbstractECBlockEntityProviderBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;

public abstract class AbstractShrineBlock extends AbstractECBlockEntityProviderBlock {

	private final ElementType elementType;

	protected AbstractShrineBlock(ElementType elementType) {
		this.elementType = elementType;
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
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
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final AbstractShrineBlockEntity shrine = (AbstractShrineBlockEntity) world.getBlockEntity(pos);

		if (shrine != null && player.getItemInHand(hand).isEmpty() && player.isShiftKeyDown()) {
			if (world.isClientSide) {
				shrine.startShowingRange();
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.consumes", elementType.getDisplayName()).withStyle(TextFormatting.YELLOW));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		BlockEntityHelper.getTileEntityAs(world, pos, AbstractShrineBlockEntity.class).filter(AbstractShrineBlockEntity::isRunning).ifPresent(s -> this.doAnimateTick(s, state, world, pos, rand));
	}

	@OnlyIn(Dist.CLIENT)
	protected void doAnimateTick(AbstractShrineBlockEntity shrine, BlockState state, World world, BlockPos pos, Random rand) {
		BoneMealItem.addGrowthParticles(world, pos, 1);
	}

}
