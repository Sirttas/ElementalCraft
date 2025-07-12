package sirttas.elementalcraft.api.infusion.tool.effect;

import com.mojang.serialization.MapCodec;

public record ToolInfusionEffectType<T extends IToolInfusionEffect>(MapCodec<T> codec) {


}
