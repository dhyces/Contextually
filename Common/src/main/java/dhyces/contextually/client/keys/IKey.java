package dhyces.contextually.client.keys;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;

public interface IKey extends Comparable<IKey> {
    Codec<IKey> CODEC = Codec.either(CodeKey.CODEC, MappingKey.CODEC)
            .xmap(
            either -> either.left().isPresent() ? either.left().get() : either.right().get(),
    key -> key instanceof CodeKey codeKey ? Either.left(codeKey) : Either.right((MappingKey) key)
            );

    int getValue();

    @Override
    default int compareTo(@NotNull IKey o) {
        return getValue() - o.getValue();
    }
}
