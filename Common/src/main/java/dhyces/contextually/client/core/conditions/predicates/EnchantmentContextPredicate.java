package dhyces.contextually.client.core.conditions.predicates;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import dhyces.contextually.util.MoreCodecs;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;
import java.util.Optional;

public record EnchantmentContextPredicate(Optional<Enchantment> enchant, MinMaxBounds.Ints levelRange) {
    public static final Codec<EnchantmentContextPredicate> CODEC = Codec.pair(
            Codec.optionalField("enchantment", BuiltInRegistries.ENCHANTMENT.byNameCodec()).codec(),
            Codec.optionalField("level_range", MoreCodecs.INT_MIN_MAX_BOUNDS_CODEC)
                    .xmap(
                            optional -> optional.orElse(MinMaxBounds.Ints.ANY),
                            ints -> Optional.ofNullable(ints.isAny() ? null : ints)
                    ).codec()
    ).xmap(
            pair -> new EnchantmentContextPredicate(pair.getFirst(), pair.getSecond()),
            predicate -> Pair.of(predicate.enchant, predicate.levelRange)
    );

    public boolean isIn(Map<Enchantment, Integer> enchants) {
        if (enchant.isPresent()) {
            var e = enchant.get();
            if (!enchants.containsKey(e)) {
                return false;
            }
            var level = enchants.get(e);
            if (levelRange != MinMaxBounds.Ints.ANY && !levelRange.matches(level)) {
                return false;
            }
            return true;
        } else if (levelRange != MinMaxBounds.Ints.ANY) {
            for (int level : enchants.values()) {
                if (levelRange.matches(level)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean matches(Enchantment enchantment, int level) {
        if (enchant.isPresent() && !enchant.get().equals(enchantment)) {
            return false;
        } else if (!levelRange.matches(level)) {
            return false;
        }
        return true;
    }
}
