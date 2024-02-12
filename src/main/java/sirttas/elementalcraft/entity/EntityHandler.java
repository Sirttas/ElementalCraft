package sirttas.elementalcraft.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.menu.IMenuOpenListener;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.item.ECItems;
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
		var entity = event.getEntity();
		var world = entity.level();

		if (!world.isClientSide && world.getRandom().nextDouble() >= ToolInfusionHelper.getDodge(entity)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		var player = event.getEntity();
		
		if (Boolean.TRUE.equals(ECConfig.COMMON.playersSpawnWithBook.get()) && !event.getEntity().level().isClientSide) {
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
	public static void onContainerOpen(PlayerContainerEvent.Open event) {
		if (event.getContainer() instanceof IMenuOpenListener listener) {
			listener.onOpen(event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			var manager = SpellTickHelper.get(event.player);

			if (manager != null) {
				manager.tick();
			}
		}
	}
}
