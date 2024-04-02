package sirttas.elementalcraft.api.element.transfer.path;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;

import java.util.List;

public interface IElementTransferPath extends IElementTypeProvider {

    boolean isValid();

    void transfer();

    List<IElementTransferPathNode> getNodes();

    static void transfer(ElementType type, int amount, List<IElementTransferPathNode> nodes) {
        if (amount <= 0) {
            return;
        }

        IElementTransferPathNode.forEachNodes(nodes, (node, prev, next) -> {
            var transferer = node.getTransferer();

            if (transferer == null) {
                return;
            }

            transferer.onTransfer(type, amount, prev, next);
        });
    }
}
