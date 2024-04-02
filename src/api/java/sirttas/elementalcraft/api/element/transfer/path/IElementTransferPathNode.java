package sirttas.elementalcraft.api.element.transfer.path;

import net.minecraft.core.BlockPos;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface IElementTransferPathNode {

    BlockPos getPos();

    default IElementTransferer getTransferer() {
        return null;
    }

    default IElementStorage getStorage() {
        return null;
    }

    default int getWeight(@Nonnull ElementType type, @Nullable IElementTransferPathNode prev, @Nullable IElementTransferPathNode next) {
        return 1;
    }

    static void forEachNodes(List<IElementTransferPathNode> nodes, Consumer consumer) {
        var size = nodes.size();

        for (int i = 0; i < size; i++) {
            var node = nodes.get(i);

            var prev = i >= 1 ? nodes.get(i - 1) : null;
            var next = i < size - 1 ? nodes.get(i + 1) : null;

            consumer.accept(node, prev, next);
        }
    }

    @FunctionalInterface
    interface Consumer {
        void accept(@Nonnull IElementTransferPathNode node, @Nullable IElementTransferPathNode prev, @Nullable IElementTransferPathNode next);
    }
}
