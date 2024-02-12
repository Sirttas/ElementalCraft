package sirttas.elementalcraft.block.shrine.upgrade.translocation;

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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.block.shrine.upgrade.directional.AbstractDirectionalShrineUpgradeBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class TranslocationShrineUpgradeBlock extends AbstractDirectionalShrineUpgradeBlock implements EntityBlock {

    public static final String NAME = "shrine_upgrade_translocation";
    public static final MapCodec<TranslocationShrineUpgradeBlock> CODEC = simpleCodec(TranslocationShrineUpgradeBlock::new);

    private static final VoxelShape BASE = Block.box(6D, 8D, 6D, 10D, 12D, 10D);
    private static final VoxelShape PIPE = Block.box(7D, 12D, 7D, 9D, 16D, 9D);
    private static final VoxelShape CONNECTOR = Block.box(7D, 6D, 7D, 9D, 8D, 9D);
    private static final Map<Direction, VoxelShape> SHAPES = ShapeHelper.directionShapes(Shapes.or(BASE, PIPE, CONNECTOR));

    public TranslocationShrineUpgradeBlock(BlockBehaviour.Properties properties) {
        super(ShrineUpgrades.TRANSLOCATION, properties);
    }

    @Override
    protected @NotNull MapCodec<TranslocationShrineUpgradeBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new TranslocationShrineUpgradeBlockEntity(pos, state);
    }

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }
}
