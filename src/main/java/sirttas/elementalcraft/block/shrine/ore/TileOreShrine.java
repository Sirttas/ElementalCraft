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
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractTileShrine;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.loot.LootHelper;

public class TileOreShrine extends AbstractTileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOreShrine.NAME) public static final TileEntityType<TileOreShrine> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.EARTH).periode(ECConfig.COMMON.oreShrinePeriode.get()).consumeAmount(ECConfig.COMMON.oreShrineConsumeAmount.get())
			.range(ECConfig.COMMON.oreShrineRange.get()).capacity(ECConfig.COMMON.shrinesCapacity.get() * 10);

	public TileOreShrine() {
		super(TYPE, PROPERTIES);
	}

	private Optional<BlockPos> findOre() {
		int range = getIntegerRange();

		return IntStream.range(-range, range + 1)
				.mapToObj(x -> IntStream.range(-range, range + 1).mapToObj(z -> IntStream.range(0, pos.getY() + 1).mapToObj(y -> new BlockPos(pos.getX() + x, y, pos.getZ() + z))))
				.flatMap(s -> s.flatMap(s2 -> s2)).filter(p -> Tags.Blocks.ORES.contains(world.getBlockState(p).getBlock())).findAny();

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

		return new AxisAlignedBB(this.getPos()).grow(range, 0, range).offset(0, -1, 0).expand(0, 1D - pos.getY(), 0);
	}


	@Override
	protected boolean doTick() {

		if (world instanceof ServerWorld && !world.isRemote) {
			return findOre().map(p -> {
				int fortune = getFortuneLevel();

				if (fortune > 0) {
					ItemStack pickaxe = new ItemStack(Items.NETHERITE_PICKAXE);

					pickaxe.addEnchantment(Enchantments.FORTUNE, fortune);
					LootHelper.getDrops((ServerWorld) this.world, p, pickaxe).forEach(s -> Block.spawnAsEntity(this.world, this.pos.up(), s));
				} else {
					LootHelper.getDrops((ServerWorld) this.world, p, isSilkTouch()).forEach(s -> Block.spawnAsEntity(this.world, this.pos.up(), s));
				}
				this.world.setBlockState(p, Blocks.STONE.getDefaultState());
				return true;
			}).orElse(false);
		}
		return false;
	}
}
