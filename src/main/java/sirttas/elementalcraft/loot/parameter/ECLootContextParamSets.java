package sirttas.elementalcraft.loot.parameter;

import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class ECLootContextParamSets {

    public static final LootContextParamSet PIPE_UPGRADE = LootContextParamSets.register("pipe_upgrade" /* TODO make a PR for neoforge to use resource location instead of string */, builder -> builder
            .required(LootContextParams.BLOCK_STATE)
            .required(LootContextParams.ORIGIN)
            .required(ECLootContextParams.DIRECTION)
            .optional(LootContextParams.THIS_ENTITY)
            .optional(LootContextParams.BLOCK_ENTITY)
            .optional(LootContextParams.EXPLOSION_RADIUS));

    private ECLootContextParamSets() {}

}
