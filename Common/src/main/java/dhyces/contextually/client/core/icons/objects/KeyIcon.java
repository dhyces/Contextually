package dhyces.contextually.client.core.icons.objects;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import dhyces.contextually.client.ContextuallyClient;
import dhyces.contextually.client.core.icons.IIconType;
import dhyces.contextually.client.core.icons.ITextureIcon;
import dhyces.contextually.client.core.icons.IconUtils;
import dhyces.contextually.client.keys.CodeKey;
import dhyces.contextually.client.keys.IKey;
import dhyces.contextually.client.keys.MappingKey;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public record KeyIcon(IKey key) implements ITextureIcon {
    public static final Codec<KeyIcon> CODEC = Codec.either(CodeKey.CODEC, MappingKey.CODEC).fieldOf("key")
            .xmap(
                    either -> either.left().isPresent() ? IconUtils.of(either.left().get()) : IconUtils.of(either.right().get()),
                    keyIcon -> keyIcon.key instanceof CodeKey codeKey ? Either.left(codeKey) : Either.right((MappingKey) keyIcon.key)
            ).codec();

    @Override
    public TextureAtlasSprite retrieveTexture() {
        return ContextuallyClient.getTextureManager().get(key.getValue());
    }

    @Override
    public IIconType<?> getType() {
        return IIconType.KEY;
    }
}
