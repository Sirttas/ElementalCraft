package sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tile.TileEC;

public class TileAccelerationShrineUpgrade extends TileEC {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockAccelerationShrineUpgrade.NAME) public static TileEntityType<TileAccelerationShrineUpgrade> TYPE;

	public TileAccelerationShrineUpgrade() {
		super(TYPE);
	}

}
