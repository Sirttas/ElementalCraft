package sirttas.elementalcraft.rune;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.cuboid.ItemModelGenerator;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.neoforged.neoforge.client.model.standalone.UnbakedStandaloneModel;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;

public class RuneModel {

    private final Material.Baked sprite;
    private final QuadCollection quadCollection;

    private RuneModel(Material.Baked sprite, QuadCollection quadCollection) {
        this.sprite = sprite;
        this.quadCollection = quadCollection;
    }

    public Material.Baked getSprite() {
        return sprite;
    }

    public List<BakedQuad> getQuads() {
        return quadCollection.getAll();
    }

    public enum Slate implements StringRepresentable {
        MINOR("minor", new Material(ElementalCraftApi.createRL("item/minor_rune_slate"))),
        STANDARD("standard", new Material(ElementalCraftApi.createRL("item/rune_slate"))),
        MAJOR("major", new Material(ElementalCraftApi.createRL("item/major_rune_slate")));

        public static final Codec<Slate> CODEC = StringRepresentable.fromEnum(Slate::values);

        private final String name;
        private final Material material;

        Slate(String name, Material material) {
            this.name = name;
            this.material = material;
        }

        @Nonnull
        @Override
        public String getSerializedName() {
            return this.name;
        }

        public Material getMaterial() {
            return material;
        }
    }

    public static class Unbaked implements UnbakedStandaloneModel<@NotNull RuneModel> {

        public static Codec<Function<StandaloneModelKey<@NotNull RuneModel>, Unbaked>> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Slate.CODEC.optionalFieldOf("slate", Slate.STANDARD).forGetter(u -> u.apply(null).slate),
                Material.CODEC.fieldOf("sprite").forGetter(u -> u.apply(null).sprite)
        ).apply(builder, (slate, sprite) -> key -> new Unbaked(key, slate, sprite)));

        private final StandaloneModelKey<@NotNull RuneModel> key;
        private final Slate slate;
        private final Material sprite;

        public Unbaked(StandaloneModelKey<@NotNull RuneModel> key, Slate slate, Material sprite) {
            this.key = key;
            this.slate = slate;
            this.sprite = sprite;
        }

        public static Codec<Unbaked> codec(StandaloneModelKey<@NotNull RuneModel> key) {
            return RecordCodecBuilder.create(builder -> builder.group(
                    Slate.CODEC.fieldOf("slate").forGetter(u -> u.slate),
                    Material.CODEC.fieldOf("sprite").forGetter(u -> u.sprite)
            ).apply(builder, (slate, sprite) -> new Unbaked(key, slate, sprite)));
        }

        @Override
        public void resolveDependencies(@NotNull Resolver resolver) { }

        @Override
        public RuneModel bake(ModelBaker baker) {
            var backedSprite = baker.materials().get(sprite, key::getName);
            var builder = new QuadCollection.Builder();

            builder.addAll(baker.compute(new ItemModelGenerator.ItemLayerKey(baker.materials().get(slate.getMaterial(), key::getName), BlockModelRotation.IDENTITY, 0)));
            builder.addAll(baker.compute(new ItemModelGenerator.ItemLayerKey(backedSprite, BlockModelRotation.IDENTITY, 1)));
            return new RuneModel(backedSprite, builder.build());
        }
    }
}
