package sirttas.elementalcraft.block.shrine.ore;

import java.util.Optional;
import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.loot.LootHelper;

public class OreShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + OreShrineBlock.NAME) public static final TileEntityType<OreShrineBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.EARTH).periode(ECConfig.COMMON.oreShrinePeriode.get()).consumeAmount(ECConfig.COMMON.oreShrineConsumeAmount.get())
			.range(ECConfig.COMMON.oreShrineRange.get()).capacity(ECConfig.COMMON.shrinesCapacity.get() * 10);

	public OreShrineBlockEntity() {
		super(TYPE, PROPERTIES);
	}

	private Optional<BlockPos> findOre() {
		int range = getIntegerRange();

		return IntStream.range(-range, range + 1)
				.mapToObj(x -> IntStream.range(-range, range + 1).mapToObj(z -> IntStream.range(0, worldPosition.getY() + 1).mapToObj(y -> new BlockPos(worldPosition.getX() + x, y, worldPosition.getZ() + z))))
				.flatMap(s -> s.flatMap(s2 -> s2)).filter(p -> Tags.Blocks.ORES.contains(level.getBlockState(p).getBlock())).findAny();

	}

	private boolean isSilkTouch() {
		return this.hasUpgrade(ShrineUpgrades.SILK_TOUCH);
	}

	private int getFortuneLevel() {
		return this.getUpgradeCount(ShrineUpgrades.FORTUNE);
	}

	@Override
	public AxisAlignedBB getRangeBoundingBox() {
		int range = getIntegerRange();

		return new AxisAlignedBB(this.getBlockPos()).inflate(range, 0, range).move(0, -1, 0).expandTowards(0, 1D - worldPosition.getY(), 0);
	}


	@Override
	protected boolean doTick() {

		if (level instanceof ServerWorld && !level.isClientSide) {
			return findOre().map(p -> {
				int fortune = getFortuneLevel();

				if (fortune > 0) {
					ItemStack pickaxe = new ItemStack(Items.NETHERITE_PICKAXE);

					pickaxe.enchant(Enchantments.BLOCK_FORTUNE, fortune);
					LootHelper.getDrops((ServerWorld) this.level, p, pickaxe).forEach(s -> Block.popResource(this.level, this.worldPosition.above(), s));
				} else {
					LootHelper.getDrops((ServerWorld) this.level, p, isSilkTouch()).forEach(s -> Block.popResource(this.level, this.worldPosition.above(), s));
				}
				this.level.setBlockAndUpdate(p, Blocks.STONE.defaultBlockState());
				return true;
			}).orElse(false);
		}
		return false;
	}
}
