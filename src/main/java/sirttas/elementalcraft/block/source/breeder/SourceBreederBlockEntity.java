package sirttas.elementalcraft.block.source.breeder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.source.breeder.pedestal.SourceBreederPedestalBlockEntity;
import sirttas.elementalcraft.block.source.trait.SourceTraitHelper;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.particle.ParticleHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SourceBreederBlockEntity extends AbstractECBlockEntity {

    private int progress;
    private final RuneHandler runeHandler;

    public SourceBreederBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.SOURCE_BREEDER, pos, state);
        runeHandler = new RuneHandler(ECConfig.COMMON.sourceBreederMaxRunes.get(), this::setChanged);
        progress = -1;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SourceBreederBlockEntity breeder) {
        if (breeder.progress < 0) {
            return;
        }
        var north = breeder.getPedestal(Direction.NORTH);
        var south = breeder.getPedestal(Direction.SOUTH);

        if (north != null && south != null) {
            breeder.makeProgress(north, south);
            return;
        }

        var west = breeder.getPedestal(Direction.WEST);
        var east = breeder.getPedestal(Direction.EAST);

        if (west != null && east != null) {
            breeder.makeProgress(west, east);
        }
    }

    private void makeProgress(SourceBreederPedestalBlockEntity pedestal1, SourceBreederPedestalBlockEntity pedestal2) {
        if (!pedestal1.hasSource() || !pedestal2.hasSource()) {
            return;
        }

        var traitHolder1 = pedestal1.getTraitHolder();
        var traitHolder2 = pedestal2.getTraitHolder();

        if (traitHolder1 == null || traitHolder2 == null) {
            return;
        }
        var oldProgress = progress;


        transfer(pedestal1);
        transfer(pedestal2);

        if (level != null && level.isClientSide && progress > oldProgress && level.random.nextDouble() < 0.2) {
            ParticleHelper.createElementFlowParticle(pedestal1.getElementType(), level, Vec3.atCenterOf((level.random.nextBoolean() ? pedestal1 : pedestal2).getBlockPos()).relative(Direction.UP, 0.4), Vec3.atCenterOf(worldPosition).relative(Direction.UP, 1.7), level.random);
        } else if (level != null && !level.isClientSide) {
            this.setChanged();
        }

        if (progress >= 1000000) {
            breed(pedestal1.getElementType(), traitHolder1, traitHolder2);
            progress = -1;
        }
    }

    private void transfer(SourceBreederPedestalBlockEntity pedestal) {
        float transferAmount = Math.min(getTransferSpeed(pedestal), (float) 1000000 - progress);

        if (transferAmount > 0) {
            float preservation = runeHandler.getBonus(Rune.BonusType.ELEMENT_PRESERVATION) + pedestal.getRuneHandler().getBonus(Rune.BonusType.ELEMENT_PRESERVATION) + 1;

            progress = Math.round(progress + pedestal.getElementStorage().extractElement(Math.round(transferAmount / preservation), false) * preservation);
        }
    }

    private float getTransferSpeed(SourceBreederPedestalBlockEntity pedestal) {
        return ECConfig.COMMON.sourceBreederTransferSpeed.get() * (runeHandler.getBonus(Rune.BonusType.SPEED) + pedestal.getRuneHandler().getBonus(Rune.BonusType.SPEED) + 1);
    }

    private SourceBreederPedestalBlockEntity getPedestal(Direction direction) {
        BlockEntity te = this.level != null ? this.level.getBlockEntity(worldPosition.relative(direction, 2)) : null;

        return te instanceof SourceBreederPedestalBlockEntity sourceBreederPedestalBlockEntity ? sourceBreederPedestalBlockEntity : null;
    }

    private void breed(ElementType elementType, ISourceTraitHolder source1, ISourceTraitHolder source2) {
        if (level == null || level.isClientSide) {
            return;
        }

        var receptacle = ReceptacleHelper.create(elementType, SourceTraitHelper.breed(level, source1.getTraits(), source2.getTraits()));

        level.addFreshEntity(new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 2.5, worldPosition.getZ() + 0.5, receptacle));
    }

    public int getProgress() {
        return progress;
    }

    public void start() {
        if (progress < 0) {
            progress = 0;
        }
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt(ECNames.PROGRESS, progress);
        compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        progress = compound.getInt(ECNames.PROGRESS);
        if (compound.contains(ECNames.RUNE_HANDLER)) {
            IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
        }
    }

    @Override
    @Nonnull
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        if (!this.remove && cap == ElementalCraftCapabilities.RUNE_HANDLE) {
            return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
        }
        return super.getCapability(cap, side);
    }

}
