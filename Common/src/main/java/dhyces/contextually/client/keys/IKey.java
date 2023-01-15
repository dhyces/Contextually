package dhyces.contextually.client.keys;

import org.jetbrains.annotations.NotNull;

public interface IKey extends Comparable<IKey> {
    int getValue();

    @Override
    default int compareTo(@NotNull IKey o) {
        return getValue() - o.getValue();
    }
}
