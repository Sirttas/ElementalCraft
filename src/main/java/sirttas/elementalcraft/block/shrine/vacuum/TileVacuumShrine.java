package sirttas.elementalcraft.block.shrine.vacuum;

import java.util.List;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractTileShrine;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.ECInventoryHelper;
import sirttas.elementalcraft.particle.ParticleHelper;

public class TileVacuumShrine extends AbstractTileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockVacuumShrine.NAME) public static final TileEntityType<TileVacuumShrine> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.AIR).consumeAmount(ECConfig.COMMON.vacuumShrineConsumeAmount.get()).range(ECConfig.COMMON.vacuumShrineRange.get());

	public TileVacuumShrine() {
		super(TYPE, PROPERTIES);
	}

	private List<ItemEntity> getEntities() {
		return this.getWorld().getEntitiesWithinAABB(ItemEntity.class, getRangeBoundingBox());
	}

	
	@Override
	protected boolean doTick() {
		IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos.down(), Direction.UP);

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
		Vector3d pos3d = Vector3d.copyCentered(this.getPos());

		getEntities().forEach(entity -> {
			if (this.elementStorage.getElementAmount() >= consumeAmount) {
				this.consumeElement(consumeAmount);
				entity.setMotion(pos3d.subtract(entity.getPositionVec()).normalize().mul(pullSpeed, pullSpeed, pullSpeed));
				if (pos3d.distanceTo(entity.getPositionVec()) <= 2 * Math.max(1, this.getMultiplier(BonusType.RANGE))) {
					doPickup(inv, entity);
				}
			}
		});
		return false;
	}

	private void doPickup(IItemHandler inv, ItemEntity entity) {
		entity.setItem(ItemHandlerHelper.insertItem(inv, entity.getItem(), false));
		if (world.isRemote) {
			ParticleHelper.createEnderParticle(world, entity.getPositionVec(), 3, world.rand);
		}
	}
}
