package sirttas.elementalcraft.block.synthesizer.cracking.sculk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.synthesizer.cracking.AbstractCrackingSynthesizerBlockEntity;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.cracking.SculkCrackingRecipe;

public class SculkCrackingSynthesizerBlockEntity extends AbstractCrackingSynthesizerBlockEntity<SculkCrackingRecipe> {

    public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(SculkCrackingSynthesizerBlock.NAME);
    private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

    public SculkCrackingSynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.SCULK_CRACKING_SYNTHESIZER, PROPERTIES, ECRecipeTypes.SCULK_CRACKING, pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SculkCrackingSynthesizerBlockEntity crackingSynthesizer) {
        crackingSynthesizer.handleSynthesis();
    }

}
