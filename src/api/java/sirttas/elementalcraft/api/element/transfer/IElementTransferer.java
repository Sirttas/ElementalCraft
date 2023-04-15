package sirttas.elementalcraft.api.element.transfer;

import net.minecraft.core.BlockPos;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPathNode;

import javax.annotation.Nullable;
import java.util.List;

@AutoRegisterCapability
public interface IElementTransferer {

	List<IElementTransferPathNode> getConnectedNodes(ElementType type);

	int getRemainingTransferAmount();

	void transfer(ElementType type, int amount, @Nullable BlockPos from, @Nullable BlockPos to);

	boolean isValid();

}
