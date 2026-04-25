package sirttas.elementalcraft.item.holder;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItems;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public record ElementHolderTestCaseHolder(
        ElementType type,
        Supplier<? extends AbstractElementHolderItem> item
) {

    public static final List<ElementHolderTestCaseHolder> HOLDERS = List.of(
            of(ElementType.FIRE, ECItems.FIRE_HOLDER),
            of(ElementType.WATER, ECItems.WATER_HOLDER),
            of(ElementType.EARTH, ECItems.EARTH_HOLDER),
            of(ElementType.AIR, ECItems.AIR_HOLDER),
            of(ElementType.FIRE, ECItems.PURE_HOLDER),
            of(ElementType.WATER, ECItems.PURE_HOLDER),
            of(ElementType.EARTH, ECItems.PURE_HOLDER),
            of(ElementType.AIR, ECItems.PURE_HOLDER)
    );

    public static final String GROUP = "stacks.holder";

    public static ElementHolderTestCaseHolder of(ElementType type, Supplier< ? extends AbstractElementHolderItem> item) {
        return new ElementHolderTestCaseHolder(type, item);
    }

    public int getTransferAmount() {
        return item.get().getTransferAmount();
    }

    public Test createTest(String name, String description, String template, BiConsumer<ECGameTestHelper, ElementHolderTestCaseHolder> function) {
        return ECGameTestUtils.createTest(GROUP, name, description, template, h -> function.accept(h, this));
    }

    public Player mockPlayer(ECGameTestHelper helper) {
        return mockPlayer(helper, Vec3.ZERO);
    }

    public Player mockPlayer(ECGameTestHelper helper, Vec3 pos) {
    	var player = helper.makeMockPlayer(GameType.SURVIVAL);

        helper.moveEntityTo(player, pos);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(item.get()));
        helper.getLevel().addFreshEntity(player);
        return player;
    }
}
