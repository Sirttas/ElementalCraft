package sirttas.elementalcraft.interaction.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public class CuriosInteractions {

    private CuriosInteractions() {}

    public static List<ItemStack> getHolders(LivingEntity entity) {
        return CuriosApi.getCuriosInventory(entity)
                .map(inv -> inv.findCurios(CuriosConstants.ELEMENT_HOLDER_SLOT))
                .stream()
                .<SlotResult>mapMulti(List::forEach)
                .map(SlotResult::stack)
                .filter(s -> !s.isEmpty())
                .toList();
    }

}
