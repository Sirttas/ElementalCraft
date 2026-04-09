package sirttas.elementalcraft.block.shrine.upgrade.bonelessgrowth;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import javax.annotation.Nonnull;

public class BonelessGrowthShrineUpgradeBlock extends ShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_boneless_growth";
	public static final MapCodec<BonelessGrowthShrineUpgradeBlock> CODEC = simpleCodec(BonelessGrowthShrineUpgradeBlock::new);


	public BonelessGrowthShrineUpgradeBlock(BlockBehaviour.Properties properties) {
		super(ShrineUpgrades.BONELESS_GROWTH, properties);
	}

	@Override
	protected @NotNull MapCodec<BonelessGrowthShrineUpgradeBlock> codec() {
		return CODEC;
	}
	
	@Nonnull
	@Override
	public Direction getFacing(@Nonnull BlockState state) {
		return Direction.DOWN;
	}

	@Nonnull
    @Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return ECShapes.BONELESS_GROWTH;
	}
}
