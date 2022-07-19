package dhyces.contextually.client.textures;

import dhyces.contextually.ContextuallyCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.stream.Stream;

public class KeyMappingTextureManager extends TextureAtlasHolder {

    public static final ResourceLocation KEYS = ContextuallyCommon.modloc("textures/atlas/keys.png");

    public KeyMappingTextureManager(TextureManager textureManager) {
        super(textureManager, KEYS, "key");
    }

    public TextureAtlasSprite get(ResourceLocation location) {
        return getSprite(location);
    }

    @Override
    protected Stream<ResourceLocation> getResourcesToLoad() {
        var resourceManager = Minecraft.getInstance().getResourceManager();
        var resourcesMap = resourceManager.listResources("textures/key", c -> c.getPath().endsWith(".png"));
        return resourcesMap.keySet().stream();
    }
}