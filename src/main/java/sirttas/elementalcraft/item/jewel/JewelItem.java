package sirttas.elementalcraft.item.jewel;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.jewel.Jewel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
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
    public @NotNull String getDescriptionId() {
        return getJewel().getDescriptionId();
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Item.TooltipContext tooltipContext, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        getJewel().appendHoverText(tooltip);
    }
}
