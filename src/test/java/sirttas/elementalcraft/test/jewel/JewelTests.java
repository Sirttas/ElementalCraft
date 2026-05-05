package sirttas.elementalcraft.test.jewel;

import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.test.annotation.ElementalCraftTest;
import sirttas.elementalcraft.test.annotation.RegistrySource;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ElementalCraftTest
public class JewelTests {

    @ParameterizedTest
    @DisplayName("Check that jewels cn be placed on gear.")
    @RegistrySource(namespace = ElementalCraftApi.MODID, value = "jewel")
    public void should_bePlaceableOnGear(Jewel jewel) {
        // Given
        var mockPlayer = Mockito.mock(Player.class);
        var inventory = new Inventory(mockPlayer, new EntityEquipment());
        var anvilMenu = new AnvilMenu(0, inventory);

        anvilMenu.setItem(0, anvilMenu.getStateId(), new ItemStack(Items.DIAMOND_CHESTPLATE));
        anvilMenu.setItem(1, anvilMenu.getStateId(), new ItemStack(jewel));

        // When
        anvilMenu.createResult();

        // Then
        assertThat(anvilMenu.getSlot(anvilMenu.getResultSlot()).getItem())
                .isNotEmpty()
                .is(Items.DIAMOND_CHESTPLATE)
                .hasDataComponentWithValue(ECDataComponents.JEWEL, jewel);
    }

}
