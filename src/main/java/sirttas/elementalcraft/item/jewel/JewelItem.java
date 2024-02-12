package sirttas.elementalcraft.item.jewel;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.property.ECProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class JewelItem extends ECItem {

    public static final String NAME = ECNames.JEWEL;

    public JewelItem() {
        super(ECProperties.Items.ITEM_UNSTACKABLE);
    }

    @Nonnull
    public static Jewel getJewel(@Nonnull ItemStack stack) {
        return JewelHelper.getJewel(stack);
    }

    public ItemStack getJewelStack(Jewel jewel) {
        ItemStack stack = new ItemStack(this);

        JewelHelper.setJewel(stack, jewel);
        return stack;
    }

    @Nonnull
    @Override
    public Component getName(@Nonnull ItemStack stack) {
        Jewel jewel = getJewel(stack);

        if (jewel != Jewels.NONE.get()) {
            return jewel.getDisplayName();
        }
        return super.getName(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        Jewel jewel = getJewel(stack);

        if (jewel != Jewels.NONE.get()) {
            jewel.appendHoverText(tooltip);
        }
    }
}
