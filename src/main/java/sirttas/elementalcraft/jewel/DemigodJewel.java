package sirttas.elementalcraft.jewel;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.container.ECContainerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class DemigodJewel extends Jewel {

    public static final String NAME = "demigod";

    public DemigodJewel() {
        super(ElementType.AIR, 10000);
        this.ticking = false;
    }

    public static boolean trigger(LivingEntity entity) {
        var demigod = Jewels.DEMIGOD.get();

        if (JewelHelper.hasJewel(entity, demigod)) {
            var itemStack = new ItemStack(Items.TOTEM_OF_UNDYING);

            if (entity instanceof ServerPlayer serverplayer) {
                serverplayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
                CriteriaTriggers.USED_TOTEM.trigger(serverplayer, itemStack);
            }
            entity.setHealth(1.0F);
            entity.removeAllEffects();
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
            entity.level.broadcastEntityEvent(entity, (byte) 35);
            demigod.consume(entity);
            return true;
        }
        return false;
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        if (super.isActive(entity, elementStorage) && entity instanceof Player player) {
            Inventory inv = player.getInventory();
            return ECContainerHelper.getSlotFor(inv, new ItemStack(Items.TOTEM_OF_UNDYING)) >= 0;
        }
        return false;
    }


    @Override
    public void consume(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        super.consume(entity, elementStorage);
        if (entity instanceof Player player) {
            Inventory inv = player.getInventory();
            var slot = ECContainerHelper.getSlotFor(inv, new ItemStack(Items.TOTEM_OF_UNDYING));

            if (slot >= 0) {
                inv.setItem(slot, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        tooltip.add(new TranslatableComponent("tooltip.elementalcraft.demigod").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }

}
