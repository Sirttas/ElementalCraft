package sirttas.elementalcraft.data;

import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockCapacityShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockEfficiencyShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockOptimizationShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockRangeShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.BlockAccelerationShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BlockFortuneShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BlockSilkTouchShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockBonelessGrowthShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockPickupShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockPlantingShrineUpgrade;
import sirttas.elementalcraft.tag.ECTags;

public class ShrineUpgradeProvider implements IDataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;

	public ShrineUpgradeProvider(DataGenerator generatorIn) {
		this.generator = generatorIn;
	}

	@Override
	public void act(DirectoryCache cache) throws IOException {
		save(cache, ShrineUpgrade.Builder.create().block(ECBlocks.oreShrine).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 2)
				.incompatibleWith(ElementalCraft.createRL(BlockFortuneShrineUpgrade.NAME)), BlockSilkTouchShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().block(ECBlocks.oreShrine).max(3).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.3F)
				.incompatibleWith(ElementalCraft.createRL(BlockSilkTouchShrineUpgrade.NAME)), BlockFortuneShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().block(ECBlocks.harvestShrine).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 3F), BlockPlantingShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().block(ECBlocks.growthShrine).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 5F).addBonus(BonusType.SPEED, 4F),
				BlockBonelessGrowthShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().block(ECBlocks.vacuumShrine).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 500F).addBonus(BonusType.SPEED, 20F).addBonus(BonusType.RANGE,
				0.5F),
				BlockPickupShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().tag(ECTags.Blocks.SHRINES_UPGRADABLES_ACCELERATION).addBonus(BonusType.SPEED, 0.5F), BlockAccelerationShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().tag(ECTags.Blocks.SHRINES_UPGRADABLES_RANGE).addBonus(BonusType.RANGE, 1.5F).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.2F).addBonus(BonusType.SPEED,
				1.2F), BlockRangeShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().tag(ECTags.Blocks.SHRINES).max(1).addBonus(BonusType.CAPACITY, 5F).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.1F).addBonus(BonusType.SPEED, 1.1F),
				BlockCapacityShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().tag(ECTags.Blocks.SHRINES).addBonus(BonusType.CAPACITY, 0.9F).addBonus(BonusType.ELEMENT_CONSUMPTION, 0.5F), BlockEfficiencyShrineUpgrade.NAME);
		save(cache, ShrineUpgrade.Builder.create().tag(ECTags.Blocks.SHRINES).max(2).addBonus(BonusType.CAPACITY, 1.25F).addBonus(BonusType.ELEMENT_CONSUMPTION, 0.75F).addBonus(BonusType.SPEED, 0.8F),
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
