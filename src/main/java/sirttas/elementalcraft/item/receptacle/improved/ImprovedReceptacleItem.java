package sirttas.elementalcraft.item.receptacle.improved;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.receptacle.ReceptacleItem;
import sirttas.elementalcraft.item.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.tag.ECTags;

public class ImprovedReceptacleItem extends ReceptacleItem {

	public static final String NAME = "receptacle_improved";

	public ImprovedReceptacleItem() {
		super(ECProperties.Items.RECEPTACLE_IMPROVED);
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return ECTags.Items.INGOTS_FIREITE.contains(repair.getItem());
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			for (ElementType elementType : ElementType.values()) {
				if (elementType != ElementType.NONE) {
					items.add(ReceptacleHelper.createImproved(elementType));
				}
			}
		}
	}
}
