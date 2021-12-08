package sirttas.elementalcraft.api.element.transfer.path;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public interface IElementTransferPath {

    boolean isValid();

    void transfer();

    default void renderDebugPath(PoseStack matrices, MultiBufferSource multiBufferSource) {}
}
