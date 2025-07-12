package sirttas.elementalcraft.block.shrine.melting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.melting.MeltingRecipe;
import sirttas.elementalcraft.recipe.melting.MeltingRecipeInput;

import java.util.List;
import java.util.Optional;

public class MeltingShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(MeltingShrineBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	protected static final List<Direction> UPGRADE_DIRECTIONS = List.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	private int cooldown;

	public MeltingShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.LAVA_SHRINE, PROPERTIES, pos, state);
		cooldown = 0;
	}

	public static boolean fill(AbstractShrineBlockEntity shrine, Direction fillingDirection, Fluid fluid, float fluidMultiplier) {
		var fluidHandler = shrine.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, shrine.getBlockPos().relative(fillingDirection, 2), fillingDirection.getOpposite());

		return fluidHandler != null && fluidHandler.fill(new FluidStack(fluid, (int) Math.round(shrine.getStrength() * fluidMultiplier)), IFluidHandler.FluidAction.EXECUTE) > 0;
	}

	private Optional<MeltingRecipe> findRecipe() {
		var input = new MeltingRecipeInput(level.getBlockState(getTargetPos()), this.getElementStorage().getElementAmount(), this.getConsumeAmount());

		return level.getRecipeManager().getRecipeFor(ECRecipeTypes.MELTING.get(), input, level)
				.map(RecipeHolder::value);
	}

	@Override
	public BlockPos getTargetPos() {
		return worldPosition.above();
	}

	@Override
	protected boolean doPeriod() {
		if (cooldown > 0) {
			cooldown--;
		} else {
			findRecipe().ifPresent(this::melt);
		}
		return false;
	}

	private void melt(MeltingRecipe recipe) {
		var fillingDirection = getUpgradeDirection(ShrineUpgrades.FILLING);

		if (fillingDirection != null && fill(this, fillingDirection, recipe.result(), recipe.fillingAmount())) {
			level.destroyBlock(getTargetPos(), false);
		} else {
			level.setBlock(getTargetPos(), recipe.result().defaultFluidState().createLegacyBlock(), 11);
			level.levelEvent(LevelEvent.LAVA_FIZZ, getTargetPos(), 0);
		}

		this.cooldown = recipe.cooldown() - 1;
		this.consumeElement(recipe.elementAmount() * this.getConsumeAmount());
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return UPGRADE_DIRECTIONS;
	}
}
