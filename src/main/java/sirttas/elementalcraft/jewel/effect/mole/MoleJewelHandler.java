package sirttas.elementalcraft.jewel.effect.mole;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.Jewels;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class MoleJewelHandler {

    private MoleJewelHandler() {}

    @SubscribeEvent
    public static void onLeftClickBlock(@Nonnull PlayerInteractEvent.LeftClickBlock event) {
        var player = event.getEntity();
        var mole = Jewels.MOLE.get();
        var pos = event.getPos();
        var level = event.getLevel();

        if (JewelHelper.hasJewel(player, mole) && level.getBlockState(pos).canHarvestBlock(level, pos, player)) {
            mole.consume(player);
            mole.apply(player);
        }
    }
}
