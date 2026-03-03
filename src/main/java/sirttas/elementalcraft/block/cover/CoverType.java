package sirttas.elementalcraft.block.cover;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import javax.annotation.Nonnull;

public enum CoverType implements StringRepresentable {
    NONE("none"),
    FRAME("frame"),
    COVERED("covered");

    public static final Codec<CoverType> CODEC = StringRepresentable.fromEnum(CoverType::values);
    public static final EnumProperty<CoverType> PROPERTY = EnumProperty.create("cover", CoverType.class);

    private final String name;

    CoverType(String name) {
        this.name = name;
    }

    @Nonnull
    @Override
    public String getSerializedName() {
        return this.name;
    }
}
