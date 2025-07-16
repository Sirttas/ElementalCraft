package sirttas.elementalcraft.interaction.jei.category.element.synthesis;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import org.jetbrains.annotations.NotNull;

public class FoodWidget implements IRecipeWidget {

    private final IDrawable fullFood;
    private final IDrawable halfFood;
    private final IDrawable emptyFood;
    private final ScreenPosition position;

    private int ticks = 0;

    FoodWidget(IGuiHelper guiHelper, ScreenPosition position) {
        fullFood = guiHelper.createDrawable(DrainingRecipeCategory.TEXTURE, 0, 9, 9, 9);
        halfFood = guiHelper.createDrawable(DrainingRecipeCategory.TEXTURE, 9, 9, 9, 9);
        emptyFood = guiHelper.createDrawable(DrainingRecipeCategory.TEXTURE, 18, 9, 9, 9);
        this.position = position;
    }

    @Override
    @NotNull
    public ScreenPosition getPosition() {
        return position;
    }

    @Override
    public void drawWidget(@NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (ticks < 20) {
            fullFood.draw(guiGraphics, 0, 0);
        } else if (ticks < 40) {
            halfFood.draw(guiGraphics, 0, 0);
        } else {
            emptyFood.draw(guiGraphics, 0, 0);
        }
    }

    @Override
    public void tick() {
        ticks++;
        if (ticks >= 60) {
            ticks = 0;
        }
    }
}
