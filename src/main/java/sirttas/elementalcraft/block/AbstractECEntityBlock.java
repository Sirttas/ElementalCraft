package sirttas.elementalcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.property.ECProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractECEntityBlock extends BaseEntityBlock {

	protected AbstractECEntityBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	protected AbstractECEntityBlock() {
		this(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES);
	}

	@Nonnull
	@Override
	@Deprecated
	public RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			dropItems(level, pos);
			dropRunes(level, pos);
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	private void dropItems(Level worldIn, BlockPos pos) {
		IItemHandler inv = ECContainerHelper.getItemHandlerAt(worldIn, pos, null);

		if (inv != null) {
			for (int i = 0; i < inv.getSlots(); i++) {
				Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), inv.getStackInSlot(i));
			}
			worldIn.updateNeighbourForOutputSignal(pos, this);
		}
	}

	private void dropRunes(Level worldIn, BlockPos pos) {
		BlockEntityHelper.getRuneHandlerAt(worldIn, pos).getRunes().forEach(rune -> Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ECItems.RUNE.get().getRuneStack(rune)));
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return !level.isClientSide ? (l, p, s, be) -> sendUpdate(be) : null;
	}
	
	@Nullable
	public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createECTicker(Level level, BlockEntityType<A> type, RegistryObject<BlockEntityType<E>> expectedType, BlockEntityTicker<? super E> ticker) {
		return createECTicker(level, type, expectedType.get(), ticker);
	}

	public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createECTicker(Level level, BlockEntityType<A> type, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
		return createTickerHelper(type, expectedType, !level.isClientSide ? createUpdateTicker(ticker) : ticker);
	}
	
	@Nullable
	public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createECServerTicker(Level level, BlockEntityType<A> type, RegistryObject<BlockEntityType<E>> expectedType, BlockEntityTicker<? super E> ticker) {
		return level.isClientSide ? null : createTickerHelper(type, expectedType.get(), createUpdateTicker(ticker));
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
	@Deprecated
	public boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
		return true;
	}
}
