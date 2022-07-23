package dhyces.contextually.client.contexts.icons;

import dhyces.contextually.ContextuallyClient;
import dhyces.contextually.ContextuallyCommon;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public record KeyCodeIcon(int value) implements ITextureIcon {
    @Override
    public TextureAtlasSprite retrieveTexture() {
        return ContextuallyClient.getTextureManager().get(value);
    }

    @Override
    public ResourceLocation getId() {
        return ContextuallyCommon.modloc("key");
    }
}
