package sirttas.elementalcraft.item.jewel;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.property.ECProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class JewelItem extends ECItem {

    public static final String NAME = ECNames.JEWEL;

    public JewelItem() {
        super(ECProperties.Items.ITEM_UNSTACKABLE);
    }

    public static Jewel getJewel(ItemStack stack) {
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

        if (jewel != null) {
            return jewel.getDisplayName();
        }
        return super.getName(stack);
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab group, @Nonnull NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            Jewel.REGISTRY.forEach(j -> items.add(getJewelStack(j)));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        Jewel jewel = getJewel(stack);

        if (jewel != null) {
            jewel.appendHoverText(tooltip);
        }
    }
}
