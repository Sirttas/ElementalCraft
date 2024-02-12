package sirttas.elementalcraft.entity.projectile;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;

public class ThrownElementCrystal extends ThrowableItemProjectile {

    public static final String NAME = "thrown_element_crystal";

    public ThrownElementCrystal(EntityType<? extends ThrownElementCrystal> type, Level level) {
        super(type, level);
    }

    public ThrownElementCrystal(Level level, double x, double y, double z) {
        super(ECEntities.THROWN_ELEMENT_CRYSTAL.get(), x, y, z, level);
    }

    public ThrownElementCrystal(Level level, LivingEntity sender) {
        super(ECEntities.THROWN_ELEMENT_CRYSTAL.get(), sender, level);
    }

    @Nonnull
    @Override
    protected Item getDefaultItem() {
        return ECItems.INERT_CRYSTAL.get();
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0);
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            dropFromLootTable();
            this.discard();
        }

    }

    protected void dropFromLootTable() {
        var itemLocation = BuiltInRegistries.ITEM.getKey(this.getItem().getItem());
        var lootTable = this.level().getServer().getLootData().getLootTable(new ResourceLocation(itemLocation.getNamespace(), "entities/thrown_element_crystal/" + itemLocation.getPath()));
        var params = new LootParams.Builder((ServerLevel)this.level())
                .withParameter(LootContextParams.THIS_ENTITY, this)
                .withParameter(LootContextParams.ORIGIN, this.position())
                .create(LootContextParamSets.SELECTOR);

        lootTable.getRandomItems(params).forEach(this::spawnAtLocation);
    }
}
