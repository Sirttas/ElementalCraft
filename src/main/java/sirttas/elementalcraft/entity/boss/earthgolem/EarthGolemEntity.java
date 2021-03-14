package sirttas.elementalcraft.entity.boss.earthgolem;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.entity.boss.AbstractECBossEntity;
import sirttas.elementalcraft.entity.goal.CastSpellGoal;
import sirttas.elementalcraft.spell.SpellTickManager;
import sirttas.elementalcraft.spell.Spells;

public class EarthGolemEntity extends AbstractECBossEntity {

	public static final String NAME = "earthgolem";
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static final EntityType<EarthGolemEntity> TYPE = null;

	public EarthGolemEntity(EntityType<EarthGolemEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public static AttributeModifierMap.MutableAttribute getAttributeModifier() {
		return AbstractECBossEntity.getAttributeModifier().createMutableAttribute(Attributes.MOVEMENT_SPEED, 0).createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 5.0D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new AttackGoal(this));
		this.goalSelector.addGoal(3, new CastSpellGoal(this, Spells.GRAVEL_FALL));
		this.goalSelector.addGoal(3, new CastSpellGoal(this, Spells.FIRE_BALL));
		this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		boolean ret = super.attackEntityFrom(source, amount);

		if (ret && source instanceof IndirectEntityDamageSource && this.rand.nextInt(10) > 2) {
			Spells.STONE_WALL.castOnSelf(this);
			SpellTickManager.getInstance(getEntityWorld()).setCooldown(this, Spells.STONE_WALL);
		}
		return ret;
	}

	private static class AttackGoal extends MeleeAttackGoal {

		public AttackGoal(EarthGolemEntity creature) {
			super(creature, 0, true);
		}

		@Override
		protected double getAttackReachSqr(LivingEntity attackTarget) {
			return this.attacker.getWidth() * 1.25F * this.attacker.getWidth() * 1.25F + attackTarget.getWidth();
		}
	}
}
