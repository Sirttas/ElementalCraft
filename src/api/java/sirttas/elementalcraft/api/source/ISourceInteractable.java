package sirttas.elementalcraft.api.source;

import net.minecraft.world.level.block.state.BlockState;

public interface ISourceInteractable {

	default boolean canInteractWithSource(BlockState state) {
		return true;
	}

}
