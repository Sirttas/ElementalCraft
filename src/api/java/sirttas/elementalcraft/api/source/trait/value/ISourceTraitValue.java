package sirttas.elementalcraft.api.source.trait.value;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public interface ISourceTraitValue {

	float getValue();
	Component getDescription();

	default boolean isActive(Level level, BlockPos pos) {
		return true;
	}
}
