package dhyces.contextually.client.core.contexts;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public record PartialBlockState(Block block, Map<String, String> propertyValueMap) implements Encoder<PartialBlockState> {
    public static final Codec<PartialBlockState> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("id").forGetter(PartialBlockState::block),
                    Codec.unboundedMap(
                            Codec.STRING,
                            Codec.STRING
                    ).fieldOf("properties").forGetter(PartialBlockState::propertyValueMap)
            ).apply(instance, PartialBlockState::new)
    );

    public DataResult<Set<BlockState>> toAvailableStates() {
        StateDefinition<Block, BlockState> stateDefinition = block.getStateDefinition();
        Set<Pair<Property<?>, Optional<?>>> properties = new HashSet<>();

        for (Map.Entry<String, String> entry : propertyValueMap.entrySet()) {
            Property<?> property = stateDefinition.getProperty(entry.getKey());
            if (property == null) {
                return DataResult.error("Property %s does not exist in block %s".formatted(entry.getKey(), BuiltInRegistries.BLOCK.getKey(block)));
            }

            Optional<?> value = property.getValue(entry.getValue());
            if (value.isEmpty()) {
                return DataResult.error("Value %s for property %s does not exist in block %s".formatted(entry.getValue(), entry.getKey(), BuiltInRegistries.BLOCK.getKey(block)));
            }

            properties.add(Pair.of(property, value));
        }

        return DataResult.success(Set.copyOf(
                stateDefinition.getPossibleStates().stream()
                        .filter(blockState -> properties.stream()
                                .allMatch(propertyValuePair -> propertyValuePair.getSecond()
                                        .map(o -> o.equals(blockState.getValue(propertyValuePair.getFirst()))).get()
                                )
                        )
                        .collect(Collectors.toSet())
                )
        );
    }

    public static List<Either<Block, PartialBlockState>> toPartialStates(Set<BlockState> blockStates) {//TODO: Rewrite, currently broken, DO NOT USE
        ImmutableList.Builder<Either<Block, PartialBlockState>> builder = new ImmutableList.Builder<>();
        HashSet<BlockState> mutable = new HashSet<>(blockStates); // LIKELY UNNEEDED
        LinkedList<BlockState> list = new LinkedList<>(blockStates);
        IntSet invalidIndices = new IntOpenHashSet();
        // Handle when all states are defined for a block. If that's the case, we want to serialize just a block
        for (int i = 0; i < list.size(); i++) {
            if (invalidIndices.contains(i)) continue;
            BlockState state = list.get(i);
            if (list.containsAll(state.getBlock().getStateDefinition().getPossibleStates())) {
                for (BlockState invalidState : state.getBlock().getStateDefinition().getPossibleStates()) {
                    invalidIndices.add(list.indexOf(invalidState));
                    mutable.remove(invalidState);
                }
                builder.add(Either.left(state.getBlock()));
            }
        }
        IntArrayList f = invalidIndices.stream().sorted((o1, o2) -> o2 - o1).collect(IntArrayList::new, IntArrayList::add, IntList::addAll);
        f.forEach(list::remove);

        // Handle condensing. We want to try to shrinkwrap these blockstates into partials if possible.
        //  ie, if all the possible values are present for a given property of a given block, we can
        //  take that property out as it doesn't have any specific definition
        Multimap<Block, BlockState> map = Util.make(HashMultimap.create(), multimap -> list.forEach(blockState -> multimap.put(blockState.getBlock(), blockState)));

        for (Map.Entry<Block, Collection<BlockState>> entry : map.asMap().entrySet()) {
            List<Property<?>> descendingProperties = entry.getKey().getStateDefinition().getProperties().stream()
                    .sorted((o1, o2) -> o2.getPossibleValues().size() - o1.getPossibleValues().size()).toList();

            Set<PartialBlockState> mutableStates = Set.copyOf(condense(Util.make(new HashSet<>(), partialBlockStates -> partialBlockStates.addAll(entry.getValue().stream().map(PartialBlockState::fromBlockState).toList())), descendingProperties, 0));

            for (PartialBlockState partialBlockState : mutableStates) {
                builder.add(Either.right(partialBlockState));
            }
        }

        return builder.build();
    }

    private String getValue(Property<?> property) {
        return propertyValueMap.get(property.getName());
    }

    private boolean isValueSame(Property<?> property, PartialBlockState other) {
        return this == other || getValue(property) == null || getValue(property).equals(other.getValue(property));
    }

    private static Set<PartialBlockState> condense(final Set<PartialBlockState> mutableStates, final List<Property<?>> descendingProperties, final int index) {
        if (index >= descendingProperties.size()) {
            return mutableStates;
        }
        Property<?> property = descendingProperties.get(index);
        // Loop through the states

        Multimap<String, PartialBlockState> collected = Util.make(HashMultimap.create(), map -> mutableStates.forEach(partialBlockState -> map.put(partialBlockState.getValue(property), partialBlockState)));

        for (Map.Entry<String, Collection<PartialBlockState>> entry : collected.asMap().entrySet()) {
            if (entry.getValue().size() > 1) {
                condense(Util.make(new HashSet<>(), set -> set.addAll(entry.getValue())), descendingProperties, index+1);
            }
        }

        if (collected.keySet().size() == property.getPossibleValues().size() && sameAmount(collected)) {
            Set<PartialBlockState> compiled = mutableStates.stream().peek(partialBlockState -> partialBlockState.propertyValueMap.remove(property.getName())).collect(Collectors.toSet());
            mutableStates.clear();
            mutableStates.addAll(compiled);
            return mutableStates;
        }
        return condense(mutableStates, descendingProperties, index+1);
    }

    private static boolean sameAmount(Multimap<String, PartialBlockState> map) {
        AtomicInteger i = null;
        for (Map.Entry<String, Collection<PartialBlockState>> entry : map.asMap().entrySet()) {
            if (i == null) {
                i = new AtomicInteger(entry.getValue().size());
            } else if (entry.getValue().size() != i.get()) {
                return false;
            }
        }
        return true;
    }

    private static <T> T cast(final Object o) {
        return (T)o;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartialBlockState that = (PartialBlockState) o;
        return Objects.equals(block, that.block) && Objects.equals(propertyValueMap, that.propertyValueMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(block, propertyValueMap);
    }

    public static PartialBlockState fromBlockState(final BlockState state) {
        return new PartialBlockState(state.getBlock(), Util.make(new HashMap<>(), stringStringHashMap ->
                state.getProperties().forEach(property -> stringStringHashMap.put(property.getName(), property.getName(cast(state.getValue(property)))))
        ));
    }

    public static Builder builder(Block block) {
        return new Builder(block);
    }

    @Override
    public <T> DataResult<T> encode(PartialBlockState input, DynamicOps<T> ops, T prefix) {
        return CODEC.encode(input, ops, prefix);
    }

    public static class Builder {
        Block block;
        Map<String, String> map;

        private Builder(Block block) {
            this.block = block;
            this.map = new HashMap<>();
        }

        public <T extends Comparable<T>> Builder with(Property<T> property, T value) {
            map.put(property.getName(), property.getName(value));
            return this;
        }

        public PartialBlockState build() {
            return new PartialBlockState(block, map);
        }
    }
}
