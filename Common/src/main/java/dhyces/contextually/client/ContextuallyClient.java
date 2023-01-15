package dhyces.contextually.client;

import dhyces.contextually.client.core.KeyContextManager;
import dhyces.contextually.client.core.conditions.IConditionType;
import dhyces.contextually.client.core.contexts.IKeyContextType;
import dhyces.contextually.client.core.icons.IIconType;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import net.minecraft.client.Minecraft;

public class ContextuallyClient {
    static final KeyMappingTextureManager KEY_TEXTURES = new KeyMappingTextureManager(Minecraft.getInstance().getTextureManager());
    static final KeyContextManager KEY_CONTEXTS = new KeyContextManager();
    //TODO: create decent screen to move contexts around
//    public static final List<RenderRect> RENDER_RECTS = Lists.newArrayList();

    public static void init() {
        IKeyContextType.BLOCK.getCodec();
        IIconType.ANIMATED.getCodec();
        IConditionType.AND.getCodec();
//        RENDER_RECTS.add(RenderRect.builder(100, 100, 60, 30).build());
    }

    public static KeyMappingTextureManager getTextureManager() {
        return KEY_TEXTURES;
    }

    public static KeyContextManager getContextManager() {
        return KEY_CONTEXTS;
    }
}
