package sirttas.elementalcraft.block.pipe.upgrade.pump;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPath;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPathNode;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pipe.ConnectionType;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeShapes;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ElementPumpPipeUpgrade extends PipeUpgrade {

    public static final String NAME = "element_pump";

    private static final Map<Direction, VoxelShape> SHAPES = ShapeHelper.directionShapes(Shapes.or(
            Block.box(6.1D, 9D, 6.1D, 7.1D, 15D, 7.1D),
            Block.box(6.1D, 9D, 8.9D, 7.1D, 15D, 9.9D),
            Block.box(8.9D, 9D, 6.1D, 9.9D, 15D, 7.1D),
            Block.box(8.9D, 9D, 8.9D, 9.9D, 15D, 9.9D),
            ElementPipeShapes.SECTION_SHAPES.get(Direction.UP),
            ElementPipeShapes.EXTRACTION_SHAPES.get(Direction.UP)
    ));

    private final RuneHandler runeHandler;

    public ElementPumpPipeUpgrade(ElementPipeBlockEntity pipe, Direction direction) {
        super(PipeUpgradeTypes.ELEMENT_PUMP.get(), pipe, direction);
        runeHandler = new RuneHandler(ECConfig.COMMON.elementPumpMaxRunes.get(), pipe::setChanged);
    }

    @Override
    public IElementTransferPath alterPath(@Nonnull IElementTransferPath path) {
        return new Path(path);
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
        return super.canPlace(connectionType) && connectionType == ConnectionType.EXTRACT && this.getPipe().getBlockState().is(ECBlocks.PIPE_IMPROVED.get());
    }

    public RuneHandler getRuneHandler() {
        return runeHandler;
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        if (compound.contains(ECNames.RUNE_HANDLER)) {
            IRuneHandler.readNBT(getRuneHandler(), compound.getList(ECNames.RUNE_HANDLER, 8));
        }
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(getRuneHandler()));
    }

    private class Path implements IElementTransferPath {

        private final IElementTransferPath parent;
        private final List<IElementTransferPathNode> nodes;

        private Path(IElementTransferPath parent) {
            this.parent = parent;
            this.nodes = parent.getNodes();
        }

        @Override
        public boolean isValid() {
            return parent.isValid();
        }

        private int getRemainingTransferAmount() {
            return nodes.stream()
                    .map(IElementTransferPathNode::getTransferer)
                    .filter(Objects::nonNull)
                    .mapToInt(IElementTransferer::getRemainingTransferAmount)
                    .min()
                    .orElse(0);
        }

        @Override
        public void transfer() {
            if (!isValid()) {
                return;
            }

            var type = parent.getElementType();
            var multiplier = runeHandler.getTransferSpeed(ECConfig.COMMON.elementPumpMultiplier.get().floatValue());
            var waste = Math.max(0, ECConfig.COMMON.elementPumpWaste.get().floatValue() / runeHandler.getElementPreservation());
            var source = nodes.get(0).getStorage();
            var target = nodes.get(nodes.size() - 1).getStorage();

            if (source == null || target == null) {
                return;
            }

            IElementTransferPath.transfer(type, Math.round(source.transferTo(target, type, (getRemainingTransferAmount() * multiplier), 1F - waste) / multiplier), nodes);
        }

        @Override
        public List<IElementTransferPathNode> getNodes() {
            return List.copyOf(nodes);
        }

        @Override
        public ElementType getElementType() {
            return parent.getElementType();
        }
    }
}
