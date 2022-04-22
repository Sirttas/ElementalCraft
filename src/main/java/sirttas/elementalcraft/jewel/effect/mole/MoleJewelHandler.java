package sirttas.elementalcraft.jewel.effect.mole;

import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.Jewels;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class MoleJewelHandler {

    private MoleJewelHandler() {}

    @SubscribeEvent
    public static void onLeftClickBlock(@Nonnull PlayerInteractEvent.LeftClickBlock event) {
        var player = event.getPlayer();

        if (JewelHelper.hasJewel(player, Jewels.MOLE) &&ForgeHooks.isCorrectToolForDrops(player.level.getBlockState(event.getPos()), player)) {
            Jewels.MOLE.consume(player);
            Jewels.MOLE.apply(player);
        }
    }
}
