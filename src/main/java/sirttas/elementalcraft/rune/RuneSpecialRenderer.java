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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.client.model.ECModelResolver;
import sirttas.elementalcraft.component.ECDataComponents;

import java.util.function.Consumer;

public class RuneSpecialRenderer implements SpecialModelRenderer<@NotNull Holder<@NotNull Rune>> {

    public static final Identifier IDENTIFIER = ElementalCraftApi.RUNE_MANAGER_KEY.identifier();

    private final RuneModelResolver resolver;

    public RuneSpecialRenderer() {
        resolver = ECModelResolver.get(RuneModelResolver.IDENTIFIER);
    }

    @Override
    public void submit(@Nullable Holder<@NotNull Rune> argument, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        if (argument == null) {
            return;
        }
        var model = resolver.getModel(argument.getKey().identifier());

        submitNodeCollector.submitItem(poseStack, ItemDisplayContext.NONE, lightCoords, overlayCoords, outlineColor, new int[0], model.getQuads(), hasFoil ? ItemStackRenderState.FoilType.STANDARD : ItemStackRenderState.FoilType.NONE);
    }

    @Override
    public @Nullable Holder<@NotNull Rune> extractArgument(@NotNull ItemStack stack) {
        return stack.get(ECDataComponents.RUNE);
    }

    @Override
    public void getExtents(@NotNull Consumer output) {

    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked<@NotNull Holder<@NotNull Rune>> {

        private static final Unbaked INSTANCE = new Unbaked();

        private Unbaked() {}

        public static final MapCodec<RuneSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public @NotNull RuneSpecialRenderer bake(@NotNull BakingContext context) {
            return new RuneSpecialRenderer();
        }

        @Override
        public @NotNull MapCodec<? extends RuneSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
