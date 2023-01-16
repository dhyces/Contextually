package dhyces.contextually.client.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import dhyces.contextually.Contextually;
import dhyces.contextually.client.core.conditions.ContextSource;
import dhyces.contextually.client.core.contexts.IKeyContext;
import dhyces.contextually.client.core.contexts.objects.BlockKeyContext;
import dhyces.contextually.client.core.contexts.objects.EntityKeyContext;
import dhyces.contextually.client.core.contexts.objects.GlobalKeyContext;
import dhyces.contextually.client.core.contexts.objects.ItemKeyContext;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.HitResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class KeyContextManager implements PreparableReloadListener {


    public static final FreezingDefaultingMapContextRegistry<BlockState, BlockState> BLOCK_STATE_CONTEXTS = FreezingDefaultingMapContextRegistry.create(Contextually.id("block_state_contexts"));
    private final FreezingDefaultingMapContextRegistry<Item, ItemStack> ITEM_CONTEXTS = FreezingDefaultingMapContextRegistry.create(Contextually.id("item_contexts"));
    private final FreezingDefaultingMapContextRegistry<EntityType<?>, Entity> ENTITY_CONTEXTS = FreezingDefaultingMapContextRegistry.create(Contextually.id("entity_type_contexts"));
    private ImmutableList<IKeyContext<Object, Object>> GLOBAL_CONTEXTS = ImmutableList.of();

    public Collection<IKeyContext<Object, Object>> getGlobalContexts() {
        return GLOBAL_CONTEXTS;
    }

    public Collection<IKeyContext<BlockState, BlockState>> getContextsForBlock(BlockState block) {
        var ret = BLOCK_STATE_CONTEXTS.get(block);
        if (BLOCK_STATE_CONTEXTS.isDefault(ret)) {
            ret = BLOCK_STATE_CONTEXTS.get(block.getBlock().defaultBlockState());
        }
        return Sets.union(new HashSet<>(ret), new HashSet<>(BLOCK_STATE_CONTEXTS.getDefaultValue()));
    }

    public Collection<IKeyContext<BlockState, BlockState>> getContextsForFluid(FluidState block) {
        return BLOCK_STATE_CONTEXTS.get(block.createLegacyBlock());
    }

    public Collection<IKeyContext<EntityType<?>, Entity>> getContextsForEntity(Entity entity) {
        return Sets.union(new HashSet<>(ENTITY_CONTEXTS.get(entity.getType())), new HashSet<>(ENTITY_CONTEXTS.getDefaultValue()));
    }

    public Collection<IKeyContext<Item, ItemStack>> getContextsForItem(Item item) {
        return ITEM_CONTEXTS.get(item);
    }

    public Collection<IKeyContext<Object, Object>> filterGlobalContexts(ContextSource contextSource) {
        return getGlobalContexts().stream().filter(context -> context.testConditions(contextSource)).toList();
    }

    public Collection<IKeyContext<BlockState, BlockState>> filterContextsForBlock(ContextSource contextSource) {
        return getContextsForBlock(contextSource.getHitResultSolidBlock().get()).stream().filter(context -> context.testConditions(contextSource)).toList();
    }

    public Collection<IKeyContext<BlockState, BlockState>> filterContextsForFluid(ContextSource contextSource) {
        return getContextsForFluid(contextSource.getHitResultFluidBlock().get().getFluidState()).stream().filter(context -> context.testConditions(contextSource)).toList();
    }

    public Collection<IKeyContext<EntityType<?>, Entity>> filterContextsForEntity(ContextSource contextSource) {
        return getContextsForEntity(contextSource.getHitResultEntity().get()).stream().filter(context -> context.testConditions(contextSource)).toList();
    }

    public Collection<IKeyContext<Item, ItemStack>> filterContextsForItem(ContextSource contextSource, InteractionHand hand) {
        return getContextsForItem(contextSource.getHandStack(hand).get().getItem()).stream().filter(context -> context.testConditions(contextSource)).toList();
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier pPreparationBarrier, ResourceManager pResourceManager, ProfilerFiller pPreparationsProfiler, ProfilerFiller pReloadProfiler, Executor pBackgroundExecutor, Executor pGameExecutor) {
        return CompletableFuture.supplyAsync(() -> new KeyContextLoader(pResourceManager))
                .thenCompose(pPreparationBarrier::wait)
                .thenAcceptAsync(loader -> {
                    BLOCK_STATE_CONTEXTS.unfreezeReset();
                    ITEM_CONTEXTS.unfreezeReset();
                    ENTITY_CONTEXTS.unfreezeReset();
                    ImmutableList.Builder<IKeyContext<Object, Object>> global = ImmutableList.builder();
                    for (Pair<? extends Collection<?>, ? extends IKeyContext<?, ?>> pair : loader.load()) {
                        var collection = pair.getFirst();
                        var context = pair.getSecond();
                        if (context instanceof BlockKeyContext blockKeyContext) {
                            if (collection.isEmpty()) {
                                BLOCK_STATE_CONTEXTS.addDefault(blockKeyContext);
                            }
                            for (Object state : collection) {
                                BLOCK_STATE_CONTEXTS.put((BlockState)state, blockKeyContext);
                            }
                        } else if (context instanceof ItemKeyContext itemKeyContext) {
                            if (collection.isEmpty()) {
                                ITEM_CONTEXTS.addDefault(itemKeyContext);
                            }
                            for (Object item : collection) {
                                ITEM_CONTEXTS.put((Item) item, itemKeyContext);
                            }
                        } else if (context instanceof EntityKeyContext entityKeyContext) {
                            if (collection.isEmpty()) {
                                ENTITY_CONTEXTS.addDefault(entityKeyContext);
                            }
                            for (Object entity : collection) {
                                ENTITY_CONTEXTS.put((EntityType<?>)entity, entityKeyContext);
                            }
                        } else if (context instanceof GlobalKeyContext globalKeyContext) {
                            global.add(globalKeyContext);
                        }
                    }
                    BLOCK_STATE_CONTEXTS.setFrozen(true);
                    ITEM_CONTEXTS.setFrozen(true);
                    ENTITY_CONTEXTS.setFrozen(true);
                    GLOBAL_CONTEXTS = global.build();
                }, pGameExecutor);
    }
}
