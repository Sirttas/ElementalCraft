package sirttas.elementalcraft.block.instrument.io.firefurnace;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.pureore.PureOreManager;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public record FireFurnaceTestHolder(
        Supplier<ItemStack> input,
        ItemLike output,
        boolean blast
) {

    public static final List<FireFurnaceTestHolder> HOLDERS = List.of(
            of(Items.COBBLESTONE, Items.STONE, false),
            of(Items.OAK_LOG, Items.CHARCOAL, false),
            of(Items.IRON_ORE, Items.IRON_INGOT),
            of(() -> PureOreManager.getInstance().createPureOre(ResourceLocation.fromNamespaceAndPath(ECNames.COMMON_TAGS_NAMESPACE, "iron")), Items.IRON_INGOT)
    );

    public static FireFurnaceTestHolder of(Supplier<ItemStack> input, ItemLike output, boolean blast) {
        return new FireFurnaceTestHolder(input, output, blast);
    }

    public static FireFurnaceTestHolder of(Supplier<ItemStack> input, ItemLike output) {
        return of(input, output, true);
    }
    public static FireFurnaceTestHolder of(ItemLike input, ItemLike output, boolean blast) {
        return of(() -> new ItemStack(input), output, blast);
    }

    public static FireFurnaceTestHolder of(ItemLike input, ItemLike output) {
        return of(input, output, true);
    }

    public Test createTest(String group, String name, String description, String template, BiConsumer<ECGameTestHelper, FireFurnaceTestHolder> function) {
        return ECGameTestUtils.createTest(group, name, description, template, h -> function.accept(h, this));
    }
}
