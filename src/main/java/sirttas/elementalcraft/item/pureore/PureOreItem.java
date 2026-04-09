package sirttas.elementalcraft.item.pureore;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.pureore.PureOre;
import sirttas.elementalcraft.pureore.display.PureOreDisplayManager;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class PureOreItem extends Item {

	public static final String NAME = "pure_ore";

	public PureOreItem(Properties properties) {
		super(properties);
	}

	@Nonnull
    @Override
	public Component getName(@Nonnull ItemStack stack) {
		var name = PureOreDisplayManager.getInstance().getPureOreName(stack);

		if (name != null) {
			return name;
		}
		return super.getName(stack);
	}

    @Override
    @Deprecated
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> builder, @NotNull TooltipFlag tooltipFlag) {
		if (tooltipFlag.isAdvanced()) {
            builder.accept(Component.translatable("tooltip.elementalcraft.pure_ore.id", PureOre.getId(itemStack).toString()).withStyle(ChatFormatting.GRAY));

			var colors = PureOreDisplayManager.getInstance().getColors(itemStack);

			if (colors != null && colors.length > 0) {
                builder.accept(Component.translatable("tooltip.elementalcraft.pure_ore.colors", getColorText(colors[0]), getColorText(colors[1]), getColorText(colors[2])).withStyle(ChatFormatting.GRAY));
			}
		}
	}

	private String getColorText(int color) {
		return "#" + Integer.toHexString(color).substring(2).toUpperCase();
	}
}
