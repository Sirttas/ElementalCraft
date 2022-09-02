package sirttas.elementalcraft.jewel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.ElementalCraft;
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

    private static final DeferredRegister<Jewel> DEFERRED_REGISTER = DeferredRegister.create(ElementalCraft.createRL(ECNames.JEWEL), ElementalCraftApi.MODID);

    public static final Supplier<IForgeRegistry<Jewel>> REGISTRY = DEFERRED_REGISTER.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<SalmonJewel> SALMON = register(SalmonJewel.NAME, SalmonJewel::new);
    public static final RegistryObject<PhoenixJewel> PHOENIX = register(PhoenixJewel.NAME, PhoenixJewel::new);
    public static final RegistryObject<BasiliskJewel> BASILISK = register(BasiliskJewel.NAME, BasiliskJewel::new);
    public static final RegistryObject<BearJewel> BEAR = register(BearJewel.NAME, BearJewel::new);
    public static final RegistryObject<TigerJewel> TIGER = register(TigerJewel.NAME, TigerJewel::new);
    public static final RegistryObject<LeopardJewel> LEOPARD = register(LeopardJewel.NAME, LeopardJewel::new);
    public static final RegistryObject<DolphinJewel> DOLPHIN = register(DolphinJewel.NAME, DolphinJewel::new);
    public static final RegistryObject<KirinJewel> KIRIN = register(KirinJewel.NAME, KirinJewel::new);
    public static final RegistryObject<ViperJewel> VIPER = register(ViperJewel.NAME, ViperJewel::new);
    public static final RegistryObject<TortoiseJewel> TORTOISE = register(TortoiseJewel.NAME, TortoiseJewel::new);
    public static final RegistryObject<ArcticHaresJewel> ARCTIC_HARES = register(ArcticHaresJewel.NAME, ArcticHaresJewel::new);
    public static final RegistryObject<MoleJewel> MOLE = register(MoleJewel.NAME, MoleJewel::new);
    public static final RegistryObject<HawkJewel> HAWK = register(HawkJewel.NAME, HawkJewel::new);
    public static final RegistryObject<DemigodJewel> DEMIGOD = register(DemigodJewel.NAME, DemigodJewel::new);
    public static final RegistryObject<StriderJewel> STRIDER = register("strider", () -> new StriderJewel(ElementType.FIRE, 10, FluidTags.LAVA));
    public static final RegistryObject<StriderJewel> WATER_STRIDER = register("water_strider", () -> new StriderJewel(ElementType.WATER, 10, FluidTags.WATER));
    public static final RegistryObject<PiglinJewel> PIGLIN = register("piglin", PiglinJewel::new);

    private Jewels() {}

    private static <T extends Jewel> RegistryObject<T> register(String name, Supplier<? extends T> builder) {
        return DEFERRED_REGISTER.register(name, builder);
    }

    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerModels(Consumer<ResourceLocation> addModel) {
        REGISTRY.get().getValues().forEach(jewel -> addModel.accept(jewel.getModelName()));
    }
}
