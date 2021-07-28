package sirttas.elementalcraft.entity.boss;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class AbstractECBossEntity extends Monster {

	protected AbstractECBossEntity(EntityType<? extends AbstractECBossEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	public static AttributeSupplier.Builder getAttributeModifier() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 500.0D).add(Attributes.ATTACK_DAMAGE, 10.0D)
				.add(Attributes.FOLLOW_RANGE, 64.0D).add(Attributes.ARMOR, 4.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

}