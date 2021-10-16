package sirttas.elementalcraft.block.shrine.enderlock;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent.SpreadPlayersCommand;
import net.minecraftforge.event.entity.EntityTeleportEvent.TeleportCommand;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class EnderLockHandler {

	private static final Multimap<Level, EnderLockShrineBlockEntity> ENDER_LOCK_SHRINES = HashMultimap.create();
	private EnderLockHandler() {}

	@SubscribeEvent
	public static void onEndermanTeleport(EntityTeleportEvent event) {
		var entity = event.getEntity();

		if (!(event instanceof TeleportCommand) && !(event instanceof SpreadPlayersCommand) && entity instanceof Player player && (player.isSpectator() || player.isCreative())) {
			return;
		}
		
		var shrines = ENDER_LOCK_SHRINES.get(entity.level);
		
		shrines.removeIf(BlockEntity::isRemoved);
		event.setCanceled(shrines.stream().anyMatch(shrine -> shrine.doLock(entity)));
	}

	public static void add(EnderLockShrineBlockEntity shrine) {
		if (!shrine.isRemoved() && shrine.hasLevel()) {
			ENDER_LOCK_SHRINES.put(shrine.getLevel(), shrine);
		}
	}
	
}