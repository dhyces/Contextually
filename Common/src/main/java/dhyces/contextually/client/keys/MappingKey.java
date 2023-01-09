package dhyces.contextually.client.keys;

import com.mojang.serialization.Codec;
import dhyces.contextually.services.Services;

public record MappingKey(String mapping) implements IKey {
    public static final Codec<MappingKey> CODEC = Codec.STRING.xmap(MappingKey::new, MappingKey::mapping);

    @Override
    public int getValue() {
        return Services.PLATFORM.getKey(KeyUtils.get(mapping)).getValue();
    }
}
