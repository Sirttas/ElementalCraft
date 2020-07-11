package sirttas.elementalcraft.event;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.infusion.InfusionHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.bauble.ItemHungerlessRing;
import sirttas.elementalcraft.network.message.ECMessage;
import sirttas.elementalcraft.network.message.MessageHandler;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.ICurioItemHandler;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public class EntityHandler {

	private static boolean lastJump = false; // we only need one because it's only used client side

	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
		LivingEntity entity = event.getEntityLiving();

		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;

			ICurioItemHandler curios = CuriosAPI.getCuriosHandler(player).orElse(null);

			if (curios != null && curios.getStackHandler("ring") != null) {
				for (int slot = 0; slot < curios.getStackHandler("ring").getSlots(); slot++) {
					ItemStack stack = curios.getStackInSlot("ring", slot).copy();

					if (stack != null && stack.getItem() instanceof ItemHungerlessRing) {
						ECItems.hungerlessRing.resetCooldown(stack);
						curios.setStackInSlot("ring", slot, stack);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityUseItemTick(LivingEntityUseItemEvent.Tick event) {
		if (InfusionHelper.hasAirInfusionFasterDraw(event.getItem()) && event.getDuration() % 3 == 0) {
			event.setDuration(event.getDuration() - 1);
		}
	}

	private static void clientAirInfusionFly(ClientPlayerEntity player) {
		if (InfusionHelper.canAirInfusionFly(player) && (player.isElytraFlying() || (player.movementInput.jump && !lastJump && !player.isElytraFlying()))) {
			player.startFallFlying();
			MessageHandler.CHANNEL.sendToServer(ECMessage.AIR_INFUSION);
		} else {
			player.stopFallFlying();
		}
		lastJump = player.movementInput.jump || player.func_233570_aj_/* isOnGround */();
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();

		if (entity instanceof ClientPlayerEntity) {
			clientAirInfusionFly((ClientPlayerEntity) event.getEntityLiving());
		} else if (entity instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();

			if (player.isElytraFlying() && InfusionHelper.canAirInfusionFly(player)) {
				player.startFallFlying();
			} else {
				player.stopFallFlying();
			}
		}
	}
}
