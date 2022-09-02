package sirttas.elementalcraft.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.renderer.ISpellInstanceRenderer;
import sirttas.elementalcraft.spell.renderer.SpellRenderers;
import sirttas.elementalcraft.spell.tick.AbstractSpellInstance;
import sirttas.elementalcraft.spell.tick.SpellTickHelper;

import javax.annotation.Nullable;

@Mixin(ItemInHandRenderer.class)
public abstract class MixinItemInHandRenderer {

    @Inject(method = "renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("RETURN"))
    public void renderArmWithItem$return(AbstractClientPlayer player, float partialTicks, float pPitch, InteractionHand hand, float pSwingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        renderSpellEffect(player, stack, hand, partialTicks, poseStack, buffer, packedLight);
    }

    private void renderSpellEffect(AbstractClientPlayer player, ItemStack stack, InteractionHand hand, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (player instanceof LocalPlayer localPlayer && !localPlayer.isScoping()) {
            var spell = SpellHelper.getSpell(stack);

            if (localPlayer.isUsingItem() && localPlayer.getUsedItemHand() == hand && !stack.isEmpty()) {
                renderSingleSpell(spell, null, localPlayer, hand, partialTicks, poseStack, buffer, packedLight);
            }
            if (hand == InteractionHand.MAIN_HAND) {
                SpellTickHelper.getSpellInstances(localPlayer).forEach(i -> renderSingleSpell(i.getSpell(), i, localPlayer, hand, partialTicks, poseStack, buffer, packedLight));
            }
        }
    }

    private void renderSingleSpell(Spell spell, @Nullable AbstractSpellInstance instance, LocalPlayer localPlayer, InteractionHand hand, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (!spell.isValid()) {
            return;
        }
        var renderer = SpellRenderers.get(spell);

        if (renderer == null) {
            return;
        }
        poseStack.pushPose();
        if (renderer instanceof ISpellInstanceRenderer spellInstanceRenderer && instance != null) {
            spellInstanceRenderer.renderFirstPerson(instance, localPlayer, partialTicks, poseStack, buffer, packedLight);
        } else {
            renderer.renderFirstPerson(spell, localPlayer, hand, partialTicks, poseStack, buffer, packedLight);
        }
        poseStack.popPose();
    }
}
