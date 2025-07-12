package sirttas.elementalcraft.block.instrument.io.mill.woodsaw.air;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.airmill.AirMill;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractAirMillBlock;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractMillBlockEntity;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

import javax.annotation.Nonnull;

public class AirMillWoodSawBlockEntity extends AbstractMillBlockEntity<SawingRecipe> implements AirMill {

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(AirMillWoodSawBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	private int damage;

	public AirMillWoodSawBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.AIR_MILL_WOOD_SAW, PROPERTIES, ElementType.AIR, pos, state);
		damage = 0;
	}

	@Override
	protected void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		damage = compound.getInt(ECNames.DAMAGE);
	}

	@Override
	protected void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		compound.putInt(ECNames.DAMAGE, damage);
	}

	@Override
	protected void collectImplicitComponents(@NotNull DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(ECDataComponents.AIR_MILL_DAMAGE, damage);
	}

	@Override
	protected void applyImplicitComponents(@NotNull DataComponentInput input) {
		super.applyImplicitComponents(input);
		damage = input.getOrDefault(ECDataComponents.AIR_MILL_DAMAGE, AirMill.getMaxDamage());
	}

	@Override
	protected SawingRecipe lookupRecipe(@NotNull SimpleIOInstrumentRecipeInput recipeInput) {
		if (damage >= AirMill.getMaxDamage()) {
			return null;
		}
		return super.lookupRecipe(recipeInput);
	}

	@Override
	public void assemble() {
		super.assemble();
		damage++;
		if (damage >= AirMill.getMaxDamage()) {
			// TODO play sound
			this.getBlockState().setValue(AbstractAirMillBlock.BROKEN, true);
		}
	}

	@Override
	public int getDamage() {
		return damage;
	}

	@Override
	public void setDamage(int damage) {
		this.damage = damage;
	}
}
