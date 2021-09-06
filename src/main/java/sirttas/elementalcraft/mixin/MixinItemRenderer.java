package sirttas.elementalcraft.mixin;

import javax.annotation.Nullable;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.SpellTickManager;

@SuppressWarnings("deprecation")
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer implements ResourceManagerReloadListener {

	@Unique private ThreadLocal<ItemStack> stack = ThreadLocal.withInitial(() -> ItemStack.EMPTY);

	@Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", 
			at = @At("HEAD"))
	public void renderItemOverlayIntoGUI(Font fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text, CallbackInfo ci) {
		this.stack.set(stack);
	}

	@ModifyVariable(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", 
			index = 8, 
			at = @At(value = "JUMP", opcode = Opcodes.IFLE, shift = At.Shift.BY, by = -3))
	public float addSpellCooldown(float value) {
		Minecraft minecraft = Minecraft.getInstance();

		value = value > 0 ? value : SpellTickManager.getInstance(minecraft.level).getCooldown(minecraft.player, SpellHelper.getSpell(stack.get()), minecraft.getFrameTime());
		stack.remove();
		return value;
	}
}
