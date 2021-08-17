package sirttas.elementalcraft.block.shrine.enderlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent.SpreadPlayersCommand;
import net.minecraftforge.event.entity.EntityTeleportEvent.TeleportCommand;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.config.ECConfig;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class EnderLockHandler {

	private static final List<Vec3i> RANGE;

	static {
		int range = ECConfig.COMMON.enderLockShrineRange.get();
		RANGE = new ArrayList<>(((range * 2 + 1) ^ 2) * 4);

		IntStream.range(-range, range + 1).forEach(x -> IntStream.range(-range, range + 1).forEach(z -> IntStream.range(0, 4).forEach(y -> RANGE.add(new Vec3i(x, y, z)))));
	}
	
	private EnderLockHandler() {}

	@SubscribeEvent
	public static void onEndermanTeleport(EntityTeleportEvent event) {
		var entity = event.getEntity();

		if (!(event instanceof TeleportCommand) && !(event instanceof SpreadPlayersCommand) && entity instanceof Player player && (player.isSpectator() || player.isCreative())) {
			return;
		}
		event.setCanceled(RANGE.stream().anyMatch(v -> BlockEntityHelper.getTileEntityAs(entity.getCommandSenderWorld(), entity.blockPosition().offset(v), EnderLockShrineBlockEntity.class)
				.filter(shrine -> shrine.doLock(entity)).isPresent()));

	}

}