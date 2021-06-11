package sirttas.elementalcraft.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sirttas.dpanvil.api.data.IDataManager;
import sirttas.dpanvil.api.tag.DataTagRegistry;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.rune.Rune;

public class ElementalCraftApi {

	public static final String MODID = "elementalcraft";
	public static final Logger LOGGER = LogManager.getLogger(ElementalCraftApi.MODID);
	
	public static final IDataManager<Rune> RUNE_MANAGER = IDataManager.builder(Rune.class, Rune.FOLDER).withIdSetter(Rune::setId).merged(Rune::merge).build();
	public static final DataTagRegistry<Rune> RUNE_TAGS = new DataTagRegistry<>();
	public static final IDataManager<ToolInfusion> TOOL_INFUSION_MANAGER = IDataManager.builder(ToolInfusion.class, ToolInfusion.FOLDER).withDefault(ToolInfusion.NONE).withIdSetter(ToolInfusion::setId).build();

	private ElementalCraftApi() {}
}
