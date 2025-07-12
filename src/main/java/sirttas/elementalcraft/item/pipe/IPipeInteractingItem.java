package sirttas.elementalcraft.item.pipe;

import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;

import javax.annotation.Nonnull;

public interface IPipeInteractingItem {

    @Nonnull
    default ItemInteractionResult useOnPipe(@Nonnull ElementPipeBlockEntity pipe, @Nonnull UseOnContext context) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
