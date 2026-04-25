package sirttas.elementalcraft.datagen.definition;

import net.minecraft.util.Util;
import net.minecraft.world.level.block.Blocks;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.budding.BudTypes;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.datagen.interaction.DatagenInteraction;

import java.util.ArrayList;
import java.util.List;

public class BudTypeDataDefinitions {

    public static final BudTypeDataDefinition AMETHYST = BudTypeDataDefinition.builder(BudTypes.AMETHYST)
            .then(Blocks.SMALL_AMETHYST_BUD)
            .then(Blocks.MEDIUM_AMETHYST_BUD)
            .then(Blocks.LARGE_AMETHYST_BUD)
            .then(Blocks.AMETHYST_CLUSTER)
            .build();
    public static final BudTypeDataDefinition SPRINGALINE = BudTypeDataDefinition.builder(BudTypes.SPRINGALINE)
            .then(ECBlocks.SMALL_SPRINGALINE_BUD)
            .then(ECBlocks.MEDIUM_SPRINGALINE_BUD)
            .then(ECBlocks.LARGE_SPRINGALINE_BUD)
            .then(ECBlocks.SPRINGALINE_CLUSTER)
            .requires(ShrineUpgrades.SPRINGALINE)
            .build();

    private static final List<BudTypeDataDefinition> ALL = Util.make(() -> {
        var list = new ArrayList<BudTypeDataDefinition>();

        list.add(AMETHYST);
        list.add(SPRINGALINE);
        list.addAll(DatagenInteraction.get().getBudTypeDataDefinitions());
        return List.copyOf(list);
    });

    private BudTypeDataDefinitions() {}

    public static List<BudTypeDataDefinition> getBudTypeDataDefinitions() {
        return ALL;
    }
}