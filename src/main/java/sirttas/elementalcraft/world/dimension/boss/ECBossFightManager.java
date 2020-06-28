package sirttas.elementalcraft.world.dimension.boss;

import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.entity.boss.ECBossEntity;
import sirttas.elementalcraft.entity.boss.earthgolem.EarthGolemEntity;

public class ECBossFightManager {

	private final ServerWorld world;
	private final ServerBossInfo bossInfo = (ServerBossInfo) new ServerBossInfo(new StringTextComponent(""), BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS).setCreateFog(true);

	private ElementType type;
	private ECBossEntity boss;


	public ECBossFightManager(ServerWorld world) {
		this.world = world;
		boss = null;
		type = ElementType.NONE;
	}

	public void tick() {
		world.getEntities(EntityType.ITEM, Predicates.alwaysTrue()).forEach(Entity::remove);
		if (!isBossAlive()) {
			if (boss != null) {
				// TODO spawn loots in overworld
			}
			boss = null;
			world.getPlayers(EntityPredicates.IS_ALIVE).forEach(this::removePlayer);
		}
	}

	private boolean isBossAlive() {
		return boss != null && boss.isAlive();
	}

	public void addPlayer(ServerPlayerEntity player) {
		bossInfo.addPlayer(player);
	}

	public void removePlayer(ServerPlayerEntity player) {
		bossInfo.removePlayer(player);
		player.changeDimension(DimensionType.OVERWORLD, BossDimension.TELEPORTER);
	}

	public void init(ElementType type) {
		this.setElementType(type);
		this.buildArena();
		this.spawnBoss();
	}

	private void buildArena() {
		// TODO
	}

	private void spawnBoss() {
		BlockPos pos = world.dimension.findSpawn(0, 50, false);

		pos = pos.equals(BlockPos.ZERO) ? world.dimension.findSpawn(50, 0, false) : pos;
		boss = EarthGolemEntity.TYPE.create(world); // TODO change based on type
		boss.setPosition(pos.getX(), pos.getY(), pos.getZ());
		bossInfo.setName(boss.getDisplayName());
		this.world.addEntity(boss);
	}


	public void setElementType(ElementType type) {
		this.type = type;
	}
}
