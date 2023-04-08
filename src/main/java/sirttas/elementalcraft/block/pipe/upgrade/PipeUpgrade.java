package sirttas.elementalcraft.block.pipe.upgrade;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPath;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.pipe.ConnectionType;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeTransferer;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.loot.ECLootContextParamSets;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class PipeUpgrade extends CapabilityProvider<PipeUpgrade> implements ItemLike {

    public static final String FOLDER = "elementalcraft/pipe_upgrade/";

    private final PipeUpgradeType<?> type;

    private final ElementPipeBlockEntity pipe;
    private final Direction direction;
    private Item item;

    protected PipeUpgrade(PipeUpgradeType<?> type, ElementPipeBlockEntity pipe, Direction direction) {
        super(PipeUpgrade.class);
        this.type = type;
        this.pipe = pipe;
        this.direction = direction;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull final Capability<T> cap, @Nullable final Direction side) {
        return side == null || side == direction ? super.getCapability(cap, side) : LazyOptional.empty();
    }

    public ResourceLocation getKey() {
        return type.getKey();
    }

    public void load(CompoundTag tag) {
        if (getCapabilities() != null && tag.contains(ECNames.FORGE_CAPS)) {
            deserializeCaps(tag.getCompound(ECNames.FORGE_CAPS));
        }
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putString("id", getKey().toString());
        saveAdditional(tag);
        return tag;
    }

    protected void saveAdditional(CompoundTag tag) {
        if (getCapabilities() != null) {
            var caps = serializeCaps();

            if (caps != null) {
                tag.put(ECNames.FORGE_CAPS, caps);
            }
        }
    }

    @Nonnull
    public PipeUpgradeType<?> getType() {
        return type;
    }

    @Nonnull
    public Direction getDirection() {
        return direction;
    }

    @Nonnull
    public ElementPipeBlockEntity getPipe() {
        return pipe;
    }

    public void added() {
        // NOOP
    }

    public void removed() {
        // NOOP
    }

    public boolean canPlace(ConnectionType connectionType) {
        return connectionType.isConnected();
    }

    public boolean canTransfer(ElementType type, ConnectionType connection) {
        return true;
    }

    public VoxelShape getShape() {
        return Shapes.empty();
    }

    public boolean replaceSection() {
        return false;
    }

    public boolean replaceExtraction() {
        return replaceSection();
    }

    public List<BlockPos> getConnections(ElementType type, ConnectionType connection) {
        return canTransfer(type, connection) ? ElementPipeTransferer.getDefaultPos(pipe.getBlockPos(), direction, connection) : Collections.emptyList();
    }

    public IElementTransferPath alterPath(@Nonnull IElementTransferPath path) {
        return path;
    }

    @Nonnull
    public RenderShape getRenderShape() {
        return RenderShape.MODEL;
    }

    public void dropAll(@Nullable Player player) {
        if (!(pipe.getLevel() instanceof ServerLevel serverLevel) || serverLevel.isClientSide) {
            return;
        }

        var registryName = getKey();
        var tableLocation = new ResourceLocation(registryName.getNamespace(), FOLDER + registryName.getPath());

        if (tableLocation.equals(BuiltInLootTables.EMPTY)) {
            return;
        }

        var lootcontext = new LootContext.Builder(serverLevel)
                .withRandom(serverLevel.random)
                .withParameter(LootContextParams.BLOCK_STATE, pipe.getBlockState())
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pipe.getBlockPos()))
                .withOptionalParameter(LootContextParams.THIS_ENTITY, player)
                .withOptionalParameter(LootContextParams.BLOCK_ENTITY, pipe)
                .create(ECLootContextParamSets.PIPE_UPGRADE);

        lootcontext.getLevel().getServer().getLootTables().get(tableLocation).getRandomItems(lootcontext).forEach(player != null ? player::spawnAtLocation : stack -> Containers.dropItemStack(serverLevel, pipe.getBlockPos().getX(), pipe.getBlockPos().getY(), pipe.getBlockPos().getZ(), stack));
    }

    @Nonnull
    @Override
    public Item asItem() {
        if (item == null) {
            item = this.type.asItem();
        }
        return item;
    }
}
