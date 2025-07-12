package sirttas.elementalcraft.block.synthesizer.combustion;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.synthesizer.AbstractContainerSynthesizerBlockEntity;
import sirttas.elementalcraft.container.SingleStackContainer;

public class CombustionSynthesizerBlockEntity extends AbstractContainerSynthesizerBlockEntity {

    public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(CombustionSynthesizerBlock.NAME);
    private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

    private final SingleStackContainer inventory;

    public CombustionSynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.COMBUSTION_SYNTHESIZER, PROPERTIES, pos, state);
        inventory = new CombustionSynthesizerContainer(this);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CombustionSynthesizerBlockEntity combustionSynthesizer) {
        combustionSynthesizer.handleSynthesis();
    }

    @Override
    protected int getElementAmountForStack(ItemStack stack) {
        return Math.round(stack.getBurnTime(null) * this.synthesisMultiplier);
    }

    @NotNull
    @Override
    public Container getInventory() {
        return inventory;
    }
}
