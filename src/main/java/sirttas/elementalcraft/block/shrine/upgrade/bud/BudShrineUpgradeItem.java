package sirttas.elementalcraft.block.shrine.upgrade.bud;

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

public class BudShrineUpgradeItem extends ShrineUpgradeItem {

    private final String tooltipKey;

    public BudShrineUpgradeItem(BudShrineUpgradeBlock block, String tooltipKey, Properties properties) {
        super(block, properties);
        this.tooltipKey = tooltipKey;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@Nullable TooltipContext tooltipContext, @NotNull TooltipDisplay display, @NotNull Consumer<Component> builder, @NotNull TooltipFlag flag) {
        builder.accept(Component.translatable(tooltipKey).withStyle(ChatFormatting.BLUE));
        super.appendHoverText(stack, tooltipContext, display, builder, flag);
    }
}