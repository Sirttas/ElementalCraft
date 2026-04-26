package sirttas.elementalcraft.datagen.definition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.data.DataManagerCodecs;
import sirttas.dpanvil.api.data.preprocessor.NeoForgeConditionsPreprocessor;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BudTypeDataDefinition {

    public static final Codec<BudTypeDataDefinition> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("sequence").forGetter(BudTypeDataDefinition::getSequence),
            DataManagerCodecs.holderCodec(ElementalCraftApi.SHRINE_UPGRADE_MANAGER_KEY, ShrineUpgrade.CODEC, false).optionalFieldOf("requires_upgrade").forGetter(BudTypeDataDefinition::getRequiredUpgrade),
            NeoForgeConditionsPreprocessor.fieldOf(BudTypeDataDefinition::getConditions)
    ).apply(builder, (_, _, _) -> {
        throw new UnsupportedOperationException("BudTypeDataDefinition deserialization is not supported.");
    }));

    private final ResourceKey<BuddingShrineBudType> key;
    private final List<Block> sequence;
    private final Optional<Holder<@NotNull ShrineUpgrade>> requiredUpgrade;
    private final List<ICondition> conditions;
    public final Identifier texture;

    private BudTypeDataDefinition(ResourceKey<BuddingShrineBudType> key, List<Block> sequence, Optional<Holder<@NotNull ShrineUpgrade>> requiredUpgrade, List<ICondition> conditions, Identifier texture) {
        this.key = key;
        this.sequence = sequence;
        this.requiredUpgrade = requiredUpgrade;
        this.conditions = conditions;
        this.texture = texture;
    }

    public static Builder builder(ResourceKey<BuddingShrineBudType> key) {
        return new Builder(key);
    }

    public ResourceKey<BuddingShrineBudType> getKey() {
        return key;
    }

    public List<Block> getSequence() {
        return sequence;
    }

    public Optional<Holder<@NotNull ShrineUpgrade>> getRequiredUpgrade() {
        return requiredUpgrade;
    }

    public List<ICondition> getConditions() {
        return conditions;
    }

    public static final class Builder {

        private final ResourceKey<BuddingShrineBudType> key;
        private final List<Block> sequence;
        private Holder<@NotNull ShrineUpgrade> requiredUpgrade;
        private final List<ICondition> conditions;
        public Identifier texture;

        public Builder(ResourceKey<BuddingShrineBudType> key) {
            this.key = key;
            this.sequence = new ArrayList<>();
            this.conditions = new ArrayList<>();
            this.texture = null;
        }

        public Builder then(Holder<Block> block) {
            return then(block.value());
        }

        public Builder then(Block block) {
            this.sequence.add(block);
            return this;
        }

        public Builder requires(ResourceKey<@NotNull ShrineUpgrade> upgrade) {
            return requires(ElementalCraftApi.SHRINE_UPGRADE_MANAGER.getOrCreateHolder(upgrade));
        }

        public Builder requires(Holder<@NotNull ShrineUpgrade> upgrade) {
            this.requiredUpgrade = upgrade;
            return this;
        }

        public Builder when(ICondition condition) {
            this.conditions.add(condition);
            return this;
        }

        public Builder texture(Identifier texture) {
            this.texture = texture;
            return this;
        }

        public BudTypeDataDefinition build() {
            if (this.sequence.isEmpty()) {
                throw new IllegalStateException("Cannot build BudTypeDataDefinition with empty sequence");
            }
            if (this.texture == null) {
                throw new IllegalStateException("Cannot build BudTypeDataDefinition without texture");
            }

            return new BudTypeDataDefinition(key, sequence, Optional.ofNullable(requiredUpgrade), conditions, texture);
        }
    }
}
