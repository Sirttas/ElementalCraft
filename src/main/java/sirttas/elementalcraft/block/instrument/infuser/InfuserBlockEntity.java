package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.SingleItemInventory;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public class InfuserBlockEntity extends AbstractInstrumentBlockEntity<IInfuser, IInfusionRecipe> implements IInfuser {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + InfuserBlock.NAME) public static final BlockEntityType<InfuserBlockEntity> TYPE = null;

	private final SingleItemInventory inventory;

	public InfuserBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, IInfusionRecipe.TYPE, ECConfig.COMMON.infuserTransferSpeed.get(), ECConfig.COMMON.infuserMaxRunes.get());
		inventory = new SingleItemInventory(this::setChanged);
		lockable = true;
	}

	@Override
	protected IInfusionRecipe lookupRecipe() {
		return this.lookupInfusionRecipe(level);
	}

	@Override
	protected boolean shouldRetriverExtractOutput() {
		return this.recipe == null;
	}

	@Override
	public void process() {
		super.process();
		if (this.level.isClientSide) {
			ParticleHelper.createCraftingParticle(getElementType(), level, Vec3.atCenterOf(worldPosition), level.random);
		}
	}

	@Override
	public Container getInventory() {
		return inventory;
	}
}
