package sirttas.elementalcraft.block.shrine.firepylon;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;

public class FirePylonBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + FirePylonBlock.NAME) public static final TileEntityType<FirePylonBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.FIRE).consumeAmount(ECConfig.COMMON.firePylonConsumeAmount.get()).range(ECConfig.COMMON.firePylonRange.get());

	protected static final List<Direction> UPGRRADE_DIRECTIONS = ImmutableList.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	public FirePylonBlockEntity() {
		super(TYPE, PROPERTIES);
	}

	private List<LivingEntity> getEntities() {
		return this.getLevel().getEntitiesOfClass(LivingEntity.class, getRangeBoundingBox(),
				e -> !e.isSpectator() && !e.hasEffect(Effects.FIRE_RESISTANCE) && !e.isOnFire() && !ToolInfusionHelper.hasFireInfusion(e));
	}
	
	@Override
	protected boolean doTick() {
		int consumeAmount = this.getConsumeAmount();
		float strength = this.getMultiplier(BonusType.STRENGTH);

		getEntities().forEach(e -> {
			if (this.elementStorage.getElementAmount() >= consumeAmount) {
				e.hurt(DamageSource.IN_FIRE, (float) (strength * ECConfig.COMMON.firePylonDamage.get()));
				e.setSecondsOnFire((int)(this.consumeElement(consumeAmount) * strength));
			}
		});
		return false;
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return UPGRRADE_DIRECTIONS;
	}
}
