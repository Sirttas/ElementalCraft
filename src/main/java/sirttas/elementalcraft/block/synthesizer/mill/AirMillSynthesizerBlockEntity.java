package sirttas.elementalcraft.block.synthesizer.mill;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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
				// TODO play sound
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
}
