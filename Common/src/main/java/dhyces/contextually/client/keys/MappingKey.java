package dhyces.contextually.client.keys;

import dhyces.contextually.services.Services;
import net.minecraft.client.KeyMapping;

public record MappingKey(KeyMapping mapping) implements IKey {
    @Override
    public int getValue() {
        return Services.PLATFORM.getKey(mapping).getValue();
    }
}
