package sirttas.elementalcraft.block.shrine.upgrade.vortex;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import javax.annotation.Nonnull;

public class VortexShrineUpgradeBlock extends ShrineUpgradeBlock implements EntityBlock {

	public static final String NAME = "shrine_upgrade_vortex";
	public static final MapCodec<VortexShrineUpgradeBlock> CODEC = simpleCodec(VortexShrineUpgradeBlock::new);

	private static final VoxelShape SHAPE = Block.box(6D, -1D, 6D, 10D, 14D, 10D);

	public VortexShrineUpgradeBlock(BlockBehaviour.Properties properties) {
		super(ShrineUpgrades.VORTEX, properties);
	}

	@Override
	protected @NotNull MapCodec<VortexShrineUpgradeBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new VortexShrineUpgradeBlockEntity(pos, state);
	}
	
	@Nonnull
	@Override
	public Direction getFacing(@Nonnull BlockState state) {
		return Direction.DOWN;
	}

	@Nonnull
    @Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
}
