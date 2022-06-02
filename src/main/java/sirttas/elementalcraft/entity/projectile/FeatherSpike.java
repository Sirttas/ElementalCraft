package sirttas.elementalcraft.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;

import javax.annotation.Nonnull;

public class FeatherSpike extends AbstractArrow {

    public static final String NAME = "feather_spike";

    @ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final EntityType<FeatherSpike> TYPE = null;

    public FeatherSpike(Level level) {
        this(TYPE, level);
    }

    public FeatherSpike(EntityType<? extends FeatherSpike> type, Level level) {
        super(type, level);
        pickup = AbstractArrow.Pickup.DISALLOWED;
    }

    public FeatherSpike(Level level, LivingEntity sender) {
        super(TYPE, sender, level);
        pickup = AbstractArrow.Pickup.DISALLOWED;
    }

    @Nonnull
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}
