package sirttas.elementalcraft.capability;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.EmptyElementStorage;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.ElementContainer;
import sirttas.elementalcraft.block.cover.Coverable;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.crafting.AbstractECCraftingBlockEntity;
import sirttas.elementalcraft.block.pipe.upgrade.capability.PipeUpgradeCapabilities;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;
import sirttas.elementalcraft.container.IContainerBlockEntity;
import sirttas.elementalcraft.container.IElementStorageBlocKEntity;
import sirttas.elementalcraft.container.IRuneableBlockEntity;
import sirttas.elementalcraft.data.attachment.ECDataAttachments;
import sirttas.elementalcraft.entity.player.PlayerElementStorage;
import sirttas.elementalcraft.entity.player.PlayerSpellTickManager;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.holder.AbstractElementHolderItem;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleItem;
import sirttas.elementalcraft.jewel.handler.ClientJewelHandler;
import sirttas.elementalcraft.jewel.handler.IJewelHandler;
import sirttas.elementalcraft.spell.tick.ISpellTickManager;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ECCapabilityHandler {

    private ECCapabilityHandler() {}

    @SubscribeEvent
    public static void registerProviders(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(ElementalCraftCapabilities.ElementStorages.BLOCK, ECBlockEntityTypes.SOURCE.get(), (blockEntity, v) -> blockEntity.getElementStorage());
        event.registerBlockEntity(ElementalCraftCapabilities.SourceTraits.BLOCK, ECBlockEntityTypes.SOURCE.get(), (pedestal, v) -> pedestal.getTraitHolder());

        List.of(
                ECBlockEntityTypes.CONTAINER,
                ECBlockEntityTypes.RESERVOIR,
                ECBlockEntityTypes.CREATIVE_CONTAINER
        ).forEach(t -> {
            event.registerBlockEntity(ElementContainer.CAPABILITY, t.get(), (blockEntity, v) -> blockEntity);
            event.registerBlockEntity(ElementalCraftCapabilities.ElementStorages.BLOCK, t.get(), (blockEntity, v) -> blockEntity.getElementStorage());
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
        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.FIRE_BLAST_FURNACE);
        registerCraftingBlockEntityCapabilities(event, ECBlockEntityTypes.PURIFIER);

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ECBlockEntityTypes.SOURCE_BREEDER.get(), IContainerBlockEntity::getItemHandler);
        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandlers.BLOCK, ECBlockEntityTypes.SOURCE_BREEDER.get(), (blockEntity, v) -> blockEntity.getRuneHandler());

        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandlers.BLOCK, ECBlockEntityTypes.EXTRACTOR.get(), (blockEntity, v) -> blockEntity.getRuneHandler());
        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandlers.BLOCK, ECBlockEntityTypes.DIFFUSER.get(), (blockEntity, v) -> blockEntity.getRuneHandler());
        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandlers.BLOCK, ECBlockEntityTypes.SORTER.get(), (blockEntity, v) -> blockEntity.getRuneHandler());
        event.registerBlockEntity(Coverable.CAPABILITY, ECBlockEntityTypes.SORTER.get(), (blockEntity, v) -> blockEntity);
        event.registerBlockEntity(Coverable.CAPABILITY, ECBlockEntityTypes.COVERABLE.get(), (blockEntity, v) -> blockEntity);

        event.registerBlockEntity(ElementalCraftCapabilities.ElementTransferers.BLOCK, ECBlockEntityTypes.PIPE.get(), (blockEntity, v) -> blockEntity.getTransferer());
        event.registerBlockEntity(Coverable.CAPABILITY, ECBlockEntityTypes.PIPE.get(), (blockEntity, v) -> blockEntity);

        PipeUpgradeCapabilities.register(PipeUpgradeCapabilities.RUNE_HANDLER, PipeUpgradeTypes.ELEMENT_PUMP.get(), (upgrade, v) -> upgrade.getRuneHandler());
        PipeUpgradeCapabilities.register(PipeUpgradeCapabilities.RUNE_HANDLER, PipeUpgradeTypes.ELEMENT_BEAM.get(), (upgrade, v) -> upgrade.getRuneHandler());
        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandlers.BLOCK, ECBlockEntityTypes.PIPE.get(), PipeUpgradeCapabilities.RUNE_HANDLER.getBlockCapabilityProvider());

        registerElementRunesCapabilities(event, ECBlockEntityTypes.CRACKING_SYNTHESIZER);
        registerIERCapabilities(event, ECBlockEntityTypes.COMBUSTION_SYNTHESIZER);
        registerElementRunesCapabilities(event, ECBlockEntityTypes.DRAINING_SYNTHESIZER);
        registerElementRunesCapabilities(event, ECBlockEntityTypes.VIBRATION_SYNTHESIZER);
        registerIERCapabilities(event, ECBlockEntityTypes.SOLAR_SYNTHESIZER);
        registerIERCapabilities(event, ECBlockEntityTypes.CULINARY_SYNTHESIZER);
        registerElementRunesCapabilities(event, ECBlockEntityTypes.SCULK_CRACKING_SYNTHESIZER);
        registerElementRunesCapabilities(event, ECBlockEntityTypes.AIR_MILL_SYNTHESIZER);

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
        ).forEach(t -> event.registerBlockEntity(ElementalCraftCapabilities.ElementStorages.BLOCK, t.get(), (blockEntity, v) -> blockEntity.getElementStorage()));

        event.registerBlockEntity(ElementalCraftCapabilities.ElementTransferers.BLOCK, ECBlockEntityTypes.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE.get(), (blockEntity, v) -> blockEntity.getTransferer());
        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandlers.BLOCK, ECBlockEntityTypes.GREATER_FORTUNE_SHRINE_UPGRADE.get(), (blockEntity, v) -> blockEntity.getRuneHandler());

        event.registerBlock(ElementalCraftCapabilities.ElementStorages.BLOCK_FOR_ELEMENT, (level, pos, state, blockEntity, context) -> {
            var storage = level.getCapability(ElementalCraftCapabilities.ElementStorages.BLOCK, pos, state, blockEntity, context != null ? context.direction() : null);

            if (storage != null) {
                return storage.forElement(context != null ? context.elementType() : ElementType.NONE);
            }
            return null;
        },
                ECBlocks.FIRE_SOURCE.get(),
                ECBlocks.WATER_SOURCE.get(),
                ECBlocks.EARTH_SOURCE.get(),
                ECBlocks.AIR_SOURCE.get(),
                ECBlocks.CONTAINER.get(),
                ECBlocks.SMALL_CONTAINER.get(),
                ECBlocks.CREATIVE_CONTAINER.get(),
                ECBlocks.FIRE_RESERVOIR.get(),
                ECBlocks.WATER_RESERVOIR.get(),
                ECBlocks.EARTH_RESERVOIR.get(),
                ECBlocks.AIR_RESERVOIR.get(),
                ECBlocks.CRACKING_SYNTHESIZER.get(),
                ECBlocks.COMBUSTION_SYNTHESIZER.get(),
                ECBlocks.DRAINING_SYNTHESIZER.get(),
                ECBlocks.VIBRATION_SYNTHESIZER.get(),
                ECBlocks.SOLAR_SYNTHESIZER.get(),
                ECBlocks.FIRE_PEDESTAL.get(),
                ECBlocks.WATER_PEDESTAL.get(),
                ECBlocks.EARTH_PEDESTAL.get(),
                ECBlocks.AIR_PEDESTAL.get(),
                ECBlocks.SOURCE_BREEDER_PEDESTAL.get(),
                ECBlocks.FIRE_PYLON.get(),
                ECBlocks.VACUUM_SHRINE.get(),
                ECBlocks.GROWTH_SHRINE.get(),
                ECBlocks.HARVEST_SHRINE.get(),
                ECBlocks.LUMBER_SHRINE.get(),
                ECBlocks.MELTING_SHRINE.get(),
                ECBlocks.ORE_SHRINE.get(),
                ECBlocks.OVERLOAD_SHRINE.get(),
                ECBlocks.SWEET_SHRINE.get(),
                ECBlocks.ENDER_LOCK_SHRINE.get(),
                ECBlocks.BREEDING_SHRINE.get(),
                ECBlocks.GROVE_SHRINE.get(),
                ECBlocks.SPRING_SHRINE.get(),
                ECBlocks.BUDDING_SHRINE.get(),
                ECBlocks.SPAWNING_SHRINE.get()
        );

        registerShrineUpgradeCapabilities(event, ECBlocks.ACCELERATION_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.RANGE_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.CAPACITY_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.EFFICIENCY_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.STRENGTH_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.OVERWHELMING_STRENGTH_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.OPTIMIZATION_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.FORTUNE_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.GREATER_FORTUNE_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.SILK_TOUCH_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.PLANTING_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.BONELESS_GROWTH_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.PICKUP_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.VORTEX_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.NECTAR_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.STEM_POLLINATION_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.PROTECTION_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.FILLING_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.SPRINGALINE_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.CERTUS_QUARTZ_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.CRYSTAL_HARVEST_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.CRYSTAL_GROWTH_SHRINE_UPGRADE.get());
        registerShrineUpgradeCapabilities(event, ECBlocks.TRANSLOCATION_SHRINE_UPGRADE.get());


        deferBlockCapabilityBellow(event, Capabilities.ItemHandler.BLOCK, ECBlocks.AIR_MILL_GRINDSTONE, ECBlocks.AIR_MILL_WOOD_SAW, ECBlocks.ENCHANTMENT_LIQUEFIER, ECBlocks.SOURCE_BREEDER);
        deferBlockCapabilityBellow(event, ElementalCraftCapabilities.RuneHandlers.BLOCK, ECBlocks.AIR_MILL_GRINDSTONE, ECBlocks.AIR_MILL_WOOD_SAW, ECBlocks.ENCHANTMENT_LIQUEFIER, ECBlocks.SOURCE_BREEDER, ECBlocks.AIR_MILL_SYNTHESIZER);
        deferBlockCapabilityBellow(event, ElementalCraftCapabilities.ElementStorages.BLOCK, ECBlocks.FIRE_RESERVOIR, ECBlocks.WATER_RESERVOIR, ECBlocks.EARTH_RESERVOIR, ECBlocks.AIR_RESERVOIR, ECBlocks.AIR_MILL_SYNTHESIZER);
        deferBlockCapabilityBellow(event, ElementalCraftCapabilities.ElementStorages.BLOCK_FOR_ELEMENT, ECBlocks.FIRE_RESERVOIR, ECBlocks.WATER_RESERVOIR, ECBlocks.EARTH_RESERVOIR, ECBlocks.AIR_RESERVOIR, ECBlocks.AIR_MILL_SYNTHESIZER);
        deferBlockCapabilityBellow(event, ElementContainer.CAPABILITY, ECBlocks.FIRE_RESERVOIR, ECBlocks.WATER_RESERVOIR, ECBlocks.EARTH_RESERVOIR, ECBlocks.AIR_RESERVOIR);

        registerElementHolderCapabilities(event, ECItems.FIRE_HOLDER);
        registerElementHolderCapabilities(event, ECItems.WATER_HOLDER);
        registerElementHolderCapabilities(event, ECItems.EARTH_HOLDER);
        registerElementHolderCapabilities(event, ECItems.AIR_HOLDER);
        registerElementHolderCapabilities(event, ECItems.PURE_HOLDER);

        event.registerItem(ElementalCraftCapabilities.ElementStorages.ITEM, (stack, v) -> ((ReceptacleItem) stack.getItem()).getElementStorage(stack), ECBlocks.FIRE_SOURCE.get(), ECBlocks.WATER_SOURCE.get(), ECBlocks.EARTH_SOURCE.get(), ECBlocks.AIR_SOURCE.get());
        event.registerItem(ElementalCraftCapabilities.SourceTraits.ITEM, (stack, v) -> ReceptacleItem.getTraitHolder(stack), ECBlocks.FIRE_SOURCE.get(), ECBlocks.WATER_SOURCE.get(), ECBlocks.EARTH_SOURCE.get(), ECBlocks.AIR_SOURCE.get());

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

    private static void registerShrineUpgradeCapabilities(RegisterCapabilitiesEvent event, AbstractShrineUpgradeBlock block) {
        event.registerBlock(ElementalCraftCapabilities.ShrineUpgrades.BLOCK, (level, pos, state, blockEntity, context) -> {
                    if (state.is(block) && block.getFacing(state) == context) {
                        return block.getUpgrade();
                    }
                    return null;
                }, block);
        event.registerItem(ElementalCraftCapabilities.ShrineUpgrades.ITEM, (stack, v) -> block.getUpgrade(), block);
    }

    private static void registerElementHolderCapabilities(RegisterCapabilitiesEvent event, Supplier<? extends AbstractElementHolderItem> holder) {
        var item = holder.get();

        event.registerItem(ElementalCraftCapabilities.ElementStorages.ITEM, (stack, v) -> item.getElementStorage(stack), item);
    }

    @SuppressWarnings("unchecked")
    private static void registerCraftingBlockEntityCapabilities(RegisterCapabilitiesEvent event, DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<? extends AbstractECCraftingBlockEntity<?, ?>>> holder) {
        var type = (BlockEntityType<AbstractECCraftingBlockEntity<?, ?>>) holder.get();

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, IContainerBlockEntity::getItemHandler);
        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandlers.BLOCK, type, (blockEntity, v) -> blockEntity.getRuneHandler());
    }

    private static <T extends BlockEntity & IContainerBlockEntity & IRuneableBlockEntity & IElementStorageBlocKEntity> void registerIERCapabilities(RegisterCapabilitiesEvent event, DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<T>> holder) {
        registerIERCapabilities(event, holder.get());
    }

    private static <T extends BlockEntity & IContainerBlockEntity & IRuneableBlockEntity & IElementStorageBlocKEntity> void registerIERCapabilities(RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, IContainerBlockEntity::getItemHandler);
        registerElementRunesCapabilities(event, type);
    }

    private static <T extends BlockEntity & IRuneableBlockEntity & IElementStorageBlocKEntity> void registerElementRunesCapabilities(RegisterCapabilitiesEvent event, DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<T>> holder) {
        registerElementRunesCapabilities(event, holder.get());
    }

    private static <T extends BlockEntity & IRuneableBlockEntity & IElementStorageBlocKEntity> void registerElementRunesCapabilities(RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
        event.registerBlockEntity(ElementalCraftCapabilities.ElementStorages.BLOCK, type, (blockEntity, v) -> blockEntity.getElementStorage());
        event.registerBlockEntity(ElementalCraftCapabilities.RuneHandlers.BLOCK, type, (blockEntity, v) -> blockEntity.getRuneHandler());
    }

    private static void registerSourceBreederPedestalCapabilities(RegisterCapabilitiesEvent event) {
        var type = ECBlockEntityTypes.SOURCE_BREEDER_PEDESTAL.get();

        registerIERCapabilities(event, type);
        event.registerBlockEntity(ElementalCraftCapabilities.SourceTraits.BLOCK, type, (pedestal, v) -> pedestal.getTraitHolder());
    }

    private static void registerPlayerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(ElementalCraftCapabilities.ElementStorages.ENTITY, EntityType.PLAYER, (player, v) -> new PlayerElementStorage(player));
        event.registerEntity(ElementalCraftCapabilities.ElementStorages.ENTITY_FOR_ELEMENT, EntityType.PLAYER, (player, t) -> {
            if (t == null || t == ElementType.NONE) {
                return EmptyElementStorage.getSingle(ElementType.NONE);
            }

            var s = player.getCapability(ElementalCraftCapabilities.ElementStorages.ENTITY, null);

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
                return player.getData(ECDataAttachments.JEWEL_HANDLER);
            }
        });
    }
}
