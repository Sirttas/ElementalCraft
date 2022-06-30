package sirttas.elementalcraft.block.shrine.vacuum;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.vortex.VortexPullPlayerMessage;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.network.message.MessageHelper;
import sirttas.elementalcraft.particle.ParticleHelper;

import java.util.List;

public class VacuumShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(VacuumShrineBlock.NAME);
	public VacuumShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.VACUUM_SHRINE, pos, state, PROPERTIES_KEY);
	}

	private List<? extends Entity> getEntities() {
		if (this.hasUpgrade(ShrineUpgrades.VORTEX)) {
			var protection = this.hasUpgrade(ShrineUpgrades.PROTECTION);

			return this.getLevel().getEntitiesOfClass(LivingEntity.class, getRangeBoundingBox()).stream()
						.filter(e -> !(e instanceof Player player && player.isCreative()) && (!protection || EntityHelper.isHostile(e)))
						.toList();
		}
		return this.getLevel().getEntitiesOfClass(ItemEntity.class, getRangeBoundingBox());
	}
	
	@Override
	protected boolean doPeriod() {
		IItemHandler inv = ECContainerHelper.getItemHandlerAt(level, worldPosition.below(), Direction.UP);

		return this.hasUpgrade(ShrineUpgrades.PICKUP) ? pickup(inv) : pull(inv);
	}

	private boolean pickup(IItemHandler inv) {
		return getEntities().stream().findAny().map(entity -> {
			doPickup(inv, (ItemEntity) entity);
			return true;
		}).orElse(false);
	}

	private boolean pull(IItemHandler inv) {
		int consumeAmount = this.getConsumeAmount();
		double pullSpeed = this.getStrength();
		Vec3 pos3d = Vec3.atCenterOf(this.getBlockPos());

		getEntities().forEach(entity -> {
			if (this.elementStorage.getElementAmount() >= consumeAmount) {
				this.consumeElement(consumeAmount);
				if (entity instanceof ServerPlayer player) {
					MessageHelper.sendToPlayer(player, new VortexPullPlayerMessage(pos3d, pullSpeed));
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

	private void doPickup(IItemHandler inv, ItemEntity entity) {
		entity.setItem(ItemHandlerHelper.insertItem(inv, entity.getItem(), false));
		if (level.isClientSide) {
			ParticleHelper.createEnderParticle(level, entity.position(), 3, level.random);
		}
	}
}
