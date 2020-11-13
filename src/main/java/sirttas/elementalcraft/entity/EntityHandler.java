package sirttas.elementalcraft.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradesMessage;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.infusion.InfusionHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.network.message.MessageHelper;
import sirttas.elementalcraft.spell.properties.SpellPropertiesMessage;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public class EntityHandler {

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

		if (player instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

			MessageHelper.sendToPlayer(serverPlayer, new ShrineUpgradesMessage(ElementalCraft.SHRINE_UPGRADE_MANAGER));
			MessageHelper.sendToPlayer(serverPlayer, new SpellPropertiesMessage(ElementalCraft.SPELL_PROPERTIES_MANAGER));
		}
		
		if (Boolean.TRUE.equals(ECConfig.COMMON.playersSpawnWithBook.get()) && !event.getEntityLiving().getEntityWorld().isRemote) {
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
