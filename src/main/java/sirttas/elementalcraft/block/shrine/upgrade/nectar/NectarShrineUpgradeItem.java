package sirttas.elementalcraft.block.shrine.upgrade.nectar;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradeItem;

import java.util.function.Consumer;

public class NectarShrineUpgradeItem extends ShrineUpgradeItem {

    public NectarShrineUpgradeItem(NectarShrineUpgradeBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@Nullable TooltipContext tooltipContext, @NotNull TooltipDisplay display, @NotNull Consumer<Component> builder, @NotNull TooltipFlag flag) {
        builder.accept(Component.translatable("tooltip.elementalcraft.shrine_upgrade.nectar").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(stack, tooltipContext, display, builder, flag);
    }
}
