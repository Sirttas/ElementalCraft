package sirttas.elementalcraft.block.synthesizer.cracking;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.cracking.CrackingRecipe;

public class CrackingSynthesizerBlockEntity extends AbstractCrackingSynthesizerBlockEntity<CrackingRecipe> {

    public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(CrackingSynthesizerBlock.NAME);
    private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

    public CrackingSynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.CRACKING_SYNTHESIZER, PROPERTIES, ECRecipeTypes.CRACKING, pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CrackingSynthesizerBlockEntity crackingSynthesizer) {
        crackingSynthesizer.handleSynthesis();
    }

}
