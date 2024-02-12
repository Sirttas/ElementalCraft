package sirttas.elementalcraft.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.entity.ECEntities;

import javax.annotation.Nonnull;

public class FeatherSpike extends AbstractArrow {

    public static final String NAME = "feather_spike";

    public FeatherSpike(Level level) {
        this(ECEntities.FEATHER_SPIKE.get(), level);
    }

    public FeatherSpike(EntityType<? extends FeatherSpike> type, Level level) {
        super(type, level, ItemStack.EMPTY);
        pickup = AbstractArrow.Pickup.DISALLOWED;
    }

    public FeatherSpike(Level level, LivingEntity sender) {
        super(ECEntities.FEATHER_SPIKE.get(), sender, level, ItemStack.EMPTY);
        pickup = AbstractArrow.Pickup.DISALLOWED;
    }

    @Nonnull
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}
