package sirttas.elementalcraft.block.shrine.upgrade;

import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.dpanvil.api.annotation.DataHolder;
import sirttas.dpanvil.api.event.DataManagerReloadEvent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BlockFortuneShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BlockNectarShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BlockSilkTouchShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockBonelessGrowthShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockMysticalGroveShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockPickupShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockPlantingShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockStemPollinationShrineUpgrade;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public class ShrineUpgrades {

	public static final String NAME = "shrine_upgrades";
	public static final String FOLDER = ElementalCraft.MODID + '_' + NAME;

	@DataHolder(ElementalCraft.MODID + ":" + BlockFortuneShrineUpgrade.NAME) public static final ShrineUpgrade FORTUNE = null;
	@DataHolder(ElementalCraft.MODID + ":" + BlockSilkTouchShrineUpgrade.NAME) public static final ShrineUpgrade SILK_TOUCH = null;
	@DataHolder(ElementalCraft.MODID + ":" + BlockPlantingShrineUpgrade.NAME) public static final ShrineUpgrade PLANTING = null;
	@DataHolder(ElementalCraft.MODID + ":" + BlockBonelessGrowthShrineUpgrade.NAME) public static final ShrineUpgrade BONELESS_GROWTH = null;
	@DataHolder(ElementalCraft.MODID + ":" + BlockPickupShrineUpgrade.NAME) public static final ShrineUpgrade PICKUP = null;
	@DataHolder(ElementalCraft.MODID + ":" + BlockNectarShrineUpgrade.NAME) public static final ShrineUpgrade NECTAR = null;
	@DataHolder(ElementalCraft.MODID + ":" + BlockMysticalGroveShrineUpgrade.NAME) public static final ShrineUpgrade MYSTICAL_GROVE = null;
	@DataHolder(ElementalCraft.MODID + ":" + BlockStemPollinationShrineUpgrade.NAME) public static final ShrineUpgrade STEM_POLLINATION = null;

	private ShrineUpgrades() {}
	
	@SubscribeEvent
	public static void onReload(DataManagerReloadEvent<ShrineUpgrade> event) {
		event.getDataManager().getData().forEach((id, upgrade) -> {
			Block block = ForgeRegistries.BLOCKS.getValue(id);

			if (block instanceof AbstractBlockShrineUpgrade) {
				((AbstractBlockShrineUpgrade) block).setUpgrade(upgrade);
			}
		});
	}

}
