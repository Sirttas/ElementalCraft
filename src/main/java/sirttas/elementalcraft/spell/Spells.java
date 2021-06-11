package sirttas.elementalcraft.spell;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.spell.air.DashSpell;
import sirttas.elementalcraft.spell.air.EnderStrikeSpell;
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

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Spells {

	@ObjectHolder(ElementalCraftApi.MODID + ":none") public static final Spell NONE = null;

	@ObjectHolder(ElementalCraftApi.MODID + ":" + GavelFallSpell.NAME) public static final GavelFallSpell GRAVEL_FALL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + StoneWallSpell.NAME) public static final StoneWallSpell STONE_WALL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + FireBallSpell.NAME) public static final FireBallSpell FIRE_BALL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + ItemPullSpell.NAME) public static final ItemPullSpell ITEM_PULL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + EnderStrikeSpell.NAME) public static final EnderStrikeSpell ENDER_STRIKE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + AnimalGrowthSpell.NAME) public static final AnimalGrowthSpell ANIMAL_GROWTH = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + TreeFallSpell.NAME) public static final TreeFallSpell TREE_FALL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + PurificationSpell.NAME) public static final PurificationSpell PURIFICATION = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + RipeningSpell.NAME) public static final RipeningSpell RIPENING = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + FlameCleaveSpell.NAME) public static final FlameCleaveSpell FLAME_CLEAVE = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + InfernoSpell.NAME) public static final InfernoSpell INFERNO = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + DashSpell.NAME) public static final DashSpell DASH = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SilkVeinSpell.NAME) public static final SilkVeinSpell SILK_VEIN = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + TranslocationSpell.NAME) public static final TranslocationSpell TRANSLOCATION = null;

	@ObjectHolder(ElementalCraftApi.MODID + ":heal") public static final EffectSpell HEAL = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":speed") public static final EffectSpell SPEED = null;

	private Spells() {}
	
	@SubscribeEvent
	public static void registerSpells(RegistryEvent.Register<Spell> event) {
		IForgeRegistry<Spell> registry = event.getRegistry();

		RegistryHelper.register(registry, new Spell(), "none");

		RegistryHelper.register(registry, new GavelFallSpell(), GavelFallSpell.NAME);
		RegistryHelper.register(registry, new StoneWallSpell(), StoneWallSpell.NAME);
		RegistryHelper.register(registry, new FireBallSpell(), FireBallSpell.NAME);
		RegistryHelper.register(registry, new ItemPullSpell(), ItemPullSpell.NAME);
		RegistryHelper.register(registry, new EnderStrikeSpell(), EnderStrikeSpell.NAME);
		RegistryHelper.register(registry, new AnimalGrowthSpell(), AnimalGrowthSpell.NAME);
		RegistryHelper.register(registry, new TreeFallSpell(), TreeFallSpell.NAME);
		RegistryHelper.register(registry, new PurificationSpell(), PurificationSpell.NAME);
		RegistryHelper.register(registry, new RipeningSpell(), RipeningSpell.NAME);
		RegistryHelper.register(registry, new FlameCleaveSpell(), FlameCleaveSpell.NAME);
		RegistryHelper.register(registry, new InfernoSpell(), InfernoSpell.NAME);
		RegistryHelper.register(registry, new DashSpell(), DashSpell.NAME);
		RegistryHelper.register(registry, new SilkVeinSpell(), SilkVeinSpell.NAME);
		RegistryHelper.register(registry, new TranslocationSpell(), TranslocationSpell.NAME);

		RegistryHelper.register(registry, new EffectSpell(new EffectInstance(Effects.HEAL, 1, 1)), "heal");
		RegistryHelper.register(registry, new EffectSpell(new EffectInstance(Effects.MOVEMENT_SPEED, 2400, 1), new EffectInstance(Effects.DIG_SPEED, 2400)), "speed");
	}

}
