package sirttas.elementalcraft.block.instrument.io.mill.woodsaw.water;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractMillBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WaterMillWoodSawBlock extends AbstractMillBlock {

	public static final String NAME = "water_mill_wood_saw";
	public static final MapCodec<WaterMillWoodSawBlock> CODEC = simpleCodec(WaterMillWoodSawBlock::new);

	public WaterMillWoodSawBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected @NotNull MapCodec<WaterMillWoodSawBlock> codec() {
		return CODEC;
	}
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new WaterMillWoodSawBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createInstrumentTicker(level, type, ECBlockEntityTypes.WATER_MILL_WOOD_SAW);
	}

}
