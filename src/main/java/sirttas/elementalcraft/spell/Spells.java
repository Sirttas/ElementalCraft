package sirttas.elementalcraft.spell;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryBuilder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.spell.Spell.Properties;
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

	private static final ResourceLocation NAME = ElementalCraft.createRL("spell");
	private static final int MIN_SPELL_ID = 0;
	private static final int MAX_SPELL_ID = Short.MAX_VALUE - 1;

	@ObjectHolder(ElementalCraft.MODID + ":none") public static Spell none;

	@ObjectHolder(ElementalCraft.MODID + ":" + SpellGavelFall.NAME) public static SpellGavelFall gravelFall;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellStoneWall.NAME) public static SpellStoneWall stoneWall;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellFireBall.NAME) public static SpellFireBall fireBall;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellItemPull.NAME) public static SpellItemPull itemPull;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellEnderStrike.NAME) public static SpellEnderStrike enderStrike;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellAnimalGrowth.NAME) public static SpellAnimalGrowth animalGrowth;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellTreeFall.NAME) public static SpellTreeFall treeFall;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellPurification.NAME) public static SpellPurification purification;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellRipening.NAME) public static SpellRipening ripening;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellFlameCleave.NAME) public static SpellFlameCleave flameCleave;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellInferno.NAME) public static SpellInferno inferno;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellDash.NAME) public static SpellDash dash;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellSilkVein.NAME) public static SpellSilkVein silkVein;

	@ObjectHolder(ElementalCraft.MODID + ":heal") public static EffectSpell heal;

	@SubscribeEvent
	public static void createSpellRegistry(RegistryEvent.NewRegistry event) {
		new RegistryBuilder<Spell>()
			.setName(NAME)
			.setIDRange(MIN_SPELL_ID, MAX_SPELL_ID)
			.setType(Spell.class)
			.setDefaultKey(ElementalCraft.createRL("none"))
			.create();
	}

	@SubscribeEvent
	public static void registerSpells(RegistryEvent.Register<Spell> event) {
		IForgeRegistry<Spell> registry = event.getRegistry();

		RegistryHelper.register(registry, new Spell(Spell.Properties.create(Spell.Type.NONE)), "none");

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

		RegistryHelper.register(registry, new EffectSpell(Properties.create(Spell.Type.MIXED).elementType(ElementType.WATER)
				.cooldown(ECConfig.COMMON.healCooldown.get()).consumeAmount(ECConfig.COMMON.healConsumeAmount.get()), new EffectInstance(Effects.INSTANT_HEALTH, 1, 1)), "heal");
	}

}
