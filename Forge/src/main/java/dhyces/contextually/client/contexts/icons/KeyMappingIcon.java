package dhyces.contextually.client.contexts.icons;

import dhyces.contextually.ContextuallyClient;
import dhyces.contextually.ContextuallyCommon;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public record KeyMappingIcon(KeyMapping mapping) implements ITextureIcon {
    @Override
    public TextureAtlasSprite retrieveTexture() {
        return ContextuallyClient.getTextureManager().get(mapping.getKey().getValue());
    }

    @Override
    public ResourceLocation getId() {
        return ContextuallyCommon.modloc("mapping");
    }
}
