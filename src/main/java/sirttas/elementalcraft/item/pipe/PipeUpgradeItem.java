package sirttas.elementalcraft.item.pipe;

import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.component.ECDataComponents;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class PipeUpgradeItem extends Item implements IPipeInteractingItem {

    private final Supplier<PipeUpgradeType<?>> supplier;
    private PipeUpgradeType<?> pipeUpgradeType;

    public PipeUpgradeItem(Supplier<PipeUpgradeType<?>> supplier, Properties properties) {
        super(properties);
        this.supplier = supplier;
    }

    public PipeUpgradeType<?> getPipeUpgradeType() {
        if (pipeUpgradeType == null) {
            pipeUpgradeType = supplier.get();
        }
        return pipeUpgradeType;
    }

    @Nonnull
    @Override
    public ItemInteractionResult useOnPipe(@Nonnull ElementPipeBlockEntity pipe, @Nonnull UseOnContext context) {
        var stack = context.getItemInHand();
        var face = context.getClickedFace();
        var player = context.getPlayer();
        var level = pipe.getLevel();

        if (level == null || pipe.getUpgrade(face) != null) {
           return ItemInteractionResult.FAIL;
        }

        var upgrade = getPipeUpgradeType().create(pipe, face);
        var customData = stack.getOrDefault(ECDataComponents.PIPE_UPGRADE_DATA, CustomData.EMPTY);

        if (!customData.isEmpty()) {
            upgrade.load(customData.copyTag(), level.registryAccess());
        }
        if (upgrade.canPlace(pipe.getConnection(face))) {
            pipe.setUpgrade(face, upgrade);
            if (player != null && !player.getAbilities().instabuild) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    player.setItemInHand(context.getHand(), ItemStack.EMPTY);
                }
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.FAIL;
    }

    @Nonnull
    @Override
    public String getDescriptionId() {
        return pipeUpgradeType.getDescriptionId();
    }

}
