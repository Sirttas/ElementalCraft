package sirttas.elementalcraft.block.source.trait;

import sirttas.dpanvil.api.data.IDataWrapper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;

public class SourceTraits {

	private SourceTraits() {}
	
	public static final IDataWrapper<SourceTrait> ELEMENT_CAPACITY = ElementalCraftApi.SOURCE_TRAIT_MANAGER.getWrapper(ElementalCraft.createRL(ECNames.ELEMENT_CAPACITY));
	public static final IDataWrapper<SourceTrait> RECOVER_RATE = ElementalCraftApi.SOURCE_TRAIT_MANAGER.getWrapper(ElementalCraft.createRL(ECNames.RECOVER_RATE));
	public static final IDataWrapper<SourceTrait> DIURNAL_NOCTURNAL = ElementalCraftApi.SOURCE_TRAIT_MANAGER.getWrapper(ElementalCraft.createRL("diurnal_nocturnal"));
	public static final IDataWrapper<SourceTrait> GENEROSITY = ElementalCraftApi.SOURCE_TRAIT_MANAGER.getWrapper(ElementalCraft.createRL("generosity"));
	public static final IDataWrapper<SourceTrait> THRIFTINESS = ElementalCraftApi.SOURCE_TRAIT_MANAGER.getWrapper(ElementalCraft.createRL("thriftiness"));
	
}
