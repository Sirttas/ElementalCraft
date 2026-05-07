package sirttas.elementalcraft.rune;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.client.model.ECModelResolver;
import sirttas.elementalcraft.component.ECDataComponents;

import java.util.function.Consumer;

public class RuneSpecialRenderer implements SpecialModelRenderer<Holder<Rune>> {

    public static final Identifier IDENTIFIER = ElementalCraftApi.RUNE_MANAGER_KEY.identifier();

    private final RuneModelResolver resolver;

    public RuneSpecialRenderer() {
        resolver = ECModelResolver.get(RuneModelResolver.IDENTIFIER);
    }

    @Override
    public void submit(@Nullable Holder<Rune> argument, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        if (argument == null) {
            return;
        }
        var model = resolver.getModel(argument.getKey().identifier());

        submitNodeCollector.submitItem(poseStack, ItemDisplayContext.NONE, lightCoords, overlayCoords, outlineColor, new int[0], model.getQuads(), hasFoil ? ItemStackRenderState.FoilType.STANDARD : ItemStackRenderState.FoilType.NONE);
    }

    @Override
    public @Nullable Holder<Rune> extractArgument(ItemStack stack) {
        return stack.get(ECDataComponents.RUNE);
    }

    @Override
    public void getExtents(Consumer output) {

    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked<Holder<Rune>> {

        private static final Unbaked INSTANCE = new Unbaked();
        public static final MapCodec<RuneSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        private Unbaked() {}

        public static Unbaked get() {
            return INSTANCE;
        }

        @Override
        public RuneSpecialRenderer bake(BakingContext context) {
            return new RuneSpecialRenderer();
        }

        @Override
        public MapCodec<? extends RuneSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
