package sirttas.elementalcraft.block.shrine.vacuum;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.block.shrine.upgrade.vortex.VortexPullPlayerPayload;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.particle.ParticleHelper;

import java.util.List;

public class VacuumShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<@NotNull IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(VacuumShrineBlock.NAME);
	private static final Holder<@NotNull IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);
	public VacuumShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.VACUUM_SHRINE, PROPERTIES, pos, state);
	}

	private List<? extends Entity> getEntities() {
		if (this.hasUpgrade(ShrineUpgrades.VORTEX)) {
			var protection = this.hasUpgrade(ShrineUpgrades.PROTECTION);

			return this.getLevel().getEntitiesOfClass(LivingEntity.class, getRange()).stream()
						.filter(e -> !(e instanceof Player player && player.getAbilities().instabuild) && (!protection || EntityHelper.isHostile(e)))
						.toList();
		}
		return this.getLevel().getEntitiesOfClass(ItemEntity.class, getRange());
	}
	
	@Override
	protected boolean doPeriod() {
		var inv = ECContainerHelper.getItemHandlerAt(level, worldPosition.below(), Direction.UP);

		return this.hasUpgrade(ShrineUpgrades.PICKUP) ? pickup(inv) : pull(inv);
	}

	private boolean pickup(ResourceHandler<@NotNull ItemResource> inv) {
		return getEntities().stream().findAny().map(entity -> {
			doPickup(inv, (ItemEntity) entity);
			return true;
		}).orElse(false);
	}

	private boolean pull(ResourceHandler<@NotNull ItemResource> inv) {
		int consumeAmount = this.getConsumeAmount();
		double pullSpeed = this.getStrength();
		Vec3 pos3d = Vec3.atCenterOf(this.getTargetPos());

		getEntities().forEach(entity -> {
			if (this.elementStorage.getElementAmount() >= consumeAmount) {
				this.consumeElement(consumeAmount);
				if (entity instanceof ServerPlayer player) {
					PacketDistributor.sendToPlayer(player, new VortexPullPlayerPayload(pos3d, pullSpeed));
				} else {
					entity.setDeltaMovement(pos3d.subtract(entity.position()).normalize().multiply(pullSpeed, pullSpeed, pullSpeed));
				}
				if (entity instanceof ItemEntity itemEntity && pos3d.distanceTo(entity.position()) <= 2 * Math.max(1, this.getMultiplier(BonusType.RANGE))) {
					doPickup(inv, itemEntity);
				}
			}
		});
		return false;
	}

	private void doPickup(ResourceHandler<@NotNull ItemResource> inv, ItemEntity entity) {
		entity.setItem(ItemUtil.insertItemReturnRemaining(inv, entity.getItem(), false, null));
		ParticleHelper.createEnderParticle(level, entity.position(), 3, level.getRandom());
	}
}
