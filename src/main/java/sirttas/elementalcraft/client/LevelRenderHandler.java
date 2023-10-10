package sirttas.elementalcraft.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import sirttas.elementalcraft.api.ElementalCraftApi;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class LevelRenderHandler {

    private static Matrix4f worldMatrix;

    private LevelRenderHandler() {}

    @SubscribeEvent
    public static void onLevelRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
            worldMatrix = new Matrix4f(event.getProjectionMatrix());
            worldMatrix.mul(event.getPoseStack().last().pose());
        }
    }

    public static Matrix4f getWorldMatrix() {
        return worldMatrix;
    }

}
