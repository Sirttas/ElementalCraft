package sirttas.elementalcraft.interaction.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import sirttas.elementalcraft.api.ElementalCraftInteraction;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public class CuriosInteractions implements ElementalCraftInteraction {

    @Override
    public boolean isActive() {
        return ModList.get().isLoaded("curios");
    }

    @Override
    public List<ItemStack> getHolders(LivingEntity entity) {
        return CuriosApi.getCuriosInventory(entity)
                .map(inv -> inv.findCurios("element_holder"))
                .stream()
                .<SlotResult>mapMulti(List::forEach)
                .map(SlotResult::stack)
                .filter(s -> !s.isEmpty())
                .toList();
    }

}
