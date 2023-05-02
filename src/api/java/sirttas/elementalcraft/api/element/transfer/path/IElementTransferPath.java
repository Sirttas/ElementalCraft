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

        var size = nodes.size();

        for (int i = 0; i < size - 1; i++) {
            var transferer = nodes.get(i).getTransferer();

            if (transferer == null) {
                continue;
            }

            var from = i >= 1 ? nodes.get(i - 1).getPos() : null;
            var to = i < size - 1 ? nodes.get(i + 1).getPos() : null;

            transferer.onTransfer(type, amount, from, to);
        }
    }
}
