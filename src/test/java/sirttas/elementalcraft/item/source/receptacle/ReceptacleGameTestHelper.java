package sirttas.elementalcraft.item.source.receptacle;

import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.source.trait.SourceTraitTestHelper;
import sirttas.elementalcraft.item.ECItems;

public class ReceptacleGameTestHelper {

    private ReceptacleGameTestHelper() {}

    public static ItemStack createSimpleReceptacle(ElementType type) {
        if (type == ElementType.NONE) {
            return new ItemStack(ECItems.EMPTY_RECEPTACLE.get());
        }
        return ReceptacleHelper.create(type, SourceTraitTestHelper.deserializeTraits(SourceTraitTestHelper.createDefaultTraits()), false);
    }
}
