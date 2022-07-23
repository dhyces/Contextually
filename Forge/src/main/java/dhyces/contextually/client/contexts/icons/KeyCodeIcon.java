package dhyces.contextually.client.contexts.icons;

import dhyces.contextually.ContextuallyClient;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public record KeyCodeIcon(int value) implements ITextureIcon {
    @Override
    public TextureAtlasSprite retrieveTexture() {
        return ContextuallyClient.getTextureManager().get(value);
    }

    @Override
    public String getId() {
        return "keycode";
    }
}
