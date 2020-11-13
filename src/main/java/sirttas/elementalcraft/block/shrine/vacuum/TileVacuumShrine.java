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
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public class TileVacuumShrine extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockVacuumShrine.NAME) public static TileEntityType<TileVacuumShrine> TYPE;

	private static final Properties PROPERTIES = Properties.create(ElementType.AIR).consumeAmount(ECConfig.COMMON.vacuumShrineConsumeAmount.get()).range(ECConfig.COMMON.vacuumShrineRange.get());

	public TileVacuumShrine() {
		super(TYPE, PROPERTIES);
	}

	private List<ItemEntity> getEntities() {
		return this.getWorld().getEntitiesWithinAABB(ItemEntity.class, getRangeBoundingBox());
	}

	
	@Override
	protected boolean doTick() {
		int consumeAmount = this.getConsumeAmount();

		if (this.consumeElement(consumeAmount) >= consumeAmount) {
			double pullSpeed = ECConfig.COMMON.vacuumShrinePullSpeed.get();
			IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos.down(), Direction.UP);
			Vector3d pos3d = Vector3d.copyCentered(this.getPos());

			getEntities().forEach(e -> {
				if (this.consumeElement(consumeAmount) >= consumeAmount) {
					e.setMotion(pos3d.subtract(e.getPositionVec()).normalize().mul(pullSpeed, pullSpeed, pullSpeed));
					if (pos3d.distanceTo(e.getPositionVec()) <= 1) {
						e.setItem(ItemHandlerHelper.insertItem(inv, e.getItem(), false));
					}
				}
			});
		}
		return false;
	}
}
