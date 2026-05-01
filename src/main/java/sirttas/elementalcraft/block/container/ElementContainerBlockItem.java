package sirttas.elementalcraft.block.container;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.gui.ElementGaugeGui;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ElementContainerBlockItem extends BlockItem {

    public ElementContainerBlockItem(AbstractElementContainerBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    public int getBarColor(@Nonnull ItemStack stack) {
        return stack.getOrDefault(ECDataComponents.ELEMENT_TYPE, ElementType.NONE).getColor();
    }

    @Override
    @Nonnull
    public Optional<TooltipComponent> getTooltipImage(@Nonnull ItemStack stack) {
        var elementType = stack.getOrDefault(ECDataComponents.ELEMENT_TYPE, ElementType.NONE);
        int amount = stack.getOrDefault(ECDataComponents.ELEMENT_AMOUNT, 0);
        var capacity = ((AbstractElementContainerBlock) getBlock()).getDefaultCapacity();

        if (elementType != ElementType.NONE && amount > 0) {
            return Optional.of(new Tooltip(elementType, amount, capacity));
        }
        return Optional.empty();
    }

    @Override
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(@Nonnull ItemStack stack) {
        int amount = stack.getOrDefault(ECDataComponents.ELEMENT_AMOUNT, 0);
        var capacity = ((AbstractElementContainerBlock) getBlock()).getDefaultCapacity();

        if (amount > 0) {
            return Math.round(amount * 13.0F / capacity);
            }
        return 0;
    }

    public record Tooltip(
            ElementType elementType,
            int amount,
            int capacity
    ) implements TooltipComponent { }

    public record ClientTooltip(
            ElementType elementType,
            int amount,
            int capacity
    ) implements ClientTooltipComponent {

        public ClientTooltip(Tooltip tooltip) {
            this(tooltip.elementType, tooltip.amount, tooltip.capacity);
        }

        @Override
        public int getHeight(@NotNull Font font) {
            return 18;
        }

        @Override
        public int getWidth(@Nonnull Font font) {
            return 16;
        }

        @Override
        public void extractImage(@NotNull Font font, int x, int y, int w, int h, @NotNull GuiGraphicsExtractor graphics) {
            ElementGaugeGui.renderElementGauge(graphics, font, x, y, amount, capacity, elementType);
        }
    }
}
