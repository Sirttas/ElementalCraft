package sirttas.elementalcraft.spell.air;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import sirttas.elementalcraft.spell.Spell;

public class TranslocationSpell extends Spell {

	public static final String NAME = "translocation";

	private void teleport(Entity sender, Vector3d newPos) {
		sender.setPos(newPos.x, newPos.y, newPos.z);
	}
	
	@Override
	public ActionResultType castOnSelf(Entity sender) {
		World world = sender.getCommandSenderWorld();
		Vector3d look = sender.getLookAngle();
		Vector3d tagetPos = sender.position().add(new Vector3d(look.x(), 0, look.z()).normalize().scale(this.getRange(sender)));
		Vector3d newPos = new Vector3d(tagetPos.x(), getHeight(world, sender, tagetPos), tagetPos.z());
		
		if (world.getChunk(((int) Math.round(newPos.x)) >> 4, ((int) Math.round(newPos.z)) >> 4) == null) {
			return ActionResultType.FAIL;
		} else if (sender instanceof LivingEntity) {
			LivingEntity livingSender = (LivingEntity) sender;
			
			if (MinecraftForge.EVENT_BUS.post(new EnderTeleportEvent(livingSender, newPos.x, newPos.y, newPos.z, 0))) {
				return ActionResultType.SUCCESS;
			}
			teleport(sender, newPos);
			livingSender.getCommandSenderWorld().playSound(null, livingSender.xo, livingSender.yo, livingSender.zo, SoundEvents.ENDERMAN_TELEPORT,
					livingSender.getSoundSource(), 1.0F, 1.0F);
			livingSender.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
		} else {
			teleport(sender, newPos);
		}
		return ActionResultType.SUCCESS;
	}

	private double getHeight(World world, Entity sender, Vector3d tagetPos) {
		double height = world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(tagetPos)).getY() + 1D;
		
		if (!sender.isOnGround()) {
			return Math.max(height, sender.getY());
		}
		return height;
	}
}
