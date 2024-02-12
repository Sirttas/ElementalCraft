package sirttas.elementalcraft.block.instrument;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.registries.DeferredHolder;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public interface IInstrumentBlock extends SimpleWaterloggedBlock {

	BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	default <T extends IInstrument, R extends IInstrumentRecipe<T>, E extends AbstractInstrumentBlockEntity<T, R>, A extends BlockEntity> BlockEntityTicker<A> createInstrumentTicker(Level level, BlockEntityType<A> type, DeferredHolder<BlockEntityType<?>, BlockEntityType<E>> expectedType) {
		return AbstractECEntityBlock.createECTicker(level, type, expectedType, AbstractInstrumentBlockEntity::tick);
	}

}
