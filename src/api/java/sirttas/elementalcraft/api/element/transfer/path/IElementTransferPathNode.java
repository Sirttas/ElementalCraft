package sirttas.elementalcraft.api.element.transfer.path;

import net.minecraft.core.BlockPos;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;

public interface IElementTransferPathNode {

    BlockPos getPos();
    IElementTransferer getTransferer();
    IElementStorage getStorage();
}
