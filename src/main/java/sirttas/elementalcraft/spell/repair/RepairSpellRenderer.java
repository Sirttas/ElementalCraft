package sirttas.elementalcraft.spell.repair;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.renderer.ECRendererHelper;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.renderer.ISpellRenderer;

import javax.annotation.Nullable;

public class RepairSpellRenderer implements ISpellRenderer {

    private static final float HAMMER_INTERVAL = 6.5F;

    @Override
    public void render(Spell spell, Entity caster, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (caster instanceof AbstractClientPlayer player) {
            var ray = EntityHelper.rayTrace(caster);
            var rayType = ray.getType();

            if (rayType == HitResult.Type.BLOCK && ray instanceof BlockHitResult blockRay && spell instanceof RepairSpell repairSpell) {
                var anvilPos = blockRay.getBlockPos().relative(blockRay.getDirection());
                var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
                var cameraPos = camera.getPosition();
                var newStack = new PoseStack();
                var anvilState = getAnvilState(player, blockRay);
                var itemToRepair = repairSpell.getItemToRepair(player);

                if (anvilState == null) {
                    return;
                }

                newStack.mulPose(Vector3f.XP.rotationDegrees(camera.getXRot()));
                newStack.mulPose(Vector3f.YP.rotationDegrees(camera.getYRot() + 180.0F));
                newStack.translate(anvilPos.getX() - cameraPos.x(), anvilPos.getY() - cameraPos.y(), anvilPos.getZ() - cameraPos.z());
                ECRendererHelper.renderBatched(anvilState, newStack, buffer, caster.getLevel(), anvilPos);
                newStack.translate(0.5, 1, 0.5);
                newStack.mulPose(anvilState.getValue(AnvilBlock.FACING).getRotation());
                if (itemToRepair.is(ECItems.STAFF.get())) {
                    newStack.mulPose(Vector3f.XP.rotationDegrees(-45.0F));
                    newStack.translate(0, -0.3, 0);
                    newStack.mulPose(Vector3f.YP.rotationDegrees(15.0F));
                }
                ECRendererHelper.renderItem(itemToRepair, newStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }
    }

    @Override
    public void renderFirstPerson(Spell spell, LocalPlayer player, InteractionHand hand, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ISpellRenderer.super.renderFirstPerson(spell, player, hand, partialTicks, poseStack, buffer, packedLight);

        var useTicks = 40F - ((player.getUseItemRemainingTicks() - partialTicks + 1.0F) % 40F);
        var swing = useTicks < HAMMER_INTERVAL * 3 ? (useTicks % HAMMER_INTERVAL) / HAMMER_INTERVAL : 0;

        Minecraft.getInstance().gameRenderer.itemInHandRenderer.renderArmWithItem(player, partialTicks, Mth.lerp(partialTicks, player.xRotO, player.getXRot()), InteractionHand.MAIN_HAND, swing, new ItemStack(ECItems.REPAIR_HAMMER.get()), 0.0F, poseStack, buffer, packedLight);
    }

    @Nullable
    private static BlockState getAnvilState(Player player, BlockHitResult blockRay) {
        return Blocks.ANVIL.getStateForPlacement(new BlockPlaceContext(player, InteractionHand.MAIN_HAND, new ItemStack(Blocks.ANVIL), blockRay));
    }

    @Override
    public boolean hideHand(InteractionHand hand) {
        return true;
    }
}
