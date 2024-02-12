package sirttas.elementalcraft.jewel;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.jewel.attack.KirinJewel;
import sirttas.elementalcraft.jewel.attack.ViperJewel;
import sirttas.elementalcraft.jewel.attribute.BearJewel;
import sirttas.elementalcraft.jewel.attribute.DolphinJewel;
import sirttas.elementalcraft.jewel.attribute.LeopardJewel;
import sirttas.elementalcraft.jewel.attribute.TigerJewel;
import sirttas.elementalcraft.jewel.defence.ArcticHaresJewel;
import sirttas.elementalcraft.jewel.defence.TortoiseJewel;
import sirttas.elementalcraft.jewel.effect.BasiliskJewel;
import sirttas.elementalcraft.jewel.effect.PhoenixJewel;
import sirttas.elementalcraft.jewel.effect.SalmonJewel;
import sirttas.elementalcraft.jewel.effect.mole.MoleJewel;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Jewels {

    private static final DeferredRegister<Jewel> DEFERRED_REGISTER = DeferredRegister.create(ElementalCraftApi.createRL(ECNames.JEWEL), ElementalCraftApi.MODID);

    public static final Registry<Jewel> REGISTRY = DEFERRED_REGISTER.makeRegistry(b -> b.defaultKey(ElementalCraftApi.createRL(ECNames.NONE)));

    public static final DeferredHolder<Jewel, Jewel> NONE = register(ECNames.NONE, () -> new Jewel(ElementType.NONE, 0));
    public static final DeferredHolder<Jewel, SalmonJewel> SALMON = register(SalmonJewel.NAME, SalmonJewel::new);
    public static final DeferredHolder<Jewel, PhoenixJewel> PHOENIX = register(PhoenixJewel.NAME, PhoenixJewel::new);
    public static final DeferredHolder<Jewel, BasiliskJewel> BASILISK = register(BasiliskJewel.NAME, BasiliskJewel::new);
    public static final DeferredHolder<Jewel, BearJewel> BEAR = register(BearJewel.NAME, BearJewel::new);
    public static final DeferredHolder<Jewel, TigerJewel> TIGER = register(TigerJewel.NAME, TigerJewel::new);
    public static final DeferredHolder<Jewel, LeopardJewel> LEOPARD = register(LeopardJewel.NAME, LeopardJewel::new);
    public static final DeferredHolder<Jewel, DolphinJewel> DOLPHIN = register(DolphinJewel.NAME, DolphinJewel::new);
    public static final DeferredHolder<Jewel, KirinJewel> KIRIN = register(KirinJewel.NAME, KirinJewel::new);
    public static final DeferredHolder<Jewel, ViperJewel> VIPER = register(ViperJewel.NAME, ViperJewel::new);
    public static final DeferredHolder<Jewel, TortoiseJewel> TORTOISE = register(TortoiseJewel.NAME, TortoiseJewel::new);
    public static final DeferredHolder<Jewel, ArcticHaresJewel> ARCTIC_HARES = register(ArcticHaresJewel.NAME, ArcticHaresJewel::new);
    public static final DeferredHolder<Jewel, MoleJewel> MOLE = register(MoleJewel.NAME, MoleJewel::new);
    public static final DeferredHolder<Jewel, HawkJewel> HAWK = register(HawkJewel.NAME, HawkJewel::new);
    public static final DeferredHolder<Jewel, DemigodJewel> DEMIGOD = register(DemigodJewel.NAME, DemigodJewel::new);
    public static final DeferredHolder<Jewel, StriderJewel> STRIDER = register("strider", () -> new StriderJewel(ElementType.FIRE, 10, FluidTags.LAVA));
    public static final DeferredHolder<Jewel, StriderJewel> WATER_STRIDER = register("water_strider", () -> new StriderJewel(ElementType.WATER, 10, FluidTags.WATER));
    public static final DeferredHolder<Jewel, PiglinJewel> PIGLIN = register("piglin", PiglinJewel::new);

    private Jewels() {}

    private static <T extends Jewel> DeferredHolder<Jewel, T> register(String name, Supplier<? extends T> builder) {
        return DEFERRED_REGISTER.register(name, builder);
    }

    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerModels(Consumer<ResourceLocation> addModel) {
        REGISTRY.holders().forEach(h -> {
            if (h.value() != NONE.value()) {
                addModel.accept(h.value().getModelName());
            }
        });
    }
}
