package sirttas.elementalcraft.api;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.dpanvil.api.data.preprocessor.InheritanceDataPreprocessor;
import sirttas.dpanvil.api.data.preprocessor.MergeDataPreprocessor;
import sirttas.dpanvil.api.data.preprocessor.NeoForgeConditionsPreprocessor;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.range.Range;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.upgrade.AbstractUpgrade;

public class ElementalCraftApi {

	public static final String MODID = "elementalcraft";
	public static final Logger LOGGER = LogManager.getLogger(ElementalCraftApi.MODID);

	public static final ResourceKey<IDataManager<Range>> RANGE_MANAGER_KEY = IDataManager.createManagerKey(identifier(ECNames.RANGE));
	public static final IDataManager<Range> RANGE_MANAGER = IDataManager.builder(Range.class, RANGE_MANAGER_KEY)
			.preprocessor(new InheritanceDataPreprocessor(Range.MERGER))
			.preprocessor(new NeoForgeConditionsPreprocessor())
			.preprocessor(new MergeDataPreprocessor(Range.MERGER))
			.withDefault(Range.DEFAULT)
			.build();

	public static final ResourceKey<IDataManager<Rune>> RUNE_MANAGER_KEY = IDataManager.createManagerKey(identifier(ECNames.RUNE));
	public static final IDataManager<Rune> RUNE_MANAGER = IDataManager.builder(Rune.class, RUNE_MANAGER_KEY)
			.preprocessor(new NeoForgeConditionsPreprocessor())
			.preprocessor(new MergeDataPreprocessor(AbstractUpgrade.MERGER))
			.build();

	public static final ResourceKey<IDataManager<ShrineUpgrade>> SHRINE_UPGRADE_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.identifier(ECNames.SHRINE_UPGRADE));
	public static final IDataManager<ShrineUpgrade> SHRINE_UPGRADE_MANAGER = IDataManager.builder(ShrineUpgrade.class, SHRINE_UPGRADE_MANAGER_KEY)
			.preprocessor(new NeoForgeConditionsPreprocessor())
			.preprocessor(new MergeDataPreprocessor(AbstractUpgrade.MERGER))
			.build();

	public static final ResourceKey<IDataManager<ToolInfusion>> TOOL_INFUSION_MANAGER_KEY = IDataManager.createManagerKey(identifier(ECNames.TOOL_INFUSION));
	public static final IDataManager<ToolInfusion> TOOL_INFUSION_MANAGER = IDataManager.builder(ToolInfusion.class, TOOL_INFUSION_MANAGER_KEY)
			.withDefault(ToolInfusion.NONE)
			.idSetter(ToolInfusion::setId)
			.build();

	public static final ResourceKey<IDataManager<SourceTrait>> SOURCE_TRAIT_MANAGER_KEY = IDataManager.createManagerKey(identifier(ECNames.SOURCE_TRAIT));
	public static final IDataManager<SourceTrait> SOURCE_TRAIT_MANAGER = IDataManager.builder(SourceTrait.class, SOURCE_TRAIT_MANAGER_KEY)
			.idSetter(SourceTrait::setId)
			.build();

    public static final ResourceKey<IDataManager<BuddingShrineBudType>> BUD_TYPE_MANAGER_KEY = IDataManager.createManagerKey(identifier(ECNames.BUD_TYPE));
    public static final IDataManager<BuddingShrineBudType> BUD_TYPE_MANAGER = IDataManager.builder(BuddingShrineBudType.class, BUD_TYPE_MANAGER_KEY)
            .build();


    private ElementalCraftApi() {}

	public static Identifier identifier(String name) {
		if (name.contains(":")) {
			return Identifier.parse(name);
		}
		return Identifier.fromNamespaceAndPath(MODID, name);
	}
}
