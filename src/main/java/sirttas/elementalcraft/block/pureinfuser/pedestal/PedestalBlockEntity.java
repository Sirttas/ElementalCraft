package sirttas.elementalcraft.block.pureinfuser.pedestal;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.entity.AbstractIERBlockEntity;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.SingleItemInventory;

public class PedestalBlockEntity extends AbstractIERBlockEntity implements IElementTypeProvider {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + PedestalBlock.NAME_FIRE) public static final TileEntityType<PedestalBlockEntity> TYPE_FIRE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PedestalBlock.NAME_WATER) public static final TileEntityType<PedestalBlockEntity> TYPE_WATER = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PedestalBlock.NAME_EARTH) public static final TileEntityType<PedestalBlockEntity> TYPE_EARTH = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PedestalBlock.NAME_AIR) public static final TileEntityType<PedestalBlockEntity> TYPE_AIR = null;

	private final SingleItemInventory inventory;
	private final SingleElementStorage elementStorage;
	private final RuneHandler runeHandler;

	private PedestalBlockEntity(TileEntityType<?> tileEntityType, ElementType type) {
		super(tileEntityType);
		inventory = new SingleItemInventory(this::setChanged);
		elementStorage = new ElementStorageRenderer(type, this::setChanged);
		runeHandler = new RuneHandler(ECConfig.COMMON.pedestalMaxRunes.get());
	}

	public static PedestalBlockEntity createFire() {
		return new PedestalBlockEntity(TYPE_FIRE, ElementType.FIRE);
	}

	public static PedestalBlockEntity createWater() {
		return new PedestalBlockEntity(TYPE_WATER, ElementType.WATER);
	}

	public static PedestalBlockEntity createEarth() {
		return new PedestalBlockEntity(TYPE_EARTH, ElementType.EARTH);
	}

	public static PedestalBlockEntity createAir() {
		return new PedestalBlockEntity(TYPE_AIR, ElementType.AIR);
	}

	public Direction getPureInfuserDirection() {
		return Stream.of(Direction.values()).filter(d -> d.getAxis().getPlane() == Direction.Plane.HORIZONTAL)
				.filter(d -> this.getLevel().getBlockEntity(worldPosition.relative(d, 3)) instanceof PureInfuserBlockEntity)
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
		return inventory.getItem(0);
	}

	@Override
	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

	@Override
	@Nonnull
	public RuneHandler getRuneHandler() {
		return runeHandler;
	}

}
