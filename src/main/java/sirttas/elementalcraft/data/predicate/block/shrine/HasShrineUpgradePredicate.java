package sirttas.elementalcraft.data.predicate.block.shrine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.dpanvil.api.data.IDataWrapper;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicateType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;

public class HasShrineUpgradePredicate implements IShrinePredicate {

	public static final String NAME = "has_shrien_upgrade";
	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final BlockPosPredicateType<HasShrineUpgradePredicate> TYPE = null;
	public static final Codec<HasShrineUpgradePredicate> CODEC = RecordCodecBuilder.create(builder -> builder.group(
	        IDataWrapper.codec(ElementalCraft.SHRINE_UPGRADE_MANAGER).fieldOf(ECNames.SHRINE_UPGRADE).forGetter(p -> p.upgrade),
			Codec.INT.optionalFieldOf(ECNames.COUNT, 1).forGetter(p -> p.count)
	).apply(builder, HasShrineUpgradePredicate::new));

	private final int count;
	private IDataWrapper<ShrineUpgrade> upgrade;

    public HasShrineUpgradePredicate(ResourceLocation upgradeId) {
        this(upgradeId, 1);
    }

    public HasShrineUpgradePredicate(ResourceLocation upgradeId, int count) {
        this(ElementalCraft.SHRINE_UPGRADE_MANAGER.getWrapper(upgradeId), count);
    }

    public HasShrineUpgradePredicate(IDataWrapper<ShrineUpgrade> upgrade) {
        this(upgrade, 1);
    }

    public HasShrineUpgradePredicate(IDataWrapper<ShrineUpgrade> upgrade, int count) {
        this.upgrade = upgrade;
        this.count = count;
    }

	@Override
	public boolean test(AbstractShrineBlockEntity shrine) {
		return upgrade.isPresent() && shrine.getUpgradeCount(upgrade.get()) >= count;
	}

	@Override
	public BlockPosPredicateType<HasShrineUpgradePredicate> getType() {
		return TYPE;
	}
}
