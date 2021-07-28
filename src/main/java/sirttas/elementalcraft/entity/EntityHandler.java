package sirttas.elementalcraft.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.player.PlayerElementStorage;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.item.ECItems;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class EntityHandler {

	private EntityHandler() {}
	
	@SubscribeEvent
	public static void onEntityUseItemTick(LivingEntityUseItemEvent.Tick event) {
		int fastDraw = ToolInfusionHelper.getFasterDraw(event.getItem());
		
		if (fastDraw >= 0 && event.getDuration() % fastDraw == 0) {
			event.setDuration(event.getDuration() - 1);
		}
	}
	
	@SubscribeEvent
	public static void onEntityLivingAttack(LivingAttackEvent event) {
		LivingEntity entity = event.getEntityLiving();
		Level world = entity.level;

		if (!world.isClientSide && world.getRandom().nextDouble() >= ToolInfusionHelper.getDodge(entity)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		Player player = event.getPlayer();
		
		if (Boolean.TRUE.equals(ECConfig.COMMON.playersSpawnWithBook.get()) && !event.getEntityLiving().getCommandSenderWorld().isClientSide) {
			CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);

			if (!tag.getBoolean(ECNames.HAS_BOOK)) {
				ItemStack book = new ItemStack(ECItems.ELEMENTOPEDIA);

				book.getOrCreateTag().putString("patchouli:book", "elementalcraft:element_book");
				ItemHandlerHelper.giveItemToPlayer(player, book);
				tag.putBoolean(ECNames.HAS_BOOK, true);
				player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
			}
		}
	}
	
	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		
		if (entity instanceof Player) {
			event.addCapability(ElementalCraft.createRL(ECNames.ELEMENT_STORAGE), PlayerElementStorage.createProvider((Player) entity));
		}
	}
}
