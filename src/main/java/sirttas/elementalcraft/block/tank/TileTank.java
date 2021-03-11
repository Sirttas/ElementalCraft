package sirttas.elementalcraft.block.tank;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.config.ECConfig;

public class TileTank extends AbstractTileElementContainer implements IElementContainer {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTank.NAME) public static final TileEntityType<TileTank> TYPE = null;

	private boolean small = false;

	public TileTank(int elementCapacity) {
		super(TYPE, sync -> new ContainerElementStorage(elementCapacity, sync));
	}

	public TileTank(boolean small) {
		this(small ? ECConfig.COMMON.tankSmallCapacity.get() : ECConfig.COMMON.tankCapacity.get());
		this.small = small;
	}

	public TileTank() {
		this(false);
	}

	@Override
	public boolean isSmall() {
		return small;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		small = compound.getBoolean(ECNames.SMALL);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putBoolean(ECNames.SMALL, small);
		return compound;
	}

}
