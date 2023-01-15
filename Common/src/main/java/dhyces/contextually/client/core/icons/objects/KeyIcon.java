package dhyces.contextually.client.core.icons.objects;

import com.mojang.serialization.Codec;
import dhyces.contextually.client.ContextuallyClient;
import dhyces.contextually.client.core.icons.IIconType;
import dhyces.contextually.client.core.icons.ITextureIcon;
import dhyces.contextually.client.keys.IKey;
import dhyces.contextually.util.MoreCodecs;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public record KeyIcon(IKey key) implements ITextureIcon {
    public static final Codec<KeyIcon> CODEC = MoreCodecs.KEY_CODEC.fieldOf("key").xmap(
            KeyIcon::new,
            KeyIcon::key
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