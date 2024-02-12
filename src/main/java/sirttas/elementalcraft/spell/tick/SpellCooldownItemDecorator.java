package sirttas.elementalcraft.spell.tick;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import sirttas.elementalcraft.spell.SpellHelper;

public class SpellCooldownItemDecorator implements IItemDecorator {
    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int x, int y) {
        var minecraft = Minecraft.getInstance();
        var localplayer = minecraft.player;
        var f = localplayer == null ? 0.0F : SpellTickHelper.getCooldown(localplayer, SpellHelper.getSpell(stack), minecraft.getFrameTime());

        if (f > 0.0F) {
            var minY = y + Mth.floor(16.0F * (1.0F - f));
            var maxY = minY + Mth.ceil(16.0F * f);

            guiGraphics.fill(RenderType.guiOverlay(), x, minY, x + 16, maxY, Integer.MAX_VALUE);
        }

        return true;
    }
}
