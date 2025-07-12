package sirttas.elementalcraft.block.container;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ElementContainer {

	BlockCapability<ElementContainer, Void> CAPABILITY = BlockCapability.createVoid(ElementalCraftApi.createRL("element_container"), ElementContainer.class);

	HolderSet<Block> getCompatibleTools();

	ISingleElementStorage getElementStorage();

	@Nullable
	default ISingleElementStorage getElementStorage(@Nullable BlockState instrumentState) {
		if (instrumentState == null || instrumentState.is(getCompatibleTools())) {
			return getElementStorage();
		}
		return null;
	}

	@Nullable
	static ISingleElementStorage getElementContainer(@Nonnull ILevelExtension level, @Nonnull BlockPos pos) {
		return getElementContainer(null, level, pos);
	}

	@Nullable
	static ISingleElementStorage getElementContainer(@Nullable BlockState state, @Nonnull ILevelExtension level, @Nonnull BlockPos pos) {
		var container = level.getCapability(CAPABILITY, pos);

		if (container != null) {
			return container.getElementStorage(state);
		}
		return null;
	}

	static boolean isValidContainer(@Nullable BlockState state, @Nullable LevelReader levelReader, @Nonnull BlockPos pos) {
		if (!(levelReader instanceof ILevelExtension level)) {
			return true;
		}
		return getElementContainer(state, level, pos) != null;
	}
}
