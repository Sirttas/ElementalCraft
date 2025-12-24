package sirttas.elementalcraft.jewel.handler;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.storage.InfiniteElementStorage;
import sirttas.elementalcraft.attributes.AttributesHelper;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.attack.AbstractAttackJewel;
import sirttas.elementalcraft.jewel.defence.DefenceJewel;
import sirttas.elementalcraft.jewel.effect.EffectJewel;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class JewelHandler implements IJewelHandler {
    private final Entity entity;
    private List<Jewel> activeJewels;
    private Multimap<Holder<Attribute>, AttributeModifier> oldAttributes;


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
        var elementStorage = entity.getCapability(ElementalCraftCapabilities.ElementStorages.ENTITY);

        if (elementStorage == null) {
            elementStorage = InfiniteElementStorage.INSTANCE;
        }

        List<Jewel> jewels = new ArrayList<>();

        for (Jewel jewel : JewelHelper.getAllJewels(entity)) {
            if (jewel.isActive(entity, elementStorage)) {
                jewels.add(jewel);
                if (jewel.isTicking()) {
                    jewel.consume(entity, elementStorage);
                    if (jewel instanceof EffectJewel effectJewel && entity instanceof LivingEntity livingEntity) {
                        effectJewel.apply(livingEntity);
                    }
                }
            }
        }

        jewels.sort(Comparator.comparing(Jewel::getKey));
        if (!jewels.equals(activeJewels)) {
            activeJewels = jewels;
            this.onActiveJewelsChanged();
        }
    }

    private void onActiveJewelsChanged() {
        this.reloadAttributes();
        if (this.entity instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new ActiveJewelsPayload(this));
        }
    }

    private void reloadAttributes() {
        if (entity instanceof LivingEntity livingEntity) {
            var attributes = JewelHelper.getJewelsAttribute(entity);
            var attributeMap = livingEntity.getAttributes();



            if (oldAttributes != null) {
                AttributesHelper.removeAttributes(attributeMap, oldAttributes);
            }
            AttributesHelper.addAttributes(attributeMap, attributes);
            oldAttributes = attributes;
        }
    }

    @SubscribeEvent
    public static void onPreLivingDamage(@Nonnull LivingDamageEvent.Pre event) {
        var source = event.getSource();

        if (source.is(ECTags.DamageTypes.BYPASSES_DEFENSE_JEWELS)) {
            return;
        }

        var target = event.getEntity();

        for (var jewel : JewelHelper.getActiveJewels(target)) {
            if (jewel instanceof DefenceJewel defenceJewel) {
                event.setNewDamage(defenceJewel.onHurt(target, source, event.getNewDamage()));
                if (!jewel.isTicking()) {
                    jewel.consume(target);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPostLivingDamage(@Nonnull LivingDamageEvent.Post event) {
        var source = event.getSource();

        if (source.is(ECTags.DamageTypes.BYPASSES_ATTACK_JEWELS)) {
            return;
        }

        var target = event.getEntity();
        var attacker = source.getEntity();

        if (attacker instanceof Projectile projectile) {
            attacker = projectile.getOwner();
        }
        if (attacker == null) {
            return;
        }
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
