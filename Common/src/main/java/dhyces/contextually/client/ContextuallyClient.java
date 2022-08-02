package dhyces.contextually.client;

import com.google.common.collect.Lists;
import dhyces.contextually.client.contexts.KeyContextManager;
import dhyces.contextually.client.gui.RenderRect;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import net.minecraft.client.Minecraft;

import java.util.List;

public class ContextuallyClient {
    static final KeyMappingTextureManager keyTextures = new KeyMappingTextureManager(Minecraft.getInstance().getTextureManager());
    static final KeyContextManager keyContexts = new KeyContextManager();
    //TODO: create decent screen to move contexts around
//    public static final List<RenderRect> RENDER_RECTS = Lists.newArrayList();

    public static void init() {
//        RENDER_RECTS.add(RenderRect.builder(100, 100, 60, 30).build());
    }

    public static KeyMappingTextureManager getTextureManager() {
        return keyTextures;
    }

    public static KeyContextManager getContextManager() {
        return keyContexts;
    }
}
