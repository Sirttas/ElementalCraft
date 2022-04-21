package sirttas.elementalcraft.jewel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.jewel.attack.KirinJewel;
import sirttas.elementalcraft.jewel.attack.ViperJewel;
import sirttas.elementalcraft.jewel.attribute.BearJewel;
import sirttas.elementalcraft.jewel.attribute.LeopardJewel;
import sirttas.elementalcraft.jewel.attribute.TigerJewel;
import sirttas.elementalcraft.jewel.defence.ArcticHaresJewel;
import sirttas.elementalcraft.jewel.defence.TortoiseJewel;
import sirttas.elementalcraft.jewel.effect.BasiliskJewel;
import sirttas.elementalcraft.jewel.effect.PhoenixJewel;
import sirttas.elementalcraft.jewel.effect.SalmonJewel;
import sirttas.elementalcraft.jewel.effect.mole.MoleJewel;
import sirttas.elementalcraft.registry.RegistryHelper;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Jewels {

    @ObjectHolder(ElementalCraftApi.MODID + ":" + SalmonJewel.NAME) public static final SalmonJewel SALMON = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":" + LeopardJewel.DolphinJewel.NAME) public static final LeopardJewel.DolphinJewel DOLPHIN = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":" + LeopardJewel.NAME) public static final LeopardJewel LEOPARD = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":" + PhoenixJewel.NAME) public static final PhoenixJewel PHOENIX = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":" + TortoiseJewel.NAME) public static final TortoiseJewel TORTOISE = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":" + DemigodJewel.NAME) public static final DemigodJewel DEMIGOD = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":" + MoleJewel.NAME) public static final MoleJewel MOLE = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":" + TigerJewel.NAME) public static final TigerJewel TIGER = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":" + BearJewel.NAME) public static final BearJewel BEAR = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":" + ViperJewel.NAME) public static final ViperJewel VIPER = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":" + HawkJewel.NAME) public static final HawkJewel HAWK = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":" + KirinJewel.NAME) public static final KirinJewel KIRIN = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":" + ArcticHaresJewel.NAME) public static final ArcticHaresJewel ARCTIC_HARES = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":strider") public static final StriderJewel STRIDER = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":water_strider") public static final StriderJewel WATER_STRIDER = null;
    @ObjectHolder(ElementalCraftApi.MODID + ":" + BasiliskJewel.NAME) public static final BasiliskJewel BASILISK = null;


    private Jewels() {}

    @SubscribeEvent
    public static void registerJewels(RegistryEvent.Register<Jewel> event) {
        IForgeRegistry<Jewel> registry = event.getRegistry();

        RegistryHelper.register(registry, new SalmonJewel(), SalmonJewel.NAME);
        RegistryHelper.register(registry, new LeopardJewel.DolphinJewel(), LeopardJewel.DolphinJewel.NAME);
        RegistryHelper.register(registry, new LeopardJewel(), LeopardJewel.NAME);
        RegistryHelper.register(registry, new PhoenixJewel(), PhoenixJewel.NAME);
        RegistryHelper.register(registry, new TortoiseJewel(), TortoiseJewel.NAME);
        RegistryHelper.register(registry, new DemigodJewel(), DemigodJewel.NAME);
        RegistryHelper.register(registry, new MoleJewel(), MoleJewel.NAME);
        RegistryHelper.register(registry, new TigerJewel(), TigerJewel.NAME);
        RegistryHelper.register(registry, new BearJewel(), BearJewel.NAME);
        RegistryHelper.register(registry, new ViperJewel(), ViperJewel.NAME);
        RegistryHelper.register(registry, new HawkJewel(), HawkJewel.NAME);
        RegistryHelper.register(registry, new KirinJewel(), KirinJewel.NAME);
        RegistryHelper.register(registry, new ArcticHaresJewel(), ArcticHaresJewel.NAME);
        RegistryHelper.register(registry, new StriderJewel(ElementType.FIRE, 10, FluidTags.LAVA), "strider");
        RegistryHelper.register(registry, new StriderJewel(ElementType.WATER, 10, FluidTags.WATER), "water_strider");
        RegistryHelper.register(registry, new BasiliskJewel(), BasiliskJewel.NAME);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerModels(Consumer<ResourceLocation> addModel) {
        Jewel.REGISTRY.getValues().forEach(jewel -> addModel.accept(jewel.getModelName()));
    }
}
