package sirttas.elementalcraft.api.block.shrine.budding;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import sirttas.dpanvil.api.data.DataManagerCodecs;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;

import java.util.List;

public record BuddingShrineBudType(
        List<Block> sequence,
        Holder<ShrineUpgrade> requiredUpgrade,
        Identifier plateModel
) {

    public static final String PLATE_MODEL_FOLDER = "elementalcraft/budding_shrine_plates";

    public static final Codec<BuddingShrineBudType> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("sequence").forGetter(BuddingShrineBudType::sequence),
            DataManagerCodecs.holderCodec(ElementalCraftApi.SHRINE_UPGRADE_MANAGER_KEY, ShrineUpgrade.CODEC, false).fieldOf("requires_upgrade").forGetter(BuddingShrineBudType::requiredUpgrade),
            Identifier.CODEC.fieldOf("plate_model").forGetter(BuddingShrineBudType::plateModel)
    ).apply(builder, BuddingShrineBudType::new));

    public static final BuddingShrineBudType AMETHYST = new BuddingShrineBudType(
            List.of(Blocks.SMALL_AMETHYST_BUD, Blocks.MEDIUM_AMETHYST_BUD, Blocks.LARGE_AMETHYST_BUD, Blocks.AMETHYST_CLUSTER),
            null,
            ElementalCraftApi.createRL(PLATE_MODEL_FOLDER + "/amethyst")
    );
}
