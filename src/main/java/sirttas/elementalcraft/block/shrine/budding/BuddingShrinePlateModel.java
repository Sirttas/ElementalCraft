package sirttas.elementalcraft.block.shrine.budding;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.neoforged.neoforge.client.model.standalone.UnbakedStandaloneModel;
import org.jetbrains.annotations.NotNull;

public class BuddingShrinePlateModel {

    private final BlockStateModelPart model;

    public BuddingShrinePlateModel(BlockStateModelPart model) {
        this.model = model;
    }

    public BlockStateModelPart getModel() {
        return model;
    }

    public record Unbaked(Variant variant) implements UnbakedStandaloneModel<@NotNull BuddingShrinePlateModel> {

        public static Codec<Unbaked> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Variant.CODEC.fieldOf("model").forGetter(u -> u.variant)
        ).apply(builder, Unbaked::new));

        @Override
        public BuddingShrinePlateModel bake(@NotNull ModelBaker baker, @NotNull ModelDebugName name) {
            return new BuddingShrinePlateModel(variant.bake(baker));
        }

        @Override
        public void resolveDependencies(@NotNull Resolver resolver) {
            variant.resolveDependencies(resolver);
        }
    }

}
