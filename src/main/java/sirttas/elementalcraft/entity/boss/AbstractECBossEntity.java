package sirttas.elementalcraft.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

public abstract class AbstractECBossEntity extends MonsterEntity {

	protected AbstractECBossEntity(EntityType<? extends AbstractECBossEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public static AttributeModifierMap.MutableAttribute getAttributeModifier() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 500.0D).add(Attributes.ATTACK_DAMAGE, 10.0D)
				.add(Attributes.FOLLOW_RANGE, 64.0D).add(Attributes.ARMOR, 4.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
	}

	@Override
	public boolean addEffect(EffectInstance effectInstanceIn) {
		return false;
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