package sirttas.elementalcraft.block.shrine.vacuum;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.particle.ParticleHelper;

import java.util.List;

public class VacuumShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + VacuumShrineBlock.NAME) public static final BlockEntityType<VacuumShrineBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.AIR)
			.consumeAmount(ECConfig.COMMON.vacuumShrineConsumeAmount.get())
			.range(ECConfig.COMMON.vacuumShrineRange.get());

	public VacuumShrineBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, PROPERTIES);
	}

	private List<ItemEntity> getEntities() {
		return this.getLevel().getEntitiesOfClass(ItemEntity.class, getRangeBoundingBox());
	}

	
	@Override
	protected boolean doPeriod() {
		IItemHandler inv = ECContainerHelper.getItemHandlerAt(level, worldPosition.below(), Direction.UP);

		return this.hasUpgrade(ShrineUpgrades.PICKUP) ? pickup(inv) : pull(inv);
	}

	private boolean pickup(IItemHandler inv) {
		return getEntities().stream().findAny().map(entity -> {
			doPickup(inv, entity);
			return true;
		}).orElse(false);
	}

	private boolean pull(IItemHandler inv) {
		int consumeAmount = this.getConsumeAmount();
		double pullSpeed = ECConfig.COMMON.vacuumShrinePullSpeed.get() * this.getMultiplier(BonusType.STRENGTH);
		Vec3 pos3d = Vec3.atCenterOf(this.getBlockPos());

		getEntities().forEach(entity -> {
			if (this.elementStorage.getElementAmount() >= consumeAmount) {
				this.consumeElement(consumeAmount);
				entity.setDeltaMovement(pos3d.subtract(entity.position()).normalize().multiply(pullSpeed, pullSpeed, pullSpeed));
				if (pos3d.distanceTo(entity.position()) <= 2 * Math.max(1, this.getMultiplier(BonusType.RANGE))) {
					doPickup(inv, entity);
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
