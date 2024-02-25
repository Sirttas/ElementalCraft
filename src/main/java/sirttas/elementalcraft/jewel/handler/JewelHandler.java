package sirttas.elementalcraft.jewel.handler;

import com.google.common.collect.Multimap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.storage.InfiniteElementStorage;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.attack.AbstractAttackJewel;
import sirttas.elementalcraft.jewel.defence.DefenceJewel;
import sirttas.elementalcraft.jewel.effect.EffectJewel;
import sirttas.elementalcraft.network.payload.PayloadHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class JewelHandler implements IJewelHandler {
    private final Entity entity;
    private List<Jewel> activeJewels;
    private Multimap<Attribute, AttributeModifier> oldAttributes;


    public JewelHandler(Entity entity) {
        this.entity = entity;
        activeJewels = new ArrayList<>();
    }

    @Nonnull
    @Override
    public List<Jewel> getActiveJewels() {
        return List.copyOf(activeJewels);
    }

    public void tick() {
        var elementStorage = entity.getCapability(ElementalCraftCapabilities.ElementStorage.ENTITY);

        if (elementStorage == null) {
            elementStorage = InfiniteElementStorage.INSTANCE;
        }

        List<Jewel> jewels = new ArrayList<>();

        for (Jewel jewel : JewelHelper.getAllJewels(entity)) {
            if (jewel.isActive(entity, elementStorage)) {
                jewels.add(jewel);
                if (jewel.isTicking()) {
                    jewel.consume(entity, elementStorage);
                    if (jewel instanceof EffectJewel effectJewel) {
                        effectJewel.apply(entity);
                    }
                }
            }
        }
        if (jewels.size() != activeJewels.size()) {
            jewels.sort(Comparator.comparing(Jewel::getKey));
        }
        if (!jewels.equals(activeJewels)) {
            activeJewels = jewels;
            this.onActiveJewelsChanged();
        }
    }

    private void onActiveJewelsChanged() {
        this.reloadAttributes();
        if (this.entity instanceof ServerPlayer player) {
            PayloadHelper.sendToPlayer(player, new ActiveJewelsPayload(this));
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
    public static void onLivingDamage(@Nonnull LivingDamageEvent event) {
        var source = event.getSource();

        if (source.is(ECTags.DamageTypes.BYPASSES_JEWELS)) {
            return;
        }

        var target = event.getEntity();

        for (var jewel : JewelHelper.getActiveJewels(target)) {
            if (jewel instanceof DefenceJewel defenceJewel) {
                defenceJewel.onHurt(target, source, event.getAmount());
                if (!jewel.isTicking()) {
                    jewel.consume(target);
                }
            }
        }

        var attacker = source.getEntity();

        if (attacker instanceof Projectile projectile) {
            attacker = projectile.getOwner();
        }
        if (attacker != null) {
            for (var jewel : JewelHelper.getActiveJewels(attacker)) {
                if (jewel instanceof AbstractAttackJewel attackJewel) {
                    attackJewel.onAttack(attacker, target);
                    if (!jewel.isTicking()) {
                        jewel.consume(attacker);
                    }
                }
            }
        }

    }
}
