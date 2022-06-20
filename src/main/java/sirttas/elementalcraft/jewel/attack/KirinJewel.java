package sirttas.elementalcraft.jewel.attack;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.List;

public class KirinJewel extends AbstractAttackJewel {

    public static final String NAME = "kirin";

    public KirinJewel() {
        super(ElementType.FIRE, 2000);
    }

    public static DamageSource holyFire(Entity source) {
        return new EntityDamageSource("elementalcraft.jewel.kirin", source).bypassArmor();
    }

    @Override
    public void onAttack(Entity attacker, LivingEntity target) {
        var isUndead = target.getMobType() == MobType.UNDEAD;

        target.hurt(holyFire(attacker), isUndead ? 10 : 5);
        target.setSecondsOnFire(isUndead ? 5 : 2);
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        tooltip.add(new TranslatableComponent("tooltip.elementalcraft.kirin").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }
}
