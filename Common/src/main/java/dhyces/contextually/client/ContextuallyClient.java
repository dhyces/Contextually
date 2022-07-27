package dhyces.contextually.client;

import dhyces.contextually.client.contexts.KeyContextManager;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import net.minecraft.client.Minecraft;

public class ContextuallyClient {
    static final KeyMappingTextureManager keyTextures = new KeyMappingTextureManager(Minecraft.getInstance().getTextureManager());
    static final KeyContextManager keyContexts = new KeyContextManager();

    ContextuallyClient() {
    }

    public static KeyMappingTextureManager getTextureManager() {
        return keyTextures;
    }

    public static KeyContextManager getContextManager() {
        return keyContexts;
    }
}
