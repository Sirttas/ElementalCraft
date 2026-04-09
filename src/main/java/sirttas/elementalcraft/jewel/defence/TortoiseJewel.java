package sirttas.elementalcraft.jewel.defence;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.gamerules.GameRules;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.tag.ECTags;

import java.util.function.Consumer;

public class TortoiseJewel extends DefenceJewel {

    public static final String NAME = "tortoise";

    public TortoiseJewel() {
        super(ElementType.EARTH, 500, false);
    }

    @Override
    public float onHurt(Entity entity, DamageSource source, float amount) {
        var level = entity.level();

        if (amount == 0 || !source.is(ECTags.DamageTypes.BLOCKED_BY_TORTOISE_JEWEL)) {
            return super.onHurt(entity, source, amount);
        }

        level.getEntitiesOfClass(FallingBlockEntity.class, entity.getBoundingBox()).forEach(e -> {
            e.discard();
            if (e.dropItem && level instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)) {
                var block = e.getBlockState().getBlock();

                e.callOnBrokenAfterFall(block, e.blockPosition());
                e.spawnAtLocation(serverLevel, block);
            }
        });
        return 0;
    }

    @Override
    public void appendHoverText(@NotNull Consumer<Component> builder) {
        builder.accept(Component.translatable("tooltip.elementalcraft.tortoise").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(builder);
    }
}
