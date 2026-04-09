package sirttas.elementalcraft.block.shrine;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;

import java.util.function.Consumer;

public class ShrineItem extends BlockItem implements IElementTypeProvider {

    public ShrineItem(AbstractShrineBlock<?> block, Properties properties) {
        super(block, properties);
    }

    @Override
    @Deprecated
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> builder, @NotNull TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("tooltip.elementalcraft.consumes", getElementType().getDisplayName()).withStyle(ChatFormatting.YELLOW));
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
    }

    @Override
    public @NotNull ElementType getElementType() {
        return ((AbstractShrineBlock<?>) getBlock()).getElementType();
    }
}
