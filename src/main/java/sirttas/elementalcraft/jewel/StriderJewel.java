package sirttas.elementalcraft.jewel;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class StriderJewel extends Jewel {

    private final TagKey<@NotNull Fluid> tag;

    public StriderJewel(ElementType elementType, int consumption, TagKey<@NotNull Fluid> tag) {
        super(elementType, consumption, true);
        this.tag = tag;
    }

    private boolean isOnFluid(Entity entity) {
        var blockpos = entity.getOnPos();
        var level = entity.level();
        var fluid = level.getBlockState(blockpos).getFluidState();

        return fluid.is(tag) && !level.getBlockState(blockpos.above()).getFluidState().getType().isSame(fluid.getType());
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        return isOnFluid(entity) && super.isActive(entity, elementStorage);
    }

    public boolean canStandOnFluid(FluidState fluid, @Nonnull Entity entity) {
        return fluid.is(tag) && super.isActive(entity, entity.getCapability(ElementalCraftCapabilities.ElementStorages.ENTITY));
    }

    @Override
    public void appendHoverText(@NotNull Consumer<Component> builder) {
        var key = tag.location();

        builder.accept(Component.translatable("tooltip.elementalcraft.strider." + key.getNamespace() + '.' + key.getPath().replace("/", ".")).withStyle(ChatFormatting.BLUE));
        super.appendHoverText(builder);
    }

    public TagKey<@NotNull Fluid> getTag() {
        return tag;
    }
}
