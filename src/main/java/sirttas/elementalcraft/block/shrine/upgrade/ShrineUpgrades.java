package sirttas.elementalcraft.block.shrine.upgrade;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.dpanvil.api.data.IDataWrapper;
import sirttas.dpanvil.api.event.DataManagerReloadEvent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.FortuneShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.NectarShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.ProtectionShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.SilkTouchShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BonelessGrowthShrineUpgradeBlock;
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

	private ShrineUpgrades() {}
	
	@SubscribeEvent
	public  void onReload(DataManagerReloadEvent<ShrineUpgrade> event) {
		event.getDataManager().getData().forEach((id, upgrade) -> {
			Block block = ForgeRegistries.BLOCKS.getValue(id);

			if (block instanceof AbstractShrineUpgradeBlock) {
				((AbstractShrineUpgradeBlock) block).setUpgrade(upgrade);
			}
		});
	}

}
