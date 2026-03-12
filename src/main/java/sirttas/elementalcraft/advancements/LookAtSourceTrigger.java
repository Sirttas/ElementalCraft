package sirttas.elementalcraft.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class LookAtSourceTrigger extends SimpleCriterionTrigger<LookAtSourceTrigger.TriggerInstance> {

    public static final String NAME = "look_at_source";

    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, i -> true);
    }

    public record TriggerInstance(
            Optional<ContextAwarePredicate> player
    ) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<LookAtSourceTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(LookAtSourceTrigger.TriggerInstance::player)
        ).apply(instance, LookAtSourceTrigger.TriggerInstance::new));

        public static Criterion<LookAtSourceTrigger.TriggerInstance> playerLookAtSource() {
            return ECCriteriaTriggers.LOOK_AT_SOURCE.get().createCriterion(new LookAtSourceTrigger.TriggerInstance(Optional.empty()));
        }

    }
}
