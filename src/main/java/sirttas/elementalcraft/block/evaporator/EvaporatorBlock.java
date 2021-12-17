package sirttas.elementalcraft.block.evaporator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.item.elemental.ShardItem;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class EvaporatorBlock extends AbstractECContainerBlock {

	public static final String NAME = "evaporator";

	private static final VoxelShape BASE_1 = Block.box(6D, 0D, 6D, 10D, 1D, 10D);
	private static final VoxelShape BASE_2 = Block.box(5D, 1D, 5D, 11D, 7D, 11D);

	private static final VoxelShape PIPE_1 = Block.box(3D, 0D, 3D, 5D, 8D, 5D);
	private static final VoxelShape PIPE_2 = Block.box(11D, 0D, 3D, 13D, 8D, 5D);
	private static final VoxelShape PIPE_3 = Block.box(3D, 0D, 11D, 5D, 8D, 13D);
	private static final VoxelShape PIPE_4 = Block.box(11D, 0D, 11D, 13D, 8D, 13D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	@Override
	public EvaporatorBlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new EvaporatorBlockEntity(pos, state);
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createECServerTicker(level, type, EvaporatorBlockEntity.TYPE, EvaporatorBlockEntity::serverTick);
	}
	
	@Nonnull
    @Override
	@Deprecated
	public InteractionResult use(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.isEmpty() || getShardElementType(stack) != ElementType.NONE) {
			return onSingleSlotActivated(world, pos, player, hand);
		}
		return InteractionResult.PASS;
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, @Nonnull LevelReader world, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state.getBlock(), world, pos.below());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Random rand) {
		BlockEntityHelper.getBlockEntityAs(world, pos, EvaporatorBlockEntity.class).filter(EvaporatorBlockEntity::canExtract)
				.ifPresent(evaporator -> ParticleHelper.createElementFlowParticle(evaporator.getElementStorage().getElementType(), world, Vec3.atCenterOf(pos.below()), Direction.DOWN, 1, rand));
	}

	public static ElementType getShardElementType(ItemStack stack) {
		if (!stack.isEmpty()) {
			Item item = stack.getItem();

			if (ECTags.Items.SHARDS.contains(item) && item instanceof ShardItem) {
				return ((ShardItem) item).getElementType();
			}
		}
		return ElementType.NONE;
	}
}
