package sirttas.elementalcraft.block.container;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.config.ECConfig;

public class ElementContainerBlockEntity extends AbstractElementContainerBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementContainerBlock.NAME) public static final TileEntityType<ElementContainerBlockEntity> TYPE = null;

	private boolean small = false;

	public ElementContainerBlockEntity(int elementCapacity) {
		super(TYPE, sync -> new ElementContainerElementStorage(elementCapacity, sync));
	}

	public ElementContainerBlockEntity(boolean small) {
		this(small ? ECConfig.COMMON.tankSmallCapacity.get() : ECConfig.COMMON.tankCapacity.get());
		this.small = small;
	}

	public ElementContainerBlockEntity() {
		this(false);
	}

	@Override
	public boolean isSmall() {
		return small;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		small = compound.getBoolean(ECNames.SMALL);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putBoolean(ECNames.SMALL, small);
		return compound;
	}

}
