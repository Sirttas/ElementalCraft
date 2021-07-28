package sirttas.elementalcraft.api.source;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;

// TODO capability ?
public interface ISourceInteractable {

	default boolean canIteractWithSource(ItemStack stack, BlockState state) {
		return true;
	}

}
