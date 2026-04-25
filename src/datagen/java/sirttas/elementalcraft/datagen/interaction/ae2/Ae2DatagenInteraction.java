package sirttas.elementalcraft.datagen.interaction.ae2;

import appeng.api.ids.AEConstants;
import appeng.core.definitions.AEBlocks;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import sirttas.elementalcraft.block.shrine.budding.BudTypes;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.datagen.definition.BudTypeDataDefinition;
import sirttas.elementalcraft.datagen.interaction.DatagenInteraction;

import java.util.List;

public class Ae2DatagenInteraction implements DatagenInteraction {

    @Override
    public List<BudTypeDataDefinition> getBudTypeDataDefinitions() {
        return List.of(BudTypeDataDefinition.builder(BudTypes.CERTUS_QUARTZ)
                .then(AEBlocks.SMALL_QUARTZ_BUD.block())
                .then(AEBlocks.MEDIUM_QUARTZ_BUD.block())
                .then(AEBlocks.LARGE_QUARTZ_BUD.block())
                .then(AEBlocks.QUARTZ_CLUSTER.block())
                .requires(ShrineUpgrades.CERTUS_QUARTZ)
                .when(new ModLoadedCondition(AEConstants.MOD_ID))
                .build());
    }
}