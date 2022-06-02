package sirttas.elementalcraft.spell;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.spell.air.DashSpell;
import sirttas.elementalcraft.spell.air.EnderStrikeSpell;
import sirttas.elementalcraft.spell.air.FeatherSpikesSpell;
import sirttas.elementalcraft.spell.air.ItemPullSpell;
import sirttas.elementalcraft.spell.air.TranslocationSpell;
import sirttas.elementalcraft.spell.earth.GavelFallSpell;
import sirttas.elementalcraft.spell.earth.SilkVeinSpell;
import sirttas.elementalcraft.spell.earth.StoneWallSpell;
import sirttas.elementalcraft.spell.earth.TreeFallSpell;
import sirttas.elementalcraft.spell.fire.FireBallSpell;
import sirttas.elementalcraft.spell.fire.FlameCleaveSpell;
import sirttas.elementalcraft.spell.fire.InfernoSpell;
import sirttas.elementalcraft.spell.water.AnimalGrowthSpell;
import sirttas.elementalcraft.spell.water.PurificationSpell;
import sirttas.elementalcraft.spell.water.RipeningSpell;

import java.util.function.Function;
import java.util.function.Supplier;

public class Spells {

	private static final DeferredRegister<Spell> DEFERRED_REGISTER = DeferredRegister.create(ElementalCraft.createRL(ECNames.SPELL), ElementalCraftApi.MODID);

	public static final Supplier<IForgeRegistry<Spell>> REGISTRY = RegistryHelper.makeRegistry(DEFERRED_REGISTER, Spell.class, b -> b.setDefaultKey(ElementalCraft.createRL("none")));


	public static final RegistryObject<Spell> NONE = register("none", Spell::new);

	public static final RegistryObject<GavelFallSpell> GRAVEL_FALL = register(GavelFallSpell.NAME, GavelFallSpell::new);
	public static final RegistryObject<StoneWallSpell> STONE_WALL = register(StoneWallSpell.NAME, StoneWallSpell::new);
	public static final RegistryObject<FireBallSpell> FIRE_BALL = register(FireBallSpell.NAME, FireBallSpell::new);
	public static final RegistryObject<ItemPullSpell> ITEM_PULL = register(ItemPullSpell.NAME, ItemPullSpell::new);
	public static final RegistryObject<EnderStrikeSpell> ENDER_STRIKE = register(EnderStrikeSpell.NAME, EnderStrikeSpell::new);
	public static final RegistryObject<FlameCleaveSpell> FLAME_CLEAVE = register(FlameCleaveSpell.NAME, FlameCleaveSpell::new);
	public static final RegistryObject<TranslocationSpell> TRANSLOCATION = register(TranslocationSpell.NAME, TranslocationSpell::new);
	public static final RegistryObject<DashSpell> DASH = register(DashSpell.NAME, DashSpell::new);
	public static final RegistryObject<SilkVeinSpell> SILK_VEIN = register(SilkVeinSpell.NAME, SilkVeinSpell::new);
	public static final RegistryObject<AnimalGrowthSpell> ANIMAL_GROWTH = register(AnimalGrowthSpell.NAME, AnimalGrowthSpell::new);
	public static final RegistryObject<RipeningSpell> RIPENING = register(RipeningSpell.NAME, RipeningSpell::new);
	public static final RegistryObject<PurificationSpell> PURIFICATION = register(PurificationSpell.NAME, PurificationSpell::new);
	public static final RegistryObject<FeatherSpikesSpell> FEATHER_SPIKES = register(FeatherSpikesSpell.NAME, k -> new FeatherSpikesSpell(k, 3));
	public static final RegistryObject<TreeFallSpell> TREE_FALL = register(TreeFallSpell.NAME, TreeFallSpell::new);
	public static final RegistryObject<InfernoSpell> INFERNO = register(InfernoSpell.NAME, InfernoSpell::new);
	public static final RegistryObject<EffectSpell> HEAL = register("heal", k -> new  EffectSpell(k, new MobEffectInstance(MobEffects.HEAL, 1, 1)));
	public static final RegistryObject<EffectSpell> SPEED = register("speed", k -> new  EffectSpell(k, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 1), new MobEffectInstance(MobEffects.DIG_SPEED, 2400)));
	public static final RegistryObject<AoeSpell> SHOCKWAVE = register("shockwave", AoeSpell::new);

	private Spells() {}

	private static <T extends Spell> RegistryObject<T> register(String name, Function<ResourceKey<Spell>, ? extends T> builder) {
		return DEFERRED_REGISTER.register(name, () -> builder.apply(ResourceKey.create(REGISTRY.get().getRegistryKey(), ElementalCraft.createRL(name))));
	}

	public static void register(IEventBus modBus) {
		DEFERRED_REGISTER.register(modBus);
	}
}
