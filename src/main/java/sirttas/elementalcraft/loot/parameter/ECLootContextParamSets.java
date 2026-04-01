package sirttas.elementalcraft.loot.parameter;

import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class ECLootContextParamSets {

    public static final ContextKeySet PIPE_UPGRADE = LootContextParamSets.register("elementalcraft_pipe_upgrade" /* TODO make a PR for neoforge to use resource location instead of string */, builder -> builder
            .required(LootContextParams.BLOCK_STATE)
            .required(LootContextParams.ORIGIN)
            .required(ECLootContextParams.DIRECTION)
            .optional(LootContextParams.THIS_ENTITY)
            .optional(LootContextParams.BLOCK_ENTITY)
            .optional(LootContextParams.EXPLOSION_RADIUS));

    private ECLootContextParamSets() {}

}
