package sirttas.elementalcraft.block.shrine.upgrade;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.property.ECProperties;

public abstract class AbstractShrineUpgradeBlock extends Block {

	private ShrineUpgrade upgrade;

	protected AbstractShrineUpgradeBlock() {
		super(ECProperties.Blocks.BLOCK_NOT_SOLID);
	}

	@Override
	@Deprecated
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		BlockEntityHelper.getTileEntityAs(world, pos.relative(getFacing(state)), AbstractShrineBlockEntity.class).ifPresent(AbstractShrineBlockEntity::setChanged);
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		BlockEntityHelper.getTileEntityAs(world, pos.relative(getFacing(state)), AbstractShrineBlockEntity.class).ifPresent(AbstractShrineBlockEntity::setChanged);
		super.onRemove(state, world, pos, newState, isMoving);
	}

	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
		Direction facing = getFacing(state);

		return BlockEntityHelper.getTileEntityAs(world, pos.relative(facing), AbstractShrineBlockEntity.class)
				.filter(shrine -> shrine.getUpgradeDirections().contains(facing.getOpposite()) && upgrade.canUpgrade(shrine)).isPresent();
	}
	
	@Override
	@Deprecated
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (!state.canSurvive(worldIn, pos)) {
			worldIn.destroyBlock(pos, true);
		} else {
			super.tick(state, worldIn, pos, rand);
		}
	}
	
	public abstract Direction getFacing(BlockState state);

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (upgrade != null) {
			upgrade.addInformation(tooltip);
		}
	}

	public ShrineUpgrade getUpgrade() {
		return upgrade;
	}

	void setUpgrade(ShrineUpgrade upgrade) {
		this.upgrade = upgrade;
	}

}
