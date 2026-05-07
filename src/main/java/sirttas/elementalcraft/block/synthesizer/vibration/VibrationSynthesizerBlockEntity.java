package sirttas.elementalcraft.block.synthesizer.vibration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.synthesizer.AbstractSynthesizerBlockEntity;
import sirttas.elementalcraft.gameevent.ECGameEvents;
import sirttas.elementalcraft.range.RangeRenderTimer;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public class VibrationSynthesizerBlockEntity extends AbstractSynthesizerBlockEntity implements GameEventListener.Provider<VibrationSystem.@NotNull Listener>, VibrationSystem {

    public static final ResourceKey<@NotNull IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(VibrationSynthesizerBlock.NAME);
    private static final Holder<@NotNull IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

    private final RangeRenderTimer rangeRenderTimer = new RangeRenderTimer();
    private VibrationSystem.Data vibrationData;
    private final VibrationSystem.Listener vibrationListener;
    private final VibrationSystem.User vibrationUser;
    private int nearbySynthesis;

    public VibrationSynthesizerBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.VIBRATION_SYNTHESIZER, PROPERTIES, pos, state);
        this.vibrationData = new VibrationSystem.Data();
        this.vibrationListener = new VibrationSystem.Listener(this);
        this.vibrationUser = new VibrationUser(pos);
        this.nearbySynthesis = 0;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, VibrationSynthesizerBlockEntity vibrationSynthesizer) {
        vibrationSynthesizer.handleSynthesis();
        VibrationSystem.Ticker.tick(level, vibrationSynthesizer.vibrationData, vibrationSynthesizer.vibrationUser);
    }

    @Override
    protected int synthesizeElement() {
        return 0;
    }


    public boolean showsRange() {
        return rangeRenderTimer.showsRange();
    }

    public void startShowingRange() {
        rangeRenderTimer.startShowingRange();
    }

    @Override
    public @NotNull Listener getListener() {
        return this.vibrationListener;
    }

    @Override
    public @NotNull Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    public @NotNull User getVibrationUser() {
        return this.vibrationUser;
    }

    @Override
    protected void loadAdditional(@Nonnull ValueInput input) {
        super.loadAdditional(input);
        this.vibrationData = input.read("listener", VibrationSystem.Data.CODEC).orElseGet(VibrationSystem.Data::new);
        this.nearbySynthesis = input.getIntOr("nearby_synthesis", 0);
    }

    @Override
    protected void saveAdditional(@Nonnull ValueOutput output) {
        super.saveAdditional(output);
        output.store("listener", VibrationSystem.Data.CODEC, this.vibrationData);
        output.putInt("nearby_synthesis", this.nearbySynthesis);
    }

    private class VibrationUser implements VibrationSystem.User {

        private final PositionSource positionSource;

        private VibrationUser(BlockPos pos) {
            this.positionSource = new BlockPositionSource(pos);
        }

        @Override
        public int getListenerRadius() {
            var range = getRange();

            return (int) Math.round(range.getSize() * 2); // * 2 to make sure we match everything in range
        }

        @Override
        public @NotNull PositionSource getPositionSource() {
            return positionSource;
        }

        @Override
        public boolean canReceiveVibration(@NotNull ServerLevel serverLevel, @NotNull BlockPos pos, @NotNull Holder<@NotNull GameEvent> gameEvent, @NotNull GameEvent.Context context) {
            if (pos == getBlockPos() || !getRange().contains(pos.getCenter())) {
                return false;
            } else if (gameEvent.is(ECGameEvents.AIR_SYNTHESIS)) {
                return true;
            }
            return getBlockState().getValue(VibrationSynthesizerBlock.PHASE) == SculkSensorPhase.INACTIVE && gameEvent.is(ECTags.GameEvents.SYNTHESIZABLE_TO_AIR);
        }

        @Override
        public void onReceiveVibration(@NotNull ServerLevel serverLevel, @NotNull BlockPos pos, @NotNull Holder<@NotNull GameEvent> gameEvent, @Nullable Entity entity, @Nullable Entity owner, float range) {
            var state = getBlockState();

            if (gameEvent.is(ECTags.GameEvents.SYNTHESIZABLE_TO_AIR)) {
                if (nearbySynthesis > 0) {
                    nearbySynthesis--;
                } else {
                    getElementStorage().insertElement(Math.round(synthesisMultiplier), ElementType.AIR, false);
                }
            } else if (gameEvent.is(ECGameEvents.AIR_SYNTHESIS)) {
                nearbySynthesis++;
            }
            if (state.getValue(VibrationSynthesizerBlock.PHASE) == SculkSensorPhase.INACTIVE) {
                serverLevel.setBlockAndUpdate(worldPosition, state.setValue(VibrationSynthesizerBlock.PHASE, SculkSensorPhase.ACTIVE));
                serverLevel.scheduleTick(worldPosition, state.getBlock(), 30);
                serverLevel.playSound(
                        null,
                        worldPosition.getX() + 0.5,
                        worldPosition.getY() + 0.5,
                        worldPosition.getZ() + 0.5,
                        SoundEvents.SCULK_CLICKING,
                        SoundSource.BLOCKS,
                        1.0F,
                        serverLevel.getRandom().nextFloat() * 0.2F + 0.8F
                );
            }
        }
    }
}
