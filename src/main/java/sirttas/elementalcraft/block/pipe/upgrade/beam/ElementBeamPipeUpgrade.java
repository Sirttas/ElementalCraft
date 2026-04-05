package sirttas.elementalcraft.block.pipe.upgrade.beam;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPathNode;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.pipe.ConnectionType;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeTransferer;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.particle.ParticleHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ElementBeamPipeUpgrade extends PipeUpgrade {

    public static final String NAME = "element_beam";

    private static final Map<Direction, VoxelShape> SHAPES = ShapeHelper.directionShapes(Block.box(7D, 9.5D, 7D, 9D, 14D, 9D));

    private final RuneHandler runeHandler;

    private ElementBeamPipeUpgrade other;

    private int transfered;

    public ElementBeamPipeUpgrade(ElementPipeBlockEntity pipe, Direction direction) {
        super(PipeUpgradeTypes.ELEMENT_BEAM.get(), pipe, direction);
        runeHandler = new RuneHandler(ECConfig.SERVER.elementBeamMaxRunes.get(), pipe::setChanged);
        transfered = 0;
    }

    @Override
    public VoxelShape getShape() {
        return SHAPES.get(this.getDirection());
    }

    @Override
    public boolean replaceSection() {
        return true;
    }

    @Override
    public boolean canPlace(ConnectionType connectionType) {
        return !connectionType.isConnected();
    }

    public boolean isLinked() {
        return getOther() != null;
    }

    @Override
    public List<BlockPos> getConnections(ElementType type, ConnectionType connection) {
        return getOther() != null ? Collections.singletonList(this.other.getPipe().getBlockPos()) : Collections.emptyList();
    }

    @Override
    public boolean canTransfer(ElementType type, ConnectionType connection) {
       return getOther() != null;
    }

    @Override
    public void onAdded() {
        tryLink();
    }

    private void tryLink() {
        if (this.other == null) {
            this.findOther().ifPresent(b -> {
                var opt = b.findOther();

                if (opt.isPresent() && opt.get() == this) {
                    this.other = b;
                    b.other = this;
                }
            });
        }
    }

    @Override
    public void onRemoved() {
        if (this.other != null) {
            this.other.other = null;
            this.other = null;
        }
    }

    @Override
    public int getWeight() {
        return (int) Math.round(Math.floor(Math.sqrt(this.getPipe().getBlockPos().distSqr(this.other.getPipe().getBlockPos())) / 2));
    }

    @Override
    public void onTransfer(ElementType type, int amount, @Nullable IElementTransferPathNode prev, @Nullable IElementTransferPathNode next) {
        var pipe = this.getPipe();
        var level = pipe.getLevel();
        var otherPipe = this.other != null ? this.other.getPipe() : null;
        var to = next != null ? next.getPos() : null;

        if (level == null || otherPipe == null || !otherPipe.getBlockPos().equals(to) || pipe.isCovered() || otherPipe.isCovered()) {
            return;
        }

        var max = pipe.getMaxTransferAmount();

        transfered += amount;
        if (transfered < max) {
            return;
        }
        transfered -= max;

        if (level.getRandom().nextDouble() < 0.2) {
            var direction = this.getDirection();
            var opposite = direction.getOpposite();

            ParticleHelper.createElementFlowParticle(type, level, Vec3.atCenterOf(to).relative(opposite, 0.5), Vec3.atCenterOf(pipe.getBlockPos()).relative(direction, 0.5), level.getRandom());
        }
    }

    private Optional<ElementBeamPipeUpgrade> findOther() {
        var pipe = this.getPipe();
        var level = pipe.getLevel();

        if (level == null) {
            return Optional.empty();
        }

        var pos = pipe.getBlockPos().mutable();
        var direction = this.getDirection();
        var opposite = direction.getOpposite();
        int range = Math.round((runeHandler.getBonus(Rune.BonusType.RANGE) + 1) * ECConfig.SERVER.elementBeamRange.get());

        for (int i = 0; i < range; i++) {
            var transferer = level.getCapability(ElementalCraftCapabilities.ElementTransferers.BLOCK, pos.move(direction), opposite);

            if (transferer instanceof ElementPipeTransferer elementPipeTransferer && elementPipeTransferer.getUpgrade(opposite) instanceof ElementBeamPipeUpgrade elementBeamPipeUpgrade) {
                return Optional.of(elementBeamPipeUpgrade);
            }
        }
        return Optional.empty();
    }

    private ElementBeamPipeUpgrade getOther() {
        if (this.other == null || !this.other.getPipe().isRemoved()) {
            this.other = null;
            this.tryLink();
        }
        return this.other;
    }

    public RuneHandler getRuneHandler() {
        return runeHandler;
    }

    @Override
    public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
        super.loadAdditional(compound, provider);
        if (compound.contains(ECNames.RUNE_HANDLER)) {
            IRuneHandler.readNBT(getRuneHandler(), compound.getList(ECNames.RUNE_HANDLER, 8));
        }
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(getRuneHandler()));
    }

}
