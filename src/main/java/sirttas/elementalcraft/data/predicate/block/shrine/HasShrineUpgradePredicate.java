package sirttas.elementalcraft.data.predicate.block.shrine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicateType;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.data.predicate.block.ECBlockPosPredicateTypes;

import javax.annotation.Nonnull;
import java.util.List;

public class HasShrineUpgradePredicate implements IShrinePredicate {

	public static final String NAME = "has_shrine_upgrade";
	public static final MapCodec<HasShrineUpgradePredicate> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
	        IDataManager.keyCodec(ElementalCraftApi.SHRINE_UPGRADE_MANAGER_KEY).fieldOf(ECNames.SHRINE_UPGRADE).forGetter(p -> p.key),
			Codec.INT.optionalFieldOf(ECNames.COUNT, 1).forGetter(p -> p.count)
	).apply(builder, HasShrineUpgradePredicate::new));

	private final int count;
	private final ResourceKey<ShrineUpgrade> key;

    public HasShrineUpgradePredicate(Identifier upgradeId) {
        this(upgradeId, 1);
    }

    public HasShrineUpgradePredicate(Identifier upgradeId, int count) {
        this(IDataManager.createKey(ElementalCraftApi.SHRINE_UPGRADE_MANAGER_KEY, upgradeId), count);
    }

	public HasShrineUpgradePredicate(ResourceKey<ShrineUpgrade> key) {
		this(key, 1);
	}

	public HasShrineUpgradePredicate(ResourceKey<ShrineUpgrade> key, int count) {
		this.key = key;
		this.count = count;
	}


	@Override
	public boolean test(AbstractShrineBlockEntity shrine) {
		return shrine.getUpgradeCount(key) >= count;
	}

	@Override
	public BlockPosPredicateType<HasShrineUpgradePredicate> getType() {
		return ECBlockPosPredicateTypes.HAS_SHRINE_UPGRADE.get();
	}

	@Override
	@Nonnull
	public List<Component> getTooltip() {
		var loc = key.identifier();

		return List.of(Component.translatable("tooltip.elementalcraft.predicate.shrine_upgrade", count, Component.translatable("block." + loc.getNamespace() + "." + loc.getPath())));
	}
}
