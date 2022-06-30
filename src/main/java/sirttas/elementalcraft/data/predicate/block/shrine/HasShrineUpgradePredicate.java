package sirttas.elementalcraft.data.predicate.block.shrine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicateType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.data.predicate.block.ECBlockPosPredicateTypes;

public class HasShrineUpgradePredicate implements IShrinePredicate {

	public static final String NAME = "has_shrine_upgrade";
	public static final Codec<HasShrineUpgradePredicate> CODEC = RecordCodecBuilder.create(builder -> builder.group(
	        IDataManager.keyCodec(ElementalCraft.SHRINE_UPGRADE_MANAGER_KEY).fieldOf(ECNames.SHRINE_UPGRADE).forGetter(p -> p.key),
			Codec.INT.optionalFieldOf(ECNames.COUNT, 1).forGetter(p -> p.count)
	).apply(builder, HasShrineUpgradePredicate::new));

	private final int count;
	private final ResourceKey<ShrineUpgrade> key;

    public HasShrineUpgradePredicate(ResourceLocation upgradeId) {
        this(upgradeId, 1);
    }

    public HasShrineUpgradePredicate(ResourceLocation upgradeId, int count) {
        this(IDataManager.createKey(ElementalCraft.SHRINE_UPGRADE_MANAGER_KEY, upgradeId), count);
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
}
