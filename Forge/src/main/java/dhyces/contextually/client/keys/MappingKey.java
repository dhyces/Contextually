package dhyces.contextually.client.keys;

import net.minecraft.client.KeyMapping;

public record MappingKey(KeyMapping mapping) implements IKey {
    @Override
    public int getValue() {
        return mapping.getKey().getValue();
    }
}
