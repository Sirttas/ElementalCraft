package sirttas.elementalcraft.block.shrine.upgrade.horizontal;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
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

public class ProtectionShrineUpgradeBlock extends AbstractHorizontalShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_protection";


	private static final VoxelShape PIPE_NORTH = Block.box(7D, 7D, 0D, 9D, 9D, 2D);
	private static final VoxelShape SHIELD_1_NORTH = Block.box(5D, 5D, 2D, 11D, 12D, 3D);
	private static final VoxelShape SHIELD_2_NORTH = Block.box(6D, 4D, 2D, 10D, 5D, 3D);
	private static final VoxelShape SHAPE_NORTH = VoxelShapes.or(PIPE_NORTH, SHIELD_1_NORTH, SHIELD_2_NORTH);

	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 7D, 14D, 9D, 9D, 16D);
	private static final VoxelShape SHIELD_1_SOUTH = Block.box(5D, 5D, 13D, 9D, 9D, 14D);
	private static final VoxelShape SHIELD_2_SOUTH = Block.box(6D, 4D, 13D, 10D, 5D, 14D);
	private static final VoxelShape SHAPE_SOUTH = VoxelShapes.or(PIPE_SOUTH, SHIELD_1_SOUTH, SHIELD_2_SOUTH);

	private static final VoxelShape PIPE_WEST = Block.box(0D, 7D, 7D, 2D, 9D, 9D);
	private static final VoxelShape SHIELD_1_WEST = Block.box(2D, 5D, 5D, 3D, 12D, 11D);
	private static final VoxelShape SHIELD_2_WEST = Block.box(2D, 4D, 6D, 3D, 5D, 10D);
	private static final VoxelShape SHAPE_WEST = VoxelShapes.or( PIPE_WEST, SHIELD_1_WEST, SHIELD_2_WEST);

	private static final VoxelShape PIPE_EAST = Block.box(14D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape SHIELD_1_EAST = Block.box(13D, 5D, 5D, 14D, 9D, 9D);
	private static final VoxelShape SHIELD_2_EAST = Block.box(13D, 4D, 6D, 14D, 5D, 10D);
	private static final VoxelShape SHAPE_EAST = VoxelShapes.or(PIPE_EAST, SHIELD_1_EAST, SHIELD_2_EAST);


	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.getValue(FACING)) {
		case SOUTH:
			return SHAPE_SOUTH;
		case WEST:
			return SHAPE_WEST;
		case EAST:
			return SHAPE_EAST;
		default:
			return SHAPE_NORTH;
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.protection").withStyle(TextFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

}
