package sirttas.elementalcraft.block.container.reservoir;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

public class ReservoirBlockEntity extends AbstractElementContainerBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + ReservoirBlock.NAME_FIRE) public static final TileEntityType<ReservoirBlockEntity> TYPE_FIRE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ReservoirBlock.NAME_WATER) public static final TileEntityType<ReservoirBlockEntity> TYPE_WATER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ReservoirBlock.NAME_EARTH) public static final TileEntityType<ReservoirBlockEntity> TYPE_EARTH = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ReservoirBlock.NAME_AIR) public static final TileEntityType<ReservoirBlockEntity> TYPE_AIR = null;
	
	private ReservoirBlockEntity(TileEntityType<?> tileEntityType, ElementType type) {
		super(tileEntityType, r -> new ReservoirElementStorage(type, ECConfig.COMMON.reservoirCapacity.get(), r));
	}
	
	public static ReservoirBlockEntity createFire() {
		return new ReservoirBlockEntity(TYPE_FIRE, ElementType.FIRE);
	}

	public static ReservoirBlockEntity createWater() {
		return new ReservoirBlockEntity(TYPE_WATER, ElementType.WATER);
	}

	public static ReservoirBlockEntity createEarth() {
		return new ReservoirBlockEntity(TYPE_EARTH, ElementType.EARTH);
	}

	public static ReservoirBlockEntity createAir() {
		return new ReservoirBlockEntity(TYPE_AIR, ElementType.AIR);
	}

	@Override
	public boolean isSmall() {
		return false;
	}
	
	@Override
	public void setChanged() {
		if (this.hasLevel() && getBlockState().getValue(ReservoirBlock.HALF) == DoubleBlockHalf.UPPER) {
			level.getBlockEntity(worldPosition.below()).setChanged();
		}
		super.setChanged();
	}
	
	@Override
	public ISingleElementStorage getElementStorage() {
		if (getBlockState().getValue(ReservoirBlock.HALF) == DoubleBlockHalf.UPPER) {
			return ((ReservoirBlockEntity) level.getBlockEntity(worldPosition.below())).getElementStorage();
		}
		return elementStorage;
	}
	
	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY && this.getBlockState().getValue(ReservoirBlock.HALF) == DoubleBlockHalf.UPPER) {
			return this.hasLevel() ? level.getBlockEntity(worldPosition.below()).getCapability(cap, side) : LazyOptional.empty();
		}
		return super.getCapability(cap, side);
	}
}
