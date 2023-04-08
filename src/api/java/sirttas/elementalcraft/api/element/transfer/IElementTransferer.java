package sirttas.elementalcraft.api.element.transfer;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPathNode;

import java.util.List;

@AutoRegisterCapability
public interface IElementTransferer {

	List<IElementTransferPathNode> getConnectedNodes(ElementType type);

	int getRemainingTransferAmount();

	void transfer(int amount);

	boolean isValid();

}
