package sirttas.elementalcraft.block.shrine.upgrade;

import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked.OverclockedAccelerationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.boneless.BonelessGrowthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.CapacityShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.EfficiencyShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.OptimizationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.RangeShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.filling.FillingShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.fortune.FortuneShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.fortune.greater.GreaterFortuneShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BudTypeShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.CrystalHarvestShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.NectarShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.ProtectionShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.planting.PlantingShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.silktouch.SilkTouchShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.strength.OverwhelmingStrengthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.strength.StrengthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.CrystalGrowthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.MysticalGroveShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.PickupShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.StemPollinationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.vortex.VortexShrineUpgradeBlock;

public class ShrineUpgrades {

	public static final String NAME = "shrine_upgrades";
	public static final String FOLDER = ElementalCraftApi.MODID + '/' + NAME;

	public static final ResourceKey<@NotNull ShrineUpgrade> FORTUNE = createKey(FortuneShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> SILK_TOUCH = createKey(SilkTouchShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> PLANTING = createKey(PlantingShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> BONELESS_GROWTH = createKey(BonelessGrowthShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> PICKUP = createKey(PickupShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> VORTEX = createKey(VortexShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> NECTAR = createKey(NectarShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> MYSTICAL_GROVE = createKey(MysticalGroveShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> STEM_POLLINATION = createKey(StemPollinationShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> PROTECTION = createKey(ProtectionShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> FILLING = createKey(FillingShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> CRYSTAL_HARVEST = createKey(CrystalHarvestShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> CRYSTAL_GROWTH = createKey(CrystalGrowthShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> STRENGTH = createKey(StrengthShrineUpgradeBlock.NAME);
    public static final ResourceKey<@NotNull ShrineUpgrade> SPRINGALINE = createKey(BudTypeShrineUpgradeBlock.SPRINGALINE_NAME);
    public static final ResourceKey<@NotNull ShrineUpgrade> CERTUS_QUARTZ = createKey(BudTypeShrineUpgradeBlock.CERTUS_QUARTZ_NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> CAPACITY = createKey(CapacityShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> RANGE = createKey(RangeShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> EFFICIENCY = createKey(EfficiencyShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> OPTIMIZATION = createKey(OptimizationShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> ACCELERATION = createKey(AccelerationShrineUpgradeBlock.NAME);

	public static final ResourceKey<@NotNull ShrineUpgrade> OVERCLOCKED_ACCELERATION = createKey(OverclockedAccelerationShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> TRANSLOCATION = createKey(TranslocationShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> GREATER_FORTUNE = createKey(GreaterFortuneShrineUpgradeBlock.NAME);
	public static final ResourceKey<@NotNull ShrineUpgrade> OVERWHELMING_STRENGTH = createKey(OverwhelmingStrengthShrineUpgradeBlock.NAME);

	private ShrineUpgrades() {}

	private static ResourceKey<@NotNull ShrineUpgrade> createKey(String name) {
		return IDataManager.createKey(ElementalCraftApi.SHRINE_UPGRADE_MANAGER_KEY, ElementalCraftApi.createRL(name));
	}

}
