package sirttas.elementalcraft.entity.boss;

import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.entity.boss.earthgolem.EarthGolemEntity;

public class ECBossFightManager {

	private final ServerWorld world;
	private final ServerBossInfo bossInfo = (ServerBossInfo) new ServerBossInfo(new StringTextComponent(""), BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS).setCreateFog(true);

	@SuppressWarnings("unused")
	private ElementType type;
	private AbstractECBossEntity boss;


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
		BlockPos pos = world.getSpawnPoint();

		// TODO move boss out of spawn
		boss = EarthGolemEntity.TYPE.create(world); // TODO change based on type
		boss.setPosition(pos.getX(), pos.getY(), pos.getZ());
		bossInfo.setName(boss.getDisplayName());
		this.world.addEntity(boss);
	}


	public void setElementType(ElementType type) {
		this.type = type;
	}
}
