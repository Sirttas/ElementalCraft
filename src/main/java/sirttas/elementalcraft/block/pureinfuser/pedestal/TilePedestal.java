package sirttas.elementalcraft.block.pureinfuser.pedestal;

import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.pureinfuser.TilePureInfuser;
import sirttas.elementalcraft.block.tile.AbstractTileECContainer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.SingleItemInventory;
import sirttas.elementalcraft.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.rune.handler.RuneHandler;

public class TilePedestal extends AbstractTileECContainer implements IElementTypeProvider {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_FIRE) public static final TileEntityType<TilePedestal> TYPE_FIRE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_WATER) public static final TileEntityType<TilePedestal> TYPE_WATER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_EARTH) public static final TileEntityType<TilePedestal> TYPE_EARTH = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_AIR) public static final TileEntityType<TilePedestal> TYPE_AIR = null;

	private final SingleItemInventory inventory;
	private final SingleElementStorage elementStorage;
	private final RuneHandler runeHandler;

	private TilePedestal(TileEntityType<?> tileEntityType, ElementType type) {
		super(tileEntityType);
		inventory = new SingleItemInventory(this::markDirty);
		elementStorage = new PedestalElementStorage(type, this::markDirty);
		runeHandler = new RuneHandler(ECConfig.COMMON.pedestalMaxRunes.get());
	}

	public static TilePedestal createFire() {
		return new TilePedestal(TYPE_FIRE, ElementType.FIRE);
	}

	public static TilePedestal createWater() {
		return new TilePedestal(TYPE_WATER, ElementType.WATER);
	}

	public static TilePedestal createEarth() {
		return new TilePedestal(TYPE_EARTH, ElementType.EARTH);
	}

	public static TilePedestal createAir() {
		return new TilePedestal(TYPE_AIR, ElementType.AIR);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.readNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
		} else { // TODO 1.17 remove
			elementStorage.readNBT(compound);
		}
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY.readNBT(runeHandler, null, compound.get(ECNames.RUNE_HANDLER));
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.writeNBT());
		compound.put(ECNames.RUNE_HANDLER, CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY.writeNBT(runeHandler, null));
		return compound;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.removed) {
			if (cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY && side != Direction.UP) {
				return LazyOptional.of(elementStorage != null ? () -> elementStorage : null).cast();
			} else if (cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
				return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
			}
		}
		return super.getCapability(cap, side);
	}

	public Direction getPureInfuserDirection() {
		return Stream.of(Direction.values()).filter(d -> d.getAxis().getPlane() == Direction.Plane.HORIZONTAL)
				.filter(d -> this.getWorld().getTileEntity(pos.offset(d, 3)) instanceof TilePureInfuser)
				.findAny().orElse(Direction.UP);
	}

	@Override
	public ElementType getElementType() {
		return elementStorage.getElementType();
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}

	public ItemStack getItem() {
		return inventory.getStackInSlot(0);
	}

	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

	@Nonnull
	public RuneHandler getRuneHandler() {
		return runeHandler;
	}

}
