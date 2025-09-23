package sirttas.elementalcraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.gametest.ExtendedGameTestHelper;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.container.ElementContainer;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.rune.RuneItem;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleGameTestHelper;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelTestHelper;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class ECGameTestHelper extends ExtendedGameTestHelper {

    public ECGameTestHelper(GameTestInfo info) {
        super(info);
    }

    public InteractionResultHolder<ItemStack> useItem(Player player) {
        return useItem(player, InteractionHand.MAIN_HAND);
    }

    public InteractionResultHolder<ItemStack> useItem(Player player, InteractionHand hand) {
        var level = player.level();
        var stack = player.getItemInHand(hand);

        return stack.use(level, player, hand);
    }

    public void useItemOn(Player player, int x, int y, int z) {
        useItemOn(player, new BlockPos(x, y, z));
    }

    public void useItemOn(Player player, BlockPos pos) {
        useItemOn(player, pos, Direction.NORTH);
    }

    public void useItemOn(Player player, BlockPos pos, Direction direction) {
        var absolutePos = absolutePos(pos);
        var result = new BlockHitResult(Vec3.atCenterOf(absolutePos), direction, absolutePos, true);
        var stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (player.isShiftKeyDown() && !stack.isEmpty()) {
            UseOnContext useoncontext = new UseOnContext(player, InteractionHand.MAIN_HAND, result);
            stack.useOn(useoncontext);
            return;
        }
        useBlock(pos, player, result);
    }

    public void discardItems(BlockPos pos, int expansionAmount) {
        getLevel().getEntities(EntityType.ITEM, new AABB(absolutePos(pos)).inflate(expansionAmount), Entity::isAlive).forEach(e -> e.remove(Entity.RemovalReason.DISCARDED));
    }

    @Nonnull
    public Player mockChiselPlayer(BlockPos pos) {
        var player = makeMockPlayer();

        player.moveTo(absoluteVec(Vec3.atCenterOf(pos)));
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ECItems.SWIFT_ALLOY_CHISEL));
        return player;
    }

    public Player mockPlayerWithJewel(Supplier<? extends Jewel> jewel) {
        return mockPlayerWithJewel(new Vec3(1, 1, 1), jewel);
    }

    public Player mockPlayerWithJewel(Vec3 pos, Supplier<? extends Jewel> jewel) {
        var player = makeMockPlayer(GameType.SURVIVAL);

        player.moveTo(absoluteVec(pos));
        player.setItemSlot(EquipmentSlot.HEAD, JewelTestHelper.createWithJewel(Items.LEATHER_HELMET, jewel));
        player.setItemInHand(InteractionHand.OFF_HAND, JewelTestHelper.createFullPureHolder());
        getLevel().addFreshEntity(player);
        return player;
    }

    public Player mockPlayerWithSpell(Vec3 pos, Holder<Spell> spell) {
        var player = makeMockPlayer(GameType.SURVIVAL);

        player.moveTo(absoluteVec(pos));

        var scroll = new ItemStack(ECItems.SCROLL);

        SpellHelper.setSpell(scroll, spell);

        player.setItemInHand(InteractionHand.MAIN_HAND, scroll);
        player.setItemInHand(InteractionHand.OFF_HAND, JewelTestHelper.createFullPureHolder());
        getLevel().addFreshEntity(player);
        return player;
    }

    @Nonnull
    public Player mockReceptaclePlayer() {
        return mockReceptaclePlayer(ElementType.NONE);
    }

    @Nonnull
    public Player mockReceptaclePlayer(ElementType type) {
        return mockReceptaclePlayer(type, -1);
    }

    @Nonnull
    public Player mockReceptaclePlayer(ElementType type, int elementAmount) {
        var player = makeMockPlayer();
        var receptacle = ReceptacleGameTestHelper.createSimpleReceptacle(type);

        if (elementAmount > 0) {
            receptacle.set(ECDataComponents.ELEMENT_AMOUNT, elementAmount);
        }

        player.moveTo(Vec3.atLowerCornerOf(this.testInfo.getStructureBlockPos()));
        player.setItemInHand(InteractionHand.MAIN_HAND, receptacle);
        return player;
    }

    public IElementStorage getElementStorage(BlockPos pos) {
        return getCapability(ElementalCraftCapabilities.ElementStorages.BLOCK, pos, null);
    }

    public ISingleElementStorage getElementContainer(BlockPos pos) {
        return ElementContainer.getElementContainer(getLevel(), absolutePos(pos));
    }

    public ISingleElementStorage requireElementContainer(BlockPos pos) {
        var container = getElementContainer(pos);

        if (container == null) {
            throw new GameTestAssertException("Expected ElementContainer at " + pos);
        }
        return container;
    }

    public <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(ItemStack input, ElementType elementType, Consumer<T> consumer) {
        runInstrument(List.of(input), elementType, true, consumer);
    }

    public <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(List<ItemStack> inputs, ElementType elementType, Consumer<T> consumer) {
        runInstrument(inputs, elementType, true, consumer);
    }

    public <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(BlockPos pos, ItemStack input, ElementType elementType, Consumer<T> consumer) {
        runInstrument(pos, List.of(input), elementType, true, consumer);
    }

    public <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(BlockPos pos, List<ItemStack> inputs, ElementType elementType, Consumer<T> consumer) {
        runInstrument(pos, inputs, elementType, true, consumer);
    }

    public <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(ItemStack input, ElementType elementType, boolean recipeAvailable, Consumer<T> consumer) {
        runInstrument(List.of(input), elementType, recipeAvailable, consumer);
    }

    public <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(List<ItemStack> inputs, ElementType elementType, boolean recipeAvailable, Consumer<T> consumer) {
        runInstrument(new BlockPos(0, 2, 0), inputs, elementType, recipeAvailable, consumer);
    }

    public <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(BlockPos pos, List<ItemStack> inputs, ElementType elementType, boolean recipeAvailable, Consumer<T> consumer) {
        T instrument = this.getBlockEntity(pos);
        var container = this.requireElementContainer(pos.below());

        this.startSequence().thenExecute(ECGameTestUtils.fixAssertions(() -> {
                    var inv = instrument.getInventory();

                    for (int i = 0; i < inputs.size(); i++) {
                        inv.setItem(i, inputs.get(i));
                    }
                    container.fill(elementType);

                    assertThat(instrument.isRecipeAvailable())
                            .withFailMessage(() -> recipeAvailable ? "Recipe is not available but it should be" : "Recipe is available but it should not be")
                            .isEqualTo(recipeAvailable);
                })).thenExecuteAfter(2, ECGameTestUtils.fixAssertions(() -> consumer.accept(instrument)))
                .thenSucceed();
    }

    public void assertRuneIs(Holder<Rune> rune, ResourceKey<Rune> name) {
        assertRuneIs(rune, name.location());
    }

    public void assertRuneIs(Holder<Rune> rune, ResourceLocation name) {
        if (!rune.is(IDataManager.createKey(ElementalCraftApi.RUNE_MANAGER_KEY, name))) {
            throw new GameTestAssertException("Expected rune " + name + " but got " + rune);
        }
    }

    public void assertRuneIs(ItemStack stack, ResourceKey<Rune> name) {
        assertRuneIs(stack, name.location());
    }

    public void assertRuneIs(ItemStack stack, ResourceLocation name) {
        var rune = RuneItem.getRune(stack);

        if (rune == null) {
            throw new GameTestAssertException("Expected rune " + name + " but got " + stack);
        }
        assertRuneIs(rune, name);
    }
}
