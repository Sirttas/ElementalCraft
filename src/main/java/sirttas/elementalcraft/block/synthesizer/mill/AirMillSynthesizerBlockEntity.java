package sirttas.elementalcraft.block.synthesizer.mill;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.airmill.AirMill;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractAirMillBlock;
import sirttas.elementalcraft.block.synthesizer.AbstractSynthesizerBlockEntity;
import sirttas.elementalcraft.component.ECDataComponents;

import javax.annotation.Nonnull;

public class AirMillSynthesizerBlockEntity extends AbstractSynthesizerBlockEntity implements AirMill {

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(AirMillSynthesizerBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	private int damage;

	public AirMillSynthesizerBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.AIR_MILL_SYNTHESIZER, PROPERTIES, pos, state);
		damage = 0;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, AirMillSynthesizerBlockEntity solarSynthesizer) {
		solarSynthesizer.handleSynthesis();
	}

	@Override
	protected int synthesizeElement() {
		var maxDamage = AirMill.getMaxDamage();

		if (damage < maxDamage) {
			damage++;
			if (damage >= maxDamage) {
				AirMill.renderMillBreaking(this.getLevel(), this.getBlockPos());
				this.getBlockState().setValue(AbstractAirMillBlock.BROKEN, true);
			}
			return Math.round(this.synthesisMultiplier);
		}
		return 0;
	}

	@Override
	public int getDamage() {
		return damage;
	}

	@Override
	public void setDamage(int damage) {
		this.damage = damage;
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
}
