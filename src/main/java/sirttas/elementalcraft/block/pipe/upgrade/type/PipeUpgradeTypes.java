package sirttas.elementalcraft.block.pipe.upgrade.type;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.function.Consumers;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.beam.ElementBeamPipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.priority.PipePriorityRingsPipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.pump.ElementPumpPipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.valve.ElementValvePipeUpgrade;
import sirttas.elementalcraft.item.pipe.PipeUpgradeItem;

import java.util.Map;

public class PipeUpgradeTypes {

    private static final Map<PipeUpgradeType<?>, Item> UPGRADE_ITEM_MAP = new Object2ObjectOpenHashMap<>();

    private static final DeferredRegister<PipeUpgradeType<?>> DEFERRED_REGISTER = DeferredRegister.create(ElementalCraftApi.createRL(ECNames.PIPE_UPGRADE_TYPE), ElementalCraftApi.MODID);

    public static final Registry<PipeUpgradeType<?>> REGISTRY = DEFERRED_REGISTER.makeRegistry(Consumers.nop());

    public static final DeferredHolder<PipeUpgradeType<?>, PipeUpgradeType<ElementPumpPipeUpgrade>> ELEMENT_PUMP = register(ElementPumpPipeUpgrade.NAME, ElementPumpPipeUpgrade::new);
    public static final DeferredHolder<PipeUpgradeType<?>, PipeUpgradeType<PipePriorityRingsPipeUpgrade>> PIPE_PRIORITY_RINGS = register(PipePriorityRingsPipeUpgrade.NAME, PipePriorityRingsPipeUpgrade::new);
    public static final DeferredHolder<PipeUpgradeType<?>, PipeUpgradeType<ElementValvePipeUpgrade>> ELEMENT_VALVE = register(ElementValvePipeUpgrade.NAME, ElementValvePipeUpgrade::new);
    public static final DeferredHolder<PipeUpgradeType<?>, PipeUpgradeType<ElementBeamPipeUpgrade>> ELEMENT_BEAM = register(ElementBeamPipeUpgrade.NAME, ElementBeamPipeUpgrade::new);

    private PipeUpgradeTypes() { }

    public static Item getUpgradeItem(PipeUpgradeType<?> type) {
        return UPGRADE_ITEM_MAP.get(type);
    }

    public static <T extends PipeUpgrade> DeferredHolder<PipeUpgradeType<?>, PipeUpgradeType<T>> register(String name, PipeUpgradeType.Factory<T> factory) {
        return DEFERRED_REGISTER.register(name, () -> new PipeUpgradeType<>(factory));
    }

    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

    public static void setup() {
        UPGRADE_ITEM_MAP.clear();
        BuiltInRegistries.ITEM.stream()
                .mapMulti(ElementalCraftUtils.cast(PipeUpgradeItem.class))
                .forEach(i -> UPGRADE_ITEM_MAP.put(i.getPipeUpgradeType(), i));
    }
}
