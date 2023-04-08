package sirttas.elementalcraft.loot;

import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import sirttas.elementalcraft.ElementalCraft;

public class ECLootContextParamSets {

    public static final LootContextParamSet PIPE_UPGRADE = LootContextParamSets.register(ElementalCraft.createRL("pipe_upgrade").toString(),builder -> builder
            .required(LootContextParams.BLOCK_STATE)
            .required(LootContextParams.ORIGIN)
            .optional(LootContextParams.THIS_ENTITY)
            .optional(LootContextParams.BLOCK_ENTITY)
            .optional(LootContextParams.EXPLOSION_RADIUS));

    private ECLootContextParamSets() {}

}
