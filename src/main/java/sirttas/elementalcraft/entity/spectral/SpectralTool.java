package sirttas.elementalcraft.entity.spectral;

import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.entity.ai.ECMemoryModuleTypes;

import java.util.List;
import java.util.UUID;

public class SpectralTool extends PathfinderMob implements OwnableEntity {

    public static final String NAME = "spectral_tool";

    private static final String OWNER_TAG = "Owner";

    protected static final Lazy<List<SensorType<? extends Sensor<? super SpectralTool>>>> SENSOR_TYPES = Lazy.of(() -> List.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.HURT_BY
    ));

    protected static final Lazy<List<MemoryModuleType<?>>> MEMORY_TYPES =  Lazy.of(() -> List.of(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_PLAYERS,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            ECMemoryModuleTypes.DIG_TARGET_STATE.get(),
            ECMemoryModuleTypes.DIG_TARGET.get(),
            ECMemoryModuleTypes.DIG_PROGRESS.get()
    ));

    private UUID owner;

    public SpectralTool(LivingEntity owner, ItemStack tool) {
        this(ECEntities.SPECTRAL_TOOL.get(), owner.level());
        this.setItemInHand(InteractionHand.MAIN_HAND, tool);
        this.owner = owner.getUUID();
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    public SpectralTool(EntityType<? extends SpectralTool> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.BLOCK_INTERACTION_RANGE, 4.5)
                .add(Attributes.FLYING_SPEED, 0.7F);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            this.discard();
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public void aiStep() {
        this.updateSwingTime();
        super.aiStep();
    }

    @Override
    public void customServerAiStep() {
        var brain = this.getBrain();

        brain.tick((ServerLevel) this.level(), this);
        brain.setActiveActivityToFirstValid(List.of(Activity.FIGHT, Activity.DIG, Activity.IDLE));
    }

    @Override
    @NotNull
    protected Brain.Provider<SpectralTool> brainProvider() {
        return Brain.provider(MEMORY_TYPES.get(), SENSOR_TYPES.get());
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return SpectralToolAi.makeBrain(this, dynamic);
    }

    @Override
    public @NotNull Brain<SpectralTool> getBrain() {
        return (Brain<SpectralTool>) super.getBrain();
    }

    @Override
    public @Nullable UUID getOwnerUUID() {
        return owner;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.owner != null) {
            compound.putUUID(OWNER_TAG, this.owner);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        UUID uuid;
        if (compound.hasUUID(OWNER_TAG)) {
            uuid = compound.getUUID(OWNER_TAG);
        } else {
            String s = compound.getString(OWNER_TAG);
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }
        if (uuid != null) {
            this.owner = uuid;
        }
    }

    public void digBlock(@NotNull BlockPos target) {
        var brain = this.getBrain();

        brain.setMemory(ECMemoryModuleTypes.DIG_TARGET.get(), target);
        brain.setMemoryWithExpiry(ECMemoryModuleTypes.DIG_TARGET_STATE.get(), this.level().getBlockState(target), 60L);
    }
}
