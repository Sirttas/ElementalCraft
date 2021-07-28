package sirttas.elementalcraft.entity.boss;

import com.google.common.base.Predicates;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.entity.boss.earthgolem.EarthGolemEntity;

public class ECBossFightManager {

	private final ServerLevel world;
	private final ServerBossEvent bossInfo = (ServerBossEvent) new ServerBossEvent(new TextComponent(""), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS).setCreateWorldFog(true);

	@SuppressWarnings("unused")
	private ElementType type;
	private AbstractECBossEntity boss;


	public ECBossFightManager(ServerLevel world) {
		this.world = world;
		boss = null;
		type = ElementType.NONE;
	}

	public void tick() {
		world.getEntities(EntityType.ITEM, Predicates.alwaysTrue()).forEach(Entity::discard);
		if (!isBossAlive()) {
			if (boss != null) {
				// TODO spawn loots in overworld
			}
			boss = null;
			world.getPlayers(EntitySelector.ENTITY_STILL_ALIVE).forEach(this::removePlayer);
		}
	}

	private boolean isBossAlive() {
		return boss != null && boss.isAlive();
	}

	public void addPlayer(ServerPlayer player) {
		bossInfo.addPlayer(player);
	}

	public void removePlayer(ServerPlayer player) {
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
		BlockPos pos = world.getSharedSpawnPos();

		// TODO move boss out of spawn
		boss = EarthGolemEntity.TYPE.create(world); // TODO change based on type
		boss.setPos(pos.getX(), pos.getY(), pos.getZ());
		bossInfo.setName(boss.getDisplayName());
		this.world.addFreshEntity(boss);
	}


	public void setElementType(ElementType type) {
		this.type = type;
	}
}
