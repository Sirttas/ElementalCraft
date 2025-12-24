package sirttas.elementalcraft.entity.spectral;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
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
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.entity.ai.ECMemoryModuleTypes;
import sirttas.elementalcraft.entity.ai.ECSensorTypes;
import sirttas.elementalcraft.entity.ai.behavior.FindBlockToDig;
import sirttas.elementalcraft.entity.ai.behavior.FollowOwner;
import sirttas.elementalcraft.entity.ai.behavior.MineTargetBlock;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SpectralTool extends PathfinderMob implements FlyingAnimal, OwnableEntity {

    public static final String NAME = "spectral_tool";

    private static final String OWNER_TAG = "Owner";

    protected static final Lazy<List<SensorType<? extends Sensor<? super SpectralTool>>>> SENSOR_TYPES = Lazy.of(() -> List.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.HURT_BY,
            ECSensorTypes.WATCH_OWNER_DIG.get()
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
    }

    public SpectralTool(EntityType<? extends SpectralTool> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes();
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            this.discard();
        }
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
        var brain = this.brainProvider().makeBrain(dynamic);

        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink()
        ));
        brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
                SetEntityLookTarget.create(8F),
                SetLookAndInteract.create(EntityType.PLAYER, 4),
                FollowOwner.create(8)
        ));
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.DIG, 10, ImmutableList.of(
                FindBlockToDig.create(6),
                new MineTargetBlock()
        ), ECMemoryModuleTypes.DIG_TARGET_STATE.get());
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(
                StopAttackingIfTargetInvalid.create(),
                SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                MeleeAttack.create(20)
        ), MemoryModuleType.ATTACK_TARGET);
        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
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
}
