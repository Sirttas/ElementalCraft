package sirttas.elementalcraft.datagen.interaction.ae2;

import appeng.api.ids.AEConstants;
import appeng.core.definitions.AEBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import sirttas.elementalcraft.block.shrine.budding.BudTypes;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.datagen.definition.BudTypeDataDefinition;
import sirttas.elementalcraft.datagen.interaction.DatagenInteraction;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
                .texture(Identifier.fromNamespaceAndPath(AEConstants.MOD_ID, "block/flawless_budding_quartz"))
                .build());
    }

    @Override
    public List<DataProvider> getProviders(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return List.of(new Ae2RecipeProvider.Runner(output, lookupProvider));
    }
}