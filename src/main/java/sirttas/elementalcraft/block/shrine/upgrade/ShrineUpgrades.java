package sirttas.elementalcraft.block.shrine.upgrade;

import net.minecraftforge.fml.common.Mod;
import sirttas.dpanvil.api.data.IDataWrapper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.upgrade.directional.StrengthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.CrystalHarvestShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.FortuneShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.NectarShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.ProtectionShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.SilkTouchShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BonelessGrowthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.FillingShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.MysticalGroveShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.PickupShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.PlantingShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.StemPollinationShrineUpgradeBlock;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class ShrineUpgrades {

	public static final String NAME = "shrine_upgrades";
	public static final String FOLDER = ElementalCraftApi.MODID + '_' + NAME;

	public static final IDataWrapper<ShrineUpgrade> FORTUNE = ElementalCraft.SHRINE_UPGRADE_MANAGER.getWrapper(ElementalCraft.createRL(FortuneShrineUpgradeBlock.NAME));
	public static final IDataWrapper<ShrineUpgrade> SILK_TOUCH = ElementalCraft.SHRINE_UPGRADE_MANAGER.getWrapper(ElementalCraft.createRL(SilkTouchShrineUpgradeBlock.NAME));
	public static final IDataWrapper<ShrineUpgrade> PLANTING = ElementalCraft.SHRINE_UPGRADE_MANAGER.getWrapper(ElementalCraft.createRL(PlantingShrineUpgradeBlock.NAME));
	public static final IDataWrapper<ShrineUpgrade> BONELESS_GROWTH = ElementalCraft.SHRINE_UPGRADE_MANAGER.getWrapper(ElementalCraft.createRL(BonelessGrowthShrineUpgradeBlock.NAME));
	public static final IDataWrapper<ShrineUpgrade> PICKUP = ElementalCraft.SHRINE_UPGRADE_MANAGER.getWrapper(ElementalCraft.createRL(PickupShrineUpgradeBlock.NAME));
	public static final IDataWrapper<ShrineUpgrade> NECTAR = ElementalCraft.SHRINE_UPGRADE_MANAGER.getWrapper(ElementalCraft.createRL(NectarShrineUpgradeBlock.NAME));
	public static final IDataWrapper<ShrineUpgrade> MYSTICAL_GROVE = ElementalCraft.SHRINE_UPGRADE_MANAGER.getWrapper(ElementalCraft.createRL(MysticalGroveShrineUpgradeBlock.NAME));
	public static final IDataWrapper<ShrineUpgrade> STEM_POLLINATION = ElementalCraft.SHRINE_UPGRADE_MANAGER.getWrapper(ElementalCraft.createRL(StemPollinationShrineUpgradeBlock.NAME));
	public static final IDataWrapper<ShrineUpgrade> PROTECTION = ElementalCraft.SHRINE_UPGRADE_MANAGER.getWrapper(ElementalCraft.createRL(ProtectionShrineUpgradeBlock.NAME));
	public static final IDataWrapper<ShrineUpgrade> FILLING = ElementalCraft.SHRINE_UPGRADE_MANAGER.getWrapper(ElementalCraft.createRL(FillingShrineUpgradeBlock.NAME));
	public static final IDataWrapper<ShrineUpgrade> CRYSTAL_HARVEST = ElementalCraft.SHRINE_UPGRADE_MANAGER.getWrapper(ElementalCraft.createRL(CrystalHarvestShrineUpgradeBlock.NAME));
	public static final IDataWrapper<ShrineUpgrade> STRENGTH = ElementalCraft.SHRINE_UPGRADE_MANAGER.getWrapper(ElementalCraft.createRL(StrengthShrineUpgradeBlock.NAME));
	
	private ShrineUpgrades() {}

}
