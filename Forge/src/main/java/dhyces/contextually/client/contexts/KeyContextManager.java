package dhyces.contextually.client.contexts;

import com.google.common.collect.*;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.conditions.Condition;
import dhyces.contextually.client.contexts.objects.BlockKeyContext;
import dhyces.contextually.client.contexts.objects.EntityKeyContext;
import dhyces.contextually.client.contexts.objects.GlobalKeyContext;
import dhyces.contextually.client.contexts.objects.IKeyContext;
import dhyces.contextually.client.util.DefaultingMultiMapDelegate;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class KeyContextManager implements PreparableReloadListener {

    final DefaultingMultiMapDelegate<BlockState, IKeyContext<BlockState>> BLOCK_STATE_CONTEXTS = createDelegate(new BlockKeyContext(ContextuallyCommon.modloc("default_block_attack"), Set.of(ContextuallyCommon.modloc("mouse_left_key")), Set.of()), new BlockKeyContext(ContextuallyCommon.modloc("default_block_use"), Set.of(ContextuallyCommon.modloc("mouse_right_key")), Set.of(new Condition.PlayerHeldBlockCondition(null))));
    final DefaultingMultiMapDelegate<EntityType<?>, IKeyContext<Entity>> ENTITY_CONTEXTS = createDelegate(new EntityKeyContext(ContextuallyCommon.modloc("default_entity_attack"), Set.of(ContextuallyCommon.modloc("mouse_left_key")), Set.of()));
    final DefaultingMultiMapDelegate<Item, IKeyContext<ItemStack>> ITEM_CONTEXTS = createDelegate();
    final List<IKeyContext<Player>> GLOBAL_CONTEXTS = Lists.newArrayList();

    public KeyContextManager() {
        BLOCK_STATE_CONTEXTS.put(Blocks.LEVER.defaultBlockState(), new BlockKeyContext(ContextuallyCommon.modloc("lever_use"), Set.of(ContextuallyCommon.modloc("mouse_right_key")), Set.of()));
        BLOCK_STATE_CONTEXTS.put(Blocks.WATER.defaultBlockState(), new BlockKeyContext(ContextuallyCommon.modloc("bucket_water"), Set.of(ContextuallyCommon.modloc("mouse_right_key")), Set.of(new Condition.PlayerHeldItemCondition(Items.BUCKET, null))));
        ENTITY_CONTEXTS.put(EntityType.VILLAGER, new EntityKeyContext(ContextuallyCommon.modloc("villager_use"), Set.of(ContextuallyCommon.modloc("mouse_right_key")), Set.of(new Condition.NotCondition(new Condition.VillagerProfessionCondition("none")))));
        ENTITY_CONTEXTS.put(EntityType.COW, new EntityKeyContext(ContextuallyCommon.modloc("cow_use"), Set.of(ContextuallyCommon.modloc("mouse_right_key")), Set.of(new Condition.PlayerHeldItemCondition(Items.BUCKET, null))));
        GLOBAL_CONTEXTS.add(new GlobalKeyContext(ContextuallyCommon.modloc("open_inventory"), Set.of(ContextuallyCommon.modloc("e_key")), Set.of()));
    }

    public boolean hasContextForBlock(BlockState block) {
        return BLOCK_STATE_CONTEXTS.containsKey(block);
    }

    public boolean hasContextForEntity(Entity entity) {
        return ENTITY_CONTEXTS.containsKey(entity.getType());
    }

    public Collection<IKeyContext<BlockState>> getContextsForBlock(BlockState block) {
        var ret = BLOCK_STATE_CONTEXTS.get(block);
        if (BLOCK_STATE_CONTEXTS.isDefault(ret)) {
            ret = BLOCK_STATE_CONTEXTS.get(block.getBlock().defaultBlockState());
        }
        return Sets.union(Sets.newHashSet(ret), Sets.newHashSet(BLOCK_STATE_CONTEXTS.getDefaultValue()));
    }

    public Collection<IKeyContext<BlockState>> getContextsForFluid(FluidState block) {
        return BLOCK_STATE_CONTEXTS.get(block.createLegacyBlock());
    }

    public Collection<IKeyContext<Entity>> getContextsForEntity(Entity entity) {
        return Sets.union(Sets.newHashSet(ENTITY_CONTEXTS.get(entity.getType())), Sets.newHashSet(ENTITY_CONTEXTS.getDefaultValue()));
    }

    public Collection<IKeyContext<ItemStack>> getContextsForItem(Item item) {
        return ITEM_CONTEXTS.get(item);
    }

    public Collection<IKeyContext<Player>> getGlobalContexts() {
        return Collections.unmodifiableCollection(GLOBAL_CONTEXTS);
    }

    private <K, V> DefaultingMultiMapDelegate<K, V> createDelegate(V... contexts) {
        return DefaultingMultiMapDelegate.createArrayListMultiMap(Arrays.asList(contexts));
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier pPreparationBarrier, ResourceManager pResourceManager, ProfilerFiller pPreparationsProfiler, ProfilerFiller pReloadProfiler, Executor pBackgroundExecutor, Executor pGameExecutor) {
        return CompletableFuture.supplyAsync(() -> new KeyContextLoader(pResourceManager)).thenCompose(pPreparationBarrier::wait).thenAcceptAsync(loader -> loader.load(), pGameExecutor);
    }
}
