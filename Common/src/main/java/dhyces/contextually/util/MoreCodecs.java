package dhyces.contextually.util;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import dhyces.contextually.client.keys.CodeKey;
import dhyces.contextually.client.keys.IKey;
import dhyces.contextually.client.keys.MappingKey;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

import java.util.*;
import java.util.stream.Stream;

public final class MoreCodecs {
    private MoreCodecs() {throw new UnsupportedOperationException();}
    public static final Codec<IKey> KEY_CODEC = Codec.either(CodeKey.CODEC, MappingKey.CODEC)
            .xmap(
                    either -> either.left().isPresent() ? either.left().get() : either.right().get(),
                    key -> key instanceof CodeKey codeKey ? Either.left(codeKey) : Either.right((MappingKey) key)
            );

    public static final Codec<InteractionHand> INTERACTION_HAND = Codec.STRING
            .xmap(
                    s -> s.equals("main_hand") ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND,
                    interactionHand -> interactionHand.equals(InteractionHand.MAIN_HAND) ? "main_hand" : "off_hand"
            );

    public static final Codec<MinMaxBounds.Ints> INT_MIN_MAX_BOUNDS_CODEC = Codec.either(
            Codec.INT,
            Codec.pair(
                    optionalIntField("min").codec(),
                    optionalIntField("max").codec()
            )
    ).xmap(
            either -> {
                Optional<Integer> exactOptional = either.left();
                Optional<Pair<OptionalInt, OptionalInt>> boundsOptional = either.right();
                MinMaxBounds.Ints ints = null;
                if (exactOptional.isPresent()) {
                    ints = MinMaxBounds.Ints.exactly(exactOptional.get());
                } else if (boundsOptional.isPresent()) {
                    if (boundsOptional.get().getFirst().isPresent()) {
                        if (boundsOptional.get().getSecond().isPresent()) {
                            ints = MinMaxBounds.Ints.between(boundsOptional.get().getFirst().getAsInt(), boundsOptional.get().getSecond().getAsInt());
                        } else {
                            ints = MinMaxBounds.Ints.atLeast(boundsOptional.get().getFirst().getAsInt());
                        }
                    } else if (boundsOptional.get().getSecond().isPresent()) {
                        ints = MinMaxBounds.Ints.atMost(boundsOptional.get().getSecond().getAsInt());
                    }
                }
                return ints;
            },
            ints -> {
                Integer min = ints.getMin();
                Integer max = ints.getMax();
                if (min != null) {
                    if (max != null) {
                        if (min.equals(max)) {
                            return Either.left(min);
                        } else {
                            return Either.right(Pair.of(OptionalInt.of(min), OptionalInt.of(max)));
                        }
                    } else {
                        return Either.right(Pair.of(OptionalInt.of(min), OptionalInt.empty()));
                    }
                } else if (max != null) {
                    return Either.right(Pair.of(OptionalInt.empty(), OptionalInt.of(max)));
                }
                return Either.right(Pair.of(OptionalInt.empty(), OptionalInt.empty()));
            }
    );

    public static final Codec<ResourceLocation> CONTEXTUALLY_RESOURCE_LOCATION = Codec.STRING.comapFlatMap(
            s -> ResourceLocation.read(s.contains(":") ? s : "contextually:" + s),
            ResourceLocation::toString
    ).stable();

    public static MapCodec<OptionalInt> optionalIntField(String field) {
        return new OptionalIntCodec(field);
    }

    public static <T> Set<T> mapOptionalListToSet(Optional<List<T>> optional) {
        return optional.map(Set::copyOf).orElse(Set.of());
    }

    public static <T> Optional<List<T>> mapSetToOptionalList(Set<T> set) {
        return conditionalNullable(set.isEmpty(), List.copyOf(set));
    }

    public static MinMaxBounds.Ints mapOptionalIntsToInts(Optional<MinMaxBounds.Ints> optional) {
        return optional.orElse(MinMaxBounds.Ints.ANY);
    }

    public static Optional<MinMaxBounds.Ints> mapIntsToOptionalInts(MinMaxBounds.Ints ints) {
        return conditionalNullable(ints.isAny(), ints);
    }

    public static <T, O> Optional<O> conditionalNullable(boolean nullCondition, O otherwise) {
        return Optional.ofNullable(nullCondition ? null : otherwise);
    }

    static class OptionalIntCodec extends MapCodec<OptionalInt> {
        final String field;

        public OptionalIntCodec(String field) {
            this.field = field;
        }

        @Override
        public <T> DataResult<OptionalInt> decode(DynamicOps<T> ops, MapLike<T> input) {
            T value = input.get(field);
            if (value != null) {
                DataResult<Integer> parsed = Codec.INT.parse(ops, value);
                if (parsed.result().isPresent()) {
                    return parsed.map(OptionalInt::of);
                }
            }
            return DataResult.success(OptionalInt.empty());
        }

        @Override
        public <T> RecordBuilder<T> encode(OptionalInt input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            if (input.isPresent()) {
                return prefix.add(field, Codec.INT.encodeStart(ops, input.getAsInt()));
            }
            return prefix;
        }

        @Override
        public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Stream.of(ops.createString(field));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OptionalIntCodec that = (OptionalIntCodec) o;
            return Objects.equals(field, that.field);
        }

        @Override
        public int hashCode() {
            return Objects.hash(field);
        }

        @Override
        public String toString() {
            return "OptionalIntCodec[" + field + "]";
        }
    }
}
