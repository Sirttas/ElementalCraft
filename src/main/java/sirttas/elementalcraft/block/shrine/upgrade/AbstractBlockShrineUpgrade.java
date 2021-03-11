package sirttas.elementalcraft.block.shrine.upgrade;

import java.util.List;

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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.shrine.AbstractTileShrine;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.property.ECProperties;

public abstract class AbstractBlockShrineUpgrade extends Block {

	private ShrineUpgrade upgrade;

	protected AbstractBlockShrineUpgrade() {
		super(ECProperties.Blocks.BLOCK_NOT_SOLID);
	}

	@Override
	@Deprecated
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		TileEntityHelper.getTileEntityAs(world, pos.offset(getFacing(state)), AbstractTileShrine.class).ifPresent(AbstractTileShrine::markDirty);
	}

	@Override
	@Deprecated
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		TileEntityHelper.getTileEntityAs(world, pos.offset(getFacing(state)), AbstractTileShrine.class).ifPresent(AbstractTileShrine::markDirty);
		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	@Deprecated
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		Direction facing = getFacing(state);

		return TileEntityHelper.getTileEntityAs(world, pos.offset(facing), AbstractTileShrine.class)
				.filter(shrine -> shrine.getUpgradeDirections().contains(facing.getOpposite()) && upgrade.canUpgrade(shrine)).isPresent();
	}

	public abstract Direction getFacing(BlockState state);

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
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
