package dhyces.contextually.client.contexts;

import com.google.common.collect.*;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.conditions.Condition;
import dhyces.contextually.client.contexts.objects.BlockKeyContext;
import dhyces.contextually.client.contexts.objects.EntityKeyContext;
import dhyces.contextually.client.contexts.objects.IKeyContext;
import dhyces.contextually.client.util.DefaultingMultiMapDelegate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public final class KeyContextManager {

    final DefaultingMultiMapDelegate<BlockState, IKeyContext<BlockState>> BLOCK_STATE_CONTEXTS = createDelegate(new BlockKeyContext(ContextuallyCommon.modloc("default_block_attack"), Set.of(ContextuallyCommon.modloc("mouse_left_key")), Set.of()));
    final DefaultingMultiMapDelegate<EntityType<?>, IKeyContext<Entity>> ENTITY_CONTEXTS = createDelegate(new EntityKeyContext(ContextuallyCommon.modloc("default_entity_attack"), Set.of(ContextuallyCommon.modloc("mouse_left_key")), Set.of()));
    final DefaultingMultiMapDelegate<Item, IKeyContext<ItemStack>> ITEM_CONTEXTS = createDelegate();

    public KeyContextManager() {
        BLOCK_STATE_CONTEXTS.put(Blocks.LEVER.defaultBlockState(), new BlockKeyContext(ContextuallyCommon.modloc("lever_use"), Set.of(ContextuallyCommon.modloc("mouse_right_key")), Set.of()));
        ENTITY_CONTEXTS.put(EntityType.VILLAGER, new EntityKeyContext(ContextuallyCommon.modloc("villager_use"), Set.of(ContextuallyCommon.modloc("mouse_right_key")), Set.of(new Condition.NotCondition(new Condition.VillagerProfessionCondition("none")))));
    }

    public Collection<IKeyContext<BlockState>> getContextsForBlock(BlockState block) {
        var ret = BLOCK_STATE_CONTEXTS.get(block);
        if (BLOCK_STATE_CONTEXTS.isDefault(ret)) {
            return Sets.union(Sets.newHashSet(ret), Sets.newHashSet(BLOCK_STATE_CONTEXTS.get(block.getBlock().defaultBlockState())));
        }
        return ret;
    }

    public Collection<IKeyContext<Entity>> getContextsForEntity(Entity entity) {
        return Sets.union(Sets.newHashSet(ENTITY_CONTEXTS.get(entity.getType())), Sets.newHashSet(ENTITY_CONTEXTS.getDefaultValue()));
    }

    public Collection<IKeyContext<ItemStack>> getContextsForItem(Item item) {
        return ITEM_CONTEXTS.get(item);
    }

    private <K, V> DefaultingMultiMapDelegate<K, V> createDelegate(V... contexts) {
        return DefaultingMultiMapDelegate.createArrayListMultiMap(Arrays.asList(contexts));
    }

}
