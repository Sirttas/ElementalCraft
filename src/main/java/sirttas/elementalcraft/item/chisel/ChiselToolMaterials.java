package sirttas.elementalcraft.item.chisel;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;
import sirttas.elementalcraft.tag.ECTags;

public class ChiselToolMaterials {

    public static final ToolMaterial DRENCHED_IRON = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 100, 0, 0, 14, ECTags.Items.INGOTS_DRENCHED_IRON);
    public static final ToolMaterial SWIFT_ALLOY = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL,500, 0, 0, 22, ECTags.Items.INGOTS_SWIFT_ALLOY);
    public static final ToolMaterial FIREITE = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL,2500, 0, 0, 22, ECTags.Items.INGOTS_FIREITE);

    private ChiselToolMaterials() {}
}
