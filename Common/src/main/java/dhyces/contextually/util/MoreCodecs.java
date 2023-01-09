package dhyces.contextually.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

import java.util.Optional;

public final class MoreCodecs {
    private MoreCodecs() {throw new UnsupportedOperationException();}
    public static final Codec<InteractionHand> INTERACTION_HAND = Codec.STRING
            .xmap(
                    s -> s.equals("main_hand") ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND,
                    interactionHand -> interactionHand.equals(InteractionHand.MAIN_HAND) ? "main_hand" : "off_hand"
            );

    public static final Codec<MinMaxBounds.Ints> INT_MIN_MAX_BOUNDS_CODEC = Codec.pair(
            Codec.optionalField("min", Codec.INT).codec(), Codec.optionalField("max", Codec.INT).codec()
    ).xmap(
            pair -> {
                Integer min = null;
                Integer max = null;
                if (pair.getFirst().isPresent()) {
                    min = pair.getFirst().get();
                }
                if (pair.getSecond().isPresent()) {
                    max = pair.getSecond().get();
                }
                return min == null ? max == null ? MinMaxBounds.Ints.ANY : MinMaxBounds.Ints.atMost(max) : max == null ? MinMaxBounds.Ints.atLeast(min) : min.equals(max) ? MinMaxBounds.Ints.exactly(min) : MinMaxBounds.Ints.between(min, max);
            },
            integerMinMaxBounds -> Pair.of(Optional.ofNullable(integerMinMaxBounds.getMin()), Optional.ofNullable(integerMinMaxBounds.getMax()))
    );

    public static final Codec<ResourceLocation> CONTEXTUALLY_RESOURCE_LOCATION = Codec.STRING.comapFlatMap(
            s -> ResourceLocation.read(s.contains(":") ? s : "contextually:" + s),
            ResourceLocation::toString
    ).stable();

    public static <T> MapCodec<Optional<T>> optionalFieldElse(String field, Codec<T> elementCodec, T orElse) {
        return Codec.optionalField(field, elementCodec).orElse(Optional.of(orElse));
    }
}
