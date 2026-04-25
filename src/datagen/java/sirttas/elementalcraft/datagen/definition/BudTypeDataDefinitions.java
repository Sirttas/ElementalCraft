package sirttas.elementalcraft.datagen.definition;

import appeng.api.ids.AEConstants;
import appeng.core.definitions.AEBlocks;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.budding.BudTypes;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

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
    public static final BudTypeDataDefinition CERTUS_QUARTZ = BudTypeDataDefinition.builder(BudTypes.CERTUS_QUARTZ) // TODO move to AE specific package
            .then(AEBlocks.SMALL_QUARTZ_BUD.block())
            .then(AEBlocks.MEDIUM_QUARTZ_BUD.block())
            .then(AEBlocks.LARGE_QUARTZ_BUD.block())
            .then(AEBlocks.QUARTZ_CLUSTER.block())
            .requires(ShrineUpgrades.CERTUS_QUARTZ)
            .when(new ModLoadedCondition(AEConstants.MOD_ID))
            .build();

    private static final List<BudTypeDataDefinition> ALL = List.of(AMETHYST, SPRINGALINE, CERTUS_QUARTZ);

    private BudTypeDataDefinitions() {}

    public static List<BudTypeDataDefinition> getBudTypeDataDefinitions() {
        return ALL;
    }
}
