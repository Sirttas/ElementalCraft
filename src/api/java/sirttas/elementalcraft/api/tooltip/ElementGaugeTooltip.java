package sirttas.elementalcraft.api.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

public record ElementGaugeTooltip(
        IElementStorage storage
) implements TooltipComponent {
}
