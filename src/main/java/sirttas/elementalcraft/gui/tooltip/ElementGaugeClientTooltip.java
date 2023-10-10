package sirttas.elementalcraft.gui.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.tooltip.ElementGaugeTooltip;
import sirttas.elementalcraft.gui.GuiHelper;

import javax.annotation.Nonnull;

public record ElementGaugeClientTooltip(
        IElementStorage storage
) implements ClientTooltipComponent {

    public ElementGaugeClientTooltip(ElementGaugeTooltip tooltip) {
        this(tooltip.storage());
    }

    @Override
    public int getHeight() {
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
    public void renderImage(@Nonnull Font font, int x, int y, @Nonnull GuiGraphics guiGraphics) {
        if (!isValid()) {
            return;
        }

        var i = 0;

        for (var elementType : ElementType.ALL_VALID) {
            var elementCapacity = storage.getElementCapacity(elementType);
            var elementAmount = storage.getElementAmount(elementType);

            if (elementCapacity > 0) {
                GuiHelper.renderElementGauge(guiGraphics, font, x + (i++ * 18), y, elementAmount, elementCapacity, elementType, false);
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
