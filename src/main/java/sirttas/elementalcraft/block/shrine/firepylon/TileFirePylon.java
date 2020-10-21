package sirttas.elementalcraft.block.shrine.firepylon;

import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.infusion.InfusionHelper;

public class TileFirePylon extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFirePylon.NAME) public static TileEntityType<TileFirePylon> TYPE;

	public TileFirePylon() {
		super(TYPE, ElementType.FIRE);
	}

	private List<LivingEntity> getEntities() {
		return this.getWorld().getEntitiesWithinAABB(LivingEntity.class, getRangeBoundingBox(),
				e -> (!e.isSpectator() && !e.isPotionActive(Effects.FIRE_RESISTANCE) && !e.isBurning()
						&& !(InfusionHelper.hasInfusion(e, EquipmentSlotType.HEAD, ElementType.FIRE) || InfusionHelper.hasInfusion(e, EquipmentSlotType.CHEST, ElementType.FIRE)
								|| InfusionHelper.hasInfusion(e, EquipmentSlotType.LEGS, ElementType.FIRE) || InfusionHelper.hasInfusion(e, EquipmentSlotType.FEET, ElementType.FIRE))));
	}
	
	@Override
	public AxisAlignedBB getRangeBoundingBox() {
		return new AxisAlignedBB(this.getPos()).grow(ECConfig.CONFIG.firePylonRange.get());
	}

	@Override
	protected void doTick() {
		int consumeAmount = ECConfig.CONFIG.firePylonConsumeAmount.get();

		if (this.getElementAmount() >= consumeAmount) {
			getEntities().forEach(e -> e.setFire(this.consumeElement(consumeAmount)));
		}
	}

}
