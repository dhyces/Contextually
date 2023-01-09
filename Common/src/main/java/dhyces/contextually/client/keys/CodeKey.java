package dhyces.contextually.client.keys;

import com.mojang.serialization.Codec;

public record CodeKey(int keyCode) implements IKey {
    public static final Codec<CodeKey> CODEC = Codec.INT.xmap(CodeKey::new, CodeKey::keyCode);

    @Override
    public int getValue() {
        return keyCode;
    }
}
