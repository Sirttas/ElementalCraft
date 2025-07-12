package sirttas.elementalcraft.block.synthesizer.culinary;

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

public class CulinarySynthesizerBlockEntity extends AbstractContainerSynthesizerBlockEntity {

    public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(CulinarySynthesizerBlock.NAME);
    private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

    private final SingleStackContainer inventory;

    public CulinarySynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.CULINARY_SYNTHESIZER, PROPERTIES, pos, state);
        inventory = new CulinarySynthesizerContainer(this);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CulinarySynthesizerBlockEntity culinarySynthesizer) {
        culinarySynthesizer.handleSynthesis();
    }

    @Override
    protected int getElementAmountForStack(ItemStack stack) {
        var foodProperties = stack.getFoodProperties(null);

        if (foodProperties == null) {
            return 0;
        }
        return Math.round((foodProperties.nutrition() + foodProperties.saturation()) * this.synthesisMultiplier);
    }

    @NotNull
    @Override
    public Container getInventory() {
        return inventory;
    }
}
