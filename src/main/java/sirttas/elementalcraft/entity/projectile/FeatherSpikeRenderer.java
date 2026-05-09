package sirttas.elementalcraft.entity.projectile;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class FeatherSpikeRenderer extends ArrowRenderer<@NotNull FeatherSpike, @NotNull ArrowRenderState> {

    public static final Identifier SPIKE = ElementalCraftApi.identifier("textures/entity/feather_spike.png");

    public FeatherSpikeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    protected @NotNull Identifier getTextureLocation(ArrowRenderState state) {
        return SPIKE;
    }
}
