package sirttas.elementalcraft.item.pureore;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.ECItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PureOreItem extends ECItem {

	public static final String NAME = "pure_ore";

	@Nonnull
    @Override
	public Component getName(@Nonnull ItemStack stack) {
		var name = ElementalCraft.PURE_ORE_MANAGER.getPureOreName(stack);

		if (name != null) {
			return Component.translatable("tooltip.elementalcraft.pure_ore", name);
		}
		return super.getName(stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		if (flag.isAdvanced()) {
			tooltip.add(Component.translatable("tooltip.elementalcraft.pure_ore.id", ElementalCraft.PURE_ORE_MANAGER.getPureOreId(stack).toString()).withStyle(ChatFormatting.GRAY));

			var colors = ElementalCraft.PURE_ORE_MANAGER.getColors(stack);

			if (colors != null && colors.length > 0) {
				tooltip.add(Component.translatable("tooltip.elementalcraft.pure_ore.colors", getColorText(colors[0]), getColorText(colors[1]), getColorText(colors[2])).withStyle(ChatFormatting.GRAY));
			}
		}
	}

	private String getColorText(int color) {
		return "#" + Integer.toHexString(color).substring(2).toUpperCase();
	}
}
