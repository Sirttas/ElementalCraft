package sirttas.elementalcraft.jewel.defence;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.GameRules;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;

public class TortoiseJewel extends DefenceJewel {

    public static final String NAME = "tortoise";

    public TortoiseJewel() {
        super(ElementType.EARTH, 500);
    }

    @Override
    public float onHurt(Entity entity, DamageSource source, float amount) {
        var level = entity.level();

        if (source.is(ECTags.DamageTypes.BLOCKED_BY_TORTOISE_JEWEL)) {
            level.getEntitiesOfClass(FallingBlockEntity.class, entity.getBoundingBox()).forEach(e -> {
                e.discard();
                if (e.dropItem && level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    var block = e.getBlockState().getBlock();

                    e.callOnBrokenAfterFall(block, e.blockPosition());
                    e.spawnAtLocation(block);
                }
            });
            return 0;
        }
        return super.onHurt(entity, source, amount);
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        tooltip.add(Component.translatable("tooltip.elementalcraft.tortoise").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }
}
