package sirttas.elementalcraft.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

public abstract class ECBossEntity extends MonsterEntity {

	public ECBossEntity(EntityType<? extends ECBossEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public static AttributeModifierMap.MutableAttribute getAttributeModifier() {
		return MonsterEntity.func_234295_eP_().createMutableAttribute(Attributes.MAX_HEALTH, 500.0D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 10.0D)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 64.0D).createMutableAttribute(Attributes.ARMOR, 4.0D).createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
	}

	@Override
	public boolean addPotionEffect(EffectInstance effectInstanceIn) {
		return false;
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return false;
	}

	/**
	 * Returns false if this Entity is a boss, true otherwise.
	 */
	@Override
	public boolean isNonBoss() {
		return false;
	}

}