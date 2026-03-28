package sirttas.elementalcraft.item;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.air.AirMillGrindstoneBlock;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.air.AirMillWoodSawBlock;
import sirttas.elementalcraft.block.synthesizer.mill.AirMillSynthesizerBlock;
import sirttas.elementalcraft.item.holder.ElementHolderItem;
import sirttas.elementalcraft.item.rune.RuneItem;
import sirttas.elementalcraft.pureore.display.PureOreDisplayManager;
import sirttas.elementalcraft.rune.RuneModel;
import sirttas.elementalcraft.spell.SpellHelper;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class ItemModelHandler {

    private ItemModelHandler() {}

    @SubscribeEvent
    public static void replaceModels(ModelEvent.ModifyBakingResult event) {
        var modelRegistry = event.getModels();

        replaceModels(modelRegistry, RuneItem.NAME, RuneModel::new);
        replaceModels(modelRegistry, AirMillSynthesizerBlock.NAME, AirMillBlockItemModel::new);
        replaceModels(modelRegistry, AirMillGrindstoneBlock.NAME, AirMillBlockItemModel::new);
        replaceModels(modelRegistry, AirMillWoodSawBlock.NAME, AirMillBlockItemModel::new);
    }

    private static void replaceModels(Map<ModelIdentifier, BakedModel> modelRegistry, String name, UnaryOperator<BakedModel> modelFactory) {
        replaceModels(modelRegistry, name, (k, v) -> modelFactory.apply(v));
    }

    private static void replaceModels(Map<ModelIdentifier, BakedModel> modelRegistry, String name, BiFunction<ModelIdentifier, BakedModel, BakedModel> modelFactory) {
        modelRegistry.computeIfPresent(ModelIdentifier.inventory(ElementalCraftApi.createRL(name)), modelFactory);
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(ECItems.PURE_ORE.get(), (s, l) -> {
            var colors = PureOreDisplayManager.getInstance().getColors(s);

            return colors != null && l < colors.length ? colors[l] : -1;
        });
        event.register((s, l) -> l == 0 ? -1 : FastColor.ARGB32.opaque(SpellHelper.getSpell(s).value().getColor()), ECItems.SCROLL.get());
        event.register((s, l) -> l == 0 ? -1 : FastColor.ARGB32.opaque(((ElementHolderItem) s.getItem()).getElementType().getColor()), ECItems.FIRE_HOLDER.get(), ECItems.WATER_HOLDER.get(), ECItems.EARTH_HOLDER.get(), ECItems.AIR_HOLDER.get()); // TODO create icon for each holder
    }
}
