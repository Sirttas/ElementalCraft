package sirttas.elementalcraft.block.container;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.ITooltipImageBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.gui.GuiHelper;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public abstract class AbstractElementContainerBlock extends AbstractECEntityBlock implements ITooltipImageBlock {

	protected AbstractElementContainerBlock() {
		super(BlockBehaviour.Properties.of(Material.GLASS).strength(2).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion());
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
		return getElementStorage(level, pos).map(storage -> storage.getElementAmount() * 15 / storage.getElementCapacity())
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
	public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Random rand) {
		getElementStorage(level, pos)
				.filter(t -> !t.isEmpty())
				.ifPresent(t -> ParticleHelper.createSourceParticle(t.getElementType(), level, Vec3.atCenterOf(pos).add(0, 0.2D, 0), rand));
	}

	private Optional<ISingleElementStorage> getElementStorage(Level level, BlockPos pos) {
		return BlockEntityHelper.getBlockEntityAs(level, pos, IElementContainer.class).map(IElementContainer::getElementStorage);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		if (ECinteractions.calledFromJEI()) {
			CompoundTag tag = stack.getTag();

			if (tag != null && tag.contains(ECNames.BLOCK_ENTITY_TAG)) {
				CompoundTag blockNbt = tag.getCompound(ECNames.BLOCK_ENTITY_TAG);

				if (blockNbt.contains(ECNames.ELEMENT_STORAGE)) {
					CompoundTag elementStorageNbt = blockNbt.getCompound(ECNames.ELEMENT_STORAGE);
					ElementType elementType = ElementType.byName(elementStorageNbt.getString(ECNames.ELEMENT_TYPE));
					int amount = elementStorageNbt.getInt(ECNames.ELEMENT_AMOUNT);
					int capacity = elementStorageNbt.getInt(ECNames.ELEMENT_CAPACITY);

					if (elementType != ElementType.NONE && amount > 0 && capacity > 0) {
						tooltip.add(new TranslatableComponent("tooltip.elementalcraft.contains", elementType.getDisplayName()).withStyle(ChatFormatting.GREEN));
						tooltip.add(new TranslatableComponent("tooltip.elementalcraft.percent_full", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(amount * 100L / capacity)).withStyle(ChatFormatting.GREEN));
					}
				}
			}
		}
	}

	@Override
	@Nonnull
	public Optional<TooltipComponent> getTooltipImage(@Nonnull ItemStack stack) {
		CompoundTag tag = stack.getTag();

		if (tag != null && tag.contains(ECNames.BLOCK_ENTITY_TAG)) {
			CompoundTag blockNbt = tag.getCompound(ECNames.BLOCK_ENTITY_TAG);

			if (blockNbt.contains(ECNames.ELEMENT_STORAGE)) {
				CompoundTag elementStorageNbt = blockNbt.getCompound(ECNames.ELEMENT_STORAGE);
				ElementType elementType = ElementType.byName(elementStorageNbt.getString(ECNames.ELEMENT_TYPE));
				int amount = elementStorageNbt.getInt(ECNames.ELEMENT_AMOUNT);
				int capacity = elementStorageNbt.getInt(ECNames.ELEMENT_CAPACITY);

				if (amount > 0) {
					return Optional.of(new Tooltip(elementType, amount, capacity));
				}
			}
		}
		return Optional.empty();
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
	
	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab group, NonNullList<ItemStack> items) {
		items.add(new ItemStack(this.asItem()));
		
		for (ElementType type : ElementType.ALL_VALID) {
			ItemStack stack = new ItemStack(this.asItem());
			CompoundTag tag = stack.getOrCreateTagElement(ECNames.BLOCK_ENTITY_TAG);
			
			tag.put(ECNames.ELEMENT_STORAGE, new SingleElementStorage(type, this.getDefaultCapacity(), this.getDefaultCapacity()).serializeNBT());
			items.add(stack);
		}
	}

	protected abstract int getDefaultCapacity();

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
		public void renderImage(@Nonnull Font font, int x, int y, @Nonnull PoseStack poseStack, @Nonnull ItemRenderer itemRenderer, int blitOffset) {
			GuiHelper.renderElementGauge(poseStack, x, y, amount, capacity, elementType, false);
		}
	}

}
