package sirttas.elementalcraft.item.chisel;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import sirttas.elementalcraft.tag.ECTags;

public class ChiselTiers {

    public static final Tier DRENCHED_IRON = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 100, 0, 0, 14, () -> Ingredient.of(ECTags.Items.INGOTS_DRENCHED_IRON));
    public static final Tier SWIFT_ALLOY = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL,500, 0, 0, 22, () -> Ingredient.of(ECTags.Items.INGOTS_SWIFT_ALLOY));
    public static final Tier FIREITE = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL,2500, 0, 0, 22, () -> Ingredient.of(ECTags.Items.INGOTS_FIREITE));

    private ChiselTiers() {}
}
