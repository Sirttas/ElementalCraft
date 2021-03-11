package sirttas.elementalcraft.block.shrine.firepylon;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractTileShrine;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.infusion.InfusionHelper;

public class TileFirePylon extends AbstractTileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFirePylon.NAME) public static final TileEntityType<TileFirePylon> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.FIRE).consumeAmount(ECConfig.COMMON.firePylonConsumeAmount.get()).range(ECConfig.COMMON.firePylonRange.get());

	protected static final List<Direction> UPGRRADE_DIRECTIONS = ImmutableList.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	public TileFirePylon() {
		super(TYPE, PROPERTIES);
	}

	private List<LivingEntity> getEntities() {
		return this.getWorld().getEntitiesWithinAABB(LivingEntity.class, getRangeBoundingBox(),
				e -> (!e.isSpectator() && !e.isPotionActive(Effects.FIRE_RESISTANCE) && !e.isBurning()
						&& !(InfusionHelper.hasInfusion(e, EquipmentSlotType.HEAD, ElementType.FIRE) || InfusionHelper.hasInfusion(e, EquipmentSlotType.CHEST, ElementType.FIRE)
								|| InfusionHelper.hasInfusion(e, EquipmentSlotType.LEGS, ElementType.FIRE) || InfusionHelper.hasInfusion(e, EquipmentSlotType.FEET, ElementType.FIRE))));
	}
	
	@Override
	protected boolean doTick() {
		int consumeAmount = this.getConsumeAmount();
		float strength = this.getMultiplier(BonusType.STRENGTH);

		getEntities().forEach(e -> {
			if (this.elementStorage.getElementAmount() >= consumeAmount) {
				e.attackEntityFrom(DamageSource.IN_FIRE, (float) (strength * ECConfig.COMMON.firePylonDamage.get()));
				e.setFire((int)(this.consumeElement(consumeAmount) * strength));
			}
		});
		return false;
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return UPGRRADE_DIRECTIONS;
	}
}
