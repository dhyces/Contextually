package dhyces.contextually.client.contexts.icons;

import dhyces.contextually.client.ContextuallyClient;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.services.Services;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public record KeyMappingIcon(KeyMapping mapping) implements ITextureIcon {
    @Override
    public TextureAtlasSprite retrieveTexture() {
        return ContextuallyClient.getTextureManager().get(Services.PLATFORM.getKey(mapping).getValue());
    }

    @Override
    public ResourceLocation getId() {
        return ContextuallyCommon.modloc("mapping");
    }
}
