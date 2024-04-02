package sirttas.elementalcraft.block.instrument.io.firefurnace;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Rotation;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ElementalCraft;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public record FireFurnaceTestHolder(
        Supplier<ItemStack> input,
        ItemLike output,
        boolean blast
) {
    public static final String BATCH_NAME = "fireFurnace";

    public static final List<FireFurnaceTestHolder> HOLDERS = List.of(
            of(Items.COBBLESTONE, Items.STONE, false),
            of(Items.OAK_LOG, Items.CHARCOAL, false),
            of(Items.IRON_ORE, Items.IRON_INGOT),
            of(() -> ElementalCraft.PURE_ORE_MANAGER.createPureOre(new ResourceLocation("forge:iron")), Items.IRON_INGOT)
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

    public TestFunction createTestFunction(String name, BiConsumer<GameTestHelper, FireFurnaceTestHolder> function) {
        return createTestFunction(name, "firefurnacegametests.fire_furnace", function);
    }

    public TestFunction createTestFunction(String name, String template, BiConsumer<GameTestHelper, FireFurnaceTestHolder> function) {
        return ECGameTestHelper.createTestFunction(BATCH_NAME, name, template, Rotation.NONE, h -> function.accept(h, this));
    }
}
