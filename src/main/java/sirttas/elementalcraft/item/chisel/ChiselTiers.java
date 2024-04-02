package sirttas.elementalcraft.item.chisel;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import sirttas.elementalcraft.tag.ECTags;

public class ChiselTiers {

    public static final Tier DRENCHED_IRON = new SimpleTier(0, 100, 0, 0, 14, null, () -> Ingredient.of(ECTags.Items.INGOTS_DRENCHED_IRON));
    public static final Tier SWIFT_ALLOY = new SimpleTier(0, 500, 0, 0, 22, null, () -> Ingredient.of(ECTags.Items.INGOTS_SWIFT_ALLOY));
    public static final Tier FIREITE = new SimpleTier(0, 2500, 0, 0, 22, null, () -> Ingredient.of(ECTags.Items.INGOTS_FIREITE));

    private ChiselTiers() {}
}
