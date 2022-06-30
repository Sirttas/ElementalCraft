package sirttas.elementalcraft.block.source.displacement.plate;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;

import javax.annotation.Nonnull;

public class SourceDisplacementPlateBlockEntity extends AbstractECBlockEntity implements IElementTypeProvider {

    private boolean running;
    private int runningTick;

    public SourceDisplacementPlateBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.SOURCE_DISPLACEMENT_PLATE, pos, state);
        running = false;
        runningTick = 0;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasSource(Level level, BlockPos pos, ElementType elementType) {
        var above = pos.above();
        var state =  level.getBlockState(above);

        return state.getBlock() == ECBlocks.SOURCE.get() && ElementType.getElementType(state) == elementType && BlockEntityHelper.getBlockEntityAs(level, above, SourceBlockEntity.class).filter(SourceBlockEntity::isAnalyzed).isPresent();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SourceDisplacementPlateBlockEntity sourceDisplacementPlate) {
        if (!sourceDisplacementPlate.running || !hasSource(level, pos,  sourceDisplacementPlate.getElementType())) {
            return;
        }
        sourceDisplacementPlate.runningTick++;
        if (sourceDisplacementPlate.runningTick >= 300) {
            sourceDisplacementPlate.runningTick = 0;
            sourceDisplacementPlate.running = false;

            if (!level.isClientSide) {
                spawnReceptacle(level, pos);
                level.setBlockAndUpdate(pos, ECBlocks.BROKEN_SOURCE_DISPLACEMENT_PLATE.get().defaultBlockState());
                level.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
            }
        }
    }

    private static void spawnReceptacle(Level level, BlockPos pos) {
        var source = BlockEntityHelper.getBlockEntityAs(level, pos.above(), SourceBlockEntity.class).orElse(null);

        if (source == null) {
            return;
        }

        var receptacle = ReceptacleHelper.create(source.getElementType());

        if (source.isStabilized()) {
            source.setStabilized(false);
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, new ItemStack(ECItems.SOURCE_STABILIZER.get())));
        }
        source.saveAdditional(receptacle.getOrCreateTagElement(ECNames.BLOCK_ENTITY_TAG));
        level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, receptacle));
    }

    @Override
    public ElementType getElementType() {
        return ((IElementTypeProvider) this.getBlockState().getBlock()).getElementType();
    }

    public boolean isRunning() {
        return running;
    }

    public int getRunningTick() {
        return runningTick;
    }

    public void start() {
        if (this.level == null || !hasSource(this.level, this.getBlockPos(), getElementType()) || this.running) {
            return;
        }
        this.running = true;
        this.runningTick = 0;
        this.setChanged();
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        running = compound.getBoolean(ECNames.RUNNING);
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean(ECNames.RUNNING, running);
    }
}
