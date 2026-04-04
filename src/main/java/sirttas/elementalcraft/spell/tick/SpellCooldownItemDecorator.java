package sirttas.elementalcraft.spell.tick;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.spell.SpellHelper;

public class SpellCooldownItemDecorator implements IItemDecorator {
    @Override
    public boolean render(@NotNull GuiGraphicsExtractor guiGraphics, @NotNull Font font, @NotNull ItemStack stack, int x, int y) {
        var minecraft = Minecraft.getInstance();
        var localplayer = minecraft.player;
        var cooldown = localplayer == null ? 0.0F : SpellTickHelper.getCooldown(localplayer, SpellHelper.getSpell(stack).value(), minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true));

        if (cooldown > 0.0F) {
            int top = y + Mth.floor(16.0F * (1.0F - cooldown));
            int bottom = top + Mth.ceil(16.0F * cooldown);
            guiGraphics.fill(RenderPipelines.GUI, x, top, x + 16, bottom, Integer.MAX_VALUE);
        }
        return true;
    }
}
