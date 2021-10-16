package sirttas.elementalcraft.block.shrine.firepylon;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;

public class FirePylonBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + FirePylonBlock.NAME) public static final BlockEntityType<FirePylonBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.FIRE).consumeAmount(ECConfig.COMMON.firePylonConsumeAmount.get()).range(ECConfig.COMMON.firePylonRange.get());

	protected static final List<Direction> UPGRRADE_DIRECTIONS = List.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	public FirePylonBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, PROPERTIES);
	}

	private List<LivingEntity> getEntities() {
		return this.getLevel().getEntitiesOfClass(LivingEntity.class, getRangeBoundingBox(), e -> !e.isSpectator() && !e.hasEffect(MobEffects.FIRE_RESISTANCE) && !e.isOnFire()
				&& !ToolInfusionHelper.hasFireInfusion(e) && (!this.hasUpgrade(ShrineUpgrades.PROTECTION) || EntityHelper.isHostile(e)));
	}
	
	@Override
	protected boolean doPeriode() {
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
