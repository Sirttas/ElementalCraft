package sirttas.elementalcraft.spell;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.spell.air.DashSpell;
import sirttas.elementalcraft.spell.air.EnderStrikeSpell;
import sirttas.elementalcraft.spell.air.FeatherSpikesSpell;
import sirttas.elementalcraft.spell.air.ItemPullSpell;
import sirttas.elementalcraft.spell.air.TranslocationSpell;
import sirttas.elementalcraft.spell.airshield.AirShieldSpell;
import sirttas.elementalcraft.spell.earth.GavelFallSpell;
import sirttas.elementalcraft.spell.earth.SilkVeinSpell;
import sirttas.elementalcraft.spell.earth.StoneWallSpell;
import sirttas.elementalcraft.spell.earth.TreeFallSpell;
import sirttas.elementalcraft.spell.fire.FireBallSpell;
import sirttas.elementalcraft.spell.fire.InfernoSpell;
import sirttas.elementalcraft.spell.flamecleave.FlameCleaveSpell;
import sirttas.elementalcraft.spell.repair.RepairSpell;
import sirttas.elementalcraft.spell.water.AnimalGrowthSpell;
import sirttas.elementalcraft.spell.water.PurificationSpell;
import sirttas.elementalcraft.spell.water.RipeningSpell;

import java.util.function.Function;

public class Spells {

	public static final ResourceKey<Registry<Spell>> KEY = ResourceKey.createRegistryKey(ElementalCraftApi.createRL(ECNames.SPELL));
	private static final DeferredRegister<Spell> DEFERRED_REGISTER = DeferredRegister.create(ElementalCraftApi.createRL(ECNames.SPELL), ElementalCraftApi.MODID);

	public static final Registry<Spell> REGISTRY = DEFERRED_REGISTER.makeRegistry(b -> b.defaultKey(ElementalCraftApi.createRL("none")));


	public static final DeferredHolder<Spell, Spell> NONE = register("none", Spell::new);

	public static final DeferredHolder<Spell, GavelFallSpell> GRAVEL_FALL = register(GavelFallSpell.NAME, GavelFallSpell::new);
	public static final DeferredHolder<Spell, StoneWallSpell> STONE_WALL = register(StoneWallSpell.NAME, StoneWallSpell::new);
	public static final DeferredHolder<Spell, FireBallSpell> FIRE_BALL = register(FireBallSpell.NAME, FireBallSpell::new);
	public static final DeferredHolder<Spell, ItemPullSpell> ITEM_PULL = register(ItemPullSpell.NAME, ItemPullSpell::new);
	public static final DeferredHolder<Spell, EnderStrikeSpell> ENDER_STRIKE = register(EnderStrikeSpell.NAME, EnderStrikeSpell::new);
	public static final DeferredHolder<Spell, FlameCleaveSpell> FLAME_CLEAVE = register(FlameCleaveSpell.NAME, FlameCleaveSpell::new);
	public static final DeferredHolder<Spell, TranslocationSpell> TRANSLOCATION = register(TranslocationSpell.NAME, TranslocationSpell::new);
	public static final DeferredHolder<Spell, DashSpell> DASH = register(DashSpell.NAME, DashSpell::new);
	public static final DeferredHolder<Spell, SilkVeinSpell> SILK_VEIN = register(SilkVeinSpell.NAME, SilkVeinSpell::new);
	public static final DeferredHolder<Spell, AnimalGrowthSpell> ANIMAL_GROWTH = register(AnimalGrowthSpell.NAME, AnimalGrowthSpell::new);
	public static final DeferredHolder<Spell, RipeningSpell> RIPENING = register(RipeningSpell.NAME, RipeningSpell::new);
	public static final DeferredHolder<Spell, PurificationSpell> PURIFICATION = register(PurificationSpell.NAME, PurificationSpell::new);
	public static final DeferredHolder<Spell, FeatherSpikesSpell> FEATHER_SPIKES = register(FeatherSpikesSpell.NAME, k -> new FeatherSpikesSpell(k, 3));
	public static final DeferredHolder<Spell, TreeFallSpell> TREE_FALL = register(TreeFallSpell.NAME, TreeFallSpell::new);
	public static final DeferredHolder<Spell, InfernoSpell> INFERNO = register(InfernoSpell.NAME, InfernoSpell::new);
	public static final DeferredHolder<Spell, EffectSpell> HEAL = register("heal", k -> new  EffectSpell(k, new MobEffectInstance(MobEffects.HEAL, 1, 1)));
	public static final DeferredHolder<Spell, EffectSpell> SPEED = register("speed", k -> new  EffectSpell(k, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 1), new MobEffectInstance(MobEffects.DIG_SPEED, 2400)));
	public static final DeferredHolder<Spell, AoeSpell> SHOCKWAVE = register("shockwave", AoeSpell::new);
	public static final DeferredHolder<Spell, AirShieldSpell> AIR_SHIELD = register(AirShieldSpell.NAME, AirShieldSpell::new);
	public static final DeferredHolder<Spell, RepairSpell> REPAIR = register(RepairSpell.NAME, RepairSpell::new);

	private Spells() {}

	private static <T extends Spell> DeferredHolder<Spell, T> register(String name, Function<ResourceKey<Spell>, ? extends T> builder) {
		return DEFERRED_REGISTER.register(name, () -> builder.apply(ResourceKey.create(KEY, ElementalCraftApi.createRL(name))));
	}

	public static void register(IEventBus modBus) {
		DEFERRED_REGISTER.register(modBus);
	}
}
