package sirttas.elementalcraft.datagen;

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
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockCapacityShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockEfficiencyShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockOptimizationShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockRangeShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockStrengthShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.BlockAccelerationShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BlockFortuneShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BlockNectarShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BlockSilkTouchShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockBonelessGrowthShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockMysticalGroveShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockPickupShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockPlantingShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockStemPollinationShrineUpgrade;
import sirttas.elementalcraft.data.predicate.block.shrine.HasShrineUpgradePredicate;
import sirttas.elementalcraft.tag.ECTags;

public class ShrineUpgradeProvider implements IDataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;

	public ShrineUpgradeProvider(DataGenerator generatorIn) {
		this.generator = generatorIn;
	}

	@Override
	public void act(DirectoryCache cache) throws IOException {
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.ORE_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 2)
				.incompatibleWith(ElementalCraft.createRL(BlockFortuneShrineUpgrade.NAME)), BlockSilkTouchShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.ORE_SHRINE).max(3).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.3F)
				.incompatibleWith(ElementalCraft.createRL(BlockSilkTouchShrineUpgrade.NAME)), BlockFortuneShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.HARVEST_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 3F), BlockPlantingShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.GROWTH_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 5F).addBonus(BonusType.SPEED, 4F)
				.incompatibleWith(ElementalCraft.createRL(BlockStemPollinationShrineUpgrade.NAME)), BlockBonelessGrowthShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.VACUUM_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 500F).addBonus(BonusType.SPEED, 20F).addBonus(BonusType.RANGE,
				0.5F), BlockPickupShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.SWEET_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 5F).addBonus(BonusType.SPEED, 2F)
				.incompatibleWith(ElementalCraft.createRL(BlockStrengthShrineUpgrade.NAME)), BlockNectarShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.GROVE_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 2F).addBonus(BonusType.SPEED, 2F), BlockMysticalGroveShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.GROWTH_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 4F).addBonus(BonusType.SPEED, 3F).addBonus(BonusType.RANGE, 0.5F)
				.incompatibleWith(ElementalCraft.createRL(BlockBonelessGrowthShrineUpgrade.NAME)), BlockStemPollinationShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().predicate(IBlockPosPredicate.match(ECTags.Blocks.SHRINES_UPGRADABLES_ACCELERATION).or(IBlockPosPredicate.match(ECBlocks.VACUUM_SHRINE)
				.and(new HasShrineUpgradePredicate(ElementalCraft.createRL(BlockPickupShrineUpgrade.NAME))))).addBonus(BonusType.SPEED, 0.5F), BlockAccelerationShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES_UPGRADABLES_RANGE).addBonus(BonusType.RANGE, 1.5F).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.2F).addBonus(BonusType.SPEED,
				1.2F), BlockRangeShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES).max(1).addBonus(BonusType.CAPACITY, 5F).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.1F).addBonus(BonusType.SPEED, 1.1F),
				BlockCapacityShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES).addBonus(BonusType.CAPACITY, 0.9F).addBonus(BonusType.ELEMENT_CONSUMPTION, 0.5F), BlockEfficiencyShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES_UPGRADABLES_STRENGTH).addBonus(BonusType.STRENGTH, 2F).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.2F)
				.incompatibleWith(ElementalCraft.createRL(BlockNectarShrineUpgrade.NAME)), BlockStrengthShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES).max(2).addBonus(BonusType.CAPACITY, 1.25F).addBonus(BonusType.ELEMENT_CONSUMPTION, 0.75F).addBonus(BonusType.SPEED, 0.8F),
				BlockOptimizationShrineUpgrade.NAME);
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
