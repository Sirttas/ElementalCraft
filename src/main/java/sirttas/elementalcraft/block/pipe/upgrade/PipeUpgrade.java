package sirttas.elementalcraft.block.pipe.upgrade;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.TypedInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPath;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPathNode;
import sirttas.elementalcraft.block.pipe.ConnectionType;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeTransferer;
import sirttas.elementalcraft.block.pipe.upgrade.capability.PipeUpgradeCapability;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.loot.parameter.ECLootContextParamSets;
import sirttas.elementalcraft.loot.parameter.ECLootContextParams;

import java.util.Collections;
import java.util.List;

public class PipeUpgrade extends AttachmentHolder implements ItemLike, TypedInstance<PipeUpgradeType<?>> {

    public static final String FOLDER = "elementalcraft/pipe_upgrades";

    private final Holder<PipeUpgradeType<?>> type;

    private final ElementPipeBlockEntity pipe;
    private final Direction direction;
    @Nullable private Item item;

    protected PipeUpgrade(Holder<PipeUpgradeType<?>> type, ElementPipeBlockEntity pipe, Direction direction) {
        this.type = type;
        this.pipe = pipe;
        this.direction = direction;
    }

    @Override
    public Holder<PipeUpgradeType<?>> typeHolder() {
        return type;
    }

    @Nullable
    @Override
    public final <T> T setData(AttachmentType<T> type, T data) {
        this.pipe.setChanged();
        return super.setData(type, data);
    }

    @Nullable
    public <T, C> T getCapability(final PipeUpgradeCapability<T, C> cap, C context) {
        return cap.getCapability(this, context);
    }

    public Identifier getKey() {
        return type.getKey().identifier();
    }

    public final void load(ValueInput input) {
        input.child(ATTACHMENTS_NBT_KEY).ifPresent(this::deserializeAttachments);
        loadAdditional(input);
    }

    protected void loadAdditional(ValueInput input) {
        // for subclasses
    }

    public void save(ValueOutput output) {
        var attachments = output.child(ATTACHMENTS_NBT_KEY);

        output.putString("id", getKey().toString());
        serializeAttachments(attachments);
        if (attachments.isEmpty()) {
            output.discard(ATTACHMENTS_NBT_KEY);
        }
        saveAdditional(output);
    }

    protected void saveAdditional(ValueOutput output) {
        // for subclasses
    }

    public Direction getDirection() {
        return direction;
    }

    public ElementPipeBlockEntity getPipe() {
        return pipe;
    }

    public void onAdded() {
        // NOOP
    }

    public void onRemoved() {
        // NOOP
    }

    public void onTransfer(ElementType type, int amount, @Nullable IElementTransferPathNode prev, @Nullable IElementTransferPathNode next) {
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

    public IElementTransferPath alterPath(IElementTransferPath path) {
        return path;
    }

    public void dropAll(@Nullable Player player) { // TODO handle explosions
        if (!(pipe.getLevel() instanceof ServerLevel serverLevel) || serverLevel.isClientSide()) {
            return;
        }

        var tableKey = this.type.value().getLootTable();
        var lootParams = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.BLOCK_STATE, pipe.getBlockState())
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pipe.getBlockPos()))
                .withParameter(ECLootContextParams.DIRECTION, direction)
                .withParameter(LootContextParams.BLOCK_ENTITY, pipe)
                .withOptionalParameter(LootContextParams.THIS_ENTITY, player)
                .create(ECLootContextParamSets.PIPE_UPGRADE);

        serverLevel.getServer().reloadableRegistries().getLootTable(tableKey).getRandomItems(lootParams).forEach(stack -> Containers.dropItemStack(serverLevel, pipe.getBlockPos().getX(), pipe.getBlockPos().getY(), pipe.getBlockPos().getZ(), stack));
    }

    @Override
    public Item asItem() {
        if (item == null) {
            item = this.type.value().asItem();
        }
        return item;
    }

    public int getWeight() {
        return 0;
    }

    public void animateTick(Level level, BlockPos pos, RandomSource random) {

    }
}
