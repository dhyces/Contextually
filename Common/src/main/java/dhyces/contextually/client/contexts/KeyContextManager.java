package dhyces.contextually.client.contexts;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.icons.IconUtils;
import dhyces.contextually.client.contexts.objects.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.HitResult;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class KeyContextManager implements PreparableReloadListener {


    private final FreezingDefaultingMapContextRegistry<BlockState, BlockState> BLOCK_STATE_CONTEXTS = FreezingDefaultingMapContextRegistry.create(ContextuallyCommon.id("blockstate_contexts"));
    private final FreezingDefaultingMapContextRegistry<Item, ItemStack> ITEM_CONTEXTS = FreezingDefaultingMapContextRegistry.create(ContextuallyCommon.id("item_contexts"));
    private final FreezingDefaultingMapContextRegistry<EntityType<?>, Entity> ENTITY_CONTEXTS = FreezingDefaultingMapContextRegistry.create(ContextuallyCommon.id("entitytype_contexts"));
    private ImmutableList<IKeyContext<Void>> GLOBAL_CONTEXTS = ImmutableList.of();

    public boolean hasContextForBlock(BlockState block) {
        return BLOCK_STATE_CONTEXTS.containsKey(block);
    }

    public boolean hasContextForEntity(Entity entity) {
        return ENTITY_CONTEXTS.containsKey(entity.getType());
    }

    public Collection<IKeyContext<Void>> getGlobalContexts() {
        return GLOBAL_CONTEXTS;
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

    public Collection<IKeyContext<Void>> filterGlobalContexts(ClientLevel level, AbstractClientPlayer player) {
        return getGlobalContexts().stream().filter(context -> context.testConditions(null, null, level, player)).toList();
    }

    public Collection<IKeyContext<BlockState>> filterContextsForBlock(BlockState blockState, HitResult hitResult, ClientLevel level, AbstractClientPlayer player) {
        return getContextsForBlock(blockState).stream().filter(context -> context.testConditions(blockState, hitResult, level, player)).toList();
    }

    public Collection<IKeyContext<BlockState>> filterContextsForFluid(FluidState fluidState, HitResult hitResult, ClientLevel level, AbstractClientPlayer player) {
        return getContextsForFluid(fluidState).stream().filter(context -> context.testConditions(fluidState, hitResult, level, player)).toList();
    }

    public Collection<IKeyContext<Entity>> filterContextsForEntity(Entity entity, HitResult hitResult, ClientLevel level, AbstractClientPlayer player) {
        return getContextsForEntity(entity).stream().filter(context -> context.testConditions(entity, hitResult, level, player)).toList();
    }

    public Collection<IKeyContext<ItemStack>> filterContextsForItem(Item item, HitResult hitResult, ClientLevel level, AbstractClientPlayer player) {
        return getContextsForItem(item).stream().filter(context -> context.testConditions(item, hitResult, level, player)).toList();
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier pPreparationBarrier, ResourceManager pResourceManager, ProfilerFiller pPreparationsProfiler, ProfilerFiller pReloadProfiler, Executor pBackgroundExecutor, Executor pGameExecutor) {
        return CompletableFuture.supplyAsync(() -> new KeyContextLoader(pResourceManager))
                .thenCompose(pPreparationBarrier::wait)
                .thenAcceptAsync(loader -> {
                    BLOCK_STATE_CONTEXTS.unfreezeReset();
                    ITEM_CONTEXTS.unfreezeReset();
                    ENTITY_CONTEXTS.unfreezeReset();
                    ImmutableList.Builder<IKeyContext<Void>> global = ImmutableList.builder();
                    for (Pair<? extends Collection<?>, ? extends IKeyContext<?>> pair : loader.load()) {
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
