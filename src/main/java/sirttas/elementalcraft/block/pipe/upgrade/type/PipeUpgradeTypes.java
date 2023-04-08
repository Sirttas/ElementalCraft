package sirttas.elementalcraft.block.pipe.upgrade.type;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
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
import java.util.function.Supplier;

public class PipeUpgradeTypes {

    private static final Map<PipeUpgradeType<?>, Item> UPGRADE_ITEM_MAP = new Object2ObjectOpenHashMap<>();

    private static final DeferredRegister<PipeUpgradeType<?>> DEFERRED_REGISTER = DeferredRegister.create(ElementalCraft.createRL(ECNames.PIPE_UPGRADE_TYPE), ElementalCraftApi.MODID);

    public static final Supplier<IForgeRegistry<PipeUpgradeType<?>>> REGISTRY = DEFERRED_REGISTER.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<PipeUpgradeType<ElementPumpPipeUpgrade>> ELEMENT_PUMP = register(ElementPumpPipeUpgrade.NAME, ElementPumpPipeUpgrade::new);
    public static final RegistryObject<PipeUpgradeType<PipePriorityRingsPipeUpgrade>> PIPE_PRIORITY_RINGS = register(PipePriorityRingsPipeUpgrade.NAME, PipePriorityRingsPipeUpgrade::new);
    public static final RegistryObject<PipeUpgradeType<ElementValvePipeUpgrade>> ELEMENT_VALVE = register(ElementValvePipeUpgrade.NAME, ElementValvePipeUpgrade::new);
    public static final RegistryObject<PipeUpgradeType<ElementBeamPipeUpgrade>> ELEMENT_BEAM = register(ElementBeamPipeUpgrade.NAME, ElementBeamPipeUpgrade::new);

    private PipeUpgradeTypes() { }

    public static Item getUpgradeItem(PipeUpgradeType<?> type) {
        return UPGRADE_ITEM_MAP.get(type);
    }

    public static <T extends PipeUpgrade> RegistryObject<PipeUpgradeType<T>> register(String name, PipeUpgradeType.Factory<T> factory) {
        return DEFERRED_REGISTER.register(name, () -> new PipeUpgradeType<>(factory));
    }

    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

    public static void setup() {
        UPGRADE_ITEM_MAP.clear();
        ForgeRegistries.ITEMS.getValues().stream()
                .mapMulti(ElementalCraftUtils.cast(PipeUpgradeItem.class))
                .forEach(i -> UPGRADE_ITEM_MAP.put(i.getPipeUpgradeType(), i));
    }
}
