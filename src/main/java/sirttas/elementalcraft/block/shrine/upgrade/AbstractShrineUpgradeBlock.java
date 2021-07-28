package sirttas.elementalcraft.block.shrine.upgrade;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
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
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		BlockEntityHelper.getTileEntityAs(world, pos.relative(getFacing(state)), AbstractShrineBlockEntity.class).ifPresent(AbstractShrineBlockEntity::setChanged);
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		BlockEntityHelper.getTileEntityAs(world, pos.relative(getFacing(state)), AbstractShrineBlockEntity.class).ifPresent(AbstractShrineBlockEntity::setChanged);
		super.onRemove(state, world, pos, newState, isMoving);
	}

	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		Direction facing = getFacing(state);

		return BlockEntityHelper.getTileEntityAs(world, pos.relative(facing), AbstractShrineBlockEntity.class)
				.filter(shrine -> shrine.getUpgradeDirections().contains(facing.getOpposite()) && upgrade.canUpgrade(shrine)).isPresent();
	}
	
	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		if (!state.canSurvive(worldIn, pos)) {
			worldIn.destroyBlock(pos, true);
		} else {
			super.tick(state, worldIn, pos, rand);
		}
	}
	
	public abstract Direction getFacing(BlockState state);

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
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
