package sirttas.elementalcraft.damagesource;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECDamageTypes {

    public static final ResourceKey<DamageType> HOLY_FIRE = ResourceKey.create(Registries.DAMAGE_TYPE, ElementalCraftApi.createRL("holy_fire"));

    private ECDamageTypes() {}
}
