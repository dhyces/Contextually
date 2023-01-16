package dhyces.contextually.client.keys;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public interface IKey extends Comparable<IKey> {
    int getValue();

    @Override
    default int compareTo(@Nonnull IKey o) {
        return getValue() - o.getValue();
    }
}
