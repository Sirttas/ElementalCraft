package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.rune.handler.EmptyRuneHandler;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandlerHelper;
import sirttas.elementalcraft.block.container.IElementContainer;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Optional;

public class BlockEntityHelper {
	
	private BlockEntityHelper() {}
	
	public static Optional<BlockEntity> getBlockEntity(@Nonnull BlockGetter world, @Nonnull BlockPos pos) {
		return Optional.ofNullable(world.getBlockEntity(pos));
	}

	public static <T> Optional<T> getBlockEntityAs(@Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull Class<T> clazz) {
		return getBlockEntity(world, pos).filter(clazz::isInstance).map(clazz::cast);
	}

	public static Optional<ISingleElementStorage> getElementContainer(@Nonnull BlockGetter world, @Nonnull BlockPos pos, boolean canUseSmall) {
		return getBlockEntityAs(world, pos, IElementContainer.class).filter(t -> !t.isSmall() || canUseSmall).map(IElementContainer::getElementStorage);
	}

	public static Optional<ISingleElementStorage> getElementContainer(BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
		return getBlockEntityAs(world, pos, IElementContainer.class).filter(t -> !t.isSmall() || state.is(ECTags.Blocks.SMALL_CONTAINER_COMPATIBLES)).map(IElementContainer::getElementStorage);
	}

	public static boolean isValidContainer(BlockState state, LevelReader world, BlockPos pos) {
		return getElementContainer(state, world, pos).isPresent();
	}
	
	public static Optional<IElementStorage> getElementStorageAt(LevelReader world, BlockPos pos) {
		return getBlockEntity(world, pos).flatMap(t -> ElementStorageHelper.get(t).resolve());
	}
	
	@Nonnull
	public static IRuneHandler getRuneHandlerAt(LevelReader world, BlockPos pos) {
		return BlockEntityHelper.getBlockEntity(world, pos)
				.map(RuneHandlerHelper::get)
				.flatMap(LazyOptional::resolve)
				.orElse(EmptyRuneHandler.INSTANCE);
	}
}
