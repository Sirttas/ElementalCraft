package sirttas.elementalcraft.api.upgrade;

import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.Products.P3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelReader;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.json.merger.BlockPredicateJsonMerger;
import sirttas.dpanvil.api.json.merger.ForeachJsonMerger;
import sirttas.dpanvil.api.json.merger.JsonMerger;
import sirttas.dpanvil.api.json.merger.JsonObjectMerger;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.name.ECNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public abstract class AbstractUpgrade<T> {

	public static final JsonMerger MERGER = JsonObjectMerger.builder()
			.with(ECNames.PREDICATE, new BlockPredicateJsonMerger())
			.with(ECNames.BONUSES, new ForeachJsonMerger((j1, j2) -> new JsonPrimitive(j1.getAsFloat() * j2.getAsFloat())))
			.build();

    private IBlockPosPredicate predicate;
    @Deprecated
    protected int maxAmount;
    private final Map<T, Float> bonuses;
	private Lazy<@NotNull List<Component>> predicateTooltip;
	
	protected AbstractUpgrade(IBlockPosPredicate predicate, Map<T, Float> map, int maxAmount) {
		this.setPredicate(predicate);
		this.bonuses = map;
		this.maxAmount = maxAmount;
	}

	protected static <T extends StringRepresentable, U extends AbstractUpgrade<T>> P3<Mu<U>, IBlockPosPredicate, Map<T, Float>, Integer> codec(Instance<U> builder, Codec<T> bonusCodec) {
		return builder.group(
				IBlockPosPredicate.CODEC.fieldOf(ECNames.PREDICATE).forGetter(AbstractUpgrade::getPredicate),
				Codec.unboundedMap(bonusCodec, Codec.FLOAT).optionalFieldOf(ECNames.BONUSES, Map.of()).forGetter(AbstractUpgrade::getBonuses),
				Codec.INT.optionalFieldOf(ECNames.MAX_AMOUNT, 0).forGetter(u -> u.maxAmount)
		);
	}

	public boolean canUpgrade(@Nonnull LevelReader level, @Nonnull BlockPos pos, @Nullable Direction direction, int amount) {
		return (maxAmount == 0 || amount < maxAmount) && predicate.test(level, pos, direction);
	}
	
	public final Map<T, Float> getBonuses() {
		return bonuses;
	}

	public IBlockPosPredicate getPredicate() {
		return predicate;
	}

	public void setPredicate(IBlockPosPredicate predicate) {
		this.predicate = predicate.simplify();
		this.predicateTooltip = Lazy.of(this.predicate::getTooltip);
	}

	public List<Component> getPredicateTooltip() {
		return predicateTooltip.get();
	}
}
