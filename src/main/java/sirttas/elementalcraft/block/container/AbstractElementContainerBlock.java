package sirttas.elementalcraft.block.container;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.tag.ECTags;

public abstract class AbstractElementContainerBlock extends AbstractECEntityBlock {

	protected AbstractElementContainerBlock() {
		super(BlockBehaviour.Properties.of(Material.GLASS).strength(2).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ElementContainerBlockEntity(pos, state);
	}
	
	@Override
	@Deprecated
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	@Deprecated
	public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
		return getElementStorage(world, pos).map(storage -> storage.getElementAmount() * 15 / storage.getElementCapacity())
				.orElse(0);
	}

	@Override
	@Deprecated
	@OnlyIn(Dist.CLIENT)
	public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 1.0F;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level world, BlockPos pos, Random rand) {
		getElementStorage(world, pos)
				.filter(t -> !t.isEmpty())
				.ifPresent(t -> ParticleHelper.createSourceParticle(t.getElementType(), world, Vec3.atCenterOf(pos).add(0, 0.2D, 0), rand));
	}

	private Optional<ISingleElementStorage> getElementStorage(Level world, BlockPos pos) {
		return BlockEntityHelper.getTileEntityAs(world, pos, IElementContainer.class).map(IElementContainer::getElementStorage);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		CompoundTag tag = stack.getTag();

		if (tag != null && tag.contains(ECNames.BLOCK_ENTITY_TAG)) {
			CompoundTag blockNbt = tag.getCompound(ECNames.BLOCK_ENTITY_TAG);

			if (blockNbt != null && blockNbt.contains(ECNames.ELEMENT_STORAGE)) {
				CompoundTag elementStorageNbt = blockNbt.getCompound(ECNames.ELEMENT_STORAGE);
				ElementType elementType = ElementType.byName(elementStorageNbt.getString(ECNames.ELEMENT_TYPE));
				int amount = elementStorageNbt.getInt(ECNames.ELEMENT_AMOUNT);
				int capacity = elementStorageNbt.getInt(ECNames.ELEMENT_CAPACITY);

				if (elementType != ElementType.NONE && amount > 0 && capacity > 0) {
					tooltip.add(new TranslatableComponent("tooltip.elementalcraft.contains", elementType.getDisplayName()).withStyle(ChatFormatting.GREEN));
					tooltip.add(new TranslatableComponent("tooltip.elementalcraft.percent_full", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(amount * 100 / capacity)).withStyle(ChatFormatting.GREEN));
				}
			}
		}
	}
	
	@Override
	@Deprecated
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockPos up = pos.above();

			if (worldIn.getBlockState(up).is(ECTags.Blocks.CONTAINER_TOOLS)) {
				worldIn.destroyBlock(up, true);
			}
		}
		super.onRemove(state, worldIn, pos, newState, isMoving);
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		items.add(new ItemStack(this.asItem()));
		
		for (ElementType type : ElementType.ALL_VALID) {
			ItemStack stack = new ItemStack(this.asItem());
			CompoundTag tag = stack.getOrCreateTagElement(ECNames.BLOCK_ENTITY_TAG);
			
			tag.put(ECNames.ELEMENT_STORAGE, new SingleElementStorage(type, this.getDefaultCapacity(), this.getDefaultCapacity()).serializeNBT());
			items.add(stack);
		}
	}
	
	protected abstract int getDefaultCapacity();

}