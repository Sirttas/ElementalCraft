package sirttas.elementalcraft.block.synthesizer.draining;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.synthesizer.AbstractSynthesizerBlockEntity;

public class DrainingSynthesizerBlockEntity extends AbstractSynthesizerBlockEntity {

    public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(DrainingSynthesizerBlock.NAME);
    private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);


    public DrainingSynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.DRAINING_SYNTHESIZER, PROPERTIES, pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DrainingSynthesizerBlockEntity drainingSynthesizer) {
        drainingSynthesizer.handleSynthesis();
    }

    @Override
    protected int synthesizeElement() {
        return 0;
    }

    public void fill() {
        insertElement(false);
    }

    public boolean needsElement() {
        return insertElement(true) <= 0;
    }

    private int insertElement(boolean simulate) {
        return this.getElementStorage().insertElement(Math.round(this.synthesisMultiplier), ElementType.WATER, simulate);
    }
}
