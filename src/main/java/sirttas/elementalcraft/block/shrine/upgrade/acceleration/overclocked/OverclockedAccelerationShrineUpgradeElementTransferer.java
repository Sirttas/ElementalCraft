package sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPathNode;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.AbstractHorizontalShrineUpgradeBlock;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class OverclockedAccelerationShrineUpgradeElementTransferer implements IElementTransferer {

    private final BlockEntity upgrade;
    private AbstractShrineBlockEntity shrine;
    private ElementPipeBlockEntity pipe;

    public OverclockedAccelerationShrineUpgradeElementTransferer(BlockEntity upgrade) {
        this.upgrade = upgrade;
    }

    @Override
    public synchronized List<IElementTransferPathNode> getConnectedNodes(@Nonnull ElementType type) {
        refreshShrine();
        refreshPipe();

        if (shrine != null && pipe != null) {
            return List.of(new ShrineNode(shrine), new PipeNode(pipe));
        } else if (shrine != null) {
            return List.of(new ShrineNode(shrine));
        } else if (pipe != null) {
            return List.of(new PipeNode(pipe));
        }
        return Collections.emptyList();
    }

    @Override
    public boolean canConnectTo(@Nonnull BlockPos to) {
        return to.equals(upgrade.getBlockPos().relative(getFacing().getOpposite()));
    }

    private synchronized void refreshShrine() {
        shrine = getBlockEntity(shrine, getFacing(), AbstractShrineBlockEntity.class);
    }

    private synchronized void refreshPipe() {
        pipe = getBlockEntity(pipe, getFacing().getOpposite(), ElementPipeBlockEntity.class);
    }

    private <T extends BlockEntity> T getBlockEntity(T existingEntity, Direction direction, Class<T> clazz) {
        if (existingEntity != null && !existingEntity.isRemoved()) {
            return existingEntity;
        }

        var level = upgrade.getLevel();

        if (level == null) {
            return null;
        }
        return BlockEntityHelper.getBlockEntityAs(level, upgrade.getBlockPos().relative(direction), clazz).orElse(null);
    }

    @Nonnull
    private Direction getFacing() {
        return upgrade.getBlockState().getValue(AbstractHorizontalShrineUpgradeBlock.FACING);
    }

    public void setShrine(AbstractShrineBlockEntity shrine) {
        this.shrine = shrine;
    }

    private record ShrineNode(AbstractShrineBlockEntity shrine) implements IElementTransferPathNode {
        @Override
        public BlockPos getPos() {
            return shrine.getBlockPos();
        }

        @Override
        public IElementStorage getStorage() {
            return shrine.getElementStorage();
        }
    }

    private record PipeNode(ElementPipeBlockEntity pipe) implements IElementTransferPathNode {
        @Override
        public BlockPos getPos() {
            return pipe.getBlockPos();
        }

        @Override
        public IElementTransferer getTransferer() {
            return pipe.getTransferer();
        }
    }
}
