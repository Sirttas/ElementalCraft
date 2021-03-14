package sirttas.elementalcraft.spell;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.spell.air.SpellDash;
import sirttas.elementalcraft.spell.air.SpellEnderStrike;
import sirttas.elementalcraft.spell.air.SpellItemPull;
import sirttas.elementalcraft.spell.earth.SpellGavelFall;
import sirttas.elementalcraft.spell.earth.SpellSilkVein;
import sirttas.elementalcraft.spell.earth.SpellStoneWall;
import sirttas.elementalcraft.spell.earth.SpellTreeFall;
import sirttas.elementalcraft.spell.fire.SpellFireBall;
import sirttas.elementalcraft.spell.fire.SpellFlameCleave;
import sirttas.elementalcraft.spell.fire.SpellInferno;
import sirttas.elementalcraft.spell.water.SpellAnimalGrowth;
import sirttas.elementalcraft.spell.water.SpellPurification;
import sirttas.elementalcraft.spell.water.SpellRipening;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Spells {

	@ObjectHolder(ElementalCraft.MODID + ":none") public static final Spell NONE = null;

	@ObjectHolder(ElementalCraft.MODID + ":" + SpellGavelFall.NAME) public static final SpellGavelFall GRAVEL_FALL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellStoneWall.NAME) public static final SpellStoneWall STONE_WALL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellFireBall.NAME) public static final SpellFireBall FIRE_BALL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellItemPull.NAME) public static final SpellItemPull ITEM_PULL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellEnderStrike.NAME) public static final SpellEnderStrike ENDER_STRIKE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellAnimalGrowth.NAME) public static final SpellAnimalGrowth ANIMAL_GROWTH = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellTreeFall.NAME) public static final SpellTreeFall TREE_FALL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellPurification.NAME) public static final SpellPurification PURIFICATION = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellRipening.NAME) public static final SpellRipening RIPENING = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellFlameCleave.NAME) public static final SpellFlameCleave FLAME_CLEAVE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellInferno.NAME) public static final SpellInferno INFERNO = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellDash.NAME) public static final SpellDash DASH = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellSilkVein.NAME) public static final SpellSilkVein SILK_VEIN = null;

	@ObjectHolder(ElementalCraft.MODID + ":heal") public static final EffectSpell HEAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":speed") public static final EffectSpell SPEED = null;

	private Spells() {}
	
	@SubscribeEvent
	public static void registerSpells(RegistryEvent.Register<Spell> event) {
		IForgeRegistry<Spell> registry = event.getRegistry();

		RegistryHelper.register(registry, new Spell(), "none");

		RegistryHelper.register(registry, new SpellGavelFall(), SpellGavelFall.NAME);
		RegistryHelper.register(registry, new SpellStoneWall(), SpellStoneWall.NAME);
		RegistryHelper.register(registry, new SpellFireBall(), SpellFireBall.NAME);
		RegistryHelper.register(registry, new SpellItemPull(), SpellItemPull.NAME);
		RegistryHelper.register(registry, new SpellEnderStrike(), SpellEnderStrike.NAME);
		RegistryHelper.register(registry, new SpellAnimalGrowth(), SpellAnimalGrowth.NAME);
		RegistryHelper.register(registry, new SpellTreeFall(), SpellTreeFall.NAME);
		RegistryHelper.register(registry, new SpellPurification(), SpellPurification.NAME);
		RegistryHelper.register(registry, new SpellRipening(), SpellRipening.NAME);
		RegistryHelper.register(registry, new SpellFlameCleave(), SpellFlameCleave.NAME);
		RegistryHelper.register(registry, new SpellInferno(), SpellInferno.NAME);
		RegistryHelper.register(registry, new SpellDash(), SpellDash.NAME);
		RegistryHelper.register(registry, new SpellSilkVein(), SpellSilkVein.NAME);

		RegistryHelper.register(registry, new EffectSpell(new EffectInstance(Effects.INSTANT_HEALTH, 1, 1)), "heal");
		RegistryHelper.register(registry, new EffectSpell(new EffectInstance(Effects.SPEED, 2400, 1), new EffectInstance(Effects.HASTE, 2400)), "speed");
	}

}
