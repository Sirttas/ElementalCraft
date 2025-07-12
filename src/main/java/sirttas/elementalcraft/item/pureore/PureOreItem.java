package sirttas.elementalcraft.item.pureore;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import sirttas.elementalcraft.pureore.PureOre;
import sirttas.elementalcraft.pureore.display.PureOreDisplayManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

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
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Item.TooltipContext tooltipContext, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		if (flag.isAdvanced()) {
			tooltip.add(Component.translatable("tooltip.elementalcraft.pure_ore.id", PureOre.getId(stack).toString()).withStyle(ChatFormatting.GRAY));

			var colors = PureOreDisplayManager.getInstance().getColors(stack);

			if (colors != null && colors.length > 0) {
				tooltip.add(Component.translatable("tooltip.elementalcraft.pure_ore.colors", getColorText(colors[0]), getColorText(colors[1]), getColorText(colors[2])).withStyle(ChatFormatting.GRAY));
			}
		}
	}

	private String getColorText(int color) {
		return "#" + Integer.toHexString(color).substring(2).toUpperCase();
	}
}
