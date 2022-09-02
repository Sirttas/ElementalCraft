package sirttas.elementalcraft.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.menu.IMenuOpenListener;
import sirttas.elementalcraft.entity.player.PlayerElementStorage;
import sirttas.elementalcraft.entity.player.PlayerSpellTickManager;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.jewel.handler.ClientJewelHandler;
import sirttas.elementalcraft.jewel.handler.JewelHandler;
import sirttas.elementalcraft.spell.tick.ISpellTickManager;
import sirttas.elementalcraft.spell.tick.SpellTickHelper;

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
		LivingEntity entity = event.getEntity();
		Level world = entity.level;

		if (!world.isClientSide && world.getRandom().nextDouble() >= ToolInfusionHelper.getDodge(entity)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		Player player = event.getEntity();
		
		if (Boolean.TRUE.equals(ECConfig.COMMON.playersSpawnWithBook.get()) && !event.getEntity().getLevel().isClientSide) {
			CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);

			if (!tag.getBoolean(ECNames.HAS_BOOK)) {
				ItemStack book = new ItemStack(ECItems.ELEMENTOPEDIA.get());

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

		if (entity instanceof Player player) {
			var provider = PlayerElementStorage.createProvider(player);

			event.addCapability(ElementalCraft.createRL(ECNames.ELEMENT_STORAGE), provider);
			event.addCapability(ElementalCraft.createRL(ECNames.SPELL_TICK_MANAGER), PlayerSpellTickManager.createProvider(player));
			if (entity.level.isClientSide) {
				event.addCapability(ElementalCraft.createRL(ECNames.JEWEL), ClientJewelHandler.createProvider());
			} else {
				event.addCapability(ElementalCraft.createRL(ECNames.JEWEL), JewelHandler.createProvider(entity, ElementStorageHelper.get(provider).orElse(null)));
			}
		}
	}

	@SubscribeEvent
	public static void onContainerOpen(PlayerContainerEvent.Open event) {
		if (event.getContainer() instanceof IMenuOpenListener listener) {
			listener.onOpen(event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			SpellTickHelper.get(event.player).ifPresent(ISpellTickManager::tick);
		}
	}
}
