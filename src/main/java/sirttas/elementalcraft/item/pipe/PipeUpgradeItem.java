package sirttas.elementalcraft.item.pipe;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.item.ECItem;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class PipeUpgradeItem extends ECItem {

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

    public InteractionResult put(ElementPipeBlockEntity pipe, UseOnContext context) {
        var stack = context.getItemInHand();
        var face = context.getClickedFace();
        var player = context.getPlayer();


        if (pipe.getUpgrade(face) != null) {
           return InteractionResult.FAIL;
        }

        var upgrade = getPipeUpgradeType().create(pipe, face);
        var tag = stack.getTag();

        if (tag != null) {
            upgrade.load(tag.getCompound(ECNames.PIPE_UPGRADE_TAG));
        }
        if (upgrade.canPlace(pipe.getConnection(face))) {
            pipe.setUpgrade(face, upgrade);
            if (player != null && !player.getAbilities().instabuild) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    player.setItemInHand(context.getHand(), ItemStack.EMPTY);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Nonnull
    @Override
    public String getDescriptionId() {
        return pipeUpgradeType.getDescriptionId();
    }

}
