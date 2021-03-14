package sirttas.elementalcraft.inventory.container;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.spell.book.ItemSpellBook;
import sirttas.elementalcraft.item.spell.book.SpellBookContainer;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECContainers {

	@ObjectHolder(ElementalCraft.MODID + ":" + ItemSpellBook.NAME) public static final ContainerType<SpellBookContainer> SPELL_BOOK = null;

	private ECContainers() {}
	
	@SubscribeEvent
	public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
		IForgeRegistry<ContainerType<?>> registry = event.getRegistry();

		RegistryHelper.register(registry, new ContainerType<SpellBookContainer>(SpellBookContainer::new), ItemSpellBook.NAME);
	}
}
