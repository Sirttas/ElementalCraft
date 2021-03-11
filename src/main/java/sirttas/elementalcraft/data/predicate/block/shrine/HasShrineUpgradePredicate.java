package sirttas.elementalcraft.data.predicate.block.shrine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicateType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.shrine.AbstractTileShrine;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;

public class HasShrineUpgradePredicate implements IShrinePredicate {

	public static final String NAME = "has_shrien_upgrade";
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static final BlockPosPredicateType<HasShrineUpgradePredicate> TYPE = null;
	public static final Codec<HasShrineUpgradePredicate> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ResourceLocation.CODEC.fieldOf(ECNames.SHRINE_UPGRADE).forGetter(p -> p.upgradeId),
			Codec.INT.optionalFieldOf(ECNames.COUNT, 1).forGetter(p -> p.count)
	).apply(builder, HasShrineUpgradePredicate::new));

	private final int count;
	private final ResourceLocation upgradeId;
	private ShrineUpgrade upgrade;

	public HasShrineUpgradePredicate(ShrineUpgrade upgrade) {
		this(upgrade, 1);
	}

	public HasShrineUpgradePredicate(ShrineUpgrade upgrade, int count) {
		this(upgrade.getId(), count);
		this.upgrade = upgrade;
	}

	public HasShrineUpgradePredicate(ResourceLocation upgradeId) {
		this(upgradeId, 1);
	}

	public HasShrineUpgradePredicate(ResourceLocation upgradeId, int count) {
		this.upgradeId = upgradeId;
		this.count = count;
	}

	@Override
	public boolean test(AbstractTileShrine shrine) {
		if (upgrade == null) {
			upgrade = ElementalCraft.SHREINE_UPGRADE_MANAGER.get(upgradeId);
		}
		return shrine.getUpgradeCount(upgrade) >= count;
	}

	@Override
	public BlockPosPredicateType<HasShrineUpgradePredicate> getType() {
		return TYPE;
	}
}
