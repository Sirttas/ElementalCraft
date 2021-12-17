package sirttas.elementalcraft.item.source.receptacle.improved;

import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.item.source.receptacle.EmptyReceptacleItem;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public class EmptyImprovedReceptacleItem extends EmptyReceptacleItem implements ISourceInteractable {

	public static final String NAME = "receptacle_improved_empty";

	public EmptyImprovedReceptacleItem() {
		super(ECProperties.Items.RECEPTACLE_IMPROVED);
	}

	@Override
	public boolean isValidRepairItem(@Nonnull ItemStack toRepair, ItemStack repair) {
		return ECTags.Items.INGOTS_FIREITE.contains(repair.getItem());
	}

}
