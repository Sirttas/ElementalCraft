package sirttas.elementalcraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.player.PlayerElementStorage;
import sirttas.elementalcraft.infusion.InfusionHelper;
import sirttas.elementalcraft.item.ECItems;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public class EntityHandler {

	private EntityHandler() {}
	
	@SubscribeEvent
	public static void onEntityUseItemTick(LivingEntityUseItemEvent.Tick event) {
		if (InfusionHelper.hasAirInfusionFasterDraw(event.getItem()) && event.getDuration() % 3 == 0) {
			event.setDuration(event.getDuration() - 1);
		}
	}
	
	@SubscribeEvent
	public static void onEntityLivingAttack(LivingAttackEvent event) {
		LivingEntity entity = event.getEntityLiving();
		World world = entity.world;

		if (!world.isRemote && InfusionHelper.hasInfusion(entity, EquipmentSlotType.CHEST, ElementType.AIR)
				&& world.getRandom().nextDouble() <= ECConfig.COMMON.chestplateAirInfusionDodgeChance.get()) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		
		if (Boolean.TRUE.equals(ECConfig.COMMON.playersSpawnWithBook.get()) && !event.getEntityLiving().getEntityWorld().isRemote) {
			CompoundNBT tag = player.getPersistentData().getCompound(PlayerEntity.PERSISTED_NBT_TAG);

			if (!tag.getBoolean(ECNames.HAS_BOOK)) {
				ItemStack book = new ItemStack(ECItems.ELEMENTOPEDIA);

				book.getOrCreateTag().putString("patchouli:book", "elementalcraft:element_book");
				ItemHandlerHelper.giveItemToPlayer(player, book);
				tag.putBoolean(ECNames.HAS_BOOK, true);
				player.getPersistentData().put(PlayerEntity.PERSISTED_NBT_TAG, tag);
			}
		}
	}
	
	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		
		if (entity instanceof PlayerEntity) {
			event.addCapability(ElementalCraft.createRL(ECNames.ELEMENT_STORAGE), PlayerElementStorage.createProvider((PlayerEntity) entity));
		}
	}
}
