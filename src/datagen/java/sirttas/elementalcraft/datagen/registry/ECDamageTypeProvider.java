package sirttas.elementalcraft.datagen.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;
import sirttas.elementalcraft.damagesource.ECDamageTypes;

public class ECDamageTypeProvider extends AbstractECRegistryBootstrap<DamageType> {

    public ECDamageTypeProvider() {
        super(Registries.DAMAGE_TYPE);
    }

    @Override
    protected void gather() {
        add(ECDamageTypes.HOLY_FIRE, new DamageType("elementalcraft.holy_fire", 0.1F, DamageEffects.BURNING));
    }
}
