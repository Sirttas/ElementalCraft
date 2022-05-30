package sirttas.elementalcraft.block.shrine.upgrade.horizontal;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CrystalHarvestShrineUpgradeBlock extends AbstractHorizontalShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_crystal_harvest";

	private static final VoxelShape CORE_1_NORTH = Block.box(5D, 5D, 4D, 11D, 16D, 7D);
	private static final VoxelShape CORE_2_NORTH = Block.box(6D, 6D, 7D, 10D, 10D, 9D);
	private static final VoxelShape PIPE_1_NORTH = Block.box(7D, 7D, 0D, 9D, 9D, 4D);
	private static final VoxelShape PIPE_2_NORTH = Block.box(7D, 13D, -1D, 9D, 15D, 4D);
	private static final VoxelShape SHAPE_NORTH = Shapes.or(CORE_1_NORTH, CORE_2_NORTH, PIPE_1_NORTH, PIPE_2_NORTH);

	private static final VoxelShape CORE_1_SOUTH = Block.box(5D, 5D, 9D, 11D, 16D, 12D);
	private static final VoxelShape CORE_2_SOUTH = Block.box(6D, 6D, 7D, 10D, 10D, 9D);
	private static final VoxelShape PIPE_1_SOUTH = Block.box(7D, 7D, 12D, 9D, 9D, 16D);
	private static final VoxelShape PIPE_2_SOUTH = Block.box(7D, 13D, 12D, 9D, 15D, 17D);
	private static final VoxelShape SHAPE_SOUTH = Shapes.or(CORE_1_SOUTH, CORE_2_SOUTH, PIPE_1_SOUTH, PIPE_2_SOUTH);

	private static final VoxelShape CORE_1_WEST = Block.box(4D, 5D, 5D, 7D, 16D, 11D);
	private static final VoxelShape CORE_2_WEST = Block.box(7D, 6D, 6D, 9D, 10D, 10D);
	private static final VoxelShape PIPE_1_WEST = Block.box(0D, 7D, 7D, 4D, 9D, 9D);
	private static final VoxelShape PIPE_2_WEST = Block.box(-1D, 13D, 7D, 4D, 15D, 9D);
	private static final VoxelShape SHAPE_WEST = Shapes.or(CORE_1_WEST, CORE_2_WEST, PIPE_1_WEST, PIPE_2_WEST);

	private static final VoxelShape CORE_1_EAST = Block.box(9D, 5D, 5D, 12D, 16D, 11D);
	private static final VoxelShape CORE_2_EAST = Block.box(7D, 6D, 6D, 9D, 10D, 10D);
	private static final VoxelShape PIPE_1_EAST = Block.box(12D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape PIPE_2_EAST = Block.box(12D, 13D, 7D, 17D, 15D, 9D);
	private static final VoxelShape SHAPE_EAST = Shapes.or(CORE_1_EAST, CORE_2_EAST, PIPE_1_EAST, PIPE_2_EAST);

	public CrystalHarvestShrineUpgradeBlock() {
		super(ShrineUpgrades.CRYSTAL_HARVEST);
	}
	
	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case SOUTH -> SHAPE_SOUTH;
			case WEST -> SHAPE_WEST;
			case EAST -> SHAPE_EAST;
			default -> SHAPE_NORTH;
		};
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
		tooltip.add(new TranslatableComponent("tooltip.elementalcraft.crystal_harvest").withStyle(ChatFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

}
