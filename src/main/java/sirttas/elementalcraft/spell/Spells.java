package sirttas.elementalcraft.spell;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryBuilder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.spell.air.SpellItemPull;
import sirttas.elementalcraft.spell.earth.SpellGavelFall;
import sirttas.elementalcraft.spell.earth.SpellStoneWall;
import sirttas.elementalcraft.spell.fire.SpellFireBall;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Spells {

	private static final ResourceLocation NAME = new ResourceLocation(ElementalCraft.MODID, "spells");
	private static final int MIN_SPELL_ID = 0;
	private static final int MAX_SPELL_ID = Short.MAX_VALUE - 1;

	@ObjectHolder(ElementalCraft.MODID + ":" + SpellGavelFall.NAME) public static SpellGavelFall gravelFall;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellStoneWall.NAME) public static SpellStoneWall stoneWall;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellFireBall.NAME) public static SpellFireBall fireBall;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellItemPull.NAME) public static SpellItemPull itemPull;

	@SubscribeEvent
	public static void createSpellRegistry(RegistryEvent.NewRegistry event) {
		RegistryBuilder<Spell> builder = new RegistryBuilder<>();
		builder.setName(NAME);
		builder.setIDRange(MIN_SPELL_ID, MAX_SPELL_ID);
		builder.setType(Spell.class);
		builder.create();
	}

	@SubscribeEvent
	public static void registerSpells(RegistryEvent.Register<Spell> event) {
		IForgeRegistry<Spell> registry = event.getRegistry();

		RegistryHelper.register(registry, new SpellGavelFall(), SpellGavelFall.NAME);
		RegistryHelper.register(registry, new SpellStoneWall(), SpellStoneWall.NAME);
		RegistryHelper.register(registry, new SpellFireBall(), SpellFireBall.NAME);
		RegistryHelper.register(registry, new SpellItemPull(), SpellItemPull.NAME);
	}
}
