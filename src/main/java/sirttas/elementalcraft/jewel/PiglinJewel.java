package sirttas.elementalcraft.jewel;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.monster.piglin.Piglin;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PiglinJewel extends Jewel {

    protected PiglinJewel() {
        super(ElementType.FIRE, 5);
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        if (entity instanceof LivingEntity livingEntity && super.isActive(entity, elementStorage)) {
            return livingEntity.level.getEntitiesOfClass(Piglin.class, livingEntity.getBoundingBox().inflate(24)).stream()
                    .anyMatch(p -> p.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty()).contains(livingEntity));
        }
        return false;
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        tooltip.add(Component.translatable("tooltip.elementalcraft.piglin").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }

}
