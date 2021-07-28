package sirttas.elementalcraft.block.entity;

import java.util.Optional;

import javax.annotation.Nonnull;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.api.rune.handler.EmptyRuneHandler;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.block.container.IElementContainer;
import sirttas.elementalcraft.tag.ECTags;

public class BlockEntityHelper {
	
	private BlockEntityHelper() {}
	
	public static Optional<BlockEntity> getTileEntity(@Nonnull BlockGetter world, @Nonnull BlockPos pos) {
		return Optional.ofNullable(world.getBlockEntity(pos));
	}

	public static <T> Optional<T> getTileEntityAs(@Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull Class<T> clazz) {
		return getTileEntity(world, pos).filter(clazz::isInstance).map(clazz::cast);
	}

	public static Optional<ISingleElementStorage> getElementContainer(@Nonnull BlockGetter world, @Nonnull BlockPos pos, boolean canUseSmall) {
		return getTileEntityAs(world, pos, IElementContainer.class).filter(t -> !t.isSmall() || canUseSmall).map(IElementContainer::getElementStorage);
	}

	public static Optional<ISingleElementStorage> getElementContainer(Block block, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
		return getTileEntityAs(world, pos, IElementContainer.class).filter(t -> !t.isSmall() || ECTags.Blocks.SMALL_TANK_COMPATIBLES.contains(block)).map(IElementContainer::getElementStorage);
	}

	public static boolean isValidContainer(Block block, LevelReader world, BlockPos pos) {
		return getElementContainer(block, world, pos).isPresent();
	}
	
	public static Optional<IElementStorage> getElementStorageAt(LevelReader world, BlockPos pos) {
		return getTileEntity(world, pos).flatMap(t -> CapabilityElementStorage.get(t).resolve());
	}
	
	@Nonnull
	public static IRuneHandler getRuneHandlerAt(LevelReader world, BlockPos pos) {
		return BlockEntityHelper.getTileEntity(world, pos)
				.map(CapabilityRuneHandler::get)
				.flatMap(LazyOptional::resolve)
				.orElse(EmptyRuneHandler.INSTANCE);
	}
}
