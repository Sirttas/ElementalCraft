package sirttas.elementalcraft.client.renderer.gui.pip;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.jspecify.annotations.Nullable;

public record GuiBlockRenderState(
        BlockModelRenderState blockModelRenderState,
        int x0,
        int y0,
        int x1,
        int y1,
        float scale,
        @Nullable ScreenRectangle scissorArea,
        @Nullable ScreenRectangle bounds
) implements PictureInPictureRenderState {

    public GuiBlockRenderState(BlockModelRenderState renderState, int x0, int y0, int x1, int y1, float scale, @Nullable ScreenRectangle scissorArea) {
        this(renderState, x0, y0, x1, y1, scale, scissorArea, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
    }
}
