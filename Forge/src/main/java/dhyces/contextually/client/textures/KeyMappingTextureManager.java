package dhyces.contextually.client.textures;

import com.google.common.collect.Maps;
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

    public TextureAtlasSprite get(int keyValue) {
        return getSprite(KeyConstants.get(keyValue));
    }

    @Override
    protected Stream<ResourceLocation> getResourcesToLoad() {
        return KeyConstants.KEY_LOCATIONS.values().stream();
//        var resourceManager = Minecraft.getInstance().getResourceManager();
//        var resourcesMap = resourceManager.listResources("textures/key", c -> c.getPath().endsWith(".png"));
//        // TODO: make this better. Not a big fan of having so much abstract string manipulation. One idea is to have a
//        //  sort of "registration" where we store a map and manually add all of the resource locations for each key.
//        //  This has the benefit of being able to immediately access an RL from an InputConstant.
//        return resourcesMap.keySet().stream().map(c -> ContextuallyCommon.modloc(c.getPath().substring(13, c.getPath().length()-4)));
    }
}