package sirttas.elementalcraft.api.source;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

// TODO capability ?
public interface ISourceInteractable {

	default boolean canInteractWithSource(ItemStack stack, BlockState state) {
		return true;
	}

}
