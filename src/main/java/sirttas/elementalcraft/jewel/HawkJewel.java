package sirttas.elementalcraft.jewel;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.function.Consumer;

public class HawkJewel extends Jewel {

    public static final String NAME = "hawk";

    public HawkJewel() {
        super(ElementType.AIR, 200, false);
    }

    @Override
    public void appendHoverText(@NotNull Consumer<Component> builder) {
        builder.accept(Component.translatable("tooltip.elementalcraft.hawk").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(builder);
    }
}
