package dhyces.contextually.client.contexts.icons;

import dhyces.contextually.ContextuallyClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public record KeyMappingIcon(KeyMapping mapping) implements ITextureIcon {
    @Override
    public TextureAtlasSprite retrieveTexture() {
        return ContextuallyClient.getTextureManager().get(mapping.getKey().getValue());
    }

    @Override
    public String getId() {
        return "mapping";
    }
}
