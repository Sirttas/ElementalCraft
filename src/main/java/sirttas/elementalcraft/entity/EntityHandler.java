package sirttas.elementalcraft.entity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.container.menu.IMenuOpenListener;
import sirttas.elementalcraft.data.attachment.ECDataAttachments;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.spell.tick.SpellTickHelper;

@EventBusSubscriber(modid = ElementalCraftApi.MODID)
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
	public static void onEntityLivingAttack(LivingIncomingDamageEvent event) {
		var entity = event.getEntity();
		var world = entity.level();

		if (!world.isClientSide && world.getRandom().nextDouble() >= ToolInfusionHelper.getDodge(entity)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onContainerOpen(PlayerContainerEvent.Open event) {
		if (event.getContainer() instanceof IMenuOpenListener listener) {
			listener.onOpen(event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Pre event) {
		var player = event.getEntity();

		if (!player.level().isClientSide) {
			player.getData(ECDataAttachments.JEWEL_HANDLER).tick();
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		var player = event.getEntity();
		var manager = SpellTickHelper.get(player);

		if (manager != null) {
			manager.tick();
		}
	}
}
