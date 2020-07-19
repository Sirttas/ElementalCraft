package sirttas.elementalcraft.block.shrine.ore;

import java.util.Optional;
import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.config.ECConfig;

public class TileOreShrine extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOreShrine.NAME) public static TileEntityType<TileOreShrine> TYPE;

	public TileOreShrine() {
		super(TYPE, ElementType.EARTH, ECConfig.CONFIG.oreShrinePeriode.get());
	}

	private Optional<BlockPos> findOre() {
		int range = ECConfig.CONFIG.oreShrineRange.get();

		return IntStream.range(-range, range + 1)
				.mapToObj(x -> IntStream.range(-range, range + 1).mapToObj(z -> IntStream.range(0, pos.getY() + 1).mapToObj(y -> new BlockPos(pos.getX() + x, y, pos.getZ() + z))))
				.flatMap(s -> s.flatMap(s2 -> s2)).filter(p -> Tags.Blocks.ORES.contains(world.getBlockState(p).getBlock())).findAny();
	}

	@Override
	protected void doTick() {
		int consumeAmount = ECConfig.CONFIG.oreShrineConsumeAmount.get();

		if (this.hasWorld() && world instanceof ServerWorld && this.getElementAmount() >= consumeAmount) {
			Optional<BlockPos> opt = findOre();

			if (opt.isPresent()) {
				BlockState blockstate = world.getBlockState(opt.get());

				blockstate.getDrops(new LootContext.Builder((ServerWorld) this.world).withRandom(this.world.rand).withParameter(LootParameters.POSITION, opt.get()).withParameter(LootParameters.TOOL,
						ItemStack.EMPTY)).forEach(s -> Block.spawnAsEntity(this.world, this.pos, s));
				this.world.setBlockState(opt.get(), Blocks.STONE.getDefaultState());
				this.consumeElement(consumeAmount);
			}
		}
	}
}
