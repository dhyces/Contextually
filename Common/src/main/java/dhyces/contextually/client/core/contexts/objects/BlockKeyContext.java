package dhyces.contextually.client.core.contexts.objects;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dhyces.contextually.Contextually;
import dhyces.contextually.client.core.conditions.IContextCondition;
import dhyces.contextually.client.core.contexts.IKeyContextType;
import dhyces.contextually.client.core.contexts.PartialBlockState;
import dhyces.contextually.client.core.icons.IIcon;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BlockKeyContext extends AbstractKeyContext<BlockState, BlockState> {
    public static final Codec<BlockKeyContext> CODEC = RecordCodecBuilder.create(instance ->
            fillBaseParts(instance).and(
                    Codec.either(
                            BuiltInRegistries.BLOCK.byNameCodec(),
                            PartialBlockState.CODEC
                    ).listOf().fieldOf("targets").xmap(
                            targets ->
                                    targets.stream().map(blockPartialBlockStateEither ->
                                            blockPartialBlockStateEither.left().isPresent() ? DataResult.success(Set.copyOf(blockPartialBlockStateEither.left().get().getStateDefinition().getPossibleStates()))
                                                                                            : blockPartialBlockStateEither.right().get().toAvailableStates()
                                    ).flatMap(dataResult -> dataResult.getOrThrow(false, Contextually.LOGGER::error).stream()).collect(Collectors.toSet()),
                            PartialBlockState::toPartialStates
                    ).forGetter(BlockKeyContext::getTargets)
            ).apply(instance, BlockKeyContext::new)
    );

    public BlockKeyContext(@Nonnull Set<IIcon> icons, @Nonnull Set<IContextCondition> conditions, @Nonnull Set<BlockState> targetedStates) {
        super(icons, targetedStates, conditions);
    }

    @Override
    public @Nonnull IKeyContextType<?> getType() {
        return IKeyContextType.BLOCK;
    }
}
