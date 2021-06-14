package sirttas.elementalcraft.datagen.managed;

import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.block.shrine.upgrade.directional.CapacityShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.EfficiencyShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.OptimizationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.RangeShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.StrengthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.AccelerationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.FortuneShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.NectarShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.SilkTouchShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BonelessGrowthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.MysticalGroveShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.PickupShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.PlantingShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.StemPollinationShrineUpgradeBlock;
import sirttas.elementalcraft.data.predicate.block.shrine.HasShrineUpgradePredicate;
import sirttas.elementalcraft.tag.ECTags;

public class ShrineUpgradeProvider implements IDataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;

	public ShrineUpgradeProvider(DataGenerator generatorIn) {
		this.generator = generatorIn;
	}

	@Override
	public void run(DirectoryCache cache) throws IOException {
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.ORE_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 2)
				.incompatibleWith(ElementalCraft.createRL(FortuneShrineUpgradeBlock.NAME)), SilkTouchShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.ORE_SHRINE).max(3).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.3F)
				.incompatibleWith(ElementalCraft.createRL(SilkTouchShrineUpgradeBlock.NAME)), FortuneShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.HARVEST_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 3F), PlantingShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.GROWTH_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 5F).addBonus(BonusType.SPEED, 4F)
				.incompatibleWith(ElementalCraft.createRL(StemPollinationShrineUpgradeBlock.NAME)), BonelessGrowthShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.VACUUM_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 500F).addBonus(BonusType.SPEED, 20F).addBonus(BonusType.RANGE,
				0.5F), PickupShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.SWEET_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 5F).addBonus(BonusType.SPEED, 2F)
				.incompatibleWith(ElementalCraft.createRL(StrengthShrineUpgradeBlock.NAME)), NectarShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.GROVE_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 2F).addBonus(BonusType.SPEED, 2F), MysticalGroveShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.GROWTH_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 4F).addBonus(BonusType.SPEED, 3F).addBonus(BonusType.RANGE, 0.5F)
				.incompatibleWith(ElementalCraft.createRL(BonelessGrowthShrineUpgradeBlock.NAME)), StemPollinationShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().predicate(IBlockPosPredicate.match(ECTags.Blocks.SHRINES_UPGRADABLES_ACCELERATION).or(IBlockPosPredicate.match(ECBlocks.VACUUM_SHRINE)
				.and(new HasShrineUpgradePredicate(ElementalCraft.createRL(PickupShrineUpgradeBlock.NAME))))).addBonus(BonusType.SPEED, 0.5F), AccelerationShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES_UPGRADABLES_RANGE).addBonus(BonusType.RANGE, 1.5F).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.2F).addBonus(BonusType.SPEED,
				1.2F), RangeShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES).max(1).addBonus(BonusType.CAPACITY, 5F).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.1F).addBonus(BonusType.SPEED, 1.1F),
				CapacityShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES).addBonus(BonusType.CAPACITY, 0.9F).addBonus(BonusType.ELEMENT_CONSUMPTION, 0.5F), EfficiencyShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES_UPGRADABLES_STRENGTH).addBonus(BonusType.STRENGTH, 2F).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.2F)
				.incompatibleWith(ElementalCraft.createRL(NectarShrineUpgradeBlock.NAME)), StrengthShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES).max(2).addBonus(BonusType.CAPACITY, 1.25F).addBonus(BonusType.ELEMENT_CONSUMPTION, 0.75F).addBonus(BonusType.SPEED, 0.8F),
				OptimizationShrineUpgradeBlock.NAME);
	}

	protected void save(DirectoryCache cache, ShrineUpgrade.Builder builder, String name) throws IOException {
		IDataProvider.save(GSON, cache, builder.toJson(), getPath(ElementalCraft.createRL(name)));
	}

	private Path getPath(ResourceLocation id) {
		return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/elementalcraft_shrine_upgrades/" + id.getPath() + ".json");
	}

	@Override
	public String getName() {
		return "ElementalCraft Shrines Upgrades";
	}
}
