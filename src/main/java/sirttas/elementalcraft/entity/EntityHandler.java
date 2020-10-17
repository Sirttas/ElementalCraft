package sirttas.elementalcraft.entity;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.infusion.InfusionHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.network.message.ECMessage;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public class EntityHandler {

	private static boolean lastJump = false; // we only need one because it's only used client side

	@SubscribeEvent
	public static void onEntityUseItemTick(LivingEntityUseItemEvent.Tick event) {
		if (InfusionHelper.hasAirInfusionFasterDraw(event.getItem()) && event.getDuration() % 3 == 0) {
			event.setDuration(event.getDuration() - 1);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void clientAirInfusionFly(ClientPlayerEntity player) {
		if (InfusionHelper.canAirInfusionFly(player) && (player.isElytraFlying() || (player.movementInput.jump && !lastJump && !player.isElytraFlying()))) {
			player.startFallFlying();
			ECMessage.AIR_INFUSION.send();
		} else {
			player.stopFallFlying();
		}
		lastJump = player.movementInput.jump || player.isOnGround();
	}

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();

		if (!entity.getItemStackFromSlot(EquipmentSlotType.CHEST).canElytraFly(entity)) {
			if (entity.getEntityWorld().isRemote && entity instanceof ClientPlayerEntity) {
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

	@SubscribeEvent
	public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();

		if (Boolean.TRUE.equals(ECConfig.CONFIG.playersSpawnWithBook.get()) && !event.getEntityLiving().getEntityWorld().isRemote) {
			CompoundNBT tag = player.getPersistentData().getCompound(PlayerEntity.PERSISTED_NBT_TAG);

			if (!tag.getBoolean(ECNames.HAS_BOOK)) {
				ItemStack book = new ItemStack(ECItems.elementopedia);

				book.getOrCreateTag().putString("patchouli:book", "elementalcraft:element_book");
				ItemHandlerHelper.giveItemToPlayer(player, book);
				tag.putBoolean(ECNames.HAS_BOOK, true);
				player.getPersistentData().put(PlayerEntity.PERSISTED_NBT_TAG, tag);
			}
		}
	}
}
