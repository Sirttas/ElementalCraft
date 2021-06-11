package sirttas.elementalcraft.block.container.creative;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlockEntity;

public class CreativeElementContainerBlockEntity extends AbstractElementContainerBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + CreativeElementContainerBlock.NAME) public static final TileEntityType<CreativeElementContainerBlockEntity> TYPE = null;

	public CreativeElementContainerBlockEntity() {
		super(TYPE, CreativeElementStorage::new);
	}

	@Override
	public boolean isSmall() {
		return false;
	}
}
