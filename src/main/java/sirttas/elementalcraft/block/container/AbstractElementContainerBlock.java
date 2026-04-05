package sirttas.elementalcraft.block.container;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public abstract class AbstractElementContainerBlock extends AbstractECEntityBlock {

	private final Holder<IConfigurableBlockEntityProperties> entityProperties;

	protected AbstractElementContainerBlock(BlockBehaviour.Properties properties, Holder<IConfigurableBlockEntityProperties> entityProperties) {
		super(properties);
		this.entityProperties = entityProperties;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new ElementContainerBlockEntity(pos, state);
	}
	
	@Override
	public boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
		return true;
	}

    @Override
    protected int getAnalogOutputSignal(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Direction direction) {
		var storage = ElementContainer.getElementContainer(level, pos);

		if (storage == null || storage.isEmpty()) {
			return 0;
		}
		return storage.getElementAmount() * 15 / storage.getElementCapacity();
	}

	@Override
	public float getShadeBrightness(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
		return 1.0F;
	}

	@Override
	public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		var storage = ElementContainer.getElementContainer(level, pos);

		if (storage == null || storage.isEmpty()) {
			return;
		}
		ParticleHelper.createSourceParticle(storage.getElementType(), level, Vec3.atCenterOf(pos).add(0, 0.2D, 0), rand);
	}

    @Override
    protected void affectNeighborsAfterRemoval(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, boolean movedByPiston) {
        BlockPos up = pos.above();

        if (level.getBlockState(up).is(ECTags.Blocks.CONTAINER_TOOLS)) {
            level.destroyBlock(up, true);
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    @Nonnull
	public ElementContainerProperties getProperties() {
		if (entityProperties.isBound() && this.entityProperties.value() instanceof ElementContainerProperties elementContainerProperties) {
			return elementContainerProperties;
		}
		return ElementContainerProperties.DEFAULT;
	}

	public int getDefaultCapacity() {
		return getProperties().capacity();
	}

}
