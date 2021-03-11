package sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tile.AbstractTileEC;

public class TileAccelerationShrineUpgrade extends AbstractTileEC {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockAccelerationShrineUpgrade.NAME) public static final TileEntityType<TileAccelerationShrineUpgrade> TYPE = null;

	public TileAccelerationShrineUpgrade() {
		super(TYPE);
	}

}
