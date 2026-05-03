package sirttas.elementalcraft.client.renderer.gui.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.tooltip.ElementGaugeTooltip;
import sirttas.elementalcraft.client.renderer.gui.ElementGaugeGui;

import javax.annotation.Nonnull;

public record ElementGaugeClientTooltip(
        IElementStorage storage
) implements ClientTooltipComponent {

    public ElementGaugeClientTooltip(ElementGaugeTooltip tooltip) {
        this(tooltip.storage());
    }

    @Override
    public int getHeight(@Nonnull Font font) {
        return isValid() ? 18 : 0;
    }

    @Override
    public int getWidth(@Nonnull Font font) {
        if (!isValid()) {
            return 0;
        }

        return Math.max((int) (ElementType.ALL_VALID.stream()
                .filter(elementType -> storage.getElementCapacity(elementType) > 0).count() * 18) - 2, 0);
    }

    @Override
    public void extractImage(@NotNull Font font, int x, int y, int w, int h, @NotNull GuiGraphicsExtractor graphics) {
        if (!isValid()) {
            return;
        }

        var i = 0;

        for (var elementType : ElementType.ALL_VALID) {
            var elementCapacity = storage.getElementCapacity(elementType);
            var elementAmount = storage.getElementAmount(elementType);

            if (elementCapacity > 0) {
                ElementGaugeGui.renderElementGauge(graphics, font, x + (i++ * 18), y, elementType, elementAmount, elementCapacity);
            }
        }
    }

    private boolean isValid() {
        if (storage instanceof IElementTypeProvider provider) {
            return provider.getElementType() != ElementType.NONE;
        }
        return storage != null;
    }

}
