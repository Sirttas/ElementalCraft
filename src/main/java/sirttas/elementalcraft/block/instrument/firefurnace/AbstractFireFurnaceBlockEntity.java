package sirttas.elementalcraft.block.instrument.firefurnace;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.inventory.IOInventory;
import sirttas.elementalcraft.inventory.InventoryTileWrapper;
import sirttas.elementalcraft.recipe.instrument.io.FurnaceRecipeWrapper;

public abstract class AbstractFireFurnaceBlockEntity<T extends AbstractCookingRecipe> extends AbstractInstrumentBlockEntity<AbstractFireFurnaceBlockEntity<T>, FurnaceRecipeWrapper<T>> {

	private float exp;
	private RecipeType<T> furnaceRecipeType;
	private final IOInventory inventory;

	protected AbstractFireFurnaceBlockEntity(BlockEntityType<? extends AbstractFireFurnaceBlockEntity<T>> blockEntityType, BlockPos pos, BlockState state, RecipeType<T> recipeType, int transferSpeed, int maxRunes) {
		super(blockEntityType, pos, state, null, transferSpeed, maxRunes);
		this.furnaceRecipeType = recipeType;
		exp = 0;
		outputSlot = 1;
		inventory = new IOInventory(this::setChanged);
	}

	@Override
	protected IItemHandler createHandler() {
		return new SidedInvWrapper(inventory, null);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.exp = compound.getFloat(ECNames.XP);
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		compound.putFloat(ECNames.XP, this.exp);
		return compound;
	}

	@Override
	protected FurnaceRecipeWrapper<T> lookupRecipe() {
		return this.getLevel().getRecipeManager().getRecipeFor(furnaceRecipeType, InventoryTileWrapper.from(this), this.getLevel()).map(FurnaceRecipeWrapper::new).orElse(null);
	}

	@Override
	protected void onProgress() {
		if (level.isClientSide) {
			Random rand = level.random;
			double x = worldPosition.getX() + (5 + rand.nextDouble() * 6) / 16;
			double y = worldPosition.getY() + 6D / 16;
			double z = worldPosition.getZ() + (5 + rand.nextDouble() * 6) / 16;

			level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
			level.addParticle(ParticleTypes.SMOKE, x, y + 0.5D, z, 0.0D, 0.0D, 0.0D);
		}
		super.onProgress();
	}

	public void dropExperience(Player player) {
		dropExperience(player.position());
	}

	public void dropExperience(Vec3 pos) {
		while (exp > 0) {
			int j = ExperienceOrb.getExperienceValue((int) exp);
			exp -= j;
			level.addFreshEntity(new ExperienceOrb(level, pos.x(), pos.y() + 0.5D, pos.z() + 0.5D, j));
		}
		exp = 0;
	}

	public void addExperience(float exp) {
		this.exp += exp;
	}

	@Override
	public Container getInventory() {
		return inventory;
	}

}