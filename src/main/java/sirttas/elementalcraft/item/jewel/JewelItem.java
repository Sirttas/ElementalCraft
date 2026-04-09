package sirttas.elementalcraft.item.jewel;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.jewel.Jewel;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class JewelItem extends Item {

    public static final String NAME = ECNames.JEWEL;

    private final Supplier<Jewel> supplier;
    private Jewel jewel;

    public JewelItem(Supplier<Jewel> supplier, Item.Properties properties) {
        super(properties);
        this.supplier = supplier;
    }

    @Nonnull
    public Jewel getJewel() {
        if (jewel == null) {
            jewel = supplier.get();
        }
        return jewel;
    }
    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack) {
        return Component.translatable(getJewel().getDescriptionId());
    }

    @Override
    @Deprecated
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> builder, @NotNull TooltipFlag tooltipFlag) {
        getJewel().appendHoverText(builder);
    }
}
