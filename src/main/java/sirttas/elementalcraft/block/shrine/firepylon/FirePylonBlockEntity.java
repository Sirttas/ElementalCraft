package sirttas.elementalcraft.block.shrine.firepylon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;

import java.util.List;

public class FirePylonBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(FirePylonBlock.NAME);

	protected static final List<Direction> UPGRADE_DIRECTIONS = List.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	public FirePylonBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.FIRE_PYLON, pos, state, PROPERTIES_KEY);
	}

	private List<LivingEntity> getEntities() {
		return this.getLevel().getEntitiesOfClass(LivingEntity.class, getRangeBoundingBox(), e -> !e.isSpectator() && !e.hasEffect(MobEffects.FIRE_RESISTANCE) && !e.isOnFire()
				&& !ToolInfusionHelper.hasFireInfusion(e) && (!this.hasUpgrade(ShrineUpgrades.PROTECTION) || EntityHelper.isHostile(e)));
	}
	
	@Override
	protected boolean doPeriod() {
		int consumeAmount = this.getConsumeAmount();

		getEntities().forEach(e -> {
			if (this.elementStorage.getElementAmount() >= consumeAmount) {
				e.hurt(DamageSource.IN_FIRE, (float) this.getStrength());
				e.setSecondsOnFire(Math.max(1, (int) (this.consumeElement(consumeAmount) * this.getStrength(1))));
			}
		});
		return false;
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return UPGRADE_DIRECTIONS;
	}
}
