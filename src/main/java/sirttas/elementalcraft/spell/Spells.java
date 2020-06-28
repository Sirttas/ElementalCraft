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

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Spells {

	private static final ResourceLocation NAME = new ResourceLocation("elementalcraft:spells");
	private static final int MIN_SPELL_ID = 0;
	private static final int MAX_SPELL_ID = Short.MAX_VALUE - 1;

	@ObjectHolder(ElementalCraft.MODID + ":" + SpellGavelFall.NAME) public static SpellGavelFall gravelFall;
	@ObjectHolder(ElementalCraft.MODID + ":" + SpellStoneWall.NAME) public static SpellStoneWall stoneWall;

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
	}
}
