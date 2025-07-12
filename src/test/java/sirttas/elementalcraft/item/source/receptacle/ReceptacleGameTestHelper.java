package sirttas.elementalcraft.item.source.receptacle;

import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.source.trait.SourceTraitTestHelper;

public class ReceptacleGameTestHelper {

    private ReceptacleGameTestHelper() {}

    public static ItemStack createSimpleReceptacle(ElementType type) {
        return ReceptacleHelper.create(type, SourceTraitTestHelper.getDefaultTraits());
    }
}
