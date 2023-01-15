package dhyces.contextually.client.keys;

import com.mojang.serialization.Codec;
import dhyces.contextually.services.Services;
import dhyces.contextually.util.KeyUtils;
import net.minecraft.client.KeyMapping;

public record MappingKey(String mappingStr) implements IKey {
    public static final Codec<MappingKey> CODEC = Codec.STRING.xmap(MappingKey::new, MappingKey::mappingStr);

    @Override
    public int getValue() {
        KeyMapping mapping = KeyUtils.get(mappingStr);
        if (mapping == null) {
            return -1;
        }
        return Services.PLATFORM.getKey(mapping).getValue();
    }
}