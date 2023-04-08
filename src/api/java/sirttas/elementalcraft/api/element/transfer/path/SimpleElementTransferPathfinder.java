package sirttas.elementalcraft.api.element.transfer.path;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

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


    public IElementTransferPath findPath(ElementType type, IElementTransferPathNode source, IElementTransferPathNode first) {
        if (type != ElementType.NONE) {
            var profiler = level.getProfiler();

            profiler.push("elementalcraft:simple_element_transfer_pathfinding");
            this.type = type;
            this.target = null;
            this.nodes.clear();
            this.path.clear();
            this.visited.clear();
            nodes.push(new ConnectNode(null, first.getPos(), first.getTransferer()));
            while (!nodes.isEmpty() && target == null) {
                nodes.pop().run();
            }
            profiler.pop();
            if (target != null) {
                path.add(0, ProcessedNode.wrap(source));
                return new Path(source.getStorage(), target, type, path);
            }
        }
        return InvalidElementTransferPath.INSTANCE;
    }

    private record Path(
            IElementStorage source,
            IElementStorage target,
            ElementType type,
            List<ProcessedNode> nodes
    ) implements IElementTransferPath {

        @Override
        public boolean isValid() {
            return !this.nodes.isEmpty() && this.target != null && nodes.stream().allMatch(ProcessedNode::isValid);
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
            if (isValid()) {
                int amount = source.transferTo(target, type, getRemainingTransferAmount());

                if (amount > 0) {
                    nodes.forEach(p -> {
                        if (p.transferer != null) {
                            p.transferer.transfer(amount);
                        }
                    });
                }
            }
        }

        @Override
        public List<IElementTransferPathNode> getNodes() {
            return List.copyOf(nodes);
        }

        @Override
        public ElementType getElementType() {
            return type;
        }
    }

    private record ProcessedNode(
            IElementTransferer transferer,
            IElementStorage storage,
            BlockPos pos
    ) implements IElementTransferPathNode {

        public static ProcessedNode wrap(IElementTransferPathNode node) {
            return new ProcessedNode(node.getTransferer(), node.getStorage(), node.getPos());
        }
        public boolean isValid() {
            return transferer == null || transferer.isValid();
        }

        @Override
        public BlockPos getPos() {
            return pos;
        }

        @Override
        public IElementTransferer getTransferer() {
            return transferer;
        }

        @Override
        public IElementStorage getStorage() {
            return storage;
        }
    }

    private abstract class AbstractNode implements Runnable {

        protected final ConnectNode parent;
        protected final BlockPos pos;

        private AbstractNode(ConnectNode parent, BlockPos pos) {
            this.parent = parent;
            this.pos = pos;
        }
    }

    private class InsertNode extends AbstractNode {

        private final IElementStorage storage;

        private InsertNode(ConnectNode parent, BlockPos pos, IElementStorage storage) {
            super(parent, pos);
            this.storage = storage;
        }

        @Override
        public void run() {
            target = storage;

            var p = parent;
            while (p != null) {
                path.add(new ProcessedNode(p.transferer, null, p.pos));
                path.add(new ProcessedNode(null, storage, pos));
                p = p.parent;
            }
        }
    }

    private class ConnectNode extends AbstractNode {

        private final IElementTransferer transferer;

        private ConnectNode(ConnectNode parent, BlockPos pos, IElementTransferer transferer) {
            super(parent, pos);
            this.transferer = transferer;
        }

        @Override
        public void run() {
            transferer.getConnectedNodes(type).forEach(node -> {
                var nodePos = node.getPos();
                var nodeStorage = node.getStorage();

                if (nodeStorage != null && nodeStorage.insertElement(1, type, true) == 0) {
                    nodes.push(new InsertNode(this, nodePos, nodeStorage));
                }

                var nodeTransferer = node.getTransferer();

                if (nodeTransferer != null && !visited.contains(nodeTransferer) && nodeTransferer.isValid()) {
                    visited.add(nodeTransferer);
                    nodes.push(new ConnectNode(this, nodePos, nodeTransferer));
                }
            });
        }

    }

}
