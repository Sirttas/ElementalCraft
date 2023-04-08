package sirttas.elementalcraft.api.element.transfer.path;

import sirttas.elementalcraft.api.element.IElementTypeProvider;

import java.util.List;

public interface IElementTransferPath extends IElementTypeProvider {

    boolean isValid();

    void transfer();

    List<IElementTransferPathNode> getNodes();
}
