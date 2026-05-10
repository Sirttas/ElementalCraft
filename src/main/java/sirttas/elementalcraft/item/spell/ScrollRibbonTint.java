package sirttas.elementalcraft.item.spell;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.spell.SpellHelper;

public class ScrollRibbonTint implements ItemTintSource {

    private static final ScrollRibbonTint INSTANCE = new ScrollRibbonTint();
    public static final MapCodec<ScrollRibbonTint> MAP_CODEC = MapCodec.unit(INSTANCE);

    public static ItemTintSource get() {
        return INSTANCE;
    }

    private ScrollRibbonTint() {}

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        return ARGB.opaque(SpellHelper.getSpell(itemStack).value().getColor());
    }

    @Override
    public MapCodec<ScrollRibbonTint> type() {
        return MAP_CODEC;
    }
}
