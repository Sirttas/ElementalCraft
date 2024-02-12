package sirttas.elementalcraft.container.menu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.spelldesk.SpellDeskBlock;
import sirttas.elementalcraft.block.spelldesk.SpellDeskMenu;
import sirttas.elementalcraft.item.source.analysis.SourceAnalysisGlassItem;
import sirttas.elementalcraft.item.source.analysis.SourceAnalysisGlassMenu;
import sirttas.elementalcraft.item.spell.book.SpellBookItem;
import sirttas.elementalcraft.item.spell.book.SpellBookMenu;

public class ECMenus {

	private static final DeferredRegister<MenuType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.MENU, ElementalCraftApi.MODID);

	public static final DeferredHolder<MenuType<?>, MenuType<SpellBookMenu>> SPELL_BOOK = register(SpellBookMenu::new, SpellBookItem.NAME);
	public static final DeferredHolder<MenuType<?>, MenuType<SpellDeskMenu>> SPELL_DESK = register(SpellDeskMenu::new, SpellDeskBlock.NAME);
	public static final DeferredHolder<MenuType<?>, MenuType<SourceAnalysisGlassMenu>> SOURCE_ANALYSIS_GLASS = register(SourceAnalysisGlassMenu::new, SourceAnalysisGlassItem.NAME);

	private ECMenus() {}

	private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(MenuType.MenuSupplier<T> menu, String name) {
		return DEFERRED_REGISTER.register(name, () -> new MenuType<>(menu, FeatureFlags.DEFAULT_FLAGS));
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
}
