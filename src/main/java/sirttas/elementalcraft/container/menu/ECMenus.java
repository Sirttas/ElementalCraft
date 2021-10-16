package sirttas.elementalcraft.container.menu;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.spelldesk.SpellDeskBlock;
import sirttas.elementalcraft.block.spelldesk.SpellDeskMenu;
import sirttas.elementalcraft.item.source.analysis.SourceAnalysisGlassItem;
import sirttas.elementalcraft.item.source.analysis.SourceAnalysisGlassMenu;
import sirttas.elementalcraft.item.spell.book.SpellBookMenu;
import sirttas.elementalcraft.item.spell.book.SpellBookItem;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECMenus {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SpellBookItem.NAME) public static final MenuType<SpellBookMenu> SPELL_BOOK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SpellDeskBlock.NAME) public static final MenuType<SpellDeskMenu> SPELL_DESK = null;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + SourceAnalysisGlassItem.NAME) public static final MenuType<SourceAnalysisGlassMenu> SOURCE_ANALYSIS_GLASS = null;

	private ECMenus() {}
	
	@SubscribeEvent
	public static void registerMenus(RegistryEvent.Register<MenuType<?>> event) {
		IForgeRegistry<MenuType<?>> registry = event.getRegistry();

		RegistryHelper.register(registry, new MenuType<>(SpellBookMenu::new), SpellBookItem.NAME);
		RegistryHelper.register(registry, new MenuType<>(SpellDeskMenu::new), SpellDeskBlock.NAME);
		RegistryHelper.register(registry, new MenuType<>(SourceAnalysisGlassMenu::new), SourceAnalysisGlassItem.NAME);
	}
}
