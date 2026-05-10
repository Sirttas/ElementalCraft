package sirttas.elementalcraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.testframework.gametest.ExtendedGameTestHelper;
import net.neoforged.testframework.gametest.ExtendedSequence;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.anchor.TranslocationAnchors;
import sirttas.elementalcraft.block.container.ElementContainer;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.rune.RuneItem;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleGameTestHelper;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class ECGameTestHelper extends ExtendedGameTestHelper {

    public ECGameTestHelper(GameTestInfo info) {
        super(info);
    }

    public InteractionResult useItem(Player player) {
        return useItem(player, InteractionHand.MAIN_HAND);
    }

    public InteractionResult useItem(Player player, InteractionHand hand) {
        var level = player.level();
        var stack = player.getItemInHand(hand);

        return stack.use(level, player, hand);
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

    public Player mockPlayerWithItem(Vec3 pos, ItemStack itemStack) {
        var player = makeMockPlayer();

        moveEntityTo(player, pos);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        return player;
    }

    public Player mockChiselPlayer(Vec3 pos) {
        return mockPlayerWithItem(pos, new ItemStack(ECItems.SWIFT_ALLOY_CHISEL));
    }

    public Player mockChiselPlayer(BlockPos pos) {
        return mockChiselPlayer(Vec3.atLowerCornerOf(pos));
    }

    public Player mockPlayerWithJewel(Supplier<? extends Jewel> jewel) {
        return mockPlayerWithJewel(new Vec3(1, 1, 1), jewel);
    }

    public Player mockPlayerWithJewel(Vec3 pos, Supplier<? extends Jewel> jewel) {
        var player = makeMockPlayer(GameType.SURVIVAL);

        moveEntityTo(player, pos);
        player.setItemSlot(EquipmentSlot.HEAD, createWithJewel(Items.LEATHER_HELMET, jewel));
        player.addItem(createFullPureHolder());
        getLevel().addFreshEntity(player);
        return player;
    }


    public Player mockPlayerWithSpell(Vec3 pos, Holder<Spell> spell) {


        var scroll = new ItemStack(ECItems.SCROLL);

        SpellHelper.setSpell(scroll, spell);

        var player = mockPlayerWithItem(pos, scroll);
        player.addItem(createFullPureHolder());
        getLevel().addFreshEntity(player);
        return player;
    }

    public Player mockReceptaclePlayer() {
        return mockReceptaclePlayer(ElementType.NONE);
    }

    public Player mockReceptaclePlayer(ElementType type) {
        return mockReceptaclePlayer(type, -1);
    }

    public Player mockReceptaclePlayer(ElementType type, int elementAmount) {
        var player = makeMockPlayer();
        var receptacle = ReceptacleGameTestHelper.createSimpleReceptacle(type);

        if (elementAmount > 0) {
            receptacle.set(ECDataComponents.ELEMENT_AMOUNT, elementAmount);
        }

        moveEntityToOrigin(player);
        player.setItemInHand(InteractionHand.MAIN_HAND, receptacle);
        return player;
    }

    public Player mockCoverFramePlayer() {
        var player = makeMockPlayer();

        moveEntityToOrigin(player);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ECItems.COVER_FRAME));
        return player;
    }

    public void moveEntityToOrigin(Entity entity) {
        moveEntityTo(entity, Vec3.ZERO);
    }

    public void moveEntityTo(Entity entity, Vec3 pos) {
        var abs = absoluteVec(pos);

        entity.snapTo(abs.x, abs.y, abs.z);
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
            throw new GameTestAssertException(Component.literal("Expected ElementContainer at " + pos), (int) getTick());
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
        runInstrument(new BlockPos(0, 1, 0), inputs, elementType, recipeAvailable, consumer);
    }

    public <T extends AbstractInstrumentBlockEntity<?, ?>> void runInstrument(BlockPos pos, List<ItemStack> inputs, ElementType elementType, boolean recipeAvailable, Consumer<T> consumer) {
        T instrument = (T) this.getBlockEntity(pos, AbstractInstrumentBlockEntity.class); // TODO: pass the class as method param
        var container = this.requireElementContainer(pos.below());

        this.startSequence().thenExecute(() -> {
                    var inv = instrument.getInventory();

                    for (int i = 0; i < inputs.size(); i++) {
                        inv.setItem(i, inputs.get(i));
                    }
                    container.fill(elementType);

                    assertThat(instrument.isRecipeAvailable())
                            .withFailMessage(() -> recipeAvailable ? "Recipe is not available but it should be" : "Recipe is available but it should not be")
                            .isEqualTo(recipeAvailable);
                }).thenExecuteAfter(2, () -> consumer.accept(instrument))
                .thenSucceed();
    }

    public void assertRuneIs(Holder<Rune> rune, ResourceKey<Rune> name) {
        assertRuneIs(rune, name.identifier());
    }

    public void assertRuneIs(Holder<Rune> rune, Identifier name) {
        if (!rune.is(IDataManager.createKey(ElementalCraftApi.RUNE_MANAGER_KEY, name))) {
            throw new GameTestAssertException(Component.literal("Expected rune " + name + " but got " + rune), (int) getTick());
        }
    }

    public void assertRuneIs(ItemStack stack, ResourceKey<Rune> name) {
        assertRuneIs(stack, name.identifier());
    }

    public void assertRuneIs(ItemStack stack, Identifier name) {
        var rune = RuneItem.getRune(stack);

        if (rune == null) {
            throw new GameTestAssertException(Component.literal("Expected rune " + name + " but got " + stack), (int) getTick());
        }
        assertRuneIs(rune, name);
    }

    public ItemStack createFullPureHolder() {
        var holder = new ItemStack(ECItems.PURE_HOLDER);

        holder.getCapability(ElementalCraftCapabilities.ElementStorages.ITEM).fill();
        return holder;
    }

    public ItemStack createWithJewel(ItemLike item, Supplier<? extends Jewel> jewel) {
        var stack = new ItemStack(item);

        JewelHelper.setJewel(stack, jewel.get());
        return stack;
    }

    public void assertEntityAlive(LivingEntity entity) {
        assertThat(entity.isAlive())
                .describedAs("%s should be alive", entity)
                .isTrue();
    }

    public void assertJewelActive(Entity entity, Supplier<? extends Jewel> jewel) {
        var j = jewel.get();

        assertThat(entity)
                .as("Entity %s should have %s in its inventory", entity, j)
                .satisfies(p -> assertThat(JewelHelper.getAllJewels(p)).contains(j))
                .as("Entity %s should have %s active", entity, j)
                .satisfies(p ->  assertThat(JewelHelper.getActiveJewels(p)).contains(j));
    }

    public void assertElementUsed(Player player, ElementType elementType) {
        assertElementUsed(player.getCapability(ElementalCraftCapabilities.ElementStorages.ENTITY), elementType);
    }

    public void assertElementUsed(IElementStorage storage, ElementType elementType) {
        assertThat(storage).isNotNull();
        assertThat(storage.getElementAmount(elementType))
                .as("Element %s should have been used", elementType.getSerializedName())
                .isLessThan(storage.getElementCapacity(elementType));
    }

    public void fireGameEvent(Holder<GameEvent> event) {
        fireGameEvent(event, Vec3.ZERO);
    }

    public void fireGameEvent(Holder<GameEvent> event, Vec3 pos) {
        getLevel().gameEvent(event, absoluteVec(pos), GameEvent.Context.of(null, null));
    }

    @Override
    public ECGameTestSequence startSequence() {
        var seq = new ECGameTestSequence();

        testInfo.sequences.add(seq);
        return seq;
    }

    public TranslocationAnchors getTranslocationAnchors() {
        var anchors = TranslocationAnchors.get(getLevel());

        assertThat(anchors)
                .as("TranslocationAnchors should have been set")
                .isNotNull();
        return anchors;
    }

    public void withTranslocationAnchorAt(BlockPos pos, Consumer<BlockPos> consumer) {
        var anchorPos = absolutePos(pos);
        var anchors = getTranslocationAnchors();

        anchors.add(anchorPos);
        consumer.accept(anchorPos);
        anchors.remove(anchorPos);
    }

    public class ECGameTestSequence extends ExtendedSequence {

        private ECGameTestSequence() {
            super(ECGameTestHelper.this);
        }

        private Runnable fixAssertions(Runnable function) {
            return () -> {
                try {
                    function.run();
                } catch (AssertionError e) {
                    ECGameTestUtils.logAssertionError(e);
                    throw new GameTestAssertException(Component.literal(e.getMessage()), (int) ECGameTestHelper.this.getTick());
                }
            };
        }

        private Runnable openTransaction(Consumer<Transaction> function) {
            return fixAssertions(() -> {
                try (Transaction transaction = Transaction.openRoot()) {
                    function.accept(transaction);
                    transaction.commit();
                }
            });
        }

        @Override
        public ECGameTestSequence thenWaitUntil(Runnable assertion) {
            return (ECGameTestSequence) super.thenWaitUntil(fixAssertions(assertion));
        }

        @Override
        public ECGameTestSequence thenWaitUntil(long expectedDelay, Runnable assertion) {
            return (ECGameTestSequence) super.thenWaitUntil(expectedDelay, fixAssertions(assertion));
        }

        @Override
        public ECGameTestSequence thenIdle(int delta) {
            return (ECGameTestSequence) super.thenIdle(delta);
        }

        @Override
        public ECGameTestSequence thenExecute(Runnable assertion) {
            return (ECGameTestSequence) super.thenExecute(fixAssertions(assertion));
        }

        @Override
        public ECGameTestSequence thenExecuteAfter(int delta, Runnable after) {
            return (ECGameTestSequence) super.thenExecuteAfter(delta, fixAssertions(after));
        }

        @Override
        public ECGameTestSequence thenExecuteFor(int delta, Runnable check) {
            return (ECGameTestSequence) super.thenExecuteFor(delta, fixAssertions(check));
        }

        public ECGameTestSequence thenExecute(Consumer<Transaction> assertion) {
            return (ECGameTestSequence) super.thenExecute(openTransaction(assertion));
        }

        public ECGameTestSequence thenExecuteAfter(int delta, Consumer<Transaction> after) {
            return (ECGameTestSequence) super.thenExecuteAfter(delta, openTransaction(after));
        }

        public ECGameTestSequence thenExecuteFor(int delta, Consumer<Transaction> check) {
            return (ECGameTestSequence) super.thenExecuteFor(delta, openTransaction(check));
        }
    }
}
