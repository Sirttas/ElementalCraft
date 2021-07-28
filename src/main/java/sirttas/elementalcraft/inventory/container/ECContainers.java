package sirttas.elementalcraft.inventory.container;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.spelldesk.SpellDeskBlock;
import sirttas.elementalcraft.block.spelldesk.SpellDeskContainer;
import sirttas.elementalcraft.item.spell.book.SpellBookItem;
import sirttas.elementalcraft.item.spell.book.SpellBookContainer;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECContainers {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SpellBookItem.NAME) public static final MenuType<SpellBookContainer> SPELL_BOOK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SpellDeskBlock.NAME) public static final MenuType<SpellDeskContainer> SPELL_DESK = null;

	private ECContainers() {}
	
	@SubscribeEvent
	public static void registerContainers(RegistryEvent.Register<MenuType<?>> event) {
		IForgeRegistry<MenuType<?>> registry = event.getRegistry();

		RegistryHelper.register(registry, new MenuType<>(SpellBookContainer::new), SpellBookItem.NAME);
		RegistryHelper.register(registry, new MenuType<>(SpellDeskContainer::new), SpellDeskBlock.NAME);
	}
}
