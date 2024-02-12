package sirttas.elementalcraft.block.container;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.ITooltipImageBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.gui.GuiHelper;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Optional;

public abstract class AbstractElementContainerBlock extends AbstractECEntityBlock implements ITooltipImageBlock {

	protected AbstractElementContainerBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new ElementContainerBlockEntity(pos, state);
	}
	
	@Override
	@Deprecated
	public boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
		return true;
	}

	@Override
	@Deprecated
	public int getAnalogOutputSignal(@Nonnull BlockState blockState, @Nonnull Level level, @Nonnull BlockPos pos) {
		return getElementStorage(level, pos)
				.map(storage -> storage.getElementAmount() * 15 / storage.getElementCapacity())
				.orElse(0);
	}

	@Override
	@Deprecated
	@OnlyIn(Dist.CLIENT)
	public float getShadeBrightness(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
		return 1.0F;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		getElementStorage(level, pos)
				.filter(t -> !t.isEmpty())
				.ifPresent(t -> ParticleHelper.createSourceParticle(t.getElementType(), level, Vec3.atCenterOf(pos).add(0, 0.2D, 0), rand));
	}

	private Optional<ISingleElementStorage> getElementStorage(Level level, BlockPos pos) {
		return BlockEntityHelper.getBlockEntityAs(level, pos, IElementContainer.class).map(IElementContainer::getElementStorage);
	}

	@Override
	@Nonnull
	public Optional<TooltipComponent> getTooltipImage(@Nonnull ItemStack stack) {
		var elementStorageNbt = getElementStorageTag(stack);

		if (elementStorageNbt != null) {
			ElementType elementType = ElementType.byName(elementStorageNbt.getString(ECNames.ELEMENT_TYPE));
			int amount = elementStorageNbt.getInt(ECNames.ELEMENT_AMOUNT);
			int capacity = elementStorageNbt.getInt(ECNames.ELEMENT_CAPACITY);

			if (amount > 0) {
				return Optional.of(new Tooltip(elementType, amount, capacity));
			}
		}
		return Optional.empty();
	}

	public CompoundTag getElementStorageTag(@Nonnull ItemStack stack) {
		CompoundTag tag = stack.getTag();

		if (tag != null && tag.contains(ECNames.BLOCK_ENTITY_TAG)) {
			CompoundTag blockNbt = tag.getCompound(ECNames.BLOCK_ENTITY_TAG);

			if (blockNbt.contains(ECNames.ELEMENT_STORAGE)) {
				return blockNbt.getCompound(ECNames.ELEMENT_STORAGE);
			}
		}
		return null;
	}


	@Override
	@Deprecated
	public void onRemove(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockPos up = pos.above();

			if (level.getBlockState(up).is(ECTags.Blocks.CONTAINER_TOOLS)) {
				level.destroyBlock(up, true);
			}
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}

	public abstract int getDefaultCapacity();

	public record Tooltip(
			ElementType elementType,
			int amount,
			int capacity
	) implements TooltipComponent { }

	public record ClientTooltip(
			ElementType elementType,
			int amount,
			int capacity
	) implements ClientTooltipComponent {

		public ClientTooltip(Tooltip tooltip) {
			this(tooltip.elementType, tooltip.amount, tooltip.capacity);
		}

		@Override
		public int getHeight() {
			return 18;
		}

		@Override
		public int getWidth(@Nonnull Font font) {
			return 16;
		}

		@Override
		public void renderImage(@Nonnull Font font, int x, int y, @Nonnull GuiGraphics guiGraphics) {
			GuiHelper.renderElementGauge(guiGraphics, font, x, y, amount, capacity, elementType, false);
		}
	}

}
