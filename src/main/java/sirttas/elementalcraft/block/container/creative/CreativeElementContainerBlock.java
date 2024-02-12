package sirttas.elementalcraft.block.container.creative;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.container.ElementContainerBlock;

import javax.annotation.Nonnull;

public class CreativeElementContainerBlock extends ElementContainerBlock {

	public static final String NAME = "creative_container";

	public static final MapCodec<CreativeElementContainerBlock> CODEC = simpleCodec(CreativeElementContainerBlock::new);

	public CreativeElementContainerBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new CreativeElementContainerBlockEntity(pos, state);
	}
	
	@Override
	public int getDefaultCapacity() {
		return 1000000;
	}

	@Override
	protected @NotNull MapCodec<CreativeElementContainerBlock> codec() {
		return CODEC;
	}
}
