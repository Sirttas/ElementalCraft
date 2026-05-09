package sirttas.elementalcraft.spell;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
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
import sirttas.elementalcraft.spell.fire.LightSpell;
import sirttas.elementalcraft.spell.flamecleave.FlameCleaveSpell;
import sirttas.elementalcraft.spell.repair.RepairSpell;
import sirttas.elementalcraft.spell.water.AnimalGrowthSpell;
import sirttas.elementalcraft.spell.water.PurificationSpell;
import sirttas.elementalcraft.spell.water.RipeningSpell;

import java.util.function.Function;

public class Spells {

	public static final ResourceKey<@NotNull Registry<@NotNull Spell>> REGISTRY_KEY = ResourceKey.createRegistryKey(ElementalCraftApi.identifier(ECNames.SPELL));
	private static final DeferredRegister<@NotNull Spell> DEFERRED_REGISTER = DeferredRegister.create(ElementalCraftApi.identifier(ECNames.SPELL), ElementalCraftApi.MODID);

	public static final Registry<@NotNull Spell> REGISTRY = DEFERRED_REGISTER.makeRegistry(b -> b.sync(true).defaultKey(ElementalCraftApi.identifier("none")));


	public static final DeferredHolder<@NotNull Spell, @NotNull Spell> NONE = register("none", Spell::new);

	public static final DeferredHolder<@NotNull Spell, @NotNull GavelFallSpell> GRAVEL_FALL = register(GavelFallSpell.NAME, GavelFallSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull StoneWallSpell> STONE_WALL = register(StoneWallSpell.NAME, StoneWallSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull FireBallSpell> FIRE_BALL = register(FireBallSpell.NAME, FireBallSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull ItemPullSpell> ITEM_PULL = register(ItemPullSpell.NAME, ItemPullSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull EnderStrikeSpell> ENDER_STRIKE = register(EnderStrikeSpell.NAME, EnderStrikeSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull FlameCleaveSpell> FLAME_CLEAVE = register(FlameCleaveSpell.NAME, FlameCleaveSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull TranslocationSpell> TRANSLOCATION = register(TranslocationSpell.NAME, TranslocationSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull DashSpell> DASH = register(DashSpell.NAME, DashSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull SilkVeinSpell> SILK_VEIN = register(SilkVeinSpell.NAME, SilkVeinSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull AnimalGrowthSpell> ANIMAL_GROWTH = register(AnimalGrowthSpell.NAME, AnimalGrowthSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull RipeningSpell> RIPENING = register(RipeningSpell.NAME, RipeningSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull PurificationSpell> PURIFICATION = register(PurificationSpell.NAME, PurificationSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull FeatherSpikesSpell> FEATHER_SPIKES = register(FeatherSpikesSpell.NAME, k -> new FeatherSpikesSpell(k, 3));
	public static final DeferredHolder<@NotNull Spell, @NotNull TreeFallSpell> TREE_FALL = register(TreeFallSpell.NAME, TreeFallSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull InfernoSpell> INFERNO = register(InfernoSpell.NAME, InfernoSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull EffectSpell> HEAL = register("heal", k -> new  EffectSpell(k, new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, 1)));
	public static final DeferredHolder<@NotNull Spell, @NotNull EffectSpell> SPEED = register("speed", k -> new  EffectSpell(k, new MobEffectInstance(MobEffects.SPEED, 2400, 1), new MobEffectInstance(MobEffects.HASTE, 2400)));
	public static final DeferredHolder<@NotNull Spell, @NotNull AoeSpell> SHOCKWAVE = register("shockwave", AoeSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull AirShieldSpell> AIR_SHIELD = register(AirShieldSpell.NAME, AirShieldSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull RepairSpell> REPAIR = register(RepairSpell.NAME, RepairSpell::new);
	public static final DeferredHolder<@NotNull Spell, @NotNull LightSpell> LIGHT = register(LightSpell.NAME, LightSpell::new);

	private Spells() {}

	private static <T extends Spell> DeferredHolder<@NotNull Spell, @NotNull T> register(String name, Function<ResourceKey<@NotNull Spell>, ? extends T> builder) {
		return DEFERRED_REGISTER.register(name, () -> builder.apply(ResourceKey.create(REGISTRY_KEY, ElementalCraftApi.identifier(name))));
	}

	public static void register(IEventBus modBus) {
		DEFERRED_REGISTER.register(modBus);
	}
}
