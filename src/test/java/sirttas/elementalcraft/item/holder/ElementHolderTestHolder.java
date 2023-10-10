package sirttas.elementalcraft.item.holder;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItems;

import java.util.List;
import java.util.function.BiConsumer;

public record ElementHolderTestHolder(
        ElementType type,
        RegistryObject< ? extends AbstractElementHolderItem> item
) {

    public static final List<ElementHolderTestHolder> HOLDERS = List.of(
            of(ElementType.FIRE, ECItems.FIRE_HOLDER),
            of(ElementType.WATER, ECItems.WATER_HOLDER),
            of(ElementType.EARTH, ECItems.EARTH_HOLDER),
            of(ElementType.AIR, ECItems.AIR_HOLDER),
            of(ElementType.FIRE, ECItems.PURE_HOLDER),
            of(ElementType.WATER, ECItems.PURE_HOLDER),
            of(ElementType.EARTH, ECItems.PURE_HOLDER),
            of(ElementType.AIR, ECItems.PURE_HOLDER)
    );

    public static final String BATCH_NAME = "element_holder";

    public static ElementHolderTestHolder of(ElementType type, RegistryObject< ? extends AbstractElementHolderItem> item) {
        return new ElementHolderTestHolder(type, item);
    }

    public int getTransferAmount() {
        return item.get().getTransferAmount();
    }

    public TestFunction createTestFunction(String name, String template, BiConsumer<GameTestHelper, ElementHolderTestHolder> function) {
        return ECGameTestHelper.createTestFunction(BATCH_NAME, name, template, Rotation.NONE, h -> function.accept(h, this));
    }

    public Player mockPlayer(GameTestHelper helper) {
    	var player = helper.makeMockPlayer();

        player.moveTo(helper.absoluteVec(Vec3.ZERO));
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(item.get()));
        helper.getLevel().addFreshEntity(player);
        return player;
    }
}
