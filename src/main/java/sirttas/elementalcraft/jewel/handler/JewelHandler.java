package sirttas.elementalcraft.jewel.handler;

import com.google.common.collect.Multimap;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.attack.AbstractAttackJewel;
import sirttas.elementalcraft.jewel.defence.DefenceJewel;
import sirttas.elementalcraft.jewel.effect.EffectJewel;
import sirttas.elementalcraft.network.message.MessageHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class JewelHandler implements IJewelHandler {

    private static final List<JewelHandler> FUTURE_HANDLERS = new ArrayList<>();
    private static final List<JewelHandler> HANDLERS = new ArrayList<>();

    private final Entity entity;
    private List<Jewel> activeJewels;
    private Multimap<Attribute, AttributeModifier> oldAttributes;

    private JewelHandler(Entity entity) {
        this.entity = entity;
        activeJewels = new ArrayList<>();
        synchronized (FUTURE_HANDLERS) {
            FUTURE_HANDLERS.add(this);
        }
    }

    @Nullable
    public static ICapabilityProvider createProvider(Entity entity) {
        if (JEWEL_HANDLER_CAPABILITY != null) {
            var handler = new JewelHandler(entity);

            return new ICapabilityProvider() {
                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
                    return JEWEL_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> handler));
                }
            };
        }
        return null;
    }

    @Nonnull
    @Override
    public List<Jewel> getActiveJewels() {
        return List.copyOf(activeJewels);
    }

    private void tick() {
        List<Jewel> jewels = new ArrayList<>();

        CapabilityElementStorage.get(entity).ifPresent(storage -> {
            for (Jewel jewel : JewelHelper.getAllJewels(entity)) {
                if (jewel.isActive(entity)) {
                    jewels.add(jewel);
                    if (jewel.isTicking()) {
                        jewel.consume(entity);
                        if (jewel instanceof EffectJewel effectJewel) {
                            effectJewel.apply(entity);
                        }
                    }
                }
            }
        });
        jewels.sort(Comparator.comparing(Jewel::getRegistryName));
        if (!jewels.equals(activeJewels)) {
            activeJewels = jewels;
            this.onActiveJewelsChanged();
        }
    }

    private void onActiveJewelsChanged() {
        this.reloadAttributes();
        if (this.entity instanceof ServerPlayer player) {
            MessageHelper.sendToPlayer(player, ActiveJewelsMessage.create(this));
        }
    }

    private void reloadAttributes() {
        if (entity instanceof LivingEntity livingEntity) {
            var attributes = JewelHelper.getJewelsAttribute(entity);
            var entityAttributes = livingEntity.getAttributes();

            if (oldAttributes != null) {
                entityAttributes.removeAttributeModifiers(oldAttributes);
            }
            entityAttributes.addTransientAttributeModifiers(attributes);
            oldAttributes = attributes;
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            synchronized (FUTURE_HANDLERS) {
                HANDLERS.addAll(FUTURE_HANDLERS);
                FUTURE_HANDLERS.clear();
            }
            var it = HANDLERS.iterator();

            while (it.hasNext()) {
                var handler = it.next();

                if (handler.entity.isRemoved()) {
                    it.remove();
                } else {
                    handler.tick();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(@Nonnull LivingAttackEvent event) {
        var attacker = event.getSource().getEntity();

        if (attacker instanceof Projectile projectile) {
            attacker = projectile.getOwner();
        }
        if (attacker != null) {
            for (var jewel : JewelHelper.getActiveJewels(attacker)) {
                if (jewel instanceof AbstractAttackJewel attackJewel) {
                    attackJewel.onAttack(attacker, event.getEntityLiving());
                    if (!jewel.isTicking()) {
                        jewel.consume(attacker);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(@Nonnull LivingDamageEvent event) {
        var target = event.getEntityLiving();

        for (var jewel : JewelHelper.getActiveJewels(target)) {
            if (jewel instanceof DefenceJewel defenceJewel) {
                defenceJewel.onHurt(target, event.getSource(), event.getAmount());
                if (!jewel.isTicking()) {
                    jewel.consume(target);
                }
            }
        }

    }
}
