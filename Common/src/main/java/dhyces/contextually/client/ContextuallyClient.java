package dhyces.contextually.client;

import dhyces.contextually.client.contexts.KeyContextManager;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import net.minecraft.client.Minecraft;

public class ContextuallyClient {
    static final KeyMappingTextureManager KEY_TEXTURES = new KeyMappingTextureManager(Minecraft.getInstance().getTextureManager());
    static final KeyContextManager KEY_CONTEXTS = new KeyContextManager();
    //TODO: create decent screen to move contexts around
//    public static final List<RenderRect> RENDER_RECTS = Lists.newArrayList();

    public static void init() {
//        RENDER_RECTS.add(RenderRect.builder(100, 100, 60, 30).build());
    }

    public static KeyMappingTextureManager getTextureManager() {
        return KEY_TEXTURES;
    }

    public static KeyContextManager getContextManager() {
        return KEY_CONTEXTS;
    }
}
