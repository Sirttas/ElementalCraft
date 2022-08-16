package sirttas.elementalcraft.jewel;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class StriderJewel extends Jewel {

    private final TagKey<Fluid> tag;

    public StriderJewel(ElementType elementType, int consumption, TagKey<Fluid> tag) {
        super(elementType, consumption);
        this.tag = tag;
    }

    private boolean isOnFluid(Entity entity) {
        BlockPos blockpos = entity.getOnPos();
        BlockState blockstate = entity.level.getBlockState(blockpos);

        return blockstate.getFluidState().is(tag);
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        return isOnFluid(entity) && super.isActive(entity, elementStorage);
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        var key = tag.location();

        tooltip.add(new TranslatableComponent("tooltip.elementalcraft.strider." + key.getNamespace() + '.' + key.getPath().replace("/", ".")).withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }

    public TagKey<Fluid> getTag() {
        return tag;
    }
}
