package sirttas.elementalcraft.spell.repair;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.renderer.SpellRenderer;
import sirttas.elementalcraft.spell.tick.SpellInstance;

import javax.annotation.Nullable;

public class RepairSpellRenderer implements SpellRenderer<RepairSpellRenderState> {

    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    private static final float HAMMER_INTERVAL = 6.5F;

    private final ItemModelResolver itemModelResolver;
    private final BlockModelResolver blockModelResolver;

    public RepairSpellRenderer() {
        this.itemModelResolver = Minecraft.getInstance().getItemModelResolver(); // TODO use context
        this.blockModelResolver = Minecraft.getInstance().getBlockModelResolver(); // TODO use context
    }

    @Override
    public RepairSpellRenderState createRenderState() {
        return new RepairSpellRenderState();
    }

    @Override
    public void extractRenderState(RepairSpellRenderState state, Holder<Spell> spell, @Nullable SpellInstance instance, Entity caster, InteractionHand hand, float partialTicks, int lightCoords) {
        SpellRenderer.super.extractRenderState(state, spell, instance, caster, hand, partialTicks, lightCoords);
        state.partialTicks = partialTicks;
        state.anvil.clear();
        state.item.clear();
        if (!(caster instanceof AbstractClientPlayer player)) {
            return;
        }
        var ray = EntityHelper.rayTrace(caster);
        var rayType = ray.getType();

        if (rayType != HitResult.Type.BLOCK || !(ray instanceof BlockHitResult blockRay)) {
            return;
        }
        var anvilState = getAnvilState(player, blockRay);

        if (anvilState == null) {
            return;
        }
        state.anvilPos = blockRay.getBlockPos().relative(blockRay.getDirection());
        state.anvilFacing = anvilState.getValue(AnvilBlock.FACING);
        blockModelResolver.update(state.anvil, anvilState, BLOCK_DISPLAY_CONTEXT);

        var useTicks = 40F - ((player.getUseItemRemainingTicks() - partialTicks + 1.0F) % 40F);
        state.firstPersonSwing = useTicks < HAMMER_INTERVAL * 3 ? (useTicks % HAMMER_INTERVAL) / HAMMER_INTERVAL : 0;

        var itemToRepair = spell.value().getItemInOtherHand(player);

        state.isStaff = itemToRepair.is(ECItems.STAFF.get());
        itemModelResolver.updateForLiving(state.item, itemToRepair, ItemDisplayContext.GROUND, player);
    }

    @Override
    public void submit(RepairSpellRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        var newStack = new PoseStack();

        newStack.mulPose(Axis.XP.rotationDegrees(camera.xRot));
        newStack.mulPose(Axis.YP.rotationDegrees(camera.yRot + 180.0F));
        newStack.translate(state.anvilPos.getX() - camera.pos.x(), state.anvilPos.getY() - camera.pos.y(), state.anvilPos.getZ() - camera.pos.z());
        state.anvil.submit(newStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        newStack.translate(0.5, 1, 0.5);
        newStack.mulPose(state.anvilFacing.getRotation());
        if (state.isStaff) {
            newStack.mulPose(Axis.XP.rotationDegrees(-45.0F));
            newStack.translate(0, -0.3, 0);
            newStack.mulPose(Axis.YP.rotationDegrees(15.0F));
        }
        state.item.submit(newStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    }

    @Override
    public void submitFirstPerson(RepairSpellRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        SpellRenderer.super.submitFirstPerson(state, poseStack, submitNodeCollector, camera);
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;

        minecraft.gameRenderer.itemInHandRenderer.renderArmWithItem(player, state.partialTicks, Mth.lerp(state.partialTicks, player.xRotO, player.getXRot()), InteractionHand.MAIN_HAND, state.firstPersonSwing, new ItemStack(ECItems.REPAIR_HAMMER), 0.0F, poseStack, submitNodeCollector, state.lightCoords);
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
