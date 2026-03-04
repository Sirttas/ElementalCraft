package sirttas.elementalcraft.block.pipe.upgrade.type;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgradeModelShaper;
import sirttas.elementalcraft.client.model.ECModelShapers;

import javax.annotation.Nonnull;

public class PipeUpgradeType<T extends PipeUpgrade> implements ItemLike {

    public final Factory<T> factory;

    private Item item;
    private Identifier key;
    private String descriptionId;
    private ResourceKey<LootTable> lootTable;
    @OnlyIn(Dist.CLIENT)
    private BakedModel model;


    public PipeUpgradeType(Factory<T> factory) {
        this.factory = factory;
    }

    public T create(ElementPipeBlockEntity pipe, Direction direction) {
        return factory.create(pipe, direction);
    }

    @Nonnull
    @Override
    public Item asItem() {
        if (item == null) {
            item = PipeUpgradeTypes.getUpgradeItem(this);
        }
        return item;
    }

    @Nonnull
    public Identifier getKey() {
        if (key == null) {
            key = PipeUpgradeTypes.REGISTRY.getKey(this);
        }

        if (key == null) {
            throw new IllegalStateException("PipeUpgradeType " + this + " is missing a registry name!");
        }
        return key;
    }

    @Nonnull
    public String getDescriptionId() {
        if (descriptionId == null) {
            var id = getKey();

            descriptionId = "elementalcraft.pipe_upgrade." + id.getNamespace() + '.' + id.getPath();
        }
        return descriptionId;
    }

    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public BakedModel getModel() {
        if (model == null) {
            model = ECModelShapers.get(PipeUpgradeModelShaper.NAME).getBlockModel(this);
        }
        return model;
    }

    public ResourceKey<LootTable> getLootTable() {
        if (lootTable == null) {
            var k = getKey();

            lootTable = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(k.getNamespace(), PipeUpgrade.FOLDER + k.getPath()));
        }
        return lootTable;
    }

    @FunctionalInterface
    public interface Factory<T extends PipeUpgrade> {
        T create(ElementPipeBlockEntity pipe, Direction direction);
    }
}
