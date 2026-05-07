package sirttas.elementalcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.spell.Spells;

public class ElementalEmberBlock extends Block {

    public static final String NAME = "elemental_ember";

    private static final VoxelShape SHAPE = Block.box(7D, 7D, 7D, 9D, 9D, 9D);

    public ElementalEmberBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return Math.clamp((int) Spells.LIGHT.get().getStrength(), 0, 15);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double d0 = (double)pos.getX() + 0.5;
        double d1 = (double)pos.getY() + 0.7;
        double d2 = (double)pos.getZ() + 0.5;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
        level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0, 0.0, 0.0);
    }
}
