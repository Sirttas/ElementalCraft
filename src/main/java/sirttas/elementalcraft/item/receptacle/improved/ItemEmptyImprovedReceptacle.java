package sirttas.elementalcraft.item.receptacle.improved;

import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.item.receptacle.ItemEmptyReceptacle;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.tag.ECTags;

public class ItemEmptyImprovedReceptacle extends ItemEmptyReceptacle implements ISourceInteractable {

	public static final String NAME = "receptacle_improved_empty";

	public ItemEmptyImprovedReceptacle() {
		super(ECProperties.Items.RECEPTACLE_IMPROVED);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return ECTags.Items.INGOTS_FIREITE.contains(repair.getItem());
	}

}
