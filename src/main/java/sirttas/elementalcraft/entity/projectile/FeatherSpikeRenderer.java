package sirttas.elementalcraft.entity.projectile;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;

import javax.annotation.Nonnull;

public class FeatherSpikeRenderer extends ArrowRenderer<FeatherSpike> {

    public static final ResourceLocation SPIKE = ElementalCraft.createRL("textures/entity/feather_spike.png");

    public FeatherSpikeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull FeatherSpike entity) {
        return SPIKE;
    }
}
