package dhyces.contextually.client.core.conditions.predicates;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public record NbtContextPredicate(Optional<CompoundTag> nbt) {
    public static final Codec<NbtContextPredicate> CODEC = CompoundTag.CODEC.xmap(compoundTag ->
            new NbtContextPredicate(Optional.of(compoundTag)),
            predicate -> predicate.nbt.orElse(new CompoundTag())
    );

    public static final NbtContextPredicate ANY = new NbtContextPredicate(Optional.empty());

    public boolean matches(CompoundTag tag) {
        return nbt.isEmpty() || nbt.get().equals(tag);
    }
}
