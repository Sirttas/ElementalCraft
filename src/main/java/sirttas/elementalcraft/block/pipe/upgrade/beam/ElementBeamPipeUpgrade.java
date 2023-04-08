package sirttas.elementalcraft.block.pipe.upgrade.beam;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.transfer.ElementTransfererHelper;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.ConnectionType;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeTransferer;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.config.ECConfig;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ElementBeamPipeUpgrade extends PipeUpgrade {

    public static final String NAME = "element_beam";

    private static final Map<Direction, VoxelShape> SHAPES = ShapeHelper.directionShapes(Block.box(7D, 9.5D, 7D, 9D, 14D, 9D));

    private ElementBeamPipeUpgrade other;
    private boolean linked;

    public ElementBeamPipeUpgrade(ElementPipeBlockEntity pipe, Direction direction) {
        super(PipeUpgradeTypes.ELEMENT_BEAM.get(), pipe, direction);
        this.linked = false;
    }

    @Override
    public VoxelShape getShape() {
        return SHAPES.get(this.getDirection());
    }

    @Override
    public boolean replaceSection() {
        return true;
    }

    @Override
    public boolean canPlace(ConnectionType connectionType) {
        return !connectionType.isConnected();
    }

    public boolean isLinked() {
        tryLink();

        return this.other != null;
    }

    @Override
    public List<BlockPos> getConnections(ElementType type, ConnectionType connection) {
        tryLink();

        return this.other != null ? Collections.singletonList(this.other.getPipe().getBlockPos()) : Collections.emptyList();
    }

    @Override
    public boolean canTransfer(ElementType type, ConnectionType connection) {
        tryLink();

        return this.other != null;
    }

    @Override
    public void added() {
        tryLink();
    }

    private void tryLink() {
        if (!this.linked) {
            this.findOther().ifPresent(b -> {
                var opt = b.findOther();

                if (opt.isPresent() && opt.get() == this) {
                    this.other = b;
                    b.other = this;
                    this.linked = true;
                    b.linked = true;
                }
            });
        }
    }

    @Override
    public void removed() {
        if (this.other != null) {
            this.other.linked = false;
            this.linked = false;
            this.other.other = null;
            this.other = null;
        }
    }

    private Optional<ElementBeamPipeUpgrade> findOther() {
        var level = this.getPipe().getLevel();

        if (level == null) {
            return Optional.empty();
        }

        var pos = this.getPipe().getBlockPos().mutable();
        var direction = this.getDirection();
        var opposite = direction.getOpposite();
        var range = ECConfig.COMMON.elementBeamRange.get();

        for (int i = 0; i < range; i++) {
            pos.move(direction);

            var opt = BlockEntityHelper.getBlockEntity(level, pos)
                    .flatMap(b -> ElementTransfererHelper.get(b, opposite).resolve())
                    .filter(ElementPipeTransferer.class::isInstance)
                    .map(ElementPipeTransferer.class::cast)
                    .map(t -> t.getUpgrade(opposite))
                    .filter(ElementBeamPipeUpgrade.class::isInstance)
                    .map(ElementBeamPipeUpgrade.class::cast);

            if (opt.isPresent()) {
                return opt;
            }
        }
        return Optional.empty();
    }

}
