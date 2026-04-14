package sirttas.elementalcraft.datagen.managed;

import appeng.api.ids.AEConstants;
import appeng.core.definitions.AEBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.dpanvil.api.data.DataManagerCodecs;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.dpanvil.api.data.preprocessor.NeoForgeConditionsPreprocessor;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class BudTypeProvider extends AbstractManagedDataBuilderProvider<BuddingShrineBudType, BudTypeProvider.BuddingShrineBudTypeBuilder> {

    public BudTypeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, ElementalCraftApi.BUD_TYPE_MANAGER, BuddingShrineBudTypeBuilder.CODEC);
    }

    @Override
    protected void collectBuilders(HolderLookup.Provider registries) {
        add(BuddingShrineBlockEntity.AMETHYST.getKey(), new BuddingShrineBudTypeBuilder(
                List.of(Blocks.SMALL_AMETHYST_BUD, Blocks.MEDIUM_AMETHYST_BUD, Blocks.LARGE_AMETHYST_BUD, Blocks.AMETHYST_CLUSTER)));
        add(IDataManager.createKey(ElementalCraftApi.BUD_TYPE_MANAGER_KEY, ElementalCraftApi.createRL("springaline")), new BuddingShrineBudTypeBuilder(
                List.of(ECBlocks.SMALL_SPRINGALINE_BUD.get(), ECBlocks.MEDIUM_SPRINGALINE_BUD.get(), ECBlocks.LARGE_SPRINGALINE_BUD.get(), ECBlocks.SPRINGALINE_CLUSTER.get()),
                ElementalCraftApi.SHRINE_UPGRADE_MANAGER.getOrCreateHolder(ShrineUpgrades.SPRINGALINE)
        ));
        add(IDataManager.createKey(ElementalCraftApi.BUD_TYPE_MANAGER_KEY, ElementalCraftApi.createRL("certus_quartz")), new BuddingShrineBudTypeBuilder(
                List.of(AEBlocks.SMALL_QUARTZ_BUD.block(), AEBlocks.MEDIUM_QUARTZ_BUD.block(), AEBlocks.LARGE_QUARTZ_BUD.block(), AEBlocks.QUARTZ_CLUSTER.block()),
                ElementalCraftApi.SHRINE_UPGRADE_MANAGER.getOrCreateHolder(ShrineUpgrades.CERTUS_QUARTZ)
        )).when(new ModLoadedCondition(AEConstants.MOD_ID));
    }


    @Override
    public @NotNull String getName() {
        return "ElementalCraft Budding Shrine Bud Types";
    }

    public static final class BuddingShrineBudTypeBuilder {

        private static final Codec<BuddingShrineBudTypeBuilder> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("sequence").forGetter(b -> b.sequence),
                DataManagerCodecs.holderCodec(ElementalCraftApi.SHRINE_UPGRADE_MANAGER_KEY, ShrineUpgrade.CODEC, false).optionalFieldOf("requires_upgrade").forGetter(b -> b.requiredUpgrade),
                NeoForgeConditionsPreprocessor.fieldOf(b -> b.conditions)
        ).apply(builder, (_, _, _) -> {
            throw new UnsupportedOperationException("Builder deserialization is not supported.");
        }));

        private final List<Block> sequence;
        private final Optional<Holder<@NotNull ShrineUpgrade>> requiredUpgrade;
        private final List<ICondition> conditions;

        public BuddingShrineBudTypeBuilder(List<Block> sequence) {
            this(sequence, null);
        }

        public BuddingShrineBudTypeBuilder(List<Block> sequence, Holder<@NotNull ShrineUpgrade> requiredUpgrade) {
            this.sequence = sequence;
            this.requiredUpgrade = Optional.ofNullable(requiredUpgrade);
            this.conditions = new ArrayList<>();
        }

        public BuddingShrineBudTypeBuilder when(ICondition condition) {
            this.conditions.add(condition);
            return this;
        }

    }

}
