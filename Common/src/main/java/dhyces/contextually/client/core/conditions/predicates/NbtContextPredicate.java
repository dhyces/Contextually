package dhyces.contextually.client.core.conditions.predicates;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

// TODO: add some more info to this to allow non-strict matching
public record NbtContextPredicate(Optional<CompoundTag> nbt) {
    public static final Codec<NbtContextPredicate> CODEC = CompoundTag.CODEC.xmap(compoundTag ->
            new NbtContextPredicate(Optional.ofNullable(compoundTag)),
            predicate -> predicate.nbt.orElse(null)
    );

    public static final NbtContextPredicate ANY = new NbtContextPredicate(Optional.empty());

    public boolean matches(CompoundTag tag) {
        return nbt.isEmpty() || nbt.get().equals(tag);
    }
}
