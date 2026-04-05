package sirttas.elementalcraft.block.instrument.io.mill.grindstone.air;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.airmill.AirMill;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractAirMillBlock;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AbstractMillGrindstoneBlockEntity;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;

import javax.annotation.Nonnull;

public class AirMillGrindstoneBlockEntity extends AbstractMillGrindstoneBlockEntity implements AirMill {

	public static final ResourceKey<@NotNull IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(AirMillGrindstoneBlock.NAME);
	private static final Holder<@NotNull IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	private int damage;

	public AirMillGrindstoneBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.AIR_MILL_GRINDSTONE, PROPERTIES, ElementType.AIR, pos, state);
		damage = 0;
	}

	@Override
	protected void loadAdditional(@Nonnull ValueInput input) {
		super.loadAdditional(input);
		damage = input.getIntOr(ECNames.DAMAGE, 0);
	}

	@Override
	protected void saveAdditional(@Nonnull ValueOutput output) {
		super.saveAdditional(output);
        output.putInt(ECNames.DAMAGE, damage);
	}

	@Override
	protected void collectImplicitComponents(@NotNull DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(ECDataComponents.AIR_MILL_DAMAGE, damage);
	}

	@Override
	protected void applyImplicitComponents(@NotNull DataComponentGetter getter) {
		super.applyImplicitComponents(getter);
		damage = getter.getOrDefault(ECDataComponents.AIR_MILL_DAMAGE, AirMill.getMaxDamage());
	}

	@Override
	protected GrindingRecipe lookupRecipe(@NotNull ServerLevel level, @NotNull SimpleIOInstrumentRecipeInput recipeInput) {
		if (damage >= AirMill.getMaxDamage()) {
			return null;
		}
		return super.lookupRecipe(level, recipeInput);
	}

	@Override
	public void assemble() {
		super.assemble();
		damage++;
		if (damage >= AirMill.getMaxDamage()) {
			AirMill.renderMillBreaking(this.getLevel(), this.getBlockPos());
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
