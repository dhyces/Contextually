package dhyces.contextually.client.contexts.icons;

import dhyces.contextually.Contextually;
import dhyces.contextually.client.ContextuallyClient;
import dhyces.contextually.client.keys.IKey;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public record KeyIcon(IKey key) implements ITextureIcon {
    @Override
    public TextureAtlasSprite retrieveTexture() {
        return ContextuallyClient.getTextureManager().get(key.getValue());
    }

    @Override
    public ResourceLocation getId() {
        return Contextually.id("key");
    }
}
