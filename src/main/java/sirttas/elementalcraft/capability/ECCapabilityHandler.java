package sirttas.elementalcraft.capability;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.EmptyElementStorage;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.AbstractECCraftingBlockEntity;
import sirttas.elementalcraft.block.entity.AbstractIERBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.pipe.upgrade.capability.PipeUpgradeCapabilities;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.block.source.trait.holder.WrapperSourceTraitHolder;
import sirttas.elementalcraft.container.IContainerBlockEntity;
import sirttas.elementalcraft.data.attachment.ECDataAttachments;
import sirttas.elementalcraft.entity.player.PlayerElementStorage;
import sirttas.elementalcraft.entity.player.PlayerSpellTickManager;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.elemental.LensItem;
import sirttas.elementalcraft.item.holder.AbstractElementHolderItem;
import sirttas.elementalcraft.jewel.handler.ClientJewelHandler;
import sirttas.elementalcraft.jewel.handler.IJewelHandler;
import sirttas.elementalcraft.jewel.handler.JewelHandler;
import sirttas.elementalcraft.spell.tick.ISpellTickManager;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECCapabilityHandler {

    private ECCapabilityHandler() {}

    @SubscribeEvent
    public static void registerProviders(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(ElementalCraftCapabilities.ElementStorage.BLOCK, ECBlockEntityTypes.SOURCE.get(), (blockEntity, v) -> blockEntity.getElementStorage());
        event.registerBlockEntity(ElementalCraftCapabilities.SourceTrait.BLOCK, ECBlockEntityTypes.SOURCE.get(), (pedestal, v) -> pedestal.getTraitHolder());

        List.of(
                ECBlockEntityTypes.CONTAINER,
                ECBlockEntityTypes.RESERVOIR,
                ECBlockEntityTypes.CREATIVE_CONTAINER
        ).forEach(t -> event.registerBlockEntity(ElementalCraftCapabilities.ElementStorage.BLOCK, t.get(), (blockEntity, v) -> blockEntity.getElementStorage()));

        List.of(
                ECBlockEntityTypes.SOLAR_SYNTHESIZER,
                ECBlockEntityTypes.MANA_SYNTHESIZER
        ).forEach(t -> {
            var type = t.get();

            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, IContainerBlockEntity::getItemHandler);
            event.registerBlockEntity(ElementalCraftCapabilities.RuneHandler.BLOCK, type, (blockEntity, v) -> blockEntity.getRuneHandler());
        });

        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.INFUSER);
        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.BINDER);
        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.BINDER_IMPROVED);
        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.CRYSTALLIZER);
        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.INSCRIBER);
        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.WATER_MILL_GRINDSTONE);
        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.AIR_MILL_GRINDSTONE);
        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.WATER_MILL_WOOD_SAW);
        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.AIR_MILL_WOOD_SAW);
        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.ENCHANTMENT_LIQUEFIER);
        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.PURE_INFUSER);
        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.FIRE_FURNACE);
        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.PURIFIER);

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ECBlockEntityTypes.SOURCE_BREEDER.get(), IContainerBlockEntity::getItemHandler);
        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandler.BLOCK, ECBlockEntityTypes.SOURCE_BREEDER.get(), (blockEntity, v) -> blockEntity.getRuneHandler());

        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandler.BLOCK, ECBlockEntityTypes.EXTRACTOR.get(), (blockEntity, v) -> blockEntity.getRuneHandler());
        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandler.BLOCK, ECBlockEntityTypes.DIFFUSER.get(), (blockEntity, v) -> blockEntity.getRuneHandler());
        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandler.BLOCK, ECBlockEntityTypes.SORTER.get(), (blockEntity, v) -> blockEntity.getRuneHandler());

        event.registerBlockEntity(ElementalCraftCapabilities.ElementTransferer.BLOCK, ECBlockEntityTypes.PIPE.get(), (blockEntity, v) -> blockEntity.getTransferer());

        PipeUpgradeCapabilities.register(PipeUpgradeCapabilities.RUNE_HANDLER, PipeUpgradeTypes.ELEMENT_PUMP.get(), (upgrade, v) -> upgrade.getRuneHandler());
        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandler.BLOCK, ECBlockEntityTypes.PIPE.get(), PipeUpgradeCapabilities.RUNE_HANDLER.getBlockCapabilityProvider());

        registerIERCapabilities(event, ECBlockEntityTypes.EVAPORATOR);
        registerIERCapabilities(event, ECBlockEntityTypes.PEDESTAL);
        registerSourceBreederPedestalCapabilities(event);

        List.of(
                ECBlockEntityTypes.FIRE_PYLON,
                ECBlockEntityTypes.VACUUM_SHRINE,
                ECBlockEntityTypes.GROWTH_SHRINE,
                ECBlockEntityTypes.HARVEST_SHRINE,
                ECBlockEntityTypes.LUMBER_SHRINE,
                ECBlockEntityTypes.LAVA_SHRINE,
                ECBlockEntityTypes.ORE_SHRINE,
                ECBlockEntityTypes.OVERLOAD_SHRINE,
                ECBlockEntityTypes.SWEET_SHRINE,
                ECBlockEntityTypes.ENDER_LOCK_SHRINE,
                ECBlockEntityTypes.BREEDING_SHRINE,
                ECBlockEntityTypes.GROVE_SHRINE,
                ECBlockEntityTypes.SPRING_SHRINE,
                ECBlockEntityTypes.BUDDING_SHRINE,
                ECBlockEntityTypes.SPAWNING_SHRINE
        ).forEach(t -> event.registerBlockEntity(ElementalCraftCapabilities.ElementStorage.BLOCK, t.get(), (blockEntity, v) -> blockEntity.getElementStorage()));

        event.registerBlockEntity(ElementalCraftCapabilities.ElementTransferer.BLOCK, ECBlockEntityTypes.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE.get(), (blockEntity, v) -> blockEntity.getTransferer());

        deferBlockCapabilityBellow(event, Capabilities.ItemHandler.BLOCK, ECBlocks.AIR_MILL_GRINDSTONE, ECBlocks.AIR_MILL_WOOD_SAW, ECBlocks.ENCHANTMENT_LIQUEFIER, ECBlocks.SOURCE_BREEDER);
        deferBlockCapabilityBellow(event, ElementalCraftCapabilities.RuneHandler.BLOCK, ECBlocks.AIR_MILL_GRINDSTONE, ECBlocks.AIR_MILL_WOOD_SAW, ECBlocks.ENCHANTMENT_LIQUEFIER, ECBlocks.SOURCE_BREEDER);
        deferBlockCapabilityBellow(event, ElementalCraftCapabilities.ElementStorage.BLOCK, ECBlocks.FIRE_RESERVOIR, ECBlocks.WATER_RESERVOIR, ECBlocks.EARTH_RESERVOIR, ECBlocks.AIR_RESERVOIR);

        registerElementHolderCapabilities(event, ECItems.FIRE_HOLDER);
        registerElementHolderCapabilities(event, ECItems.WATER_HOLDER);
        registerElementHolderCapabilities(event, ECItems.EARTH_HOLDER);
        registerElementHolderCapabilities(event, ECItems.AIR_HOLDER);
        registerElementHolderCapabilities(event, ECItems.PURE_HOLDER);

        event.registerItem(ElementalCraftCapabilities.SourceTrait.ITEM, (stack, v) -> new WrapperSourceTraitHolder(stack), ECItems.RECEPTACLE.get());

        registerLensCapabilities(event, ECItems.FIRE_LENS);
        registerLensCapabilities(event, ECItems.WATER_LENS);
        registerLensCapabilities(event, ECItems.EARTH_LENS);
        registerLensCapabilities(event, ECItems.AIR_LENS);

        registerPlayerCapabilities(event);
    }

    @SafeVarargs
    private static <T, C> void deferBlockCapabilityBellow(RegisterCapabilitiesEvent event, BlockCapability<T, C> capability, Supplier<? extends Block>... blocks) {
        event.registerBlock(capability, (l, p, s, be, c) -> {
            if (s.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
                return l.getCapability(capability, p.below(), null, null, c);
            }
            return null;
        }, Arrays.stream(blocks)
                .map(Supplier::get)
                .toArray(Block[]::new));
    }

    private static void registerElementHolderCapabilities(RegisterCapabilitiesEvent event, Supplier<? extends AbstractElementHolderItem> holder) {
        var item = holder.get();

        event.registerItem(ElementalCraftCapabilities.ElementStorage.ITEM, (stack, v) -> item.getElementStorage(stack), item);
    }

    private static void registerLensCapabilities(RegisterCapabilitiesEvent event, Supplier<? extends LensItem> holder) {
        var item = holder.get();

        event.registerItem(ElementalCraftCapabilities.ElementStorage.ITEM_LENS, item::getStorage, item);
    }

    @SuppressWarnings("unchecked")
    private static void registerCraftingBlockEntityCapabilities(RegisterCapabilitiesEvent event, DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<? extends AbstractECCraftingBlockEntity<?, ?>>> holder) {
        var type = (BlockEntityType<AbstractECCraftingBlockEntity<?, ?>>) holder.get();

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, IContainerBlockEntity::getItemHandler);
        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandler.BLOCK, type, (blockEntity, v) -> blockEntity.getRuneHandler());
    }

    private static void registerIERCapabilities(RegisterCapabilitiesEvent event, DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<? extends AbstractIERBlockEntity>> holder) {
        registerIERCapabilities(event, holder.get());
    }

    private static void registerIERCapabilities(RegisterCapabilitiesEvent event, BlockEntityType<? extends AbstractIERBlockEntity> type) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, IContainerBlockEntity::getItemHandler);
        event.registerBlockEntity(ElementalCraftCapabilities.ElementStorage.BLOCK, type, (blockEntity, v) -> blockEntity.getElementStorage());
        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandler.BLOCK, type, (blockEntity, v) -> blockEntity.getRuneHandler());
    }

    private static void registerSourceBreederPedestalCapabilities(RegisterCapabilitiesEvent event) {
        var type = ECBlockEntityTypes.SOURCE_BREEDER_PEDESTAL.get();

        registerIERCapabilities(event, type);
        event.registerBlockEntity(ElementalCraftCapabilities.SourceTrait.BLOCK, type, (pedestal, v) -> pedestal.getTraitHolder());
    }

    private static void registerPlayerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(ElementalCraftCapabilities.ElementStorage.ENTITY, EntityType.PLAYER, (player, v) -> new PlayerElementStorage(player));
        event.registerEntity(ElementalCraftCapabilities.ElementStorage.ENTITY_FOR_ELEMENT, EntityType.PLAYER, (player, t) -> {
            if (t == null || t == ElementType.NONE) {
                return EmptyElementStorage.getSingle(ElementType.NONE);
            }

            var s = player.getCapability(ElementalCraftCapabilities.ElementStorage.ENTITY, null);

            if (s == null) {
                return EmptyElementStorage.getSingle(t);
            }
            return s.forElement(t);
        });
        event.registerEntity(ISpellTickManager.CAPABILITY, EntityType.PLAYER, (player, v) -> {
            var spellTickManager = player.getData(ECDataAttachments.SPELL_TICK_MANAGER);

            return player instanceof ServerPlayer serverPlayer ? new PlayerSpellTickManager(serverPlayer, spellTickManager) : spellTickManager;
        });
        event.registerEntity(IJewelHandler.CAPABILITY, EntityType.PLAYER, (player, v) -> {
            if (player.level().isClientSide) {
                return new ClientJewelHandler();
            } else {
                return new JewelHandler(player);
            }
        });
    }
}
