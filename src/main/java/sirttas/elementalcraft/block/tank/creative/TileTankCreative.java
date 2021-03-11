package sirttas.elementalcraft.block.tank.creative;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tank.AbstractTileElementContainer;

public class TileTankCreative extends AbstractTileElementContainer {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTankCreative.NAME) public static final TileEntityType<TileTankCreative> TYPE = null;

	public TileTankCreative() {
		super(TYPE, CreativeElementStorage::new);
	}

	@Override
	public boolean isSmall() {
		return false;
	}
}
