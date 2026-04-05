package sirttas.elementalcraft.item.pipe;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;

import javax.annotation.Nonnull;

public interface IPipeInteractingItem {

    @Nonnull
    default InteractionResult useOnPipe(@Nonnull ElementPipeBlockEntity pipe, @Nonnull UseOnContext context) {
        return InteractionResult.PASS;
    }
}
