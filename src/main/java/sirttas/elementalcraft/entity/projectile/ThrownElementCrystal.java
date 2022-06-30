package sirttas.elementalcraft.entity.projectile;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;

public class ThrownElementCrystal extends ThrowableItemProjectile {

    public static final String NAME = "thrown_element_crystal";

    public ThrownElementCrystal(EntityType<? extends ThrownElementCrystal> type, Level level) {
        super(type, level);
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
        result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            dropFromLootTable();
            this.discard();
        }

    }

    protected void dropFromLootTable() {
        ResourceLocation itemLocation = ForgeRegistries.ITEMS.getKey(this.getItem().getItem());
        LootTable lootTable = this.level.getServer().getLootTables().get(new ResourceLocation(itemLocation.getNamespace(), "entities/thrown_element_crystal/" + itemLocation.getPath()));
        LootContext ctx = new LootContext.Builder((ServerLevel)this.level)
                .withRandom(this.random)
                .withParameter(LootContextParams.THIS_ENTITY, this)
                .withParameter(LootContextParams.ORIGIN, this.position())
                .create(LootContextParamSets.SELECTOR);

        lootTable.getRandomItems(ctx).forEach(this::spawnAtLocation);
    }
}
