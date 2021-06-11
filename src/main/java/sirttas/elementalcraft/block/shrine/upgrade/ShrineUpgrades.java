package sirttas.elementalcraft.block.shrine.upgrade;

import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.dpanvil.api.annotation.DataHolder;
import sirttas.dpanvil.api.event.DataManagerReloadEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.FortuneShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.NectarShrineUpgradeBlock;
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

	@DataHolder(ElementalCraftApi.MODID + ":" + FortuneShrineUpgradeBlock.NAME) public static final ShrineUpgrade FORTUNE = null;
	@DataHolder(ElementalCraftApi.MODID + ":" + SilkTouchShrineUpgradeBlock.NAME) public static final ShrineUpgrade SILK_TOUCH = null;
	@DataHolder(ElementalCraftApi.MODID + ":" + PlantingShrineUpgradeBlock.NAME) public static final ShrineUpgrade PLANTING = null;
	@DataHolder(ElementalCraftApi.MODID + ":" + BonelessGrowthShrineUpgradeBlock.NAME) public static final ShrineUpgrade BONELESS_GROWTH = null;
	@DataHolder(ElementalCraftApi.MODID + ":" + PickupShrineUpgradeBlock.NAME) public static final ShrineUpgrade PICKUP = null;
	@DataHolder(ElementalCraftApi.MODID + ":" + NectarShrineUpgradeBlock.NAME) public static final ShrineUpgrade NECTAR = null;
	@DataHolder(ElementalCraftApi.MODID + ":" + MysticalGroveShrineUpgradeBlock.NAME) public static final ShrineUpgrade MYSTICAL_GROVE = null;
	@DataHolder(ElementalCraftApi.MODID + ":" + StemPollinationShrineUpgradeBlock.NAME) public static final ShrineUpgrade STEM_POLLINATION = null;

	private ShrineUpgrades() {}
	
	@SubscribeEvent
	public static void onReload(DataManagerReloadEvent<ShrineUpgrade> event) {
		event.getDataManager().getData().forEach((id, upgrade) -> {
			Block block = ForgeRegistries.BLOCKS.getValue(id);

			if (block instanceof AbstractShrineUpgradeBlock) {
				((AbstractShrineUpgradeBlock) block).setUpgrade(upgrade);
			}
		});
	}

}
