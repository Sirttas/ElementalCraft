package sirttas.elementalcraft.api.block.shrine.budding;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.data.DataManagerCodecs;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;

import java.util.List;

public record BuddingShrineBudType(
        List<Block> sequence,
        Holder<@NotNull ShrineUpgrade> requiredUpgrade
) {
    public static final Codec<BuddingShrineBudType> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("sequence").forGetter(BuddingShrineBudType::sequence),
            DataManagerCodecs.holderCodec(ElementalCraftApi.SHRINE_UPGRADE_MANAGER_KEY, ShrineUpgrade.CODEC, false).fieldOf("requires_upgrade").forGetter(BuddingShrineBudType::requiredUpgrade)
    ).apply(builder, BuddingShrineBudType::new));
}
