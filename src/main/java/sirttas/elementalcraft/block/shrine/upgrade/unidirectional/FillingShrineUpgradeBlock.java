package sirttas.elementalcraft.block.shrine.upgrade.unidirectional;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;

public class FillingShrineUpgradeBlock extends AbstractShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_filling";

	private static final VoxelShape BASE = Block.box(3D, 4D, 3D, 13D, 8D, 13D);
	private static final VoxelShape PIPE_UP = Block.box(7D, 8D, 7D, 2D, 8D, 2D);
	
	private static final VoxelShape PIPE_NORTH = Block.box(7D, 0D, 4D, 9D, 4D, 6D);
	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 0D, 10D, 9D, 4D, 12D);
	private static final VoxelShape PIPE_WEST = Block.box(4D, 0D, 7D, 6D, 4D, 9D);
	private static final VoxelShape PIPE_EAST = Block.box(10D, 0D, 7D, 12D, 4D, 9D);
	
	private static final VoxelShape PIPE_NORTH_WEST = Block.box(4D, 0D, 4D, 6D, 4D, 6D);
	private static final VoxelShape PIPE_NORTH_EAST = Block.box(10D, 0D, 4D, 12D, 4D, 6D);
	private static final VoxelShape PIPE_SOUTH_WEST = Block.box(4D, 0D, 10D, 6D, 4D, 12D);
	private static final VoxelShape PIPE_SOUTH_EAST = Block.box(10D, 0D, 10D, 12D, 4D, 12D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE, PIPE_UP, PIPE_NORTH, PIPE_SOUTH, PIPE_WEST, PIPE_EAST, PIPE_NORTH_WEST, PIPE_NORTH_EAST, PIPE_SOUTH_WEST, PIPE_SOUTH_EAST);

	@Override
	public Direction getFacing(BlockState state) {
		return Direction.DOWN;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.filling").withStyle(TextFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
}
