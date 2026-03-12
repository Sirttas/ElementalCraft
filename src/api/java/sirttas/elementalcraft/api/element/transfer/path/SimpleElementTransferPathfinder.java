package sirttas.elementalcraft.api.element.transfer.path;

import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleElementTransferPathfinder {

    private ElementType type;
    private IElementTransferPathNode source;
    private final Deque<NodeVisitor> nodes;
    private final List<Path> paths;
    private final List<IElementTransferer> visited;
    private final Level level;

    public SimpleElementTransferPathfinder(Level level) {
        this.type = ElementType.NONE;
        this.source = null;
        this.level = level;
        this.nodes = new ArrayDeque<>();
        this.paths = new LinkedList<>();
        this.visited = new ArrayList<>();
    }

    public synchronized List<IElementTransferPath> findPaths(ElementType type, IElementTransferPathNode source, IElementTransferPathNode first) {
        if (type == ElementType.NONE) {
            return Collections.emptyList();
        }
        Profiler.get().push("elementalcraft:simple_element_transfer_pathfinding");
        this.type = type;
        this.source = source;
        this.nodes.clear();
        this.paths.clear();
        this.visited.clear();
        nodes.push(new NodeVisitor(null, first));
        while (!nodes.isEmpty()) {
            nodes.pop().visit();
        }
        paths.sort(Comparator.comparing(Path::weight));
        Profiler.get().pop();
        return List.copyOf(paths);
    }

    private record Path(
            IElementStorage source,
            IElementStorage target,
            ElementType type,
            List<IElementTransferPathNode> nodes,
            int weight
    ) implements IElementTransferPath {

        public Path(IElementStorage source, IElementStorage target, ElementType type, List<IElementTransferPathNode> nodes) {
            this(source, target, type, List.copyOf(nodes), getWeight(type, nodes));
        }

        private static int getWeight(ElementType type, List<IElementTransferPathNode> nodes) {
            var weight = new AtomicInteger(0);

            IElementTransferPathNode.forEachNodes(nodes, (node, prev, next) -> weight.addAndGet(node.getWeight(type, prev, next)));
            return weight.get();
        }

        @Override
        public boolean isValid() {
            return !this.nodes.isEmpty() && this.target != null && nodes.stream().allMatch(n -> {
                var transferer = n.getTransferer();

                return transferer == null || transferer.isValid();
            });
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

            IElementTransferPath.transfer(type, source.transferTo(target, type, getRemainingTransferAmount()), getNodes());
        }

        @Override
        public List<IElementTransferPathNode> getNodes() {
            return List.copyOf(nodes);
        }

        @Override
        public @NotNull ElementType getElementType() {
            return type;
        }
    }

    private class NodeVisitor {

        final NodeVisitor parent;
        final IElementTransferPathNode node;

        private NodeVisitor(NodeVisitor parent, IElementTransferPathNode node) {
            this.parent = parent;
            this.node = node;
        }

        public void visit() {
            var storage = node.getStorage();

            if (storage != null) {
                paths.add(createPath(storage));
            }

            var transferer = node.getTransferer();

            if (transferer != null && !visited.contains(transferer) && transferer.isValid()) {
                transferer.getConnectedNodes(type).forEach(n -> nodes.push(new NodeVisitor(this, n)));
                visited.add(transferer);
            }
        }

        @NotNull
        private Path createPath(IElementStorage storage) {
            var list = new LinkedList<IElementTransferPathNode>();

            list.add(source);
            var p = parent;
            while (p != null) {
                list.add(p.node);
                p = p.parent;
            }
            list.add(node);
            return new Path(source.getStorage(), storage, type, list);
        }
    }
}
