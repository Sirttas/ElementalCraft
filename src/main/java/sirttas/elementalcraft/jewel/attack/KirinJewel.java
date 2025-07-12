package sirttas.elementalcraft.jewel.attack;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.damagesource.ECDamageTypes;

import java.util.List;

public class KirinJewel extends AbstractAttackJewel {

    public static final String NAME = "kirin";

    public KirinJewel() {
        super(ElementType.FIRE, 2000, false);
    }

    public static DamageSource holyFire(Entity source) {
        return source.damageSources().source(ECDamageTypes.HOLY_FIRE, source);
    }

    @Override
    public void onAttack(Entity attacker, LivingEntity target) {
        var isUndead = target.getType().is(EntityTypeTags.UNDEAD);

        target.hurt(holyFire(attacker), isUndead ? 10 : 5);
        target.igniteForSeconds(isUndead ? 5 : 2);
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        tooltip.add(Component.translatable("tooltip.elementalcraft.kirin").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }
}
