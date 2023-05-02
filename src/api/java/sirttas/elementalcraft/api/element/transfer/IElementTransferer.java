package sirttas.elementalcraft.api.element.transfer;

import net.minecraft.core.BlockPos;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPathNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@AutoRegisterCapability
public interface IElementTransferer {

	List<IElementTransferPathNode> getConnectedNodes(@Nonnull ElementType type);

	default int getRemainingTransferAmount() {
		return Integer.MAX_VALUE;
	}

	default void onTransfer(@Nonnull ElementType type, int amount, @Nullable BlockPos from, @Nullable BlockPos to) { }

	default boolean canConnectTo(@Nonnull BlockPos to) {
		return true;
	}

	default boolean isValid() {
		return true;
	}

}
