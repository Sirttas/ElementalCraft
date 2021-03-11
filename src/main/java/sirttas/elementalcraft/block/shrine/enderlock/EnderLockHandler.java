package sirttas.elementalcraft.block.shrine.enderlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.config.ECConfig;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public class EnderLockHandler {

	private static final List<Vector3i> RANGE;

	static {
		int range = ECConfig.COMMON.enderLockShrineRange.get();
		RANGE = new ArrayList<>(((range * 2 + 1) ^ 2) * 4);

		IntStream.range(-range, range + 1).forEach(x -> IntStream.range(-range, range + 1).forEach(z -> IntStream.range(0, 4).forEach(y -> RANGE.add(new Vector3i(x, y, z)))));
	}
	
	private EnderLockHandler() {}

	@SubscribeEvent
	public static void onEndermanTeleport(EnderTeleportEvent event) {
		LivingEntity entity = event.getEntityLiving();
		
		event.setCanceled(
				RANGE.stream().anyMatch(
						v -> TileEntityHelper.getTileEntityAs(entity.getEntityWorld(), entity.getPosition().add(v), TileEnderLockShrine.class).filter(TileEnderLockShrine::doLock).isPresent()));

	}

}
