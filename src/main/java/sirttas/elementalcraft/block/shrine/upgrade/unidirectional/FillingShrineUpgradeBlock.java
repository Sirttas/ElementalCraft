package sirttas.elementalcraft.block.shrine.upgrade.unidirectional;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;

public class FillingShrineUpgradeBlock extends AbstractShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_filling";

	private static final VoxelShape BASE = Block.box(3D, 4D, 3D, 13D, 8D, 13D);
	private static final VoxelShape PIPE_UP = Block.box(7D, 8D, 7D, 9D, 16D, 9D);
	
	private static final VoxelShape PIPE_NORTH = Block.box(7D, 0D, 4D, 9D, 4D, 6D);
	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 0D, 10D, 9D, 4D, 12D);
	private static final VoxelShape PIPE_WEST = Block.box(4D, 0D, 7D, 6D, 4D, 9D);
	private static final VoxelShape PIPE_EAST = Block.box(10D, 0D, 7D, 12D, 4D, 9D);
	
	private static final VoxelShape PIPE_NORTH_WEST = Block.box(4D, 0D, 4D, 6D, 4D, 6D);
	private static final VoxelShape PIPE_NORTH_EAST = Block.box(10D, 0D, 4D, 12D, 4D, 6D);
	private static final VoxelShape PIPE_SOUTH_WEST = Block.box(4D, 0D, 10D, 6D, 4D, 12D);
	private static final VoxelShape PIPE_SOUTH_EAST = Block.box(10D, 0D, 10D, 12D, 4D, 12D);

	private static final VoxelShape SHAPE = Shapes.or(BASE, PIPE_UP, PIPE_NORTH, PIPE_SOUTH, PIPE_WEST, PIPE_EAST, PIPE_NORTH_WEST, PIPE_NORTH_EAST, PIPE_SOUTH_WEST, PIPE_SOUTH_EAST);

	public FillingShrineUpgradeBlock() {
		super(ElementalCraft.createRL(NAME));
	}
	
	@Override
	public Direction getFacing(BlockState state) {
		return Direction.DOWN;
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(new TranslatableComponent("tooltip.elementalcraft.filling").withStyle(ChatFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
}
