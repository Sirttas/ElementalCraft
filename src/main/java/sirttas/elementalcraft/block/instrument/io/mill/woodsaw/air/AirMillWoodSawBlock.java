package sirttas.elementalcraft.block.instrument.io.mill.woodsaw.air;

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
import sirttas.elementalcraft.block.instrument.io.mill.AbstractAirMillBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AirMillWoodSawBlock extends AbstractAirMillBlock {
	public static final String NAME = "air_mill_wood_saw";
	public static final MapCodec<AirMillWoodSawBlock> CODEC = simpleCodec(AirMillWoodSawBlock::new);

	public AirMillWoodSawBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected @NotNull MapCodec<AirMillWoodSawBlock> codec() {
		return CODEC;
	}


	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return isLower(state) ? new AirMillWoodSawBlockEntity(pos, state) : null;
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return isLower(state) ? createInstrumentTicker(level, type, ECBlockEntityTypes.AIR_MILL_WOOD_SAW) : null;
	}

}
