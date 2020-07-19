package sirttas.elementalcraft.block.shrine.vacuum;

import java.util.List;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.config.ECConfig;

public class TileVacuumShrine extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockVacuumShrine.NAME) public static TileEntityType<TileVacuumShrine> TYPE;

	public TileVacuumShrine() {
		super(TYPE, ElementType.AIR);
	}

	private List<ItemEntity> getEntities() {
		return this.getWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(this.getPos()).grow(ECConfig.CONFIG.vacuumShrineRange.get()));
	}


	@Override
	protected void doTick() {
		int consumeAmount = ECConfig.CONFIG.vacuumShrineConsumeAmount.get();
		double pullSpeed = ECConfig.CONFIG.vacuumShrinePullSpeed.get();
		TileEntity te = this.world.getTileEntity(pos.down());
		IInventory inv = te instanceof IInventory ? (IInventory) te : null;
		Vec3d pos3d = new Vec3d(this.getPos()).add(0.5D, 0.5D, 0.5D);

		getEntities().forEach(e -> {
			if (this.consumeElement(consumeAmount) >= consumeAmount) {
				e.setMotion(pos3d.subtract(e.getPositionVector()).normalize().mul(pullSpeed, pullSpeed, pullSpeed));
				if (inv != null && pos3d.distanceTo(e.getPositionVector()) <= 1) {
					e.setItem(HopperTileEntity.putStackInInventoryAllSlots(null, inv, e.getItem(), Direction.UP));
				}
			}
		});
	}
}
