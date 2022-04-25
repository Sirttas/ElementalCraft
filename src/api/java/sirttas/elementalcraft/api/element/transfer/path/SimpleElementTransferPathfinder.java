package sirttas.elementalcraft.api.element.transfer.path;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.transfer.CapabilityElementTransferer;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class SimpleElementTransferPathfinder {

    private ElementType type;
    private final Deque<AbstractNode> nodes;
    private final List<ProcessedNode> path;
    private final List<IElementTransferer> visited;
    private final Level level;
    private IElementStorage target;

    public SimpleElementTransferPathfinder(Level level) {
        this.type = ElementType.NONE;
        this.level = level;
        this.nodes = new ArrayDeque<>();
        this.path = new ArrayList<>();
        this.visited = new ArrayList<>();
    }

    public IElementTransferPath findPath(ElementType type, IElementStorage source, IElementTransferer first, BlockPos start) {
        if (type != ElementType.NONE) {
            var profiler = level.getProfiler();

            profiler.push("elementalcraft:simple_element_transfer_pathfinding");
            this.type = type;
            this.target = null;
            this.nodes.clear();
            this.path.clear();
            this.visited.clear();
            nodes.push(new ConnectNode(null, start, first, null));
            while (!nodes.isEmpty() && target == null) {
                nodes.pop().run();
            }
            profiler.pop();
            if (target != null) {
                return new Path(source, target, type, path);
            }
        }
        return InvalidElementTransferPath.INSTANCE;
    }

    private record Path(
            IElementStorage source,
            IElementStorage target,
            ElementType type,
            List<ProcessedNode> path
    ) implements IElementTransferPath {

        @Override
        public boolean isValid() {
            return !this.path.isEmpty() && this.source != null && this.target != null && path.stream().allMatch(ProcessedNode::isValid);
        }

        private int getRemainingTransferAmount() {
            return path.stream()
                    .mapToInt(node -> node.transferer.getRemainingTransferAmount())
                    .min()
                    .orElse(0);
        }

        @Override
        public void transfer() {
            if (isValid()) {
                int amount = source.extractElement(getRemainingTransferAmount(), type, true);

                if (amount > 0) {
                    int remainingAmount = amount - source.transferTo(target, type, amount);

                    if (remainingAmount > 0) {
                        path.forEach(p -> p.transferer.transfer(remainingAmount));
                    }
                }
            }
        }
    }

    private record ProcessedNode(
            IElementTransferer transferer,
            BlockPos pos,
            Direction side
    ) {
        public boolean isValid() {
            return transferer.isValid() && (side == null || transferer.getConnection(side).isConnected());
        }
    }

    private abstract class AbstractNode implements Runnable {

        protected final ConnectNode parent;
        protected final BlockPos pos;
        protected final Direction side;

        private AbstractNode(ConnectNode parent, BlockPos pos, Direction side) {
            this.parent = parent;
            this.pos = pos;
            this.side = side;
        }
    }

    private class InsertNode extends AbstractNode {

        private final IElementStorage storage;

        private InsertNode(ConnectNode parent, BlockPos pos, IElementStorage storage, Direction side) {
            super(parent, pos, side);
            this.storage = storage;
        }

        @Override
        public void run() {
            target = storage;

            var p = parent;
            while (p != null) {
                path.add(new ProcessedNode(p.transferer, p.pos, p.side));
                p = p.parent;
            }
        }
    }

    private class ConnectNode extends AbstractNode {

        private final IElementTransferer transferer;

        private ConnectNode(ConnectNode parent, BlockPos pos, IElementTransferer transferer, Direction side) {
            super(parent, pos, side);
            this.transferer = transferer;
        }

        @Override
        public void run() {
            transferer.getConnectionStream().forEach(entry -> {
                Direction side = entry.getKey();
                IElementTransferer.ConnectionType connection = entry.getValue();

                if (connection == IElementTransferer.ConnectionType.INSERT) {
                    getConnectedCapability(side, CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY).ifPresent(s -> {
                        if (s.canPipeInsert(type) && s.insertElement(1, type, true) == 0) {
                            nodes.push(new InsertNode(this, pos.relative(side), s, side.getOpposite()));
                        }
                    });
                } else if (connection == IElementTransferer.ConnectionType.CONNECT) {
                    getConnectedCapability(side, CapabilityElementTransferer.ELEMENT_TRANSFERER_CAPABILITY).ifPresent(t -> {
                        if (!visited.contains(t) && t.isValid()) {
                            visited.add(t);
                            nodes.push(new ConnectNode(this, pos.relative(side), t, side.getOpposite()));
                        }
                    });
                }
            });
        }

        private <T> LazyOptional<T> getConnectedCapability(Direction face, Capability<T> cap) {
            var be = level.getBlockEntity(pos.relative(face));

            if (be != null) {
                return be.getCapability(cap, face.getOpposite());
            }
            return LazyOptional.empty();
        }

    }

}
