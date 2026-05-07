package sirttas.elementalcraft.rune;

import com.mojang.math.Transformation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.cuboid.ItemModelGenerator;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.client.model.ComposedModelState;
import net.neoforged.neoforge.client.model.standalone.UnbakedStandaloneModel;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.NonNull;
import sirttas.elementalcraft.api.ElementalCraftApi;

import javax.annotation.Nonnull;
import java.util.List;

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

    public record Unbaked(Slate slate, Material sprite) implements UnbakedStandaloneModel<@NotNull RuneModel> {

        private static final Transformation OVERLAY_TRANSFORM = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1.002F, 1.002F, 1.002F), new Quaternionf());
        private static final ModelState OVERLAY_STATE = new ComposedModelState(BlockModelRotation.IDENTITY, OVERLAY_TRANSFORM);

        public static Codec<Unbaked> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Slate.CODEC.optionalFieldOf("slate", Slate.STANDARD).forGetter(u -> u.slate),
                Material.CODEC.fieldOf("sprite").forGetter(u -> u.sprite)
        ).apply(builder, Unbaked::new));

        @Override
        public @NonNull RuneModel bake(ModelBaker baker, @NotNull ModelDebugName name) {
            var backedSprite = baker.materials().get(sprite, name);
            var builder = new QuadCollection.Builder();

            builder.addAll(baker.compute(new ItemModelGenerator.ItemLayerKey(baker.materials().get(slate.getMaterial(), name), BlockModelRotation.IDENTITY, 0)));
            builder.addAll(baker.compute(new ItemModelGenerator.ItemLayerKey(backedSprite, OVERLAY_STATE, 1)));
            return new RuneModel(backedSprite, builder.build());
        }

        @Override
        public void resolveDependencies(@NotNull Resolver resolver) {
        }
    }
}
