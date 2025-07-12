package sirttas.elementalcraft.jewel.effect;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.Jewels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class DemigodJewel extends EffectJewel {

    public static final String NAME = "demigod";

    public DemigodJewel() {
        super(ElementType.AIR, 10000, false,
                new MobEffectInstance(MobEffects.REGENERATION, 900, 1),
                new MobEffectInstance(MobEffects.ABSORPTION, 100, 1),
                new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
    }

    public static boolean trigger(LivingEntity entity) {
        var demigod = Jewels.DEMIGOD.get();

        if (!JewelHelper.hasJewel(entity, demigod)) {
            return false;
        }
        demigod.apply(entity);
        demigod.consume(entity);
        return true;
    }

    @Override
    public void apply(LivingEntity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            serverPlayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
            CriteriaTriggers.USED_TOTEM.trigger(serverPlayer, new ItemStack(Items.TOTEM_OF_UNDYING));
        }
        entity.setHealth(1.0F);
        super.apply(entity);
        entity.level().broadcastEntityEvent(entity, EntityEvent.TALISMAN_ACTIVATE);
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        if (entity instanceof Player player && super.isActive(entity, elementStorage)) {
            return ECContainerHelper.getSlotFor(player.getInventory(), new ItemStack(Items.TOTEM_OF_UNDYING)) >= 0;
        }
        return false;
    }


    @Override
    public void consume(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        super.consume(entity, elementStorage);
        if (!(entity instanceof Player player)) {
            return;
        }

        var inv = player.getInventory();
        var slot = ECContainerHelper.getSlotFor(inv, new ItemStack(Items.TOTEM_OF_UNDYING));

        if (slot >= 0) {
            inv.setItem(slot, ItemStack.EMPTY);
        }
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        tooltip.add(Component.translatable("tooltip.elementalcraft.demigod").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }

}
