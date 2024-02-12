package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.rune.handler.EmptyRuneHandler;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.block.container.IElementContainer;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class BlockEntityHelper {
	
	private BlockEntityHelper() {}
	
	public static Optional<BlockEntity> getBlockEntity(@Nonnull BlockGetter level, @Nonnull BlockPos pos) {
		return Optional.ofNullable(level.getBlockEntity(pos));
	}

	public static <T> Optional<T> getBlockEntityAs(@Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Class<T> clazz) {
		return getBlockEntity(level, pos).filter(clazz::isInstance).map(clazz::cast);
	}

	public static Optional<ISingleElementStorage> getElementContainer(@Nonnull BlockGetter level, @Nonnull BlockPos pos, boolean canUseSmall) {
		return getBlockEntityAs(level, pos, IElementContainer.class).filter(t -> !t.isSmall() || canUseSmall).map(IElementContainer::getElementStorage);
	}

	public static Optional<ISingleElementStorage> getElementContainer(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
		return getBlockEntityAs(level, pos, IElementContainer.class).filter(t -> !t.isSmall() || state.is(ECTags.Blocks.SMALL_CONTAINER_COMPATIBLES)).map(IElementContainer::getElementStorage);
	}

	public static boolean isValidContainer(BlockState state, LevelReader world, BlockPos pos) {
		return getElementContainer(state, world, pos).isPresent();
	}

	@Nullable
	public static <T, C> T getCapability(BlockCapability<T, C> cap, @Nonnull BlockEntity blockEntity, C context) {
		if (!blockEntity.hasLevel()) {
			return null;
		}
		return blockEntity.getLevel().getCapability(cap, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, context);
	}

	@Nonnull
	public static IRuneHandler getRuneHandlerAt(LevelReader level, BlockPos pos) {
		return getRuneHandlerAt(level, pos, null);
	}

	@Nonnull
	public static IRuneHandler getRuneHandlerAt(LevelReader level, BlockPos pos, @Nullable Direction direction) {
		if (!(level instanceof Level l)) {
			return EmptyRuneHandler.INSTANCE;
		}

		var cap = l.getCapability(ElementalCraftCapabilities.RuneHandler.BLOCK, pos, direction);

		if (cap == null) {
			return EmptyRuneHandler.INSTANCE;
		}
		return cap;
	}
}
