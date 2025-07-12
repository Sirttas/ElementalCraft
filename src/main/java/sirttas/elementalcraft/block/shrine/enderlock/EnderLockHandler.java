package sirttas.elementalcraft.block.shrine.enderlock;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent.SpreadPlayersCommand;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent.TeleportCommand;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class EnderLockHandler {

	private static final Multimap<Level, EnderLockShrineBlockEntity> ENDER_LOCK_SHRINES = HashMultimap.create();
	private static final Set<Class<? extends EntityTeleportEvent>> PROTECTED_EVENTS = new HashSet<>();

	static {
		protect(TeleportCommand.class);
		protect(SpreadPlayersCommand.class);
	}

	private EnderLockHandler() {}

	@SubscribeEvent
	public static void onEntityTeleport(EntityTeleportEvent event) {
		var entity = event.getEntity();

		if (PROTECTED_EVENTS.contains(event.getClass()) || (entity instanceof Player player && (player.isSpectator() || player.isCreative()))) {
			return;
		}
		
		var shrines = ENDER_LOCK_SHRINES.get(entity.level());
		
		shrines.removeIf(BlockEntity::isRemoved);
		event.setCanceled(shrines.stream().anyMatch(shrine -> shrine.doLock(entity)));
	}

	public static void add(EnderLockShrineBlockEntity shrine) {
		if (!shrine.isRemoved() && shrine.hasLevel()) {
			ENDER_LOCK_SHRINES.put(shrine.getLevel(), shrine);
		}
	}

	public static void protect(Class<? extends EntityTeleportEvent> event) {
		PROTECTED_EVENTS.add(event);
	}
}
