package dhyces.contextually.client.contexts.icons;

import dhyces.contextually.client.ContextuallyClient;
import dhyces.contextually.ContextuallyCommon;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public record KeyTextureIcon(ResourceLocation texture) implements ITextureIcon {
    @Override
    public TextureAtlasSprite retrieveTexture() {
        return ContextuallyClient.getTextureManager().get(texture);
    }

    @Override
    public ResourceLocation getId() {
        return ContextuallyCommon.id("key_texture");
    }
}
