package sirttas.elementalcraft.block.tank;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.AbstractBlockECTileProvider;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.particle.ParticleHelper;

public abstract class AbstractBlockTank extends AbstractBlockECTileProvider {

	protected AbstractBlockTank() {
		super(AbstractBlock.Properties.create(Material.GLASS).hardnessAndResistance(2).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1).notSolid());
	}

	@Override
	@Deprecated
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	@Deprecated
	public int getComparatorInputOverride(BlockState blockState, World world, BlockPos pos) {
		return getElementStorage(world, pos).map(tank -> tank.getElementAmount() * 15 / tank.getElementCapacity())
				.orElse(0);
	}

	@Override
	@Deprecated
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 1.0F;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
		getElementStorage(world, pos)
				.filter(t -> !t.isEmpty())
				.ifPresent(t -> ParticleHelper.createSourceParticle(t.getElementType(), world, Vector3d.copyCentered(pos).add(0, 0.2D, 0), rand));
	}

	private Optional<ISingleElementStorage> getElementStorage(World world, BlockPos pos) {
		return TileEntityHelper.getTileEntityAs(world, pos, IElementContainer.class).map(IElementContainer::getElementStorage);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		CompoundNBT tag = stack.getTag();

		if (tag != null && tag.contains(ECNames.BLOCK_ENTITY_TAG)) {
			CompoundNBT blockNbt = tag.getCompound(ECNames.BLOCK_ENTITY_TAG);

			if (blockNbt != null && blockNbt.contains(ECNames.ELEMENT_STORAGE)) {
				CompoundNBT elementStorageNbt = blockNbt.getCompound(ECNames.ELEMENT_STORAGE);
				ElementType elementType = ElementType.byName(elementStorageNbt.getString(ECNames.ELEMENT_TYPE));
				int amount = elementStorageNbt.getInt(ECNames.ELEMENT_AMOUNT);
				int capacity = elementStorageNbt.getInt(ECNames.ELEMENT_CAPACITY);

				if (elementType != ElementType.NONE && amount > 0 && capacity > 0) {
					tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.contains", elementType.getDisplayName()).mergeStyle(TextFormatting.GREEN));
					tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.percent_full", ItemStack.DECIMALFORMAT.format(amount * 100 / capacity)).mergeStyle(TextFormatting.GREEN));
				}
			}
		}
	}
	
	@Override
	@Deprecated
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.matchesBlock(newState.getBlock())) {
			BlockPos up = pos.up();

			if (!worldIn.getBlockState(up).isValidPosition(worldIn, up)) {
				worldIn.destroyBlock(up, true);
			}
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		items.add(new ItemStack(this.asItem()));
		
		for (ElementType type : ElementType.allValid()) {
			ItemStack stack = new ItemStack(this.asItem());
			CompoundNBT tag = stack.getOrCreateChildTag(ECNames.BLOCK_ENTITY_TAG);
			
			tag.put(ECNames.ELEMENT_STORAGE, new SingleElementStorage(type, this.getDefaultCapacity(), this.getDefaultCapacity()).writeNBT());
			items.add(stack);
		}
	}

	protected abstract int getDefaultCapacity();

}