package dhyces.contextually.client.core.icons.objects;

import com.mojang.serialization.Codec;
import dhyces.contextually.client.ContextuallyClient;
import dhyces.contextually.client.core.icons.IIconType;
import dhyces.contextually.client.core.icons.ITextureIcon;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public record KeyTextureIcon(ResourceLocation texture) implements ITextureIcon {
    public static final Codec<KeyTextureIcon> CODEC = ResourceLocation.CODEC.fieldOf("key_texture").xmap(KeyTextureIcon::new, KeyTextureIcon::texture).codec();

    @Override
    public TextureAtlasSprite retrieveTexture() {
        return ContextuallyClient.getTextureManager().get(texture);
    }

    @Override
    public IIconType<?> getType() {
        return IIconType.KEY_TEXTURE;
    }
}
