package sirttas.elementalcraft.block.tank.reservoir;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;
import sirttas.elementalcraft.block.tank.AbstractTileElementContainer;
import sirttas.elementalcraft.config.ECConfig;

public class TileReservoir extends AbstractTileElementContainer {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockReservoir.NAME_FIRE) public static final TileEntityType<TileReservoir> TYPE_FIRE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockReservoir.NAME_WATER) public static final TileEntityType<TileReservoir> TYPE_WATER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockReservoir.NAME_EARTH) public static final TileEntityType<TileReservoir> TYPE_EARTH = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockReservoir.NAME_AIR) public static final TileEntityType<TileReservoir> TYPE_AIR = null;
	
	private TileReservoir(TileEntityType<?> tileEntityType, ElementType type) {
		super(tileEntityType, r -> new StaticElementStorage(type, ECConfig.COMMON.reservoirCapacity.get(), r));
	}
	
	public static TileReservoir createFire() {
		return new TileReservoir(TYPE_FIRE, ElementType.FIRE);
	}

	public static TileReservoir createWater() {
		return new TileReservoir(TYPE_WATER, ElementType.WATER);
	}

	public static TileReservoir createEarth() {
		return new TileReservoir(TYPE_EARTH, ElementType.EARTH);
	}

	public static TileReservoir createAir() {
		return new TileReservoir(TYPE_AIR, ElementType.AIR);
	}

	@Override
	public boolean isSmall() {
		return false;
	}
	
	@Override
	public void markDirty() {
		if (this.hasWorld() && getBlockState().get(BlockReservoir.HALF) == DoubleBlockHalf.UPPER) {
			world.getTileEntity(pos.down()).markDirty();
		}
		super.markDirty();
	}
	
	@Override
	public ISingleElementStorage getElementStorage() {
		if (getBlockState().get(BlockReservoir.HALF) == DoubleBlockHalf.UPPER) {
			return ((TileReservoir) world.getTileEntity(pos.down())).getElementStorage();
		}
		return elementStorage;
	}
	
	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.removed && cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY && this.getBlockState().get(BlockReservoir.HALF) == DoubleBlockHalf.UPPER) {
			return this.hasWorld() ? world.getTileEntity(pos.down()).getCapability(cap, side) : LazyOptional.empty();
		}
		return super.getCapability(cap, side);
	}
}
