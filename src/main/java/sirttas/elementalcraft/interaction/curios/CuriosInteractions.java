package sirttas.elementalcraft.interaction.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypeMessage;

import java.util.List;

public class CuriosInteractions {

    private CuriosInteractions() {}

    public static void registerSlots() {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder(CuriosConstants.ELEMENT_HOLDER_SLOT).icon(CuriosConstants.EMPTY_ELEMENT_HOLDER_SLOT).size(4).build());
    }

    public static List<ItemStack> getHolders(LivingEntity entity) {
        return CuriosApi.getCuriosHelper().findCurios(entity, CuriosConstants.ELEMENT_HOLDER_SLOT).stream()
                .map(SlotResult::stack)
                .filter(s -> !s.isEmpty())
                .toList();
    }

}
