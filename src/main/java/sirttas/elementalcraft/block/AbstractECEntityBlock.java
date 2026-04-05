package sirttas.elementalcraft.block;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractECEntityBlock extends BaseEntityBlock {

	protected AbstractECEntityBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<@NotNull T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<@NotNull T> type) {
		return !level.isClientSide() ? (_, _, _, be) -> sendUpdate(be) : null;
	}
	
	@Nullable
	public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<@NotNull A> createECTicker(Level level, BlockEntityType<@NotNull A> type, DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull E>> expectedType, BlockEntityTicker<? super E> ticker) {
		return createECTicker(level, type, expectedType.get(), ticker);
	}

	public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<@NotNull A> createECTicker(Level level, BlockEntityType<@NotNull A> type, BlockEntityType<@NotNull E> expectedType, BlockEntityTicker<? super E> ticker) {
		return createTickerHelper(type, expectedType, !level.isClientSide() ? createUpdateTicker(ticker) : ticker);
	}
	
	@Nullable
	public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<@NotNull A> createECServerTicker(Level level, BlockEntityType<@NotNull A> type, DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull E>> expectedType, BlockEntityTicker<? super E> ticker) {
		return level.isClientSide() ? null : createTickerHelper(type, expectedType.get(), createUpdateTicker(ticker));
	}

	private static <E extends BlockEntity> BlockEntityTicker<? super E> createUpdateTicker(BlockEntityTicker<? super E> ticker) {
		return (l, pos, state, be) -> {
			ticker.tick(l, pos, state, be);
			sendUpdate(be);
		};
	}

	private static void sendUpdate(BlockEntity blockEntity) {
		if (blockEntity instanceof AbstractECBlockEntity ecBlockEntity) {
			ecBlockEntity.sendUpdate();
		}
	}

	@Override
	public boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
		return true;
	}
}
