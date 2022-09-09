package sirttas.elementalcraft.client;

import com.mojang.math.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class LevelRenderHandler {

    private static Matrix4f worldMatrix;

    private LevelRenderHandler() {}

    @SubscribeEvent
    public static void onLevelRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
            worldMatrix = event.getProjectionMatrix().copy();
            worldMatrix.multiply(event.getPoseStack().last().pose());
        }
    }

    public static Matrix4f getWorldMatrix() {
        return worldMatrix;
    }

}
