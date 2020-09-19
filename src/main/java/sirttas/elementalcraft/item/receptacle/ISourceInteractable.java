package sirttas.elementalcraft.item.receptacle;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

// TODO capability ?
public interface ISourceInteractable {

	default boolean canIteractWithSource(ItemStack stack, BlockState state) {
		return true;
	}

}
