package sirttas.elementalcraft.block.airmill;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.component.ECDataComponents;

public class AirMillDamageRangeSelectItemModelProperty implements RangeSelectItemModelProperty {

    private static final AirMillDamageRangeSelectItemModelProperty INSTANCE = new AirMillDamageRangeSelectItemModelProperty();
    public static final MapCodec<AirMillDamageRangeSelectItemModelProperty> MAP_CODEC = MapCodec.unit(INSTANCE);

    private AirMillDamageRangeSelectItemModelProperty() {}

    public static AirMillDamageRangeSelectItemModelProperty get() {
        return INSTANCE;
    }

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        float damage = itemStack.getOrDefault(ECDataComponents.AIR_MILL_DAMAGE, 0);
        float maxDamage = AirMill.getMaxDamage();
        return Mth.clamp(damage / maxDamage, 0.0F, 1.0F);
    }

    @Override
    public MapCodec<? extends RangeSelectItemModelProperty> type() {
        return MAP_CODEC;
    }
}
