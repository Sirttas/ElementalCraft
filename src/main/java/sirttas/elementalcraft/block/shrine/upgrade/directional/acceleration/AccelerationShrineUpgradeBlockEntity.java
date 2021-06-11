package sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;

public class AccelerationShrineUpgradeBlockEntity extends AbstractECBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + AccelerationShrineUpgradeBlock.NAME) public static final TileEntityType<AccelerationShrineUpgradeBlockEntity> TYPE = null;

	public AccelerationShrineUpgradeBlockEntity() {
		super(TYPE);
	}

}
